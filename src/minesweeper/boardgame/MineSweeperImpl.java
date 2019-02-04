package minesweeper.boardgame;

import java.util.ArrayList;

import minesweeper.boardgame.model.BoxStatePlayer;
import minesweeper.boardgame.model.GameBox;
import minesweeper.boardgame.model.MineSweeperScore;

public class MineSweeperImpl implements MineSweeper
{
	protected ArrayList<GameBox> boxes = new ArrayList<>();

	protected int BOARD_SIZE = 6;

	protected boolean youLost = false;

	protected int bombToGenerate;

	public MineSweeperImpl() {
		this(6);
	}

	public MineSweeperImpl(int size)
	{
		BOARD_SIZE = size;
		
		for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
		{
			boxes.add(new GameBox());
		}
		
		bombToGenerate = (int) ((BOARD_SIZE*BOARD_SIZE)/4);

		fillWithBombs();	

		registerNumbers();
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
			boxes.get(indexMax).isBomb = true;
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
		return boxes.get(xi + yi*BOARD_SIZE).isBomb;
	}

	public void printFullGame()
	{
		for(int yi = 0; yi < BOARD_SIZE; yi++)
		{
			for(int xi = 0; xi < BOARD_SIZE; xi++)
			{
				boolean isBomb = boxes.get(yi*BOARD_SIZE + xi).isBomb;
				if(isBomb)System.out.print("X");
				else if(boxes.get(yi*BOARD_SIZE + xi).number == 0) System.out.print("-");
				else System.out.print(boxes.get(yi*BOARD_SIZE + xi).number);
				System.out.print(" ");
			}		
			System.out.println();
		}
	}

	@Override
	public void printGame()
	{
		for(int yi = 0; yi < BOARD_SIZE; yi++)
		{
			for(int xi = 0; xi < BOARD_SIZE; xi++)
			{
				GameBox box = boxes.get(yi*BOARD_SIZE + xi);
				switch(box.playerState)
				{
				case FLAGGED:
					System.out.print("F");
					break;

				case UNKNOWN:
					System.out.print("O");
					break;

				case OPENED:
					if(box.isBomb)System.out.print("B");
					else System.out.print(box.number == 0 ? "-" : box.number);						
					break;
				}

				System.out.print(" ");
			}		
			System.out.println();
		}
	}

	@Override
	public void putFlagAt(int x, int y) {
		if(youLost)return;
		GameBox box = boxes.get(y*BOARD_SIZE + x);
		if(box.playerState != BoxStatePlayer.UNKNOWN)return;
		box.playerState = BoxStatePlayer.FLAGGED;
	}


	@Override
	public int openBoxAt(int x, int y) {
		return openBoxAt(x, y, false);
	}
	
	private GameBox getBoxAt(int x, int y)
	{
		return boxes.get(y*BOARD_SIZE + x);
	}

	private int openBoxAt(int x, int y, boolean isGame) {
		if(youLost)return -1;
		GameBox target = boxes.get(y*BOARD_SIZE + x);
		if(target.playerState == BoxStatePlayer.OPENED)
		{
			if(!isGame)
			{
				System.err.println("Illegal move : " + x + "." + y);				
			}
			return 0;
		}
		target.playerState = BoxStatePlayer.OPENED;

		if(target.isBomb)
		{
			youLost = true;
		}
		else if(target.number == 0)
		{
			openNeighbours(x,y);
		}

		if(isGameWon()) return 1;

		return target.isBomb ? -1 : 0;
	}

	protected boolean isGameWon()
	{
		boolean everyNoBomb = true;
		boolean everyFlag = true;
		
		for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
		{
			GameBox box = boxes.get(i);
			/** Every NotBomb opened **/
			if(!box.isBomb && box.playerState != BoxStatePlayer.OPENED) everyNoBomb = false;

			/**Every Bomb flagged**/
			if(box.isBomb && box.playerState != BoxStatePlayer.FLAGGED) everyFlag = false;
			
			if(!everyNoBomb && !everyFlag)
			{
				return false;
			}
		}		
		return true;
	}

