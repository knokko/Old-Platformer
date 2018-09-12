package tiles;

import java.awt.geom.Point2D;

import colliders.Collider;
import colliders.ColliderCircle;

public class TileSphere extends Tile {
	
	public final double radius;

	public TileSphere(String name, double radius) {
		super(name);
		this.radius = radius;
	}
	
	@Override
	public Collider getCollider(int x, int y){
		x = x / 60 * 60;
		y = y / 60 * 60;
		return new ColliderCircle(radius, new Point2D.Double(x + 30, y + 30));
	}
}
