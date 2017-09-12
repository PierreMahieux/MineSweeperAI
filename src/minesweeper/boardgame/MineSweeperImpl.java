package minesweeper.boardgame;

import java.util.ArrayList;

import minesweeper.boardgame.model.BoxStateGame;
import minesweeper.boardgame.model.GameBox;

public class MineSweeperImpl implements MineSweeper
{
	protected ArrayList<GameBox> boxes = new ArrayList<>();
	
	protected final int BOARD_SIZE = 4;
	
	protected int bombNumber = BOARD_SIZE*BOARD_SIZE/4;
	
	public MineSweeperImpl() {
		for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
		{
			boxes.add(new GameBox());
		}
		
		fillWithBombs();		
		
		printBoard();
	}
	
	protected void fillWithBombs()
	{
		int mineCount = 0;
		while(mineCount < bombNumber)
		{
			double maxRand = 0;
			int indexMax = 0;
			for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
			{
				if(boxes.get(indexMax).gameState != BoxStateGame.BOMB)
				{
					double rand = Math.random();
					if( rand > maxRand) 
					{
						maxRand = rand;
						indexMax = i;
					}
				}
			}
			
			boxes.get(indexMax).gameState = BoxStateGame.BOMB;
			mineCount++;
		}
	}
	
	protected void printBoard()
	{
		for(int yi = 0; yi < BOARD_SIZE; yi++)
		{
			for(int xi = 0; xi < BOARD_SIZE; xi++)
			{
				BoxStateGame state = boxes.get(yi*BOARD_SIZE + xi).gameState;
				if(state == BoxStateGame.BOMB)System.out.print("X");
				else System.out.print("O");
			}		
			System.out.println();
		}
	}
}
