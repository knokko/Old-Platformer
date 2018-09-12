package colliders;

import java.awt.geom.Point2D.Double;

public class ColliderTriangle extends Collider {
	
	double minX;
	double maxX;
	double minY;
	double maxY;
	
	double rc;
	
	boolean left;

	public ColliderTriangle(double xMin, double yMin, double xMax, double yMax, boolean openLeft) {
		minX = xMin;
		minY = yMin;
		maxX = xMax;
		maxY = yMax;
		left = openLeft;
		rc = (maxY - minY) / (maxX - minX);
	}

	@Override
	public double minX() {
		return minX;
	}

	@Override
	public double minY() {
		return minY;
	}

	@Override
	public double maxX() {
		return maxX;
	}

	@Override
	public double maxY() {
		return maxY;
	}

	@Override
	public boolean hit(Collider other) {
		if(other instanceof ColliderBox){
			ColliderBox box = (ColliderBox) other;
			if(box.minX <= maxX && box.maxX >= minX && box.minY <= maxY && box.maxY >= minY){
				if(isInCollider(new Double(box.minX, box.minY)) || isInCollider(new Double(box.minX, box.maxY)) || isInCollider(new Double(box.maxX, box.minY)) || isInCollider(new Double(box.maxX, box.maxY)))
					return true;
				if(left){
					double x = minX;
					if(box.minX > minX)
						x = box.minX;
					double y = minY;
					if(box.minY > minY)
						y = box.minY;
					if(isInCollider(new Double(x, y)))
						return true;
				}
				else {
					double x = maxX;
					if(box.maxX < maxX)
						x = box.maxX;
					double y = minY;
					if(box.minY > minY)
						y = box.minY;
					if(isInCollider(new Double(x, y)))
						return true;
				}
			}
			return false;
		}
		if(other instanceof ColliderCircle){
			ColliderCircle circle = (ColliderCircle) other;
			if(circle.minX() <= maxX && circle.maxX() >= minX && circle.minY() <= maxY && circle.maxY() >= minY){
				if(circle.isInCollider(new Double(minX, minY)) || circle.isInCollider(new Double(maxX, minY)) || circle.isInCollider(new Double(left ? maxX : minX, maxY)))
					return true;
				double rotation = Math.atan2(minY - circle.center.y, left ? maxX - circle.center.x : minX - circle.center.x);
				double x = circle.center.x + Math.cos(rotation) * circle.radius;
				double y = circle.center.y + Math.sin(rotation) * circle.radius;
				if(isInCollider(new Double(x, y)))
					return true;
				x = left ? maxX : minX;
				y = circle.center.y;
				if(y > maxY)
					y = maxY;
				if(y < minY)
					y = minY;
				if(circle.isInCollider(new Double(x, y)))
					return true;
			}
			return false;
		}
		if(other instanceof ColliderTriangle){
			ColliderTriangle tri = (ColliderTriangle) other;
			if(tri.minX <= maxX && tri.maxX >= minX && tri.minY <= maxY && tri.maxY >= minY){
				if(isInCollider(new Double(tri.minX, tri.minY)) || isInCollider(new Double(tri.maxX, tri.minY)) || isInCollider(new Double(tri.left ? tri.maxX : tri.minX, tri.maxY)))
					return true;
				if(left){
					double x = minX;
					if(tri.minX > minX)
						x = tri.minX;
					double y = minY;
					if(tri.minY > minY)
						y = tri.minY;
					if(isInCollider(new Double(x, y)) && tri.isInCollider(new Double(x, y)))
						return true;
				}
				else {
					double x = maxX;
					if(tri.maxX < maxX)
						x = tri.maxX;
					double y = minY;
					if(tri.minY > minY)
						y = tri.minY;
					if(isInCollider(new Double(x, y)) && tri.isInCollider(new Double(x, y)))
						return true;
				}
			}
		}
		if(other instanceof ColliderNull)
			return false;
		if(other instanceof ColliderList)
			return other.hit(this);
		throw new Collider.CollissionException();
	}

