import java.awt.Color;
import java.util.ArrayList;

/**
 * This is the original framework I used to test the 3d projection system I had created.
 * It was just 8 points that were floating in the middle of the screen.
 * It no longer works as I have not expanded it to the traditional map structure I created.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class CubeMap implements Map 
{
	public CubeMap()
	{
		ArrayList<Point3D> cube = new ArrayList<Point3D>();
		cube.add(new Point3D(50,50,50));
		cube.add(new Point3D(-50,50,50));
		cube.add(new Point3D(50,-50,50));
		cube.add(new Point3D(50,50,-50));
		cube.add(new Point3D(50,-50,-50));
		cube.add(new Point3D(-50,-50,-50));
		cube.add(new Point3D(-50,50,-50));
		cube.add(new Point3D(-50,-50,50));
		
		Point3D point = new Point3D(0, 0, 0);
		
		Quad quad = new Quad(point, 5, 10, 5, Color.GREEN);
	}

	@Override
	public ArrayList<Quad> getQuads() 
	{
		return null;
	}

	@Override
	public ArrayList<Floor> getFloors() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Wall> getWalls()
	{
		return null;
	}
	
	public Quad[] getQuadArray()
	{
		return null;
	}

	@Override
	public ArrayList<EnemySpawner> getEnemySpawners() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DeathZone getDeathZone()
	{
		return null;
	}

	@Override
	public ArrayList<GunHolder> getGunHolders() {
		// TODO Auto-generated method stub
		return null;
	}
}
