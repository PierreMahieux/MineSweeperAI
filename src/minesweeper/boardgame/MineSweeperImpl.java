package minesweeper.boardgame;

import java.util.ArrayList;

import minesweeper.boardgame.model.BoxStatePlayer;
import minesweeper.boardgame.model.GameBox;
import minesweeper.boardgame.model.MineSweeperScore;

public class MineSweeperImpl implements MineSweeper
{
	protected ArrayList<GameBox> boxes = new ArrayList<>();
	
	protected final int BOARD_SIZE = 6;
	
	protected int bombToGenerate = BOARD_SIZE*BOARD_SIZE/4;
	
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
		while(mineCount <= bombToGenerate)
		{
			double maxRand = 0;
			int indexMax = 0;
			for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
			{
				if(!boxes.get(i).isBomb)
				{
					double rand = Math.random();
					if( rand > maxRand) 
					{
						maxRand = rand;
						indexMax = i;
					}
				}
			}			
			boxes.get(indexMax).isBomb = true;;
			mineCount++;
		}
	}
	
	protected void registerNumbers()
	{
		for(int yi = 0; yi < BOARD_SIZE; yi++)
		{
			for(int xi = 0; xi < BOARD_SIZE; xi++)
			{
				if(!boxes.get(yi*BOARD_SIZE + xi).isBomb)
				{
					boxes.get(yi*BOARD_SIZE + xi).number = countBombsAround(xi, yi);
				}
			}
		}
	}
	
	protected int countBombsAround(int xi, int yi)
	{
		int bombs = 0;
		if(xi != 0 && yi != 0)bombs += bombAt(xi-1, yi-1) ? 1:0; 						//TOP LEFT
		if(xi != 0)bombs += bombAt(xi-1, yi) ? 1:0;										//LEFT
		if(yi != 0)bombs += bombAt(xi, yi-1) ? 1:0;										//TOP
		if(xi != BOARD_SIZE-1 && yi != 0)bombs += bombAt(xi+1, yi-1) ? 1:0; 			//TOP RIGHT
		if(xi != BOARD_SIZE-1 && yi != BOARD_SIZE-1)bombs += bombAt(xi+1, yi+1) ? 1:0;	//BOTTOM RIGHT
		if(xi != 0 && yi != BOARD_SIZE-1)bombs += bombAt(xi-1, yi+1) ? 1:0; 			//BOTTOM LEFT
		if(yi != BOARD_SIZE-1)bombs += bombAt(xi, yi+1) ? 1:0;							//BOTTOM
		if(xi != BOARD_SIZE-1)bombs += bombAt(xi+1, yi) ? 1:0;							//RIGHT
		
		return bombs;
	}
	
	protected boolean bombAt(int xi, int yi)
	{
		return !boxes.get(xi + yi*BOARD_SIZE).isBomb;
	}
	
	protected void printBoard()
	{
		for(int yi = 0; yi < BOARD_SIZE; yi++)
		{
			for(int xi = 0; xi < BOARD_SIZE; xi++)
			{
				boolean isBomb = boxes.get(yi*BOARD_SIZE + xi).isBomb;
				if(isBomb)System.out.print("X");
				else System.out.print(boxes.get(yi*BOARD_SIZE + xi).number);
				System.out.print(" ");
			}		
			System.out.println();
		}
	}

	@Override
	public void putFlagAt(int x, int y) {
		boxes.get(y*BOARD_SIZE + x).playerState = BoxStatePlayer.FLAGGED;
	}

	@Override
	public boolean openBoxAt(int x, int y) {
		boxes.get(y*BOARD_SIZE + x).playerState = BoxStatePlayer.OPENED;
		return !bombAt(x,y);
	}

	@Override
	public int getSquaredSize() {
		return BOARD_SIZE;
	}

	@Override
	public int getScore() {
		int score = 0;
		
		for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
		{
			if(boxes.get(i).playerState == BoxStatePlayer.OPENED) score++;
		}		
		
		return score;
	}

	@Override
	public MineSweeperScore getEndGameData() {
		return new MineSweeperScore() {	
			
			@Override
			public int getOpenedCases() {
				int score = 0;				
				for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
				{
					if(boxes.get(i).playerState == BoxStatePlayer.OPENED) score++;
				}
				return score;
			}	
			
			@Override
			public int getFlaggedMines() {
				int score = 0;				
				for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
				{
					if(boxes.get(i).playerState == BoxStatePlayer.FLAGGED && boxes.get(i).isBomb) score++;
				}
				return score;
			}	
			
			@Override
			public int getFlagNumber() {
				int score = 0;				
				for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
				{
					if(boxes.get(i).playerState == BoxStatePlayer.FLAGGED) score++;
				}
				return score;
			}
		};
	}
}
