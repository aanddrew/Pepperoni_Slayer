import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * A player is a camera that can move with keyboard controls
 * @author Andrew Weller
 */
public class Player extends Camera
{
	public static final double MOVE_SPEED_DEFAULT = 12;
	public static final double FALL_SPEED = 12;
	public static final double TURN_SPEED = 0.0025;
	public static final double STANDING_TALLNESS = 100;
	public static final int CROSSHAIR_LENGTH = 5;
	public static final double MAX_VERT_ANGLE = Math.PI/4;
	public static final double AIR_MOVE_ACCEL = 5;
	public static final double MAX_LIN_SPEED = 13;
	public static final double JUMP_SPEED = 8;
	public static final double GRAV_CONST = 0.53;
	public static final double LOOK_SPEED = 0.6;
	public static final int BUY_DIST = 600;
	
	private boolean isAlive;
	
	private Gun gun;
	
	private int cash;
	
	public static final int FULL_HEALTH = 100;
	private double health;
	
	public static final int REGEN_TIME = 7500;
	public static final double REGEN_AMOUNT = 0.1;
	public static final int REGEN_SPEED = 20;
	private int regenTimer;
	
	private boolean movingRight;
	private boolean movingLeft;
	private boolean movingForward;
	private boolean movingBackward;
	private boolean walking;
	
	private boolean rightJump;
	private boolean leftJump;
	private boolean forwardJump;
	private boolean backwardJump;
	
	private boolean isPaused;
	public boolean roundChanging;
	
	//for airborne purposes
	private double xSpeed;
	private double zSpeed;
	private double jumpingYAngle;
	
	private double moveAngle;
	
	private double moveSpeed;
	private double verticalSpeed;
	private boolean airborne;
	
	private Wall onWall;
	
	private double tallness;
	
	private Robot robot;
	
	private Game game;
	
	private Animator animator;
	
	/**
	 * A player is a camera that can move with keyboard controls
	 * @param xIn Starting X
	 * @param yIn Starting Y
	 * @param zIn starting Z
	 * @param frameIn Frame for the camera to be painted on.
	 * @throws AWTException 
	 */
	public Player(double xIn, double yIn, double zIn, JFrame frameIn, Game gameIn) throws AWTException, IOException
	{
		super(xIn, yIn, zIn, frameIn);
		
		movingRight = false;
		movingLeft = false;
		movingForward = false;
		movingBackward = false;
		walking = false;
		
		isPaused = false;
		
		moveSpeed = MOVE_SPEED_DEFAULT;
		verticalSpeed = 0;
		
		cash = 0;
		
		moveAngle = getYAngle();
		
		tallness = STANDING_TALLNESS;
		
		robot = new Robot();
		
		onWall = null;
		
		game = gameIn;
		
		roundChanging = false;
		
		health = FULL_HEALTH;
		regenTimer = REGEN_TIME;
		
		isAlive = true;
		
		gun = new WaterPistolMk1(this);
		
		animator = new Animator(this);
		
		addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if (e.getKeyCode() == KeyEvent.VK_W) movingForward = true;
				if (e.getKeyCode() == KeyEvent.VK_S) movingBackward = true;
				if (e.getKeyCode() == KeyEvent.VK_A) movingLeft = true;
				if (e.getKeyCode() == KeyEvent.VK_D) movingRight = true;
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) walking = true;
				
