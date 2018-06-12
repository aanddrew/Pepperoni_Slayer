import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * This is a machine gun that I made as a sub-class to the Gun Class.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class AutoBlaster6000 extends Gun 
{
	public static final int PRICE =10000;
	
	public static final String NAME = "Auto Blaster 6000";
	
	private final Image GUN_STILL_IMAGE = ImageIO.read(new File("AutoBlaster6000_0.png"));
	private ArrayList<Image> gunAnimation;
	
	public static final int MAG_SIZE = 100;
	public static final int RELOAD_TIME = 100;
	
	public static final int BULLET_SPREAD = 40;
	public static final int FIRE_SPEED = 3;
	
	private int fireCount;
	
	private boolean firing;
	
	private ArrayList<BulletTrail> bulletTrails;
	
	//there are two constructors because one is used for the gunHolder class where there is no player to assign the gun to.
	public AutoBlaster6000() throws IOException
	{
		super(MAG_SIZE, RELOAD_TIME, PRICE, null);
		setImage(GUN_STILL_IMAGE);
		
		//this gun has a special gun animation that can be expanded to more than two pictures, but it looks good enough to me.
		gunAnimation = new ArrayList<Image>();
		gunAnimation.add(GUN_STILL_IMAGE);
		gunAnimation.add(ImageIO.read(new File("AutoBlaster6000_1.png")));
		
		bulletTrails = new ArrayList<BulletTrail>();
		firing = false;
		fireCount = 0;
	}
	
	public AutoBlaster6000(Player playerIn) throws IOException
	{
		super(MAG_SIZE, RELOAD_TIME, PRICE, playerIn);
		setImage(GUN_STILL_IMAGE);
		
		gunAnimation = new ArrayList<Image>();
		gunAnimation.add(GUN_STILL_IMAGE);
		gunAnimation.add(ImageIO.read(new File("AutoBlaster6000_1.png")));
		
		bulletTrails = new ArrayList<BulletTrail>();
		firing = false;
		fireCount = 0;
	}
	
	@Override
	public void fire(ArrayList<Enemy> enemiesIn) 
	{
		if (!isReloading())
		{
			firing = true;
		}
	}

	public void update()
	{
		super.update();
		if (firing && !isReloading())
		{
			fireCount++;
			if (fireCount == FIRE_SPEED)
			{
				setImage(gunAnimation.get(getBullets()%gunAnimation.size()));
				fireBullets(1);
				Point3D randLoc = new Point3D(player.getX3D()-BULLET_SPREAD/2+Math.random()*BULLET_SPREAD, player.getY3D()-BULLET_SPREAD/2 + Math.random()*BULLET_SPREAD, player.getZ3D()-BULLET_SPREAD+Math.random()*BULLET_SPREAD);
				bulletTrails.add(new BulletTrail(randLoc, player.getYAngle(), player.getVertAngle()));
				checkEnemiesHit();
				fireCount = 0;
			}
		}
	}
	
	@Override
	public void unfire() 
	{
		setImage(GUN_STILL_IMAGE);
		firing = false;
	}

	@Override
	public void reload() 
	{
		setReloading(true);
		bulletTrails.removeAll(bulletTrails);
	}

	@Override
	public void checkEnemiesHit() 
	{
		for (Enemy e : player.getGame().getEnemies())
		{
			try
			{
				if ((new Point(player.getCenterX(), player.getCenterY())).distance(e.getLocation().getProjection((Camera)player)) < e.getEffectiveRadius((Camera)player))
				{
					e.takeDamage(1);
					if (!e.isAlive())
					{
						player.earnCash(e.getCashValue());
					}
				}
			}
			catch (NullPointerException exc) {}
		}
	}

	public String getName()
	{
		return NAME;
	}
	
	public ArrayList<BulletTrail> getBulletTrails() 
	{
		return bulletTrails;
	}
}
