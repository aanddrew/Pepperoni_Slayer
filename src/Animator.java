import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sun.prism.Graphics;

/**
 * This class is responsible for drawing all the hud to the player JComponent object
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class Animator
{
	private Player player;
	private final Image WATER_DROP = ImageIO.read(new File("waterDrop25.png"));
	
	public Animator(Player playerIn) throws IOException
	{
		player = playerIn;
	}
	
	public void drawHud()
	{
		
	}
	
	public void drawGun()
	{
		player.getGun().paint(player.getG2d());
	}
	
	public void drawAmmo()
	{
		for (int i = 0; i < player.getGun().getBullets(); i++)
		{
			player.getG2d().drawImage(WATER_DROP, 30 + (i%10)*30, player.getHeight()-105 - ((i/10) * 26), null);
		}
	}
	
	public void drawBulletTrails(ArrayList<BulletTrail> bulletTrails)
	{
		for (int i = 0; i < bulletTrails.size(); i++)
		{
			if (bulletTrails.get(i) != null)
			{
				bulletTrails.get(i).paint(player.getG2d(), (Camera) player);
				if (bulletTrails.get(i).isDead())
				{
					bulletTrails.remove(i);
					i--;
				}
			}
		}
	}
	
	public void drawGunPriceTag(Gun gun)
	{
		try
		{
			player.getG2d().setFont(new Font("Calibri", Font.PLAIN, 20));
			
			int nameWidth = player.getG2d().getFontMetrics().stringWidth(gun.getName());
			int cashWidth = player.getG2d().getFontMetrics().stringWidth("$" + gun.getPrice());
			
			player.getG2d().setColor(new Color(0,0,255,128));
			player.getG2d().fillRect(player.getCenterX()-nameWidth/2, player.getCenterY()+145, nameWidth, 20);
			
			player.getG2d().fillRect(player.getCenterX()-cashWidth/2, player.getCenterY()+185, cashWidth, 20);
			
			player.getG2d().setColor(new Color(255,255,255));
			player.getG2d().drawString(gun.getName(), player.getCenterX()-nameWidth/2, player.getCenterY()+160);
			player.getG2d().drawString("$" + gun.getPrice(), player.getCenterX()-cashWidth/2, player.getCenterY()+200);
		}
		catch (ClassCastException e)
		{
			
		}
	}
	
	public void drawReloadCircle()	
	{
		player.getG2d().setColor(new Color(0,0,255,127));
		player.getG2d().setStroke(new BasicStroke(5));
		player.getG2d().drawArc(player.getCenterX() - 20, player.getCenterY()-20, 40,40, 90, -360 * player.getGun().getReloadCounter()/player.getGun().getReloadTime());
	}
	
	public void drawCrossHair()
	{
		player.getG2d().setStroke(new BasicStroke(2));
		player.getG2d().setColor(Color.LIGHT_GRAY);
		player.getG2d().drawLine((player.getHalfWidth()-player.getFrame().getInsets().left)-player.CROSSHAIR_LENGTH, 
								 (player.getHeight()/2)-player.getFrame().getInsets().top, 
								 (player.getHalfWidth()-player.getFrame().getInsets().left)+player.CROSSHAIR_LENGTH, 
								 (player.getHeight()/2)-player.getFrame().getInsets().top);
		player.getG2d().drawLine( player.getHalfWidth()-player.getFrame().getInsets().left,                   
								 (player.getHalfHeight()-player.getFrame().getInsets().top)-player.CROSSHAIR_LENGTH, 
								 (player.getWidth()/2)-player.getFrame().getInsets().left, 
								 (player.getHalfHeight()-player.getFrame().getInsets().top)+player.CROSSHAIR_LENGTH);
	}
	
	public void drawHealth()
	{
		player.getG2d().setColor(Color.RED);
		player.getG2d().fillRect(30, player.getHeight()-70, player.FULL_HEALTH*3, 25);
		player.getG2d().setColor(Color.BLUE);
		player.getG2d().fillRect(30, player.getHeight()-70, (int)player.getHealth()*3, 25);
		
		player.getG2d().fillArc(30 + player.FULL_HEALTH*3 + 25 ,player.getHeight()-70, 25, 25, 90, (int)(-1*360*((double)player.getRegenTimer()/player.REGEN_TIME)));
	}
	
	public void drawCash()
	{
		player.getG2d().setColor(new Color(0,0,255,127));
		player.getG2d().fillRect(player.getWidth()-150, player.getHeight()-315, 100, 20);
		try
		{
			player.getG2d().setFont(new Font("Calibri", Font.PLAIN, 20));
			player.getG2d().setColor(Color.WHITE);
			player.getG2d().drawString("$" + player.getCash(), player.getWidth()-150, player.getHeight()-300);
		}
		catch (ClassCastException e)
		{
			
		}
	}
	
	public void drawRoundTitle()
	{
		try
		{
			player.getG2d().setFont(new Font("Calibri", Font.PLAIN, 40));
			player.getG2d().setColor(Color.BLUE);
			player.getG2d().drawString("Round: " + (player.getGame().getRound().getRoundNum()+1), player.getCenterX() - 70, player.getCenterY()-50);
		}
		catch (ClassCastException e)
		{
			
		}
	}
	
	public void drawRoundNum()
	{
		try
		{
			player.getG2d().setFont(new Font("Calibri", Font.PLAIN, 40));
			player.getG2d().setColor(Color.BLUE);
			player.getG2d().drawString("" + player.getGame().getRoundNum(), player.getWidth()-100, player.getHeight()-100);
		}
		//I have no idea why this error is thrown, its not reproducible, but im just gonna try catch it
		catch (ClassCastException e)
		{
			
		}
	}
	
	public void setClearCursor()
	{
		player.getFrame().setCursor(player.getFrame().getToolkit().createCustomCursor(
	            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
	            "null"));
	}
	
	public void drawPaused()
	{
		player.getFrame().setCursor(Cursor.getDefaultCursor());
		//BLACK GREY SCREEN OVER THE GAME
		player.getG2d().setColor(new Color(0,0,0,127));
		player.getG2d().fillRect(0, 0, player.getWidth(), player.getHeight());
		
		try
		{
			player.getG2d().setColor(Color.BLUE);
			player.getG2d().drawString("PAUSED", player.getCenterX(), player.getCenterY());
		}
		catch (ClassCastException e)
		{
			
		}
	}
}