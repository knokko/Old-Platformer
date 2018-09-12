package colliders;

import java.awt.geom.Point2D.Double;

public class ColliderNull extends Collider {

	public ColliderNull() {}

	@Override
	public boolean hit(Collider other){
		return false;
	}

	@Override
	public boolean equals(Collider other) {
		return other instanceof ColliderNull;
	}
	
	@Override
	public String toString(){
		return "null collider";
	}

	@Override
	public double distanceTo(Collider other) {
		return java.lang.Double.NaN;
	}

	@Override
	public boolean isInCollider(Double position) {
		return false;
	}

	@Override
	public double minX() {
		return java.lang.Double.NaN;
	}

	@Override
	public double minY() {
		return java.lang.Double.NaN;
	}

	@Override
	public double maxX() {
		return java.lang.Double.NaN;
	}

	@Override
	public double maxY() {
		return java.lang.Double.NaN;
	}
}
