import java.awt.AWTException;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

public class GameRunner 
{
	public static void main(String[] args) throws InterruptedException, AWTException, IOException
	{
		Game myGame = new Game();
		
		myGame.loadMap(new FirstMap());
		
		while (true)
		{
			Thread.sleep(15);
			myGame.update();
		}
	}
}
