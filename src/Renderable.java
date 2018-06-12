import java.awt.Graphics2D;

public interface Renderable extends Object3D
{
	public void paint(Graphics2D g2d, Camera camera);
}
