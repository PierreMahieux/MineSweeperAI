package minesweeper.launcher;

import minesweeper.boardgame.MineSweeper;
import minesweeper.boardgame.MineSweeperImpl;

public class Launcher {

	public static void main(String[] args)
	{
		System.out.println("perdu");
		
		MineSweeper boardGame = new MineSweeperImpl();
	}

}
