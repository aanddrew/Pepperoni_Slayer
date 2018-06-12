import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Point3D implements Object3D
{
	private double x;
	private double y;
	private double z;
	
	public Point3D(double xIn, double yIn, double zIn)
	{
		x = xIn;
		y = yIn;
		z = zIn;
	}
	
	public double getX3D() {return x;}
	public double getY3D() {return y;}
	public double getZ3D() {return z;}
	
	public void setX3D(double xIn) {x = xIn;}
	public void setY3D(double yIn) {y = yIn;}
	public void setZ3D(double zIn) {z = zIn;}
	
	/**
	 * Gets the 2D coordinates of this point being projected onto a JComponent Camera
	 * @param camera The camera that is viewing this point.
	 * @return
	 */
	public Point getProjection(Camera camera)
	{
		//GET THE RELATIVE X Y AND Z DIST FROM THE CAMERA TO THE POINT FIRST
		double xDiff = getX3D() - camera.getX3D();
		double yDiff = getY3D() - camera.getY3D();
		double zDiff = getZ3D() - camera.getZ3D();
		
		double effectiveXDiff = xDiff * Math.cos(camera.getYAngle()) - zDiff * Math.sin(camera.getYAngle());
		double effectiveYDiff = yDiff;
		double effectiveZDiff = zDiff * Math.cos(camera.getYAngle()) + xDiff * Math.sin(camera.getYAngle());
		
		//BEST ATTEMPT SO FAR: JUST KEEP THE ZDIFF ONE COMMENTED OUT
		effectiveYDiff = effectiveYDiff * Math.cos(camera.getVertAngle()) + effectiveZDiff * Math.sin(camera.getVertAngle());
		effectiveZDiff = effectiveZDiff * Math.cos(camera.getVertAngle()) - effectiveYDiff * Math.sin(camera.getVertAngle());

		double xAngle = Math.atan(effectiveXDiff/effectiveZDiff);
		double yAngle = Math.atan(effectiveYDiff/effectiveZDiff);
		
		xAngle *= Math.cos(yAngle/camera.getFOV());
		yAngle *= Math.cos(camera.getVertAngle());

		int xCoord = (int) (camera.getCenterX() + (xAngle / camera.getFOV()) * camera.getWidth());
		int yCoord = (int) (camera.getCenterY() + (yAngle / camera.getFOV()) * camera.getWidth());
		
		if (effectiveZDiff < 0 || effectiveZDiff > 10000)
		{	
			return null;
		}
		return new Point(xCoord, yCoord);
	}
	
	public double getEffectiveZDiff(Camera camera)
	{
		double zDiff = getZ3D() - camera.getZ3D();
		double xDiff = getX3D() - camera.getX3D();
		double yDiff = getY3D() - camera.getY3D();
		
		double effectiveZDiff = zDiff * Math.cos(camera.getYAngle()) + xDiff * Math.sin(camera.getYAngle());
		effectiveZDiff = effectiveZDiff * Math.cos(camera.getVertAngle()) - (yDiff * Math.cos(camera.getYAngle()) + xDiff * Math.sin(camera.getYAngle())) * Math.sin(camera.getVertAngle());
		
		return zDiff * Math.cos(camera.getYAngle()) + xDiff * Math.sin(camera.getYAngle());
	}
	public double getEffectiveYDiff(Camera camera)
	{
		double yDiff = getY3D() - camera.getY3D();
		double effectiveYDiff = yDiff * Math.cos(camera.getVertAngle()) + getEffectiveZDiff(camera) * Math.sin(camera.getVertAngle());
		return effectiveYDiff;
	}
	
	/**
	 * Move the point x, y, z units (positively)
	 * @param xIn x+=xIn;
	 * @param yIn y+=yIn;
	 * @param zIn z+=zIn;
	 */
	public void translate(int xIn, int yIn, int zIn)
	{
		x += xIn;
		y += yIn;
		z += zIn;
	}
	
	public void moveX(double dX) {x += dX;}
	public void moveY(double dY) {y += dY;}
	public void moveZ(double dZ) {z += dZ;}

	@Override
	public double getDist(Object3D o) 
	{
		return Math.sqrt(
				Math.pow(o.getX3D() - getX3D(), 2) + 
				Math.pow(o.getY3D() - getY3D(), 2) +
				Math.pow(o.getZ3D() - getZ3D(), 2));
	}
	
	/**
	 * Paint a circle 5 pixels wide with center at this point onto Graphics2D g2d
	 * @param g2d Graphics to paint on.
	 * @param camera Camera the point is being viewed by
	 */
	public void paint(Graphics2D g2d, Camera camera)
	{
		try
		{
			g2d.fillOval((int)getProjection(camera).getX(), (int)getProjection(camera).getY(), 5,5);
		}
		catch (NullPointerException e)
		{
			
		}
	}
}
