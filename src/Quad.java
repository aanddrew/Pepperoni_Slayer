import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Quad implements Object3D, Renderable
{
	public static final int POINT_DIST = 20;
	private Point3D topCorner;
	private int width;
	private int height;
	private int orientation;
	private Color color;
	
	private Point3D center;
	
	private Point3D[][] points;
	private Point3D[] pointsUnordered;
	private Point3D[] middleSides;
	private Point3D[] corners;
	
	public Quad(Point3D topCornerIn, int widthIn, int heightIn, int orientationIn, Color colorIn)
	{
		width = (widthIn + POINT_DIST) / POINT_DIST;
		height = (heightIn + POINT_DIST) / POINT_DIST;
		points = new Point3D[heightIn][widthIn];
		topCorner = topCornerIn;
		orientation = orientationIn;
		color = colorIn;
		
		for (int row = 0; row < height; row++)
		{
			if (orientation == 0)
			{
				for (int col = 0; col < width; col++)
				{
					if (row == 0 || col == 0 || row == height - 1 || col == width - 1)
					{
						points[row][col] = new Point3D(topCorner.getX3D() + col*POINT_DIST, 
													   topCorner.getY3D() + row*POINT_DIST, 
													   topCorner.getZ3D());
					}
					else
					{
						points[row][col] = null;
					} 
				}
			}
			else if (orientation == 1)
			{
				for (int col = 0; col < width; col++)
				{
					if (row == 0 || col == 0 || row == height - 1 || col == width - 1)
					{
						points[row][col] = new Point3D(topCorner.getX3D() + col*POINT_DIST, 
													   topCorner.getY3D(), 
													   topCorner.getZ3D() + row*POINT_DIST);
					}
					else
					{
						points[row][col] = null;
					}
				}
			}
			else if (orientation == 2)
			{
				for (int col = 0; col < width; col++)
				{
					if (row == 0 || col == 0 || row == height - 1 || col == width - 1)
					{
						points[row][col] = new Point3D(topCorner.getX3D(), 
													   topCorner.getY3D() + row*POINT_DIST, 
													   topCorner.getZ3D() + col*POINT_DIST);
					}
					else
					{
						points[row][col] = null;
					}
				}
			}
			else
			{
				throw new InvalidQuadOrientationError();
			}
		}
		
		int amtPoints = 0;
		for (Point3D[] pointList : points)
		{
			for (Point3D point : pointList)
			{
				if (point != null)
				{
					amtPoints++;
				}
			}
		}
		pointsUnordered = new Point3D[amtPoints];
		
		int i = 0;
		for (Point3D[] pointList : points)
		{
			for (Point3D point : pointList)
			{
				if (point != null)
				{
					pointsUnordered[i] = point; 
					i++;
				}
			}
		}
		
		double avgX = 0;
		double avgY = 0;
		double avgZ = 0;
		int nPoints = 0;
		for (i = 0; i < points.length; i++)
		{
			for (int j = 0; j < points[i].length; j++)
			{
				try
				{
					avgX += points[i][j].getX3D();
					avgY += points[i][j].getY3D();
					avgZ += points[i][j].getZ3D();
					nPoints++;
				}
				catch(NullPointerException e)
				{
					
				}
			}
		}
		center =  new Point3D(avgX/nPoints, avgY/nPoints, avgZ/nPoints);
		
		corners = new Point3D[4];
		corners[0] = points[0][0];
		corners[1] = points[0][width-1];
		corners[2] = points[height-1][width-1];
		corners[3] = points[height-1][0];
		
		middleSides = new Point3D[4];
		middleSides[0] = points[0][(width-1)/2];
		middleSides[1] = points[width-1][(height-1)/2];
		middleSides[2] = points[height-1][(width-1)/2];
		middleSides[3] = points[(height-1)/2][0];
	}
	
	public int getOrientation()
	{
		return orientation;
	}
	public Point3D getTopCorner()
	{
		return topCorner;
	}
	
	public void paint(Graphics2D g2d, Camera camera)
	{
		ArrayList<Integer> xPointsList = new ArrayList<Integer>();
		ArrayList<Integer> yPointsList = new ArrayList<Integer>();
		int nPoints = 0;
		
		for (int firstRow = 0; firstRow < width; firstRow++)
		{
			//NUMBER 1
			try
			{
				xPointsList.add((int)points[0][firstRow].getProjection(camera).getX());
				yPointsList.add((int)points[0][firstRow].getProjection(camera).getY());
				nPoints++;
			}
			catch (NullPointerException e)
			{
				
			}
		}
		for (int firstCol = 1; firstCol < height; firstCol++)
		{
			try
			{
				xPointsList.add((int)points[firstCol][width-1].getProjection(camera).getX());
				yPointsList.add((int)points[firstCol][width-1].getProjection(camera).getY());
				nPoints++;
			}
			catch (NullPointerException e)
			{
				
			}
		}
		for (int lastRow = width-1; lastRow >=1; lastRow--)
		{
			//NUMBER 3
			try
			{
				xPointsList.add((int)points[height-1][lastRow].getProjection(camera).getX());
				yPointsList.add((int)points[height-1][lastRow].getProjection(camera).getY());
				nPoints++;
			}
			catch (NullPointerException e)
			{
				
			}
		}
		for (int lastCol = height-1; lastCol >= 0; lastCol--)
		{
			try
			{
				xPointsList.add((int)points[lastCol][0].getProjection(camera).getX());
				yPointsList.add((int)points[lastCol][0].getProjection(camera).getY());
				nPoints++;
			}
			catch (NullPointerException e)
			{
				
			}
		}

		
		int[] xPoints = new int[nPoints];
		int[] yPoints = new int[nPoints];
		for (int i = 0; i < nPoints; i++)
		{
			xPoints[i] = xPointsList.get(i);
			yPoints[i] = yPointsList.get(i);
		}
		try
		{
			g2d.setColor(color);
			g2d.fillPolygon(xPoints, yPoints, nPoints);
		}
		catch (NullPointerException e)
		{
			
		}
	}

	public int getWidth() {return width*POINT_DIST;}
	public int getHeight() {return height*POINT_DIST;}
	
	public double getEffectiveZDiff(Camera camera)
	{
		Point3D closest = corners[0];
//		for (int i = 1; i < middleSides.length; i++)
//		{
//			try
//			{
//				if (middleSides[i].getEffectiveZDiff(camera) < closest.getEffectiveZDiff(camera))
//				{
//					closest = middleSides[i];
//				}
//			}
//			catch (NullPointerException e)
//			{
//				
//			}
//		}
		for (int i = 1; i < corners.length; i++)
		{
			try
			{
				if (Math.abs(corners[i].getEffectiveZDiff(camera)) < Math.abs(closest.getEffectiveZDiff(camera)))
				{
					closest = corners[i];
				}
			}
			catch (NullPointerException e)
			{
				
			}
		}
		return closest.getEffectiveZDiff(camera);
//		return (getCenter().getZ3D() - camera.getZ3D()) * Math.cos(camera.getYAngle()) + (getCenter().getX3D() - camera.getX3D()) * Math.sin(camera.getYAngle());
	}
	
	public double getYDiff(Camera camera)
	{
		return getY3D() - camera.getY3D();
	}
	
	public Point3D getCenter()
	{
		return center;
	}
	
	@Override
	public double getDist(Object3D o) 
	{
		return Math.sqrt(
					Math.pow(o.getX3D() - getCenter().getX3D(), 2) + 
					Math.pow(o.getY3D() - getCenter().getY3D(), 2) +
					Math.pow(o.getZ3D() - getCenter().getZ3D(), 2));
	}
	@Override
	public double getX3D() {return topCorner.getX3D();}
	@Override
	public double getY3D() {return topCorner.getY3D();}
	@Override
	public double getZ3D() {return topCorner.getZ3D();}
}