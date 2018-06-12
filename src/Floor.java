import java.awt.Color;
import java.awt.Rectangle;

public class Floor extends Quad 
{
	public Floor(Point3D topCornerIn, int widthIn, int heightIn, Color colorIn)
	{
		super(topCornerIn, widthIn, heightIn, 1, colorIn);
	}
	
	public boolean playerIsOn(Player player)
	{
		return (Math.abs(player.getFeetY() - getY3D()) < 10 
				&& new Rectangle((int)getX3D(), (int)getZ3D(), getWidth(), getHeight()).contains(player.getX3D(), player.getZ3D())
				);
	}
	
	public boolean playerIsBelow(Player player)
	{
		return (player.getY3D() - getY3D() + player.getTallness() < 50 && player.getY3D() - getY3D() + player.getTallness() > 0
				&& new Rectangle((int)getX3D(), (int)getZ3D(), getWidth(), getHeight()).contains(player.getX3D(), player.getZ3D())
				);
	}
}
