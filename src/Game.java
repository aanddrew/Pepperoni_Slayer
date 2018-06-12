import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * The game object, this initializes the parameters for the game.
 * If you would like to change the starting round to round n, simply change the line: "round = new Round(0, this);" to "round = new Round(n, this);"
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class Game 
{
	public static final String TITLE = "Pepperoni Slayer";
	
	private Player myPlayer;
	private JFrame frame;
	private Map map;
	private Round round;
	private int betweenRoundTimer;
	
	public Game() throws AWTException, IOException
	{
		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1024, 768));
		
		myPlayer = new Player(0,-Player.STANDING_TALLNESS,0, frame, this);
		
		betweenRoundTimer = 0;
		
		frame.add(myPlayer);
		frame.setVisible(true);		
		round = new Round(0, this);
	}
	
	public void update()
	{
		round.roundTimerIncrement();
		if (!myPlayer.isPaused() && myPlayer.isAlive())
		{
			myPlayer.update();
			myPlayer.drawHud();
			myPlayer.repaint();
			
			for (int i = 0; i < getEnemies().size(); i++)
			{
				getEnemies().get(i).chasePlayer(myPlayer);
				if (getEnemies().get(i).getHealth() <= 0)
				{
					getEnemies().get(i).kill();
					getEnemies().remove(i);
					i--;
				}
			}
			for (EnemySpawner eS : map.getEnemySpawners())
			{
				if (round.shouldSpawn())
				{
					Enemy newEnemy = eS.Spawn(round);
					getEnemies().add(newEnemy);
					myPlayer.loadEnemy(newEnemy);
				}	
			}
			if (round.roundOver())
			{
				if (betweenRoundTimer < Round.TIME_BETWEEN_ROUNDS)
				{
					myPlayer.setRoundChanging(true);
					betweenRoundTimer ++;
				}
				else
				{
					betweenRoundTimer = 0;
					myPlayer.setRoundChanging(false);
					round = new Round(this.getRoundNum()+1, this);
				}
			}
		}
		else
		{
			myPlayer.getAnimator().drawPaused();
			myPlayer.repaint();
		}
	}
	
	public Round getRound()
	{
		return round;
	}
	
	public boolean isPaused()
	{
		return myPlayer.isPaused();
	}
	
	public int getRoundNum()
	{
		return round.getRoundNum();
	}
	
	public boolean cameraSortingQuads()
	{
		return myPlayer.sortingQuads();
	}
	
	public void loadMap(Map mapIn) throws IOException
	{
		map = mapIn;
		myPlayer.loadMap(mapIn);
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public ArrayList<Enemy> getEnemies()
	{
		return getRound().getEnemies();
	}
}