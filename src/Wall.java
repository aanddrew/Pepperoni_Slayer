import java.awt.Color;
import java.awt.Rectangle;

/**
 * A wall is a quad that stands up straight and can detect when a player collides with it.
 * The hitbox is not a rectangular prism, but rather a shape that looks like this:
 * o--------o
 * Initially the player was able to get stuck inside the wall by approaching from the side, but the circles at the ends of the wall prevent them from getting in there.
 * The wall also contains a floor at the top of it that prevents the player from walking on the inside of it.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class Wall extends Quad
{
	public static int WALL_THICKNESS = 50;
	public Wall(Point3D topCornerIn, int widthIn, int heightIn, int orientationIn, Color colorIn)
	{
		super(topCornerIn, widthIn, heightIn, orientationIn, colorIn);
	}
	
	public boolean inContact(Player player)
	{
		if (getOrientation() == 0)
		{
			double zDiff = getZ3D() -  player.getZ3D();
			Rectangle rect2D = new Rectangle((int)getTopCorner().getX3D(), (int)getTopCorner().getY3D(), getWidth(), getHeight());
			if (rect2D.contains(player.getX3D(), player.getFeetY()) || rect2D.contains(player.getX3D(), player.getY3D()))
			{
				if (Math.abs(zDiff) < WALL_THICKNESS)
				{
					return true;
				}

			}
		}
		else if (getOrientation() == 2)
		{
			double xDiff = getX3D() - player.getX3D();
			Rectangle rect2D = new Rectangle((int)getTopCorner().getZ3D(), (int)getTopCorner().getY3D(), getWidth(), getHeight());
			if (rect2D.contains(player.getZ3D(), player.getFeetY()) || rect2D.contains(player.getZ3D(), player.getY3D()))
			{
				if (Math.abs(xDiff) < WALL_THICKNESS)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public Point3D getOtherTopCorner()
	{
		if (getOrientation() == 0)
		{
			return new Point3D(getTopCorner().getX3D()+getWidth(), getTopCorner().getY3D(),getTopCorner().getZ3D());
		}
		return new Point3D(getTopCorner().getX3D(), getTopCorner().getY3D(), getTopCorner().getZ3D()+getWidth());
	}
	
	public int collideWithEdge(Player player)
	{
		if (playerInYZone(player) && 
				(Math.sqrt(Math.pow(player.getX3D()-getTopCorner().getX3D(), 2) + 
					  Math.pow(player.getZ3D()-getTopCorner().getZ3D(), 2)) < WALL_THICKNESS))
		{
			return 0;
		}
		else if (playerInYZone(player) && 
				(Math.sqrt(Math.pow(player.getX3D()-getOtherTopCorner().getX3D(), 2) + 
				  	  Math.pow(player.getZ3D()-getOtherTopCorner().getZ3D(), 2)) < WALL_THICKNESS))
		{
			return 1;
		}
		return 2;
	}
	
	public boolean playerIsOn(Player player)
	{
		Floor hypo;
		if (getOrientation() == 0)
		{
			hypo = new Floor(new Point3D(getTopCorner().getX3D(), getTopCorner().getY3D(), getTopCorner().getZ3D()-WALL_THICKNESS), getWidth(), WALL_THICKNESS*2, Color.BLACK);
			return hypo.playerIsOn(player);
		}
		else if (getOrientation() == 2)
		{	
			hypo = new Floor(new Point3D(getTopCorner().getX3D()-WALL_THICKNESS, getTopCorner().getY3D(), getTopCorner().getZ3D()), WALL_THICKNESS*2, getWidth(), Color.BLACK);
			return hypo.playerIsOn(player);
		}
		return false;
	}
	
	public boolean playerInYZone(Player player)
	{
		return (player.getFeetY() < getY3D() + getHeight() && player.getFeetY() > getY3D()) || 
				(player.getY3D() < getY3D() + getHeight() && player.getY3D() > getY3D());
	}
}
