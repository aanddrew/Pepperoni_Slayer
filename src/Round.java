import java.util.ArrayList;

public class Round 
{
	private int roundTimer;
	public static final int TIME_BETWEEN_ROUNDS = 150;
	private int roundNumber;
	private double enemyMoveSpeed;
	private int enemiesSpawned;
	private Game game;
	
	private ArrayList<Enemy> enemies;
	
	public Round(int roundNumberIn, Game gameIn)
	{
		roundNumber = roundNumberIn;
		enemies = new ArrayList<Enemy>();
		enemiesSpawned = 0;
		roundTimer = 0;
		game = gameIn;
	}
	
	public double getEnemyHp()
	{
		return 0.6 + (double)roundNumber/5;
	}
	
	public int getMaxEnemies()
	{
		return 4 + (roundNumber-1) *5 + (int)(0.05*Math.pow(roundNumber, 2)) + (int)(0.0005*Math.pow(roundNumber, 3));
	}
	
	public boolean stillSpawning()
	{
		return enemiesSpawned < getMaxEnemies();
	}
	
	public boolean roundOver()
	{
		return (!stillSpawning() && enemies.size()==0);
	}
	
	public int getRoundNum()
	{
		return roundNumber;
	}
	
	public void roundTimerIncrement()
	{
		roundTimer++;
	}
	
	public int getRoundTimer()
	{
		return roundTimer;
	}
	
	public int getDamage()
	{
		return 15 + 4*(int)((Math.log(getRoundNum())));
	}
	
	public boolean shouldSpawn()
	{
		if (Math.random() < getSpawnRate() && stillSpawning())
		{
			enemiesSpawned ++;
			return true;
		}
		return false;
	}
	
	public ArrayList<Enemy> getEnemies()
	{
		return enemies;
	}
	
	public double getSpawnRate()
	{
		return 0.01;
	}
}
