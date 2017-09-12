package minesweeper.boardgame;

import java.util.ArrayList;

import minesweeper.boardgame.model.BoxStateGame;
import minesweeper.boardgame.model.GameBox;

public class MineSweeperImpl implements MineSweeper
{
	protected ArrayList<GameBox> boxes = new ArrayList<>();
	
	protected final int BOARD_SIZE = 6;
	
	protected int bombNumber = BOARD_SIZE*BOARD_SIZE/4;
	
	public MineSweeperImpl() {
		for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
		{
			boxes.add(new GameBox());
		}
		
		fillWithBombs();	
		
		registerNumbers();
		
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
	
	protected void registerNumbers()
	{
		for(int yi = 0; yi < BOARD_SIZE; yi++)
		{
			for(int xi = 0; xi < BOARD_SIZE; xi++)
			{
				if(BoxStateGame.BOMB != boxes.get(yi*BOARD_SIZE + xi).gameState)
				{
					boxes.get(yi*BOARD_SIZE + xi).number = countBombsAround(xi, yi);
				}
			}
		}
	}
	
	protected int countBombsAround(int xi, int yi)
	{
		int bombs = 0;
		if(xi != 0 && yi != 0)bombs += bombAt(xi-1, yi-1) ? 1:0; 					//TOP LEFT
		if(xi != 0)bombs += bombAt(xi-1, yi) ? 1:0;									//LEFT
		if(yi != 0)bombs += bombAt(xi, yi-1) ? 1:0;									//TOP
		if(xi != BOARD_SIZE-1 && yi != 0)bombs += bombAt(xi+1, yi-1) ? 1:0; 			//TOP RIGHT
		if(xi != BOARD_SIZE-1 && yi != BOARD_SIZE-1)bombs += bombAt(xi+1, yi+1) ? 1:0;	//BOTTOM RIGHT
		if(xi != 0 && yi != BOARD_SIZE-1)bombs += bombAt(xi-1, yi+1) ? 1:0; 			//BOTTOM LEFT
		if(yi != BOARD_SIZE-1)bombs += bombAt(xi, yi+1) ? 1:0;						//BOTTOM
		if(xi != BOARD_SIZE-1)bombs += bombAt(xi+1, yi) ? 1:0;						//RIGHT
		
		return bombs;
	}
	
	protected boolean bombAt(int xi, int yi)
	{
		return boxes.get(xi + yi*BOARD_SIZE).gameState == BoxStateGame.BOMB;
	}
	
	protected void printBoard()
	{
		for(int yi = 0; yi < BOARD_SIZE; yi++)
		{
			for(int xi = 0; xi < BOARD_SIZE; xi++)
			{
				BoxStateGame state = boxes.get(yi*BOARD_SIZE + xi).gameState;
				if(state == BoxStateGame.BOMB)System.out.print("X");
				else System.out.print(boxes.get(yi*BOARD_SIZE + xi).number);
				System.out.print(" ");
			}		
			System.out.println();
		}
	}
}
