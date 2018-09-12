package colliders;

import java.awt.geom.Point2D.Double;

public class ColliderCircle extends Collider {
	
	Double center;
	
	double radius;

	public ColliderCircle(double circleRadius, Double circleCenter) {
		radius = circleRadius;
		center = circleCenter;
	}

	@Override
	public boolean hit(Collider other) {
		if(other instanceof ColliderBox){
			return distanceToBox((ColliderBox) other) <= radius;
		}
		if(other instanceof ColliderCircle){
			ColliderCircle circle = (ColliderCircle) other;
			return circle.center.distance(center) <= radius + circle.radius;
		}
		if(other instanceof ColliderNull)
			return false;
		if(other instanceof ColliderTriangle || other instanceof ColliderList)
			return other.hit(this);
		throw new Collider.CollissionException();
	}

	@Override
	public boolean equals(Collider other) {
		if(other instanceof ColliderCircle){
			ColliderCircle circle = (ColliderCircle) other;
			return circle.center.equals(center) && radius == circle.radius;
		}
		return false;
	}

	@Override
	public double distanceTo(Collider other) {
		if(other instanceof ColliderBox)
			return distanceToBox((ColliderBox) other);
		if(other instanceof ColliderCircle){
			ColliderCircle circle = (ColliderCircle) other;
			double distance = center.distance(circle.center) - radius - circle.radius;
			return distance > 0 ? distance : 0;
		}
		if(other instanceof ColliderNull)
			return java.lang.Double.NaN;
		if(other instanceof ColliderTriangle || other instanceof ColliderList)
			return other.distanceTo(this);
		throw new Collider.CollissionException();
	}

	@Override
	public boolean isInCollider(Double position) {
		return position.distance(center) <= radius;
	}

	@Override
	public double minX() {
		return center.x - radius;
	}

	@Override
	public double minY() {
		return center.y - radius;
	}

	@Override
	public double maxX() {
		return center.x + radius;
	}

	@Override
	public double maxY() {
		return center.y + radius;
	}
	
	private double distanceToBox(ColliderBox box){
		double x = center.x;
		if(center.x > box.maxX)
			x = box.maxX;
		if(center.x < box.minX)
			x = box.minX;
		double y = center.y;
		if(center.y > box.maxY)
			y = box.maxY;
		if(center.y < box.minY)
			y = box.minY;
		return center.distance(x, y);
	}
}
