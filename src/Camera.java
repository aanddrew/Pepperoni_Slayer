import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * This is a camera that takes in a bunch of "Renderable" objects and paints them onto the screen.
 * There is no actual 3d projection being done in this class. That is done in the Point3D class.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class Camera extends JComponent implements Object3D
{
	private Point3D location;
	private double fov;
	
	private JFrame frame;
	private Graphics2D g2d;
	
	private double yAngle;
	private double vertAngle;
	
	private Map map;
	
	private boolean sortingQuads;
	private ArrayList<Renderable> renders;
	
	public Camera(double xIn, double yIn, double zIn, JFrame frameIn)
	{
		location = new Point3D(xIn, yIn, zIn);
		fov = 4*Math.PI/8;
		frame = frameIn;
		
		yAngle = 0;
		vertAngle = 0;
		
		map = null;
		
		sortingQuads = false;
		
		renders = new ArrayList<Renderable>();
		
		setSize(frame.getWidth(), frame.getHeight());
	}
	
	public double getX3D() {return location.getX3D();}
	public double getY3D() {return location.getY3D();}
	public double getZ3D() {return location.getZ3D();}
	
	public Point3D getLocation3D() {return location;}
	
	public void setX3D(double xIn) {location.setX3D(xIn);} 
	public void setY3D(double yIn) {location.setY3D(yIn);} 
	public void setZ3D(double zIn) {location.setZ3D(zIn);} 
	
	public double getYAngle() {return yAngle;}
	
	public double getVertAngle() {return vertAngle;}
	
	public void moveX(double dX) {location.moveX(dX);}
	public void moveY(double dY) {location.moveY(dY);}
	public void moveZ(double dZ) {location.moveZ(dZ);}
	
	public void rotateY(double dTheta) {yAngle += dTheta;}
	
	public void rotateVertical(double dTheta) {vertAngle += dTheta;}
	public void setVertAngle(double thetaIn) {vertAngle = thetaIn;}
	public void setYAngle(double thetaIn) {yAngle = thetaIn;}
	
	public double getFOV() {return fov;}
	
	public int getWidth() {return frame.getWidth();}
	public int getHeight() {return frame.getHeight();}
	public int getHalfWidth() {return getWidth()/2;}
	public int getHalfHeight() {return getHeight()/2;}
	
	public void paintComponent(Graphics g)
	{
		g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		
		try
		{
			map.getDeathZone().paint(g2d, this);
		}
		catch(NullPointerException e) {}
		
		try
		{
			sortRenders(renders);
		}
		catch (NullPointerException e)
		{
			
		}

		for (int i = 0; i < renders.size(); i++)
		{
			try
			{
				renders.get(i).paint(g2d, this);
				if (renders.get(i) instanceof Enemy)
				{
					if (!((Enemy) renders.get(i)).isAlive())
					{
						renders.remove(i);
						i--;
					}
				}
			}
			catch(NullPointerException e)
			{
				
			}
		}
		
		if (this instanceof Player)
		{
			((Player) this).drawHud();
		}
	}
	
	public boolean sortingQuads()
	{
		return sortingQuads;
	}

	public void sortRenders(ArrayList<Renderable> renderables)
	{
		for (int i = 0; i < renderables.size()-1; i++)
		{
			if (renderables.get(i).getDist(this) < renderables.get(i+1).getDist(this))
			{
				Renderable temp = renderables.get(i);
				renderables.set(i, renderables.get(i+1));
				renderables.set(i+1, temp);
			}
		}
	}
	
	public Graphics2D getG2d()
	{
		return g2d;
	}
	
	public int getCenterX()
	{
		return (getWidth()/2)-getFrame().getInsets().left;
	}
	public int getCenterY()
	{
		return (getHeight()/2)-getFrame().getInsets().top;
	}
	
	@Override
	public double getDist(Object3D o) 
	{
		return Math.sqrt(
				Math.pow(o.getX3D() - getX3D(), 2) + 
				Math.pow(o.getY3D() - getY3D(), 2) +
				Math.pow(o.getZ3D() - getZ3D(), 2));
	}
	
	public Point getFrameLocation()
	{
		return frame.getLocation();
	}
	
	public void loadMap(Map mapIn) throws IOException
	{
		map = mapIn;
		for (Quad quad : map.getQuads())
		{
			renders.add(quad);
		}
		
		for (GunHolder gunHolder : map.getGunHolders())
		{
//			gunHolder.loadGun(new WaterPistolMk2(((Player) this)));
			renders.add(gunHolder);
		}
		sortRenders(renders);
	}
	
	public void loadEnemy(Enemy e)
	{
		renders.add(e);
	}
	
	public Map getMap()
	{
		return map; 
	}
	
	public void showPaused()
	{
		
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
}
