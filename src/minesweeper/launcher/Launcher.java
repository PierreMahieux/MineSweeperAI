package minesweeper.launcher;

import minesweeper.boardgame.MineSweeper;
import minesweeper.boardgame.MineSweeperImpl;
import minesweeper.ai.Ann;

public class Launcher {

	public static void main(String[] args)
	{
		System.out.println("perdu");
		// pute
		
		MineSweeper boardGame = new MineSweeperImpl();
		boardGame.toString(); //GETTING RID OF THE WARNING UNUSED
		
		//TEST creation ANN
		Ann ann = new Ann(boardGame);
				
		System.out.println("OVER");
	}

}
