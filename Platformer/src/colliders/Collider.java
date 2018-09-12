package colliders;

import java.awt.geom.Point2D;

public abstract class Collider {
	
	public abstract double minX();
	
	public abstract double minY();
	
	public abstract double maxX();
	
	public abstract double maxY();
	
	public abstract boolean hit(Collider other);
	
	public abstract boolean equals(Collider other);
	
	public abstract double distanceTo(Collider other);
	
	public abstract boolean isInCollider(Point2D.Double position);
	
	public static class CollissionException extends RuntimeException {

		private static final long serialVersionUID = -4849637632915697254L;
		
	}
}
