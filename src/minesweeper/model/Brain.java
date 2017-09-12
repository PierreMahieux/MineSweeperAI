package minesweeper.model;

import java.util.ArrayList;

import utils.Matrix;

public class Brain
{
	
	private ArrayList<Matrix> weigths = new ArrayList<Matrix>(); 	//passage
	private ArrayList<Matrix> states = new ArrayList<Matrix>(); 	//Etats
	
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
	
	public Brain(ArrayList<Matrix> list)
	{
		for(Matrix m : list)
		{
			weigths.add(new Matrix(m));
			states.add(new Matrix(1,m.getSize()[0]));
		}
		states.add(new Matrix(1,list.get(list.size()-1).getSize()[1]));
		
		show();
	}
	
	public Brain(int... levels)
	{
		if(levels.length < 2)
		{
			System.err.println("Brain too litle. Ask Pierre if you want to know what it feels like.");
			return;
		}
		
		for(int i = 0; i < levels.length; i++)
		{
			states.add(new Matrix(1,levels[i]));
			
			if(i!=levels.length-1)
			{
				weigths.add(Matrix.random(levels[i], levels[i+1]));
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

