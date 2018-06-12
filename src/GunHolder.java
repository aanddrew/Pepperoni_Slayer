import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public class GunHolder implements Renderable
{
	public static final int RADIUS = 100;
	public static final Color BACK_COLOR = new Color(0,0,255,100);
	
	private Image scaledImage;
	
	private Point3D location;
	private int radius;
	private Gun gun;
	
	public GunHolder(Point3D locationIn, Gun gunIn)
	{
		location = locationIn;
		gun = gunIn;
		radius = RADIUS;
		
		scaledImage = gun.getImage();
	}

	public double getEffectiveRadius(Camera camera)
	{
		return Math.atan(getRadius()/this.getDist(camera)) * (camera.getWidth() / camera.getFOV());
	}

	@Override
	public void paint(Graphics2D g2d, Camera camera) 
	{
		int effectiveRadius = (int)getEffectiveRadius(camera);
		try
		{
			int x = (int)location.getProjection(camera).getX();
			int y = (int)location.getProjection(camera).getY();
			
//			g2d.setColor(Color.BLACK);
//			int borderSize = effectiveRadius/10;
//			
//			g2d.fillOval(x-borderSize/2, y-borderSize/2, effectiveRadius+borderSize, effectiveRadius+borderSize);
//			
			g2d.setColor(BACK_COLOR);
			g2d.fillOval(x, y, effectiveRadius, effectiveRadius);
			
			if (gun != null)
			{
//				if (Math.random()<0.05)
//				{
//					scaledImage = gun.getImage().getScaledInstance(effectiveRadius, effectiveRadius, Image.SCALE_FAST);
//				}
//				g2d.drawImage(gun.getImage().getScaledInstance(effectiveRadius, effectiveRadius, Image.SCALE_FAST), x, y, null);
//				g2d.drawImage(gun.getImage(), x, y, null);
//				g2d.drawImage(scaledImage, x, y, null);
//				g2d.drawImage(gun.getImage(), x, y, effectiveRadius, effectiveRadius, null);
			}
			
//			g2d.fillRect(x, y-(effectiveRadius/4), (int)(effectiveRadius*(double)getHealth()/round.getEnemyHp()), effectiveRadius/5);
			
		}
		catch(NullPointerException e)
		{
			
		}
	}
	
	public void resetGun() throws InstantiationException, IllegalAccessException
	{
//		Class gunClass = gun.getClass();
		gun = gun.getClass().newInstance();
	}
	
	public Gun getGun()
	{
		return gun;
	}
	
	public void loadGun(Gun gunIn)
	{
		gun = gunIn;
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public Point3D getLocation() {return location;}
	
	@Override
	public double getX3D() {return location.getX3D();}

	@Override
	public double getY3D() {return location.getY3D();}

	@Override
	public double getZ3D() {return location.getZ3D();}

	@Override
	public double getDist(Object3D o) {return location.getDist(o);}
}
