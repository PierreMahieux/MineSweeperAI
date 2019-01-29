package utils;

public class MyUtils {

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
	
	protected static boolean isInside(int j, int i, double[][] image)
	{
		int height = image.length;
		int width = image[0].length;
		return j > 0 && i > 0 && j < height && i < width;
	}
}
