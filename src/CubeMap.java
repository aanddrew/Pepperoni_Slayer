import java.awt.Color;
import java.util.ArrayList;

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
