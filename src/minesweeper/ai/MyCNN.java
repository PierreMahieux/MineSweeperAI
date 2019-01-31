package minesweeper.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import utils.Matrix;
import utils.MyUtils;

public class MyCNN {
	
	protected ArrayList<ArrayList<Matrix>> cvLayers = new ArrayList<>();

	public MyCNN(int nbCvLayers, int nbFilters)
	{
		for(int l = 0; l < nbCvLayers; l++)
		{	
			ArrayList<Matrix> cvFilters = new ArrayList<>();
			
			for(int i = 0; i < nbFilters; i++)
			{
				cvFilters.add(Matrix.random(5, 5));
			}
			
			cvLayers.add(cvFilters);
		}
	}
	
	public Matrix getMatrix(int indexInLayer, int indexInFilter)
	{
		return cvLayers.get(indexInLayer).get(indexInFilter);
	}
	
	public int getNbLayers() {return cvLayers.size();}
	public int getNbFilters() {return cvLayers.get(0).size();}

	
	public MyCNN(ArrayList<ArrayList<Matrix>> matrices)
	{
		for(List<Matrix> layer : matrices)
		{
			cvLayers.add(new ArrayList<>(layer));
		}
	}
	

	public Point getBoxToFlip(int[][] theBoard)
	{
		double [][] resultBoard = new double[theBoard.length][theBoard[0].length];
		double[][] board = new double[theBoard.length][theBoard[0].length];
		for(int yi = 0; yi < theBoard.length; yi++)
		{
			for(int xi = 0; xi < theBoard[0].length; xi++)
			{
				if(theBoard[yi][xi] < 0)
				{
					board[yi][xi] = (double)theBoard[yi][xi] / 2.0;					
				}
				else
				{
					board[yi][xi] = (double)theBoard[yi][xi] / 8.0;
				}
			}
		}
		
		ArrayList<Point> neighborhoodModifiers = MyUtils.getModifiersForNeighbors(4);

		for(int yi = 0; yi < board.length; yi++)
		{
			for(int xi = 0; xi < board[0].length; xi++)
			{
				if(board[yi][xi] == -0.5)
				{
					double[][] cutBoard = new double[9][9];
					//System.out.println("Board");
					//MyUtils.showTab(board);
					for(Point p : neighborhoodModifiers)
					{
						if(MyUtils.isInside(yi+p.y, xi+p.x, board))
						{
							cutBoard[p.y+4][p.x+4] = board[yi+p.y][xi+p.x];						
						}
						else
						{
							cutBoard[p.y+4][p.x+4] = -1;
						}
					}
					resultBoard[yi][xi] = evalutatePos(cutBoard);
					//System.out.println("CutBoard");
					//MyUtils.showTab(cutBoard);
				}
				else
				{
					resultBoard[yi][xi] = -1000;
				}
			}
		}

		//System.out.println("resultBoard");
		//MyUtils.showTab(resultBoard);

		Point max = new Point(0,0);
		for(int yi = 0; yi < resultBoard.length; yi++)
		{
			for(int xi = 0; xi < resultBoard[0].length; xi++)
			{
				if(resultBoard[max.y][max.x] < resultBoard[yi][xi])
				{
					max.setLocation(xi, yi);
				}
			}
		}
		
		return max;
	}
	
	protected double evalutatePos(double[][] cutBoard)
	{
		ArrayList<double[][]> inputs = new ArrayList<>();

		inputs.add(cutBoard);
		
		for(ArrayList<Matrix> layerFilters : cvLayers)
		{
			int startSize = inputs.size();
			for(int i = 0; i < startSize; i++)
			{
				for(Matrix filter : layerFilters)
				{
					inputs.add(MyUtils.downSampleConvolution(filter, inputs.get(i)));
				}				
			}

			for(int i = 0; i < startSize; i++)
			{
				inputs.remove(0);
			}
		}
		
		double finalValue = 0;

		for(double[][] i :inputs)
		{
			double v = i[0][0];
			finalValue += v/inputs.size(); //Mean the outputs
		}
		
		return finalValue;
	}

	public void show() 
	{
		int i = 0;
		for(ArrayList<Matrix> ml : cvLayers)
		{
			++i;
			System.out.println("layers n°" + i);
			for(Matrix m : ml)
			{
				m.show();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void showBoard(double[][] board)
	{
		for(int yi = 0; yi < board.length; yi++)
		{
			for(int xi = 0; xi < board[0].length; xi++)
			{
				System.out.print(board[yi][xi] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
