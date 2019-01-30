package minesweeper.ai;

import java.awt.Point;
import java.util.ArrayList;

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

	/*
	public MyCNN(List<Matrix> matrices)
	{
		cvFilters.addAll(matrices);
	}
	*/

	public Point getBoxToFlip(int[][] theBoard)
	{
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

		//showBoard(board);
		
		ArrayList<double[][]> inputs = new ArrayList<>();
		//TODO THIS IS DEBUG ONLY
		double[][] firstInput = new double[9][9];
		for(int yi = 0; yi < 9; yi++)
		{
			for(int xi = 0; xi < 9; xi++)
			{
				firstInput[yi][xi] = board[yi][xi];
			}				
		}
		inputs.add(firstInput);
		
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
		
		//TODO TREAT SINGE SCALARS
		
		/*
		for(Matrix m : cvFilters)
		{
			board = MyUtils.convolution(m, board);
		}
		
		for(int yi = 0; yi < theBoard.length; yi++)
		{
			for(int xi = 0; xi < theBoard[0].length; xi++)
			{
				if(theBoard[yi][xi] != -1)
				{
					board[yi][xi] = -10000.0;				
				}
			}
		}

		//showBoard(board);
		*/
		Point max = new Point(0,0);
		for(int yi = 0; yi < board.length; yi++)
		{
			for(int xi = 0; xi < board[0].length; xi++)
			{
				if(board[max.y][max.x] < board[yi][xi])
				{
					max.setLocation(xi, yi);
				}
			}
		}
		
		return max;
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
