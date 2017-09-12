package minesweeper.boardgame.model;

public class GameBox {
	BoxStateGame gameState;
	BoxStatePlayer playerState;
	Integer number;
	
	public GameBox() {
		this.playerState = BoxStatePlayer.UNKNOWN;
	}
}