				if (e.getKeyCode() == KeyEvent.VK_SPACE) 
				{
					jump();
				}
				
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					robot.mouseMove(getHalfWidth(), getHalfHeight());
					if (!isPaused) getFrame().setTitle(Game.TITLE + " (PAUSED)");
					if (isPaused) getFrame().setTitle(Game.TITLE);
					isPaused = !isPaused; 
				}
				if (e.getKeyCode() == KeyEvent.VK_R && gun.getBullets() < gun.getMagSize())
				{
					gun.reload();
				}
				
				if (e.getKeyCode() == KeyEvent.VK_E)
				{
					try {
						buyGun();
					} catch (InstantiationException | IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) 
			{
				if (e.getKeyCode() == KeyEvent.VK_W) movingForward = false;
				if (e.getKeyCode() == KeyEvent.VK_S) movingBackward = false;
				if (e.getKeyCode() == KeyEvent.VK_A) movingLeft = false;
				if (e.getKeyCode() == KeyEvent.VK_D) movingRight = false;
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) walking = false;
				if (!airborne)
				{
					if (e.getKeyCode() == KeyEvent.VK_W) forwardJump = false;
					if (e.getKeyCode() == KeyEvent.VK_S) backwardJump = false;
					if (e.getKeyCode() == KeyEvent.VK_A) leftJump = false;
					if (e.getKeyCode() == KeyEvent.VK_D) rightJump = false;
				}
			}
			@Override
			public void keyTyped(KeyEvent e) 
			{
				
			}
		});
		
		addMouseListener(new MouseAdapter()
				{ 
					public void mousePressed(MouseEvent e)
					{
						if (e.getButton() == MouseEvent.BUTTON1 && !gun.isReloading() && !isPaused && gun.getBullets() > 0)
						{							
							gun.fire(game.getEnemies());
//							gun.checkEnemiesHit();
						}
					}
					public void mouseReleased(MouseEvent e)
					{
						gun.unfire();
					}
				});
		addMouseWheelListener(new MouseWheelListener()
				{
					@Override
					public void mouseWheelMoved(MouseWheelEvent arg0) 
					{
						jump();
					}
				});
		setFocusable(true);
	}

	public Floor onFloor()
	{
		for (Quad floor : getMap().getFloors())
		{
			if (((Floor) floor).playerIsOn(this))
			{
				return (Floor) floor;
			}
		}
		return null;
	}
	public Floor belowFloor()
	{
		for (Quad floor : getMap().getFloors())
		{
			if (((Floor) floor).playerIsBelow(this))
			{
				return (Floor) floor;
			}
		}
		return null;
	}	
	private Wall onWall()
	{
		for (Wall wall : getMap().getWalls())
		{
			if (wall.playerIsOn(this)) return wall;
		}
		return null;
	}
	
	public void jump()
	{
		if (!airborne || onWall() != null)
		{
			if (movingForward) 
			{
				zSpeed = Math.sin(getYAngle()+Math.PI/2)*moveSpeed;
				xSpeed = Math.cos(getYAngle()-Math.PI/2)*moveSpeed;
				forwardJump = true;
			}
			if (movingBackward)
			{
				zSpeed = (-1 * Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				xSpeed = (-1 * Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
				backwardJump = true;
			}
			if (movingLeft) 
			{
				xSpeed = (-1*Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				zSpeed = (Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
				leftJump = true;
			}
			if (movingRight)
			{
				xSpeed = (Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				zSpeed = (-1*Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
				rightJump = true;
			}
			verticalSpeed = JUMP_SPEED;
		}
		jumpingYAngle = getYAngle()%(Math.PI*2);
		if (xSpeed < 0) jumpingYAngle += Math.PI;

		airborne = true;
	}

	public void drawEnemies(Graphics2D g2d)
	{
		for (Enemy enemy : game.getEnemies())
		{
			enemy.paint(g2d, (Camera) this);
		}
	}
	
	public void getHurt(Enemy e)
	{
		regenTimer = 0;
		health -= e.getDamage();
		moveX((getX3D() - e.getX3D()));
		moveZ((getZ3D() - e.getZ3D()));
	}
	public void earnCash(int amount) {cash+=amount;}
	public void setRoundChanging(boolean changing){	roundChanging = changing;}
	
	public Gun getGun()	{return gun;}
	public Animator getAnimator() {return animator;}
	public Game getGame() {return game;}
	public double getMoveAngle() {return moveAngle;}
	public double getFeetY() {return getY3D() + getTallness();}
	public int getRegenTimer() {return regenTimer;}
	public int getCash() {return cash;}
	public double getTallness(){return tallness;}
	public double getHealth(){return health;}
	public boolean isPaused() {return isPaused;}

	public void buyGun() throws InstantiationException, IllegalAccessException
	{
		try
		{
			if (cash - mouseOnGun().getPrice() < 0)
			{
				
			}
			else
			{
				mouseOnGun().setPlayer(this);
				gun = mouseOnGun();
				cash -= mouseOnGun().getPrice();
				mouseOnGunHolder().resetGun();
			}
		}
		catch (NullPointerException e) {}

	}
	
	public GunHolder mouseOnGunHolder()
	{
		for (GunHolder gunHolder : getMap().getGunHolders())
		{
			if ((new Point(getCenterX(), getCenterY())).distance(gunHolder.getLocation().getProjection((Camera)this)) < gunHolder.getEffectiveRadius((Camera) this)
					&& this.getDist(gunHolder) < BUY_DIST)
			{
				return gunHolder;
			}
		}
		return null;
	}
	
	public Gun mouseOnGun()
	{
		for (GunHolder gunHolder : getMap().getGunHolders())
		{
			if ((new Point(getCenterX(), getCenterY())).distance(gunHolder.getLocation().getProjection((Camera)this)) < gunHolder.getEffectiveRadius((Camera) this)
					&& this.getDist(gunHolder) < BUY_DIST)
			{
				return gunHolder.getGun();
			}
		}
		return null;
	}
	
	public boolean isAlive()
	{
		return (getHealth() > 0 && !((Camera)(this)).getMap().getDeathZone().playerInKillZone(this));
	}
	
	public void drawHud()
	{
		animator.drawAmmo();
		animator.drawGun();
		
		animator.drawHealth();
		animator.drawCrossHair();
		animator.drawBulletTrails(gun.getBulletTrails());
		
		animator.drawRoundNum();
		
		animator.drawCash();
		
		try {animator.drawGunPriceTag(mouseOnGun());} catch (NullPointerException e){}
		
		if (gun.isReloading())
		{
			animator.drawReloadCircle();
		}

		if (roundChanging)
		{
			animator.drawRoundTitle();
		}
		
		if (isPaused)
		{
			animator.drawReloadCircle();
			animator.drawPaused();
		}
		else
		{
			//sets the cursor to an empty image
			animator.setClearCursor();
		}
	}
	
	//this method is so unorganized I would rather just leave it alone
	public void update()
	{
		gun.update();

		if (regenTimer >= REGEN_TIME)
		{
			regenTimer = REGEN_TIME;
			if (health < FULL_HEALTH)
			{
				health += REGEN_AMOUNT;	
			}
		}
		else
		{
			regenTimer += REGEN_SPEED;
		}
		moveAngle = Math.atan(zSpeed/xSpeed);
		if (xSpeed < 0) moveAngle += Math.PI;
		else if (zSpeed < 0) moveAngle = 2*Math.PI+moveAngle;
		
		setYAngle(getYAngle()%(Math.PI*2));
		if (getYAngle() < 0)
		{
			setYAngle(getYAngle() + Math.PI*2);
		}
		
		if (walking) moveSpeed = MOVE_SPEED_DEFAULT * 0.3;
		else moveSpeed = MOVE_SPEED_DEFAULT;
		
		if (isPaused) 
		{
			showPaused();
			return;
		}
		if (airborne)
		{
			if (forwardJump || backwardJump || rightJump || leftJump) 
			{
				xSpeed = 0;
				zSpeed = 0;
			}
			verticalSpeed -= GRAV_CONST;
			moveY(-1*verticalSpeed);
			if (belowFloor() != null)
			{
				setY3D(belowFloor().getY3D() - getTallness());
				airborne = false;
				verticalSpeed = 0;
			}
			if (onWall() != null)
			{
				setY3D(onWall().getY3D() - getTallness()-0.1);
				airborne = false;
				verticalSpeed = 0;
			}
			if (forwardJump) 
			{
				zSpeed += (Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				xSpeed += (Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
				forwardJump = false;
			}
			if (backwardJump)
			{
				zSpeed += (-1 * Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				xSpeed += (-1 * Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
				backwardJump = false;
			}
			if (leftJump) 
			{
				xSpeed += (-1*Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				zSpeed += (Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
				leftJump = false;
			}
			if (rightJump)
			{
				xSpeed += (Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				zSpeed += (-1*Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
				rightJump = false;
			}
			
			if (movingForward) 
			{		
				xSpeed += Math.sin(getYAngle()) * AIR_MOVE_ACCEL;
				zSpeed += Math.cos(getYAngle())* AIR_MOVE_ACCEL;
				if (Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(zSpeed, 2)) > MAX_LIN_SPEED)
				{
					xSpeed -= Math.sin(getYAngle()) * AIR_MOVE_ACCEL;
					zSpeed -= Math.cos(getYAngle())* AIR_MOVE_ACCEL;
					xSpeed *= 1/0.95;
					zSpeed *= 1/0.95;
				}

			}
			if (movingBackward)
			{
				xSpeed -= Math.sin(getYAngle())* AIR_MOVE_ACCEL;
				zSpeed -= Math.cos(getYAngle())* AIR_MOVE_ACCEL;
				if (Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(zSpeed, 2)) > MAX_LIN_SPEED)
				{
					xSpeed += Math.sin(getYAngle())* AIR_MOVE_ACCEL;
					zSpeed += Math.cos(getYAngle())* AIR_MOVE_ACCEL;
					xSpeed *= 1/0.95;
					zSpeed *= 1/0.95;
				}
			}
			if (movingLeft) 
			{
				xSpeed -= Math.cos(getYAngle())* AIR_MOVE_ACCEL;
				zSpeed += Math.sin(getYAngle())* AIR_MOVE_ACCEL;
				if (Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(zSpeed, 2)) > MAX_LIN_SPEED)
				{
					xSpeed += Math.cos(getYAngle())* AIR_MOVE_ACCEL;
					zSpeed -= Math.sin(getYAngle())* AIR_MOVE_ACCEL;
					xSpeed *= 1/0.95;
					zSpeed *= 1/0.95;
				}
			}
			if (movingRight)
			{
				xSpeed += Math.cos(getYAngle())* AIR_MOVE_ACCEL;
				zSpeed -= Math.sin(getYAngle())* AIR_MOVE_ACCEL;
				if (Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(zSpeed, 2)) > MAX_LIN_SPEED)
				{
					xSpeed -= Math.cos(getYAngle())* AIR_MOVE_ACCEL;
					zSpeed += Math.sin(getYAngle())* AIR_MOVE_ACCEL;
					xSpeed *= 1/0.95;
					zSpeed *= 1/0.95;
				}
			}
		}
		else if (!airborne)
		{
			xSpeed = 0;
			zSpeed = 0;
			if (movingForward) 
			{
				zSpeed += (Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				xSpeed += (Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
			}
			if (movingBackward)
			{
				zSpeed += (-1 * Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				xSpeed += (-1 * Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
			}
			if (movingLeft) 
			{
				xSpeed += (-1*Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				zSpeed += (Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
			}
			if (movingRight)
			{
				xSpeed += (Math.sin(getYAngle()+Math.PI/2)*moveSpeed);
				zSpeed += (-1*Math.cos(getYAngle()-Math.PI/2)*moveSpeed);
			}
		}
		//do not remove, they cancel out a multiplication in the airborne braces
		xSpeed *= 0.9;
		zSpeed *= 0.9;
		
		moveX(xSpeed);
		moveZ(zSpeed);
		
		//wall collision
		for (Wall wall : getMap().getWalls())
		{
			if (wall.playerIsOn(this)) onWall = wall;
			else onWall = null;
			if (wall.getOrientation() == 0)
			{
				if (wall.inContact(this))
				{
					moveZ(-1*zSpeed);
				}
				if (wall.collideWithEdge(this) == 0)
				{
					moveX(-1*xSpeed);
					moveX(-3);
				}
				else if(wall.collideWithEdge(this) == 1)
				{
					moveX(-1*xSpeed);
					moveX(3);
				}
			}
			else if (wall.getOrientation() == 2)
			{
				if (wall.inContact(this))
				{
					moveX(-1*xSpeed);
					if (getFeetY() < wall.getTopCorner().getY3D())
					{

					}
				}
				if (wall.collideWithEdge(this) == 0)
				{
					moveZ(-1*zSpeed);
					moveZ(-3);
				}
				if (wall.collideWithEdge(this) == 1)
				{
					moveZ(-1*zSpeed);
					moveZ(3);
				}
			}
		}

		if (onFloor() == null && onWall() == null)
		{
			airborne = true;
		}
		else
		{
			
		}
		
		//handling mouseinput
		double mouseVertToRotate = (MouseInfo.getPointerInfo().getLocation().getY() - getFrameLocation().getY() - getHeight()/2) * TURN_SPEED;
		double mouseYToRotate = (MouseInfo.getPointerInfo().getLocation().getX() - getFrameLocation().getX() - getWidth()/2) * TURN_SPEED;
		rotateVertical(-LOOK_SPEED*mouseVertToRotate);
			   rotateY( LOOK_SPEED*mouseYToRotate);
		if (getVertAngle() > MAX_VERT_ANGLE) setVertAngle(MAX_VERT_ANGLE);
		else if (getVertAngle() < -1*MAX_VERT_ANGLE) setVertAngle(-1*MAX_VERT_ANGLE);
		if (getFrame().isFocused())
		{
			robot.mouseMove(getHalfWidth()+getFrame().getX(), getHalfHeight()+getFrame().getY());
		}
	}
}
