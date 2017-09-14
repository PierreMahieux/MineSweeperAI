package minesweeper.launcher;

import minesweeper.ai.Ann;
import minesweeper.boardgame.MineSweeper;
import minesweeper.boardgame.MineSweeperImpl;

public class Launcher {

	public static void main(String[] args)
	{
		System.out.println("perdu");
		// pute
		
		MineSweeper boardGame = new MineSweeperImpl();
		boardGame.toString(); //GETTING RID OF THE WARNING UNUSED
		
		Ann ann = new Ann(boardGame);
		ann.toString();
	}

}
