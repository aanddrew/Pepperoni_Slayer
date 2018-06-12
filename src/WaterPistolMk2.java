import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class WaterPistolMk2 extends Gun
{
	public static final String NAME = "Water Pistol Mark 2";
	public static final int PRICE = 1200;
	public static final int DAMAGE = 2;
	private final Image GUN_STILL_IMAGE = ImageIO.read(new File("waterPistolMk2_0.png"));
	private final Image GUN_SHOOTING_IMAGE = ImageIO.read(new File("waterPistolMk2_shooting.png"));
	public static final int MAG_SIZE = 9;
	public static final int RELOAD_TIME = 40;
	
	private BulletTrail bulletTrail;
	
	public WaterPistolMk2() throws IOException
	{
		super(MAG_SIZE, RELOAD_TIME, PRICE, null);
		setImage(GUN_STILL_IMAGE);
		
		bulletTrail = null;
	}
	
	public WaterPistolMk2(Player playerIn) throws IOException
	{
		super(MAG_SIZE, RELOAD_TIME, PRICE, playerIn);
		setImage(GUN_STILL_IMAGE);
		
		bulletTrail = null;
	}
	
	@Override
	public void fire(ArrayList<Enemy> enemiesIn) 
	{
		if (!isReloading())
		{
			bulletTrail = new BulletTrail(player.getLocation3D(), player.getYAngle(), player.getVertAngle());
			setImage(GUN_SHOOTING_IMAGE);
			fireBullets(1);
			checkEnemiesHit();
		}
	}

	@Override
	public void unfire() 
	{
		setImage(GUN_STILL_IMAGE);
	}

	@Override
	public void reload() 
	{
		setReloading(true);
	}

	@Override
	public void checkEnemiesHit() 
	{
		for (Enemy e : player.getGame().getEnemies())
		{
			try
			{
				if ((new Point(player.getCenterX(), player.getCenterY())).distance(e.get2DCenter((Camera) player)) < e.getEffectiveRadius((Camera)player))
				{
					e.takeDamage(DAMAGE);
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
		ArrayList<BulletTrail> trails = new ArrayList<BulletTrail>();
		trails.add(bulletTrail);
		return trails;
	}
}