	protected void openNeighbours(int xi, int yi)
	{
		if(xi != 0 && yi != 0)openBoxAt(xi-1, yi-1, true); 							//TOP LEFT
		if(xi != 0)openBoxAt(xi-1, yi, true);										//LEFT
		if(yi != 0)openBoxAt(xi, yi-1, true);										//TOP
		if(xi != BOARD_SIZE-1 && yi != 0)openBoxAt(xi+1, yi-1, true); 				//TOP RIGHT
		if(xi != BOARD_SIZE-1 && yi != BOARD_SIZE-1)openBoxAt(xi+1, yi+1, true);	//BOTTOM RIGHT
		if(xi != 0 && yi != BOARD_SIZE-1)openBoxAt(xi-1, yi+1, true); 				//BOTTOM LEFT
		if(yi != BOARD_SIZE-1)openBoxAt(xi, yi+1, true);							//BOTTOM
		if(xi != BOARD_SIZE-1)openBoxAt(xi+1, yi, true);							//RIGHT
	}

	@Override
	public int getSquareSize() {
		return BOARD_SIZE;
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

	@Override
	public int[][] getBoardSnapshot() {
		int tab[][] = new int[BOARD_SIZE][BOARD_SIZE];
		for(int yi = 0; yi < BOARD_SIZE; yi++)
		{
			for(int xi = 0; xi < BOARD_SIZE; xi++)
			{
				int i = yi * BOARD_SIZE + xi;
				switch(boxes.get(i).playerState)
				{
				case FLAGGED:
					tab[yi][xi] = -2;
					break;

				case UNKNOWN:
					tab[yi][xi] = -01;
					break;

				case OPENED:
					tab[yi][xi] = boxes.get(i).number;				
					break;
				}
			}
		}
		return tab;
	}
	
	
	protected int countUncoveredCells()
	{
		int count = 0;
		for(int i = 0; i < BOARD_SIZE*BOARD_SIZE; i++)
		{
			if(boxes.get(i).playerState != BoxStatePlayer.UNKNOWN)
			{
				count++;
			}
		}
		
		return count;
	}

	public static MineSweeper getAStartedBoard(int boardSize, int openBoxes)
	{
		MineSweeperImpl board = null;
		
		while(board == null)
		{
			board = new MineSweeperImpl(boardSize);
			int result = 0;
			
			for(int i = 0; i < openBoxes && result == 0; i++)
			{
				int randX = (int) (Math.random() * boardSize);
				int randY = (int) (Math.random() * boardSize);
				
				result = board.openBoxAt(randX, randY, true);
			}
			
			if(result != 0)
			{
				board = null;
			}
		}
		
		return board;
	}


	public static MineSweeper getTestBoard(int boardSize, boolean bombAtCenter, int uncoveredCells)
	{
		MineSweeperImpl board = new MineSweeperImpl(boardSize);
		while(board.bombAt((boardSize-1)/2, (boardSize-1)/2) != bombAtCenter || (!bombAtCenter && board.getBoxAt((boardSize-1)/2, (boardSize-1)/2).number == 0))
		{
			board = new MineSweeperImpl(boardSize);
		}
		
		for(int yi = 0; yi < boardSize; yi++)
		{
			for(int xi = 0; xi < boardSize; xi++)
			{
				if(board.getBoxAt(xi, yi).number == 0 && board.getBoxAt(xi, yi).playerState == BoxStatePlayer.UNKNOWN)
				{
					board.openBoxAt(xi, yi, true);
				}
			}
		}
		
		uncoveredCells -= board.countUncoveredCells();
		
		for(int i = 0; i < uncoveredCells; i++)
		{
			int randX = (int) (Math.random() * boardSize);
			int randY = (int) (Math.random() * boardSize);
			
			while(board.bombAt(randX, randY) || board.getBoxAt(randX, randY).playerState != BoxStatePlayer.UNKNOWN || (randX == (boardSize-1)/2&&randY == (boardSize-1)/2))
			{
				randX = (int) (Math.random() * boardSize);
				randY = (int) (Math.random() * boardSize);
			}
			
			board.openBoxAt(randX, randY, true);
		}
		return board;
	}
}
