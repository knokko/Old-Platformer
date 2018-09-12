package particles;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class Particle {
	
	public Point2D.Double position;
	
	public boolean inactive;
	
	public Particle(Point2D.Double point) {
		position = point;
	}
	
	public abstract void update();
	public abstract void paint(Graphics gr);
}
