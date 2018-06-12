import java.awt.Graphics2D;

/**
 * A class that is renderable is one that can be painted onto a Camera object.
 * @author Andrew Weller andrewweller.cs@gmail.com
 */
public interface Renderable extends Object3D
{
	public void paint(Graphics2D g2d, Camera camera);
}
