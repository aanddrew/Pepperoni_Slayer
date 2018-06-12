import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * An enemy, which is actually just a big floating red circle. This is the inspiration for the name of this project, "pepperoni slayer".
 * You will no longer be able to unsee that.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public class Enemy implements Object3D, Renderable
{
	public static final int START_CASH_VALUE = 15;
	private Point3D location;
	private Round round;
	public static final Color COLOR = Color.RED;
	private double health;
	public static final double START_MOVE_SPEED = 2;
	private double moveSpeed;
	public static final int STARTING_RADIUS = 100;
	private int radius;
	private int blue;
	
	private int damage;
	private int START_DAMAGE = 15;
	
	public Enemy(double xIn, double yIn, double zIn, Round roundIn)
	{
		location = new Point3D(xIn, yIn, zIn);
		moveSpeed = START_MOVE_SPEED;
		round = roundIn;
		health = round.getEnemyHp();
		damage = round.getDamage();
		radius = STARTING_RADIUS;
	}
	
	/**
	 * The enemy will chase the player by taking a direct path, ignoring obstacles.
	 * This is the only kind of enemy pathing that I was willing to put into my project.
	 * @param player The player to be chased.
	 */
	public void chasePlayer(Player player)
	{
		double xDiff = player.getX3D() - this.getX3D();
		double yDiff = player.getY3D() - this.getY3D();
		double zDiff = player.getZ3D() - this.getZ3D();
		
		double yAngle = Math.atan(zDiff/xDiff);
		double vertAngle = Math.asin(yDiff/player.getDist(this));
		if (xDiff < 0)
		{
			yAngle += Math.PI;
		}
		
		if (!touching(player))
		{
			location.moveX(moveSpeed*Math.cos(yAngle));
			location.moveZ(moveSpeed*Math.sin(yAngle));
			if (Math.abs(yDiff)<=100)
				location.moveY(moveSpeed*Math.sin(vertAngle));
		}
		else
		{
			player.getHurt(this);
		}
	}
	
	public int getCashValue()
	{
		int cashBonus = 0;
		if (round.getRoundTimer() < round.getRoundNum() * 600)
		{
			cashBonus += round.getRoundNum();
		}
		
		return START_CASH_VALUE + round.getRoundNum()/10 + cashBonus;
	}
	
	public void takeDamage(int dmgIn)
	{
		health -= dmgIn;
		blue = 200;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public void kill()
	{
		radius = 0;
	}
	
	@Override
	public void paint(Graphics2D g2d, Camera camera) 
	{ 
		double colorScale = (1-this.getDist(camera)/3000);
		if (colorScale < 0)
		{
			colorScale = 0;
		}
		if (blue >= 10)
		{
			blue-= 5;
		}
		
		Color bodyColor = new Color((int)(COLOR.getRed()*colorScale), (int)(COLOR.getGreen()*colorScale), (int)(COLOR.getBlue()*colorScale) + blue);
		int effectiveRadius = (int)getEffectiveRadius(camera);
		try
		{
			int x = (int)location.getProjection(camera).getX();
			int y = (int)location.getProjection(camera).getY();
			g2d.setColor(Color.BLACK);
			int borderSize = effectiveRadius/10;
			
			g2d.fillOval(x-borderSize/2, y-borderSize/2, effectiveRadius+borderSize, effectiveRadius+borderSize);
			
			g2d.setColor(bodyColor);
			g2d.fillOval(x, y, effectiveRadius, effectiveRadius);
			
			g2d.fillRect(x, y-(effectiveRadius/4), (int)(effectiveRadius*(double)getHealth()/round.getEnemyHp()), effectiveRadius/5);
			
		}
		catch(NullPointerException e)
		{
			
		}
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public Point get2DCenter(Camera camera)
	{
		return new Point((int)(this.getLocation().getProjection(camera).getX() + this.getEffectiveRadius(camera)/2),
				(int)(this.getLocation().getProjection(camera).getY() + this.getEffectiveRadius(camera)/2));
	}
	
	public double getEffectiveRadius(Camera camera)
	{
		return Math.atan(getRadius()/this.getDist(camera)) * (camera.getWidth() / camera.getFOV());
	}
	
	public boolean touching(Object3D o)
	{
		return getDist(o) < getRadius();
	}
	
	public double getHealth()
	{
		return health;
	}
	
	public boolean isAlive()
	{
		return health > 0;
	}
	public Point3D getLocation()
	{
		return location;
	}

	@Override
	public double getDist(Object3D o) 
	{
		return Math.sqrt(
				Math.pow(o.getX3D() - getX3D(), 2) + 
				Math.pow(o.getY3D() - getY3D(), 2) +
				Math.pow(o.getZ3D() - getZ3D(), 2));
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
