package minesweeper.ai;

import java.util.ArrayList;

import minesweeper.model.Brain;
import utils.Matrix;
import minesweeper.boardgame.MineSweeper;

public class Ann {

	protected final int SIZE_OUTPUT = 4;
	
	private Brain brain;
	private ArrayList<Integer> layers = new ArrayList<Integer>();
	private Matrix outputs = null, inputs = null;
	ArrayList<Integer>  playerInputs = new ArrayList<Integer>();
		
	/***
	 * Create an Artificial Neural Network based on a given boardgame.
	 * The ANN will have 1 input neuron per boardgame box and 4 output neurons.
	 * Hidden layers are generated by using the "pyramid rule" as exposed in the class from Pierre Chevaillier.
	 * @param board
	 */
	public Ann(MineSweeper board) {
		int boardSize = board.getSquareSize();
		int inputLayerSize = boardSize*boardSize;
		
		double r = Math.cbrt(inputLayerSize/SIZE_OUTPUT);
		
		layers.add(inputLayerSize);
		layers.add((int)(SIZE_OUTPUT*r*r));
		layers.add((int)(SIZE_OUTPUT*r));
		layers.add(SIZE_OUTPUT);
		
		brain = new Brain(layers);
	}
	
	public String toString() {
		return "Brain : " + brain.toString() + "\nLayers : " + layers.toString();
	}
	
	/***
	 * Just play a turn of the game
	 */
	public void playTurn(MineSweeper board) {
		inputs = convertBoardToMatrix(board);
		outputs = brain.compute(inputs);
		System.out.println(outputs);
		convertOutputs(board);
		System.out.println(playerInputs);
	}
	
	/**
	 * Post treatment of the output so they can be used as player inputs on the game
	 */
	public void convertOutputs(MineSweeper board) {
		// What is the meaning of the outputs :
		// x,y : coordinates of the place we will treat
		int boardSize = board.getSquareSize();
		playerInputs.add((int) ((outputs.get(0, 0)+1)*boardSize/2));
		playerInputs.add((int) ((outputs.get(0, 1)+1)*boardSize/2));
		
		// action : will we dig or flag this place
		if (outputs.get(0, 2)>outputs.get(0, 3)) {
			playerInputs.add(0);
		}
		else {
			playerInputs.add(1);
		}
	}
	
	/***
	 * Converts a MineSweeper board into an input Matrix.
	 * @param board
	 * @return the Matrix representing the board
	 */
	public Matrix convertBoardToMatrix(MineSweeper board) {
		int[][] intSnap = board.getBoardSnapshot();
		double[] doubleSnap = new double[intSnap.length*intSnap.length];

		for (int yi = 0; yi < intSnap.length; yi++ ) {
			for (int xi = 0; xi < intSnap.length; xi++ ) {
			doubleSnap[yi*intSnap.length + xi] = (double)intSnap[yi][xi];
			}
		}
		double [][] in = {doubleSnap};
		
		return new Matrix(in);
	}
}
