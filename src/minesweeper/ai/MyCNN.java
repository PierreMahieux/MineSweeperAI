package minesweeper.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import utils.Matrix;
import utils.MyUtils;

public class MyCNN {
	
	protected ArrayList<ArrayList<Matrix>> cvLayers = new ArrayList<>();
	
	protected static final int filterSize = 3;

	public MyCNN(int nbCvLayers, int nbFilters)
	{
		for(int l = 0; l < nbCvLayers; l++)
		{	
			ArrayList<Matrix> cvFilters = new ArrayList<>();
			
			for(int i = 0; i < nbFilters; i++)
			{
				cvFilters.add(Matrix.random(filterSize, filterSize));
			}
			
			cvLayers.add(cvFilters);
		}
	}

	
	public MyCNN(ArrayList<ArrayList<Matrix>> matrices)
	{
		for(List<Matrix> layer : matrices)
		{
			cvLayers.add(new ArrayList<>(layer));
		}
	}
	
	public Matrix getMatrix(int indexInLayer, int indexInFilter)
	{
		return cvLayers.get(indexInLayer).get(indexInFilter);
	}
	
	public int getNbLayers() {return cvLayers.size();}
	public int getNbFilters() {return cvLayers.get(0).size();}

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
		
		ArrayList<Point> neighborhoodModifiers = MyUtils.getModifiersForNeighbors((filterSize-1)/2*getNbFilters());

		for(int yi = 0; yi < board.length; yi++)
		{
			for(int xi = 0; xi < board[0].length; xi++)
			{
				if(board[yi][xi] == -0.5)
				{
					double[][] cutBoard = new double[1+(filterSize-1)*getNbFilters()][1+(filterSize-1)*getNbFilters()];
					boolean allCovered = true;
					//System.out.println("Board");
					//MyUtils.showTab(board);
					for(Point p : neighborhoodModifiers)
					{
						if(MyUtils.isInside(yi+p.y, xi+p.x, board))
						{
							cutBoard[p.y+(filterSize-1)][p.x+(filterSize-1)] = board[yi+p.y][xi+p.x];
							if(allCovered)
							{
								if(board[yi+p.y][xi+p.x] != -0.5)
								{
									allCovered = false;
								}
							}
							
						}
						else
						{
							cutBoard[p.y+(filterSize-1)][p.x+(filterSize-1)] = -1;
						}
					}

					//MyUtils.showTab(board, "Fullboard evaluated at " + xi + "," + yi);
					if(allCovered)
					{
						resultBoard[yi][xi] = 0.5;
					}
					else
					{
						resultBoard[yi][xi] = evalutatePos(cutBoard);						
					}
					//System.out.println("Result : " + resultBoard[yi][xi]);
				}
				else
				{
					resultBoard[yi][xi] = -1;
				}
			}
		}

		
		//MyUtils.showTab(resultBoard, "resultBoard");

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
	
	public double evalutatePos(double[][] cutBoard)
	{
		ArrayList<double[][]> inputs = new ArrayList<>();

		inputs.add(cutBoard);
		
		//MyUtils.showTab(cutBoard, "cutBoard");
		
		for(ArrayList<Matrix> layerFilters : cvLayers)
		{
			int startSize = inputs.size();
			for(int i = 0; i < startSize; i++)
			{
				//MyUtils.showTab(inputs.get(i), "inputs.get(i) " + (i+1) + " / " + startSize);
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
			if(i.length != 1 && i[0].length!=1)
			{
				System.err.println("Error in serial convolutions");
			}
			//System.out.print(v + " - ");
			finalValue += v/inputs.size(); //Mean the outputs
		}
		//System.out.println();
		return MyUtils.sigmoid(finalValue);
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
