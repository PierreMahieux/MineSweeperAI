package minesweeper.boardgame.model;

public class GameBox {
	
	public BoxStateGame gameState;
	public BoxStatePlayer playerState;
	public Integer number;
	
	public GameBox() {
		this.playerState = BoxStatePlayer.UNKNOWN;
	}
}
