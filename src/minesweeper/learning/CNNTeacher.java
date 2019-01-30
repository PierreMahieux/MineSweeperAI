package minesweeper.learning;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import minesweeper.ai.MyCNN;
import minesweeper.boardgame.MineSweeper;
import minesweeper.boardgame.MineSweeperImpl;
import utils.Matrix;
import utils.Organizer;
import utils.Scorable;

public class CNNTeacher {
	
	private int brainNumber = 1;
	
	int boardSize = 10;
	
	private Organizer<Brain> brainList = new Organizer<>();

	public CNNTeacher()
	{
		//testMerge();
		
		ArrayList<MyCNN> firstBrains = new ArrayList<>();
		
		for(int i = 0; i < this.brainNumber; i++)
		{
			firstBrains.add(new MyCNN(2,4));
		}
		
		
		runStep(firstBrains); //fills brainLists with sorted brains
		/*
		for(int i = 0; i < 100; i++)
		{
			ArrayList<MyCNN> newBrainList = getRandomBrainsFrom(brainList.subList(0, 20));
			brainList.clear();
			runStep(newBrainList);
		}

		playAGameForMe(brainList.get(0).brain);
		playAGameForMe(brainList.get(0).brain);
		playAGameForMe(brainList.get(0).brain);
		playAGameForMe(brainList.get(0).brain);
		*/
	}
	
	@SuppressWarnings("unused")
	private void testMerge()
	{
		MyCNN a = new MyCNN(2,1);
		MyCNN b = new MyCNN(2,1);
		//MyCNN c = merge(a,b);
		
		System.out.println("a");
		a.show();
		System.out.println("b");
		b.show();
		System.out.println("c");
		//c.show();
	}
	
	private void runStep(ArrayList<MyCNN> list)
	{		
		for(MyCNN cnn : list)
		{
			Brain b = new Brain(cnn);
			b.score = playAGame(b.brain);		
			brainList.add(b);
		}
		
		for(int i = 0; i < 10; i++)
		{	
			String s = " - ";
			if(i == 0)s = "";
			System.out.print(s + brainList.get(i).score);
		}		
		System.out.println();
	}
	
	/*
	public MyCNN merge(MyCNN aCNN, MyCNN anotherCNN)
	{
		if(aCNN.getNbLayers() != anotherCNN.getNbLayers())
		{
			System.err.println("SIZE ERROR");
			return null;
		}
		
		//CNN have same amount of layers
		ArrayList<Matrix> matrices = new ArrayList<>();
		
		int cut = (int) (Math.random() * aCNN.getNbLayers());
		
		for(int i = 0; i < aCNN.getNbLayers(); i++)
		{
			Matrix mA = aCNN.getMatrix(i);
			Matrix mB = anotherCNN.getMatrix(i);
			
			int height = mA.getSize()[0];
			int width = mA.getSize()[1];
			Matrix newMatrix = new Matrix(height, width);
		
			for(int yi = 0; yi < height; yi++)
			{
				for(int xi = 0; xi < width; xi++)
				{
					double noise = 1.0;
					if(Math.random() > 0.7)
					{
						noise = Math.random() > 0.5 ? 1 : -1;
						noise += Math.random() * 0.1;
					}					
					
					if(i > cut)
					{
						newMatrix.set(yi, xi, mB.get(yi, xi)*noise);
					}
					else
					{
						newMatrix.set(yi, xi, mA.get(yi, xi)*noise);
					}
					
					
				}				
			}
			
			matrices.add(newMatrix);
		}

		return new MyCNN(matrices);
	}
	*/
	private ArrayList<MyCNN> getRandomBrainsFrom(List<Brain> list)
	{
		ArrayList<MyCNN> a = new ArrayList<>();
		
		for(Brain b : list)
		{
			a.add(b.brain);
		}

		MyCNN brainA = list.get((int) (Math.random() * list.size())).brain;
		MyCNN brainB = list.get((int) (Math.random() * list.size())).brain;
		
		while(brainA == brainB)
		{
			brainB = list.get((int) (Math.random() * list.size())).brain;
		}
		/*
		while(a.size() < this.brainNumber)
		{
			a.add(merge(brainA, brainB));
		}
		*/
		return a;
	}
	
	private void playAGameForMe(MyCNN brain)
	{
		MineSweeper boardGame = new MineSweeperImpl(boardSize); 
		int f = 0;

		while(f==0)
		{
			boardGame.printGame();
			Point p = brain.getBoxToFlip(boardGame.getBoardSnapshot());
			//System.out.println(p.x + " " + p.y);
			f = boardGame.openBoxAt(p.x, p.y);
			
			
			System.out.println();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		boardGame.printGame();
		System.out.println((f == -1 ? "Lost" : "Victory") + " ! ");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int playAGame(MyCNN brain)
	{
		int score = 0;
		for(int i = 0; i < 5; i++)
		{
			MineSweeper boardGame = new MineSweeperImpl(boardSize); 
			int currentScore = 0;
			int f = 0;

			while(f==0)
			{
				Point p = brain.getBoxToFlip(boardGame.getBoardSnapshot());
				//System.out.println(p.x + " " + p.y);
				currentScore++;
				f = boardGame.openBoxAt(p.x, p.y);
				//boardGame.printGame();
			}

			//System.out.println((f == -1 ? "Lost" : "Victory") + " ! " + score);
			score += currentScore;
			
			if(f==1)
			{
				//boardGame.printGame();
				score += 100;
			}
		}
		return score/5;
	}
	
	public class Brain implements Scorable
	{
		public MyCNN brain;
		public int score;
		
		@Override
		public int getScore()
		{
			return score;
		}
		
		public Brain(MyCNN b) {
			brain = b;
			score = 0;
		}
		
	}
}
