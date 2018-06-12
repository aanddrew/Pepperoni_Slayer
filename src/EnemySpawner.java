
/**
 * An imaginary object that spawns an enemy at a random spot within SPAWN_VARIANCE units of the location.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class EnemySpawner implements Object3D
{
	public static final int SPAWN_VARIANCE = 700;
	private Point3D location;
	public EnemySpawner(int x, int y, int z)
	{
		location = new Point3D(x,y,z);
	}
	
	public Enemy Spawn(Round roundIn)
	{
		double xRandMod = Math.random()*SPAWN_VARIANCE * Math.pow(-1, (int)(Math.random()*2)+1);
		double zRandMod = Math.random()*SPAWN_VARIANCE * Math.pow(-1, (int)(Math.random()*2)+1);
		return new Enemy(getX3D() + xRandMod, getY3D(), getZ3D()+zRandMod, roundIn);
	}
	
	@Override
	public double getDist(Object3D o) 
	{
		return location.getDist(o);
	}
	@Override
	public double getX3D() 
	{
		return location.getX3D();
	}
	@Override
	public double getY3D() 
	{
		return location.getY3D();
	}
	@Override
	public double getZ3D() 
	{
		return location.getZ3D();
	}
}
