package minesweeper.boardgame;

import minesweeper.boardgame.model.MineSweeperScore;

public interface MineSweeper {
	public void putFlagAt(int x, int y);
	
	/**
	 * 
	 * @param x xpos
	 * @param y ypos
	 * @return 0 as long as we can play. -1 means game lost, +1 you won !
	 */
	public int openBoxAt(int x, int y);
	
	public int getSquareSide();
	
	public MineSweeperScore getEndGameData();
	
	/**
	 * Returns a list of integer sized getSquaredSize�.
	 * @return -2 Means FLAG, -1 means UNKNOWN, 0>8 The amount of nearby mines
	 */
	public int[] getBoardSnapshot();
}
