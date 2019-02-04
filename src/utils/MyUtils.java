package utils;

import java.awt.Point;
import java.util.ArrayList;

public class MyUtils {
	
	public static double sigmoid(double input)
	{
		return 1/(1+Math.exp(-input));
	}

	public static double[][] convolution(Matrix m, double[][] image)
	{
		double[][] result = new double[image.length][image[0].length];

		for(int yi = 0; yi < image.length; yi++)
		{
			for(int xi = 0; xi < image[0].length; xi++)
			{
				double tmp = 0;
				int y = 0;
				int x = 0;
				double mValue = 0;


				//TOP
				mValue = m.get(0, 1);
				x = xi;
				y = yi-1;
				if(isInside(y, x, image))
				{
					tmp += (mValue * image[y][x]);					
				}

				//TOP RIGHT
				mValue = m.get(0, 2);
				x = xi+1;
				y = yi-1;
				if(isInside(y, x, image))
				{
					tmp += (mValue * image[y][x]);					
				}


				//RIGHT
				mValue = m.get(1, 2);
				x = xi+1;
				y = yi;
				if(isInside(y, x, image))
				{
					tmp += (mValue * image[y][x]);					
				}


				//BOTRIGHT
				mValue = m.get(2, 2);
				x = xi+1;
				y = yi+1;
				if(isInside(y, x, image))
				{
					tmp += (mValue * image[y][x]);					
				}


				//BOT
				mValue = m.get(2, 1);
				x = xi;
				y = yi+1;
				if(isInside(y, x, image))
				{
					tmp += (mValue * image[y][x]);					
				}


				//BOTLEFT
				mValue = m.get(2, 0);
				x = xi-1;
				y = yi+1;
				if(isInside(y, x, image))
				{
					tmp += (mValue * image[y][x]);					
				}


				//LEFT
				mValue = m.get(1, 0);
				x = xi-1;
				y = yi;
				if(isInside(y, x, image))
				{
					tmp += (mValue * image[y][x]);					
				}


				//TOPLEFT
				mValue = m.get(0, 0);
				x = xi-1;
				y = yi-1;
				if(isInside(y, x, image))
				{
					tmp += (mValue * image[y][x]);					
				}


				//CENTER
				mValue = m.get(1, 1);
				x = xi;
				y = yi;
				if(isInside(y, x, image))
				{
					tmp += (mValue * image[y][x]);					
				}			

				result[yi][xi] = tmp;
			}	
		}

		return result;
	}


	public static double[][] downSampleConvolution(Matrix m, double[][] image)
	{	
		if(m.getSize()[0] != m.getSize()[1])
		{
			System.err.println("Filter's not a square.");
		}
		
		int matrixOffset = (m.getSize()[0] - 1)/2;

		ArrayList<Point> modifiers = getModifiersForNeighbors(matrixOffset);

		double[][] result = new double[image.length - 2*matrixOffset][image[0].length - 2*matrixOffset];

		for(int yi = matrixOffset, yr = 0; yi < image.length - matrixOffset; yi++, yr++)
		{
			for(int xi = matrixOffset, xr = 0; xi < image[0].length - matrixOffset; xi++, xr++)
			{
				double tmp = 0;
				for(Point p : modifiers)
				{
					double mValue = m.get(matrixOffset + p.y, matrixOffset + p.x);
					int x = xi + p.x;
					int y = yi + p.y;
					if(isInside(y, x, image))
					{
						tmp += (mValue * image[y][x]);					
					}
				}				

				result[yr][xr] = tmp;
			}
		}

		return result;
	}

	public static boolean isInside(int j, int i, double[][] image)
	{
		int height = image.length;
		int width = image[0].length;
		return j >= 0 && i >= 0 && j < height && i < width;
	}


	public static ArrayList<Point> getModifiersForNeighbors(int neighborhoodSize)
	{
		return getModifiersForNeighbors(neighborhoodSize, true);
	}


	public static ArrayList<Point> getModifiersForNeighbors(int neighborhoodSize, boolean includeCenter)
	{
		ArrayList<Point> points = new ArrayList<>();

		for(int yi = -neighborhoodSize; yi <= neighborhoodSize; yi++)
		{
			for(int xi = -neighborhoodSize; xi <= neighborhoodSize; xi++)
			{
				if(!(xi == 0 && yi == 0 && !includeCenter))
				{
					points.add(new Point(xi,yi));					
				}
			}			
		}


		return points;
	}

	public static void showTab(double[][] tab)
	{
		showTab(tab,null);
	}

	public static void showTab(double[][] tab, String tabName)
	{
		if(tabName != null)
		{
			System.out.println(tabName + " : ");
		}
		
		for(int yi = 0; yi < tab.length; yi++)
		{
			for(int xi = 0; xi < tab[0].length; xi++)
			{	
				System.out.print(tab[yi][xi] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
