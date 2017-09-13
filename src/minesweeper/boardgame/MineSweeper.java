package minesweeper.boardgame;

import minesweeper.boardgame.model.MineSweeperScore;

public interface MineSweeper {
	public void putFlagAt(int x, int y);
	public boolean openBoxAt(int x, int y);
	
	public int getSquaredSize();
	
	public int getScore();
	
	public MineSweeperScore getEndGameData();
}
