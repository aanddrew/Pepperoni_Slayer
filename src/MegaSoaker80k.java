import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Another gun, this one fires slower than the AutoBlaster 6000, but does 3 damage instead of one.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class MegaSoaker80k extends Gun 
{
	public static final int PRICE =7000;
	
	public static final String NAME = "Mega Soaker 80K";
	public static final int DAMAGE = 2;
	
	private final Image GUN_STILL_IMAGE = ImageIO.read(new File("MegaSoaker80k_0.png"));
	private ArrayList<Image> gunAnimation;
	
	public static final int MAG_SIZE = 30;
	public static final int RELOAD_TIME = 60;
	
	public static final int BULLET_SPREAD = 5;
	public static final int FIRE_SPEED = 8;
	
	private int fireCount;
	
	private boolean firing;
	
	private ArrayList<BulletTrail> bulletTrails;
	
	public MegaSoaker80k() throws IOException
	{
		super(MAG_SIZE, RELOAD_TIME, PRICE, null);
		setImage(GUN_STILL_IMAGE);
		
		gunAnimation = new ArrayList<Image>();
		gunAnimation.add(GUN_STILL_IMAGE);
		gunAnimation.add(ImageIO.read(new File("MegaSoaker80k_1.png")));
		
		bulletTrails = new ArrayList<BulletTrail>();
		firing = false;
		fireCount = 0;
	}
	
	public MegaSoaker80k(Player playerIn) throws IOException
	{
		super(MAG_SIZE, RELOAD_TIME, PRICE, playerIn);
		setImage(GUN_STILL_IMAGE);
		
		gunAnimation = new ArrayList<Image>();
		gunAnimation.add(GUN_STILL_IMAGE);
		gunAnimation.add(ImageIO.read(new File("MegaSoaker80k_1.png")));
		
		bulletTrails = new ArrayList<BulletTrail>();
		firing = false;
		fireCount = 0;
	}
	
	@Override
	public void fire(ArrayList<Enemy> enemiesIn) 
	{
		if (!isReloading())
		{
			fireCount = FIRE_SPEED-1;
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
				setImage(gunAnimation.get(1));
				fireBullets(1);
				Point3D randLoc = new Point3D(player.getX3D()-BULLET_SPREAD/2+Math.random()*BULLET_SPREAD, player.getY3D()-BULLET_SPREAD/2 + Math.random()*BULLET_SPREAD, player.getZ3D()-BULLET_SPREAD+Math.random()*BULLET_SPREAD);
				bulletTrails.add(new BulletTrail(randLoc, player.getYAngle(), player.getVertAngle()));
				checkEnemiesHit();
				fireCount = 0;
			}
			else
			{	
				if (fireCount == FIRE_SPEED/2)
				{
					setImage(gunAnimation.get(0));
				}
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
	
	public int getDamage()
	{
		return DAMAGE;
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
					e.takeDamage(getDamage());
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
