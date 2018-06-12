import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is a map object. The only one that I have made.
 * It is basically just a big ArrayList of quads, floors, walls, gunholders, and enemy spawners.
 * This map is loaded into the player object where the objects are used for player collision, and rendered via the camera class, which Player extends.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class FirstMap implements Map 
{
	private ArrayList<Quad> quads;
	private ArrayList<Floor> floors;
	private ArrayList<Wall> walls;
	
	private Quad[] quadArray;
	private ArrayList<EnemySpawner> enemySpawners;
	
	private ArrayList<GunHolder> gunHolders;
	
	private DeathZone deathZone;
	
	public FirstMap() throws IOException
	{
		quads = new ArrayList<Quad>();
		floors = new ArrayList<Floor>();
		walls = new ArrayList<Wall>();
		
		quads.add(new Floor(new Point3D(-500,0,-500), 500,500, Color.ORANGE));
		quads.add(new Floor(new Point3D(-500,0,0), 500,500, Color.ORANGE));
		quads.add(new Floor(new Point3D(0,0,-500), 500,500, Color.ORANGE));
		quads.add(new Floor(new Point3D(0,0,0), 500,500, Color.ORANGE));
		
		quads.add(new Floor(new Point3D(-200,0,1100), 200, 300, Color.YELLOW));
		quads.add(new Floor(new Point3D(0,0,1300), 100, 100, Color.GREEN));
		quads.add(new Floor(new Point3D(-500,0,500),1000,600, Color.GRAY));
		quads.add(new Floor(new Point3D(-1000, 0 , 600), 500, 600, Color.GREEN));
		
		quads.add(new Floor(new Point3D(-200, -240, 0), 100, 1100, Color.YELLOW));
		
		//staircase
		quads.add(new Floor(new Point3D(0, 0,1100), 100,100, Color.MAGENTA));
		quads.add(new Floor(new Point3D(0, -40,1200), 100,100, Color.MAGENTA));
		quads.add(new Floor(new Point3D(0, -80,1300), 100,100, Color.MAGENTA));
		quads.add(new Floor(new Point3D(-100, -120,1300), 100,100, Color.MAGENTA));
		quads.add(new Floor(new Point3D(-200, -160,1300), 100,100, Color.MAGENTA));
		quads.add(new Floor(new Point3D(-200, -200,1200), 100,100, Color.MAGENTA));
		quads.add(new Floor(new Point3D(-200, -240,1100), 100,100, Color.MAGENTA));
		
		quads.add(new Floor(new Point3D(-1200,0,0-300), 700,300, Color.PINK));
		
		quads.add(new Floor(new Point3D(800,0,-200), 600,1100, Color.GREEN));
		quads.add(new Floor(new Point3D(500,0,0), 300,300, Color.PINK));
		quads.add(new Floor(new Point3D(-100,0,-900), 300,400, Color.PINK));
		quads.add(new Floor(new Point3D(-900,0,-1500), 1700,600, Color.GRAY));
		quads.add(new Floor(new Point3D(-1200,0,-700), 300,400, Color.PINK));
		quads.add(new Floor(new Point3D(-1200,0,-1400), 300,100, Color.PINK));
		quads.add(new Floor(new Point3D(-1200,0,-1300), 100,600, Color.PINK));

		quads.add(new Wall(new Point3D(-1000,-100,600), 500,100, 0, Color.ORANGE));
		quads.add(new Wall(new Point3D(-200,-340,1400), 300, 340, 0, Color.DARK_GRAY));
		quads.add(new Wall(new Point3D(-200,-340,1100), 300, 340, 2, Color.GREEN));

		
		quads.add(new Wall(new Point3D(100,-340,1100), 300, 340, 2, Color.PINK));
		
		quads.add(new Wall(new Point3D(-500,-100,0), 600, 100, 2, Color.DARK_GRAY));
		quads.add(new Wall(new Point3D(-500,-100,-500), 200, 100, 2, Color.GREEN));
		quads.add(new Wall(new Point3D(-500,-100,-500), 400, 100, 0, Color.PINK));
		quads.add(new Wall(new Point3D(-900,-100,-900), 800, 100, 0, Color.ORANGE));
		quads.add(new Wall(new Point3D(-100,-100,-900), 400, 100, 2, Color.DARK_GRAY));
		quads.add(new Wall(new Point3D(200,-100,-900), 400, 100, 2, Color.DARK_GRAY));


		//testing walls on paper
//		quads.add(new Wall(new Point3D(200,-150,300), 100, 100, 0, Color.PINK));
//		quads.add(new Wall(new Point3D(-220,-150,300), 200, 100, 2, Color.BLUE));
		
		enemySpawners = new ArrayList<EnemySpawner>();
		enemySpawners.add(new EnemySpawner(-900,-200,1100));
		enemySpawners.add(new EnemySpawner(1300,-200,800));
		enemySpawners.add(new EnemySpawner(-1100, -200, -100));
		enemySpawners.add(new EnemySpawner(0,-200,-1300));
		
		quadArray = new Quad[quads.size()];
		for (int i = 0; i < quads.size(); i++)
		{
			quadArray[i] = quads.get(i);
		}
		
		for (Quad quad : quads)
		{
			if (quad instanceof Floor)
			{
				floors.add((Floor) quad);
			}
			if (quad instanceof Wall)
			{
				walls.add((Wall) quad);
			}
		}
		
		gunHolders = new ArrayList<GunHolder>();
		gunHolders.add(new GunHolder(new Point3D(-400,-150,200), new WaterPistolMk2()));
		gunHolders.add(new GunHolder(new Point3D(-50, -420, 50), new AutoBlaster6000()));
		gunHolders.add(new GunHolder(new Point3D(1000, -150, 400), new MegaSoaker80k()));
		
		deathZone = new DeathZone(new Point3D(-2000,400,-2000), 4000, 4000);
	}
	
	public ArrayList<Quad> getQuads() 
	{
		return quads;
	}
	
	public ArrayList<Floor> getFloors()
	{
		return floors;
	}
	
	public ArrayList<Wall> getWalls()
	{
		return walls;
	}
	
	public Quad[] getQuadArray()
	{
		return quadArray;
	}
	
	public ArrayList<EnemySpawner> getEnemySpawners()
	{
		return enemySpawners;
	}
	
	public DeathZone getDeathZone()
	{
		return deathZone;
	}
	
	public ArrayList<GunHolder> getGunHolders()
	{
		return gunHolders;
	}
}