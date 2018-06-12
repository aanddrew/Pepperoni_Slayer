import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class WaterPistolMk1 extends Gun
{
	public static final String NAME = "Water Pistol Mark 1";
	public static final int PRICE = 500;
	public static final int DAMAGE = 1;
	private final Image GUN_STILL_IMAGE = ImageIO.read(new File("waterPistolMk1_0.png"));
	private final Image GUN_SHOOTING_IMAGE = ImageIO.read(new File("waterPistolMk1_shooting.png"));
	public static final int MAG_SIZE = 7;
	public static final int RELOAD_TIME = 50;
	
	private BulletTrail bulletTrail;
	
	public WaterPistolMk1() throws IOException
	{
		super(MAG_SIZE, RELOAD_TIME, PRICE, null);
		setImage(GUN_STILL_IMAGE);
		
		bulletTrail = null;
	}
	
	public WaterPistolMk1(Player playerIn) throws IOException
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
		for (int i = 0; i < player.getGame().getEnemies().size(); i++)
		{
			try
			{
				if ((new Point(player.getCenterX(), player.getCenterY())).distance(player.getGame().getEnemies().get(i).get2DCenter((Camera) player)) < player.getGame().getEnemies().get(i).getEffectiveRadius((Camera)player))
				{
					player.getGame().getEnemies().get(i).takeDamage(DAMAGE);
					if (!player.getGame().getEnemies().get(i).isAlive())
					{
						player.earnCash(player.getGame().getEnemies().get(i).getCashValue());
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
