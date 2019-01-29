package utils;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Organizer<T extends Scorable> extends ArrayList<T>
{
	private boolean search = false;
	
	public Organizer()
	{
		super();
		this.clear();
	}
	public boolean add(T a)
	{
		for(int i=0; i<this.size() && !search; i++)
		{
			if(a.getScore()>this.get(i).getScore())
			{
				this.add(i, a);
				search = true;
			}
		}
		if(!search)
		{
			this.add(this.size(),a);
		}
		search = false;
		return true;
	}
	
	public double moyenne()
	{
		double moyenne = 0;
		for(int i=0; i<this.size(); i++)
		{
			moyenne += this.get(i).getScore();
		}
		moyenne /= size();
		return moyenne;
	}
	
	public void show()
	{
		System.out.print(size() + " : ");
		for(int i = 0; i<size();i++)
		{
			System.out.print(" : " + this.get(i).getScore());
		}
		System.out.println();
	}
}

