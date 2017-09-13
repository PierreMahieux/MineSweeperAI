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
	
	public int getSquaredSize();
	
	public int getScore();
	
	public MineSweeperScore getEndGameData();
}
