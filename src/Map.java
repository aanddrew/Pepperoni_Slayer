import java.util.ArrayList;

public interface Map 
{
	public ArrayList<Quad> getQuads();
	public ArrayList<Floor> getFloors();
	public ArrayList<Wall> getWalls();
	public Quad[] getQuadArray();
	public ArrayList<EnemySpawner> getEnemySpawners();
	public DeathZone getDeathZone();
	public ArrayList<GunHolder> getGunHolders();
}
