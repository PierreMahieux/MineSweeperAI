package minesweeper.boardgame;

import java.util.ArrayList;

import minesweeper.boardgame.model.GameBox;

public class MineSweeperImpl implements MineSweeper
{
	protected ArrayList<GameBox> boxes = new ArrayList<>();
	
	protected final int BOARD_SIZE = 4;
	
	public MineSweeperImpl() {
		for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
		{
			boxes.add(new GameBox());
		}
		
		printBoard();
	}
	
	protected void printBoard()
	{
		for(int yi = 0; yi < BOARD_SIZE; yi++)
		{
			for(int xi = 0; xi < BOARD_SIZE; xi++)
			{
				boxes.get(yi*BOARD_SIZE + xi);
				System.out.print("O");
			}		
			System.out.println();
		}
	}
}
