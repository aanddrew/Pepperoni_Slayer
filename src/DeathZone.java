import java.awt.Color;

/**
 * The large orange square at the bottom of the map that the game checks to see if you die on.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class DeathZone extends Floor 
{
	public DeathZone(Point3D topCornerIn, int widthIn, int heightIn)
	{
		super(topCornerIn, widthIn, heightIn, new Color(204,68,34));
	}
	
	public boolean playerInKillZone(Player player)
	{
		return player.getFeetY() > this.getY3D();
	}
}
