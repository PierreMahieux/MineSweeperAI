package minesweeper.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import utils.Matrix;
import utils.MyUtils;

public class MyCNN {

	protected ArrayList<Matrix> cvLayers = new ArrayList<>();

	public MyCNN(int nbLayers)
	{
		for(int i = 0; i < nbLayers; i++)
		{
			cvLayers.add(Matrix.random(3, 3));
		}
	}
	
	public Matrix getMatrix(int indexInLayer)
	{
		return cvLayers.get(indexInLayer);
	}
	
	public int getNbLayers() {return cvLayers.size();}

	public MyCNN(List<Matrix> matrices)
	{
		cvLayers.addAll(matrices);
	}

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
		for(Matrix m : cvLayers)
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
		for(Matrix m : cvLayers)
		{
			m.show();
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
