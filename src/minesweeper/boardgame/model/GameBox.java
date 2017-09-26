package minesweeper.boardgame.model;

public class GameBox {
	
	public boolean isBomb;
	public BoxStatePlayer playerState;
	public Integer number;
	
	public GameBox() {
		this.playerState = BoxStatePlayer.UNKNOWN;
		this.isBomb = false;
		this.number = -1;
	}
}
