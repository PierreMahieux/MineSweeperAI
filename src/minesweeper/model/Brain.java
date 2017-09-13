package minesweeper.model;

import java.util.ArrayList;

import utils.Matrix;

public class Brain
{
	// Matrice de passage
	private ArrayList<Matrix> weigths = new ArrayList<Matrix>();
	private ArrayList<Matrix> states = new ArrayList<Matrix>();
	

	/**
	 *  Creates a brain from another brain's data
	 * @param copy Brain to Copy
	 */
	public Brain(Brain copy)
	{
		for(Matrix m : copy.weigths)
		{
			weigths.add(new Matrix(m));
		}
		for(Matrix m : copy.states)
		{
			states.add(new Matrix(m));
		}
	}
	
	/**
	 * Brain(1,2,3,4) will create a Brain with 1 input, two hidden layers of 2 and 3 neurons, and 4 outputs.
	 * @param levels : a list of integer that will represent the number of neurons by level.
	 */
	public Brain(int... levels)
	{
		if(levels.length < 2)
		{
			System.err.println("Brain too litle. Ask Pierre if you want to know what it feels like.");
			return;
		}
		
		for(int i = 0; i < levels.length; i++)
		{
			// Add a state matrix sized (1, number of neurons in this level)
			states.add(new Matrix(1,levels[i]));
			
			//As weights are transition between states, there's one state level more than weigths 
			if(i!=levels.length-1)
			{
				// Create a random matrix sized (number of neurons in previous State, number of neurons in next State)
				weigths.add(Matrix.random(levels[i], levels[i+1]));
			}
		}	
	}
		
	/**
	 * Brain(levels) will create a Brain depending on the numbers in the "levels" ArrayList.
	 * Same result as Brain(int.. levels);
	 * @param levels : a list of integer that will represent the number of neurons by level.
	 */
	public Brain(ArrayList<Integer> levels)
	{
		if(levels.size() < 2)
		{
			System.err.println("Brain too litle. Ask Pierre if you want to know what it feels like.");
			return;
		}
		
		for(int i = 0; i < levels.size(); i++)
		{
			// Add a state matrix sized (1, number of neurons in this level)
			states.add(new Matrix(1,levels.get(i)));
			
			//As weights are transition between states, there's one state level more than weigths 
			if(i!=levels.size()-1)
			{
				// Create a random matrix sized (number of neurons in previous State, number of neurons in next State)
				weigths.add(Matrix.random(levels.get(i), levels.get(i+1)));
			}
		}	
	}
	
	public ArrayList<Matrix> getStates()
	{
		return new ArrayList<>(states);
	}
	
	public ArrayList<Matrix> getWeights()
	{
		return new ArrayList<>(weigths);
	}
	
	/**
	 * Execute the Brain with input Data
	 * @param inputs State matrix of the inputs
	 * @return a Matrix representing the output of the neural network
	 */
	public Matrix compute(Matrix inputs)
	{
		states.get(0).set(inputs);
		for(int i = 1; i < states.size(); i++)
		{
			states.get(i).set(states.get(i-1).times(weigths.get(i-1)));
			states.get(i).timesTanH();
			states.get(i).masque(0.01);  //0.01 Treshold for neurons			
		}
			
		return states.get(states.size()-1);
	}
	
	/**
	 * Reproduction of two brains, letting mom and dad untouched
	 * @param pap 
	 * @param mam
	 * @return A child Brain with some Dad and Mom features and maybe random mutations
	 */
	static public Brain fusion(Brain pap, Brain mam)
	{
		Brain child = new Brain(pap);
		if(pap.weigths.size() != mam.weigths.size())return null;
		
		for(int i = 0; i < pap.weigths.size() ; i++)
		{
			child.weigths.get(i).set(pap.weigths.get(i).fusion(mam.weigths.get(i)));
		}
		//child.show();
		return child;
	}
	
	public String toString()
	{
		String s = "";
		for(Matrix m : weigths)
		{
			s+=m.toString();
		}
		return s;
	}
	
	public String getType()
	{
		return (weigths.size() + " ");
	}
	
	public void show()
	{
		System.out.println("Weigths :");
		for(Matrix m : weigths)
		{
			m.show();
		}
		System.out.println("\nStates :");
		
		for(Matrix m : states)
		{
			m.show();
		}
		System.out.println("\n\n");
	}
	
}