	@Override
	public boolean equals(Collider other) {
		if(other instanceof ColliderTriangle){
			ColliderTriangle triangle = (ColliderTriangle) other;
			return triangle.minX == minX && triangle.minY  == minY && triangle.maxX == maxX && triangle.maxY == maxY && triangle.left == left;
		}
		return false;
	}

	@Override
	public double distanceTo(Collider other) {
		if(hit(other))
			return 0;
		if(other instanceof ColliderNull)
			return java.lang.Double.NaN;
		if(other instanceof ColliderList)
			return other.distanceTo(this);
		if(other instanceof ColliderBox){
			ColliderBox box = (ColliderBox) other;
			double distance;
			double d;
			double y1 = maxY;
			if(y1 > box.maxY && box.maxY >= minY)
				y1 = box.maxY;
			else if(y1 > box.maxY)
				y1 = minY;
			double x = left ? maxX : minX;
			if(x > box.maxX)
				x = box.maxX;
			if(x < box.minX)
				x = box.minX;
			double y = y1;
			if(y > box.maxY)
				y = box.maxY;
			if(y < box.minY)
				y = box.minY;
			distance = new Double(x, y).distance(new Double(left ? maxX : minX, y1));
			double x1 = maxX;
			if(x1 > box.maxX && box.maxX >= minX)
				x1 = box.maxX;
			else if(x1 > box.maxX)
				x1 = minX;
			x = x1;
			if(x > box.maxX)
				x = box.maxX;
			if(x < box.minX)
				x = box.minX;
			d = new Double(x, y).distance(new Double(x1, minY));
			if(d < distance)
				distance = d;
			if(box.minX > minX && box.maxX < maxX && box.minY > minY){
				if(left){
					// box.minX = 22 en box.maxX = 27 en box.minY = 52 && box.maxY = 56 en minX = 20 en maxX = 35 en minY = 30 en maxY = 45
					// rc =  (45 - 30) / (35 - 20) = 15 / 15 = 1
					// angle = atan(rc) + 1.5PI = 6.27
					// startY = 30 + (27 - 20) * 1 = 30 + 7 * 1 = 37
					// d = (37 - 52) / (sin(6.27) - sin(1.56)) = (-15) / (-1.01) = 14.81
					double angle = Math.atan(rc) + 1.5 * Math.PI; //5.76
					double startY = minY + (box.maxX - minX) * rc; // 18.66
					//box.minY + sin(angle) * l = startY + sin(angle - 1.5PI) * l
					//sin(angle) * l - sin(angle - 1.5PI) * l = startY - box.minY
					//l * (sin(angle) - sin(angle - 1.5PI)) = startY - box.minY
					//l = (startY - box.minY) / (sin(angle) - sin(angle - 1.5PI))
					d = (startY - box.minY) / (Math.sin(angle) - Math.sin(angle - 1.5 * Math.PI));
				}
				else {
					double angle = Math.atan(rc) + 1.5 * Math.PI;
					double startY = (maxX - box.minX) * rc;
					d = (startY - box.minY) / (Math.sin(angle) - Math.sin(angle - 1.5 * Math.PI));
				}
				if(d < distance)
					distance = d;
			}
			return distance;
		}
		if(other instanceof ColliderCircle){
			//TODO finish this!
		}
		if(other instanceof ColliderTriangle){
			//TODO finish this!
		}
		throw new Collider.CollissionException();
	}

	@Override
	public boolean isInCollider(Double position) {
		if(position.x < minX || position.x > maxX || position.y < minY || position.y > maxX)
			return false;
		double x = left ? position.x - minX : maxX - position.x;
		double y = minY + rc * x;
		return position.y < y;
	}
	
	public double distanceToCollider(){
		return 0;
	}
}
