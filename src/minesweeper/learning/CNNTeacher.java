package minesweeper.learning;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import minesweeper.ai.MyCNN;
import minesweeper.boardgame.MineSweeper;
import minesweeper.boardgame.MineSweeperImpl;
import utils.Matrix;
import utils.Organizer;
import utils.Scorable;

public class CNNTeacher {

	private int brainNumber = 500;
	private int keptBrains = 20;

	int boardSize = 20;

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
		
		while(true)
		{
			for(int i = 0; i < 100; i++)
			{
				System.out.println((i+1) + " / 100");
				ArrayList<MyCNN> newBrainList = getRandomBrainsFrom(brainList.subList(0, keptBrains));
				brainList.clear();
				runStep(newBrainList);
			}

			playAGameForMe(brainList.get(0).brain);
			playAGameForMe(brainList.get(0).brain);
			playAGameForMe(brainList.get(0).brain);
		}
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
		long start = new Date().getTime();
		for(int c = 0; c < 10; c++)
		{
			if(c==0||c==9)System.out.print("|");
			else System.out.print("-");
		}
		int c = 0;
		System.out.println();
		for(MyCNN cnn : list)
		{
			Brain b = new Brain(cnn);
			b.score = playAGame(b.brain);		
			brainList.add(b);


			if((c * 100.0 / list.size())%10 == 9)
			{
				System.out.print("|");
			}

			++c;
		}
		System.out.print(" after " + ((new Date().getTime() - start) / 1000) + " s.\n Best : ");

		for(int i = 0; i < keptBrains && i < brainList.size(); i++)
		{	
			String s = " - ";
			if(i == 0)s = "";
			System.out.print(s + brainList.get(i).score);
		}		
		System.out.println();
	}


	public MyCNN merge(MyCNN aCNN, MyCNN anotherCNN)
	{

		ArrayList<ArrayList<Matrix>> matricesLists = new ArrayList<>();

		if(aCNN.getNbLayers() != anotherCNN.getNbLayers())
		{
			System.err.println("SIZE ERROR");
			return null;
		}

		//CNN have same amount of layers

		int cut = (int) (Math.random() * aCNN.getNbLayers());

		for(int i = 0; i < aCNN.getNbLayers(); i++)
		{
			ArrayList<Matrix> matrices = new ArrayList<>();
			for(int li = 0; li < aCNN.getNbFilters(); li++)
			{
				Matrix mA = aCNN.getMatrix(i,li);
				Matrix mB = anotherCNN.getMatrix(i,li);

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

			matricesLists.add(matrices);
		}

		return new MyCNN(matricesLists);
	}

	private ArrayList<MyCNN> getRandomBrainsFrom(List<Brain> list)
	{
		ArrayList<MyCNN> a = new ArrayList<>();

		for(Brain b : list)
		{
			a.add(b.brain);
		}
		
		while(a.size() < this.brainNumber)
		{

			MyCNN brainA = list.get((int) (Math.pow(Math.random(),4) * list.size())).brain;
			MyCNN brainB = list.get((int) (Math.pow(Math.random(),4) * list.size())).brain;

			while(brainA == brainB)
			{
				brainB = list.get((int) (Math.random() * list.size())).brain;
			}

			a.add(merge(brainA, brainB));
		}

		return a;
	}

	private void playAGameForMe(MyCNN brain)
	{
		MineSweeper boardGame = MineSweeperImpl.getAStartedBoard(boardSize, 5); 
		int f = 0;
		int score = -1;
		while(f==0)
		{
			boardGame.printGame();
			Point p = brain.getBoxToFlip(boardGame.getBoardSnapshot());
			//System.out.println(p.x + " " + p.y);
			f = boardGame.openBoxAt(p.x, p.y);

			score++;

			System.out.println();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		boardGame.printGame();
		System.out.println((f == -1 ? "Lost" : "Victory") + " ! " + score);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int playAGame(MyCNN brain)
	{
		int score = 0;
		for(int i = 0; i < 5; i++)
		{
			MineSweeper boardGame = MineSweeperImpl.getAStartedBoard(boardSize, 5); 
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
				score += 500;
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
