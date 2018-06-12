import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class BulletTrail 
{
	public static final Color STARTING_COLOR = new Color(0,255,255,255);
	public static final int THICKNESS = 3000;
	private Point3D origin;
	private double yAngle;
	private double vertAngle;
	private Point3D[] points;
	private Color color;
	
	
	public static final int POINT_DIST = 100;
	
	/**
	 * A bullet trail that is actually just an array of 3d points that a line is drawn through.
	 * @param originIn starting point
	 * @param yAngleIn the angle that the trail faces on the y (vertical axis)
	 * @param vertAngleIn the angle of attack of the angle
	 */
	public BulletTrail(Point3D originIn, double yAngleIn, double vertAngleIn)
	{
		origin = originIn;
		yAngle = yAngleIn;
		vertAngle = vertAngleIn;
		color = STARTING_COLOR;
		
		points = new Point3D[20];
		for (int i = 2; i < points.length; i++)
		{
			points[i] = new Point3D(origin.getX3D() - i*Math.cos(yAngle+Math.PI/2)*POINT_DIST, 
					origin.getY3D() + i*Math.sin(-1/Math.cos(vertAngle)*vertAngle)*POINT_DIST, 
					origin.getZ3D() + i*Math.sin(yAngle+Math.PI/2)*POINT_DIST);
		}
	}
	
	public Point3D[] getPoints()
	{
		return points;
	}
	
	public void paint(Graphics2D g2d, Camera camera)
	{
		g2d.setStroke(new BasicStroke(5));
		if (color.getAlpha() > 10)
		{
			color = new Color(STARTING_COLOR.getRed(), STARTING_COLOR.getGreen(), STARTING_COLOR.getBlue(), color.getAlpha()-10);
		}
		else
		{
			color = new Color(STARTING_COLOR.getRed(), STARTING_COLOR.getGreen(), STARTING_COLOR.getBlue(), 0);
		}
		for (int i = 0; i < getPoints().length-1; i++)
		{
			try
			{
				g2d.setStroke(new BasicStroke((int)(THICKNESS/origin.getDist(getPoints()[i]))));
				g2d.setColor(color);
				g2d.drawLine((int)getPoints()[i].getProjection(camera).getX(), 
						  (int)getPoints()[i].getProjection(camera).getY(), 
						  (int)getPoints()[i+1].getProjection(camera).getX(), 
						  (int)getPoints()[i+1].getProjection(camera).getY());
			}
			catch (NullPointerException e)
			{
				
			}
		}
	}
	
	public boolean isDead()
	{
		return color.getAlpha() == 0;
	}
	
	public Point3D getOrigin() {return origin;}
}
