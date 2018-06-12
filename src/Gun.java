import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

/**
 * A gun class that has basic gun properties.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public abstract class Gun 
{
	private Image image;
	protected Player player;
	
	private int magSize;
	private int bullets;
	
	private int reloadCounter;
	private int reloadTime;
	
	private boolean shooting;
	private boolean reloading;
	
	private int price;
	
	public Gun(int magSizeIn, int reloadTimeIn, int priceIn, Player playerIn)
	{
		magSize = magSizeIn;
		player = playerIn;
		
		shooting = false;
		bullets = magSize;
		
		reloading = false;
		reloadCounter = 0;
		reloadTime = reloadTimeIn;
		
		price = priceIn;
	}
	
	public abstract void fire(ArrayList<Enemy> enemiesIn);
	public abstract void unfire();
	public abstract void reload();
	public abstract void checkEnemiesHit();
	public abstract ArrayList<BulletTrail> getBulletTrails();
	public abstract String getName();
	
	public boolean isReloading() {return reloading;}
	
	public void setPlayer(Player playerIn) {player = playerIn;}
	
	public void setPrice(int priceIn) {price = priceIn;}
	public int getPrice() {return price;};
	
	public void setImage(Image imageIn) {image = imageIn;}
	public void setReloading(boolean reload) {reloading = reload;}
	
	public void paint(Graphics2D g2d)
	{
		g2d.drawImage(getImage() , player.getFrame().getWidth()-350, player.getFrame().getHeight()-300,null);
	}
	
	public void fireBullets(int numBullets) {bullets -= numBullets;}
	
	public boolean isShooting() {return shooting;}
	
	public void update()
	{
		if (getBullets() == 0) 
		{
			setReloading(true);
			getBulletTrails().removeAll(getBulletTrails());
		}
		
		
		if (isReloading() && getReloadCounter() < getReloadTime() && !player.isPaused())
		{
			reloadCounter ++;
			
			if (reloadCounter == getReloadTime() - 1)
			{
				bullets = getMagSize();
				setReloading(false);
			}
		}
		else if (!player.isPaused())
		{
			reloadCounter = 0;
		}
	}
	
	public Image getImage() {return image;}
	
	public int getMagSize() {return magSize;}
	public int getBullets() {return bullets;}
	
	public int getReloadCounter() {return reloadCounter;}
	public int getReloadTime() {return reloadTime;}
}
