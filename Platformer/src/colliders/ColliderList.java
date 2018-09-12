package colliders;

import java.awt.geom.Point2D.Double;

public class ColliderList extends Collider {
	
	Collider[] colliders;
	
	double minX;
	double minY;
	double maxX;
	double maxY;

	public ColliderList(Collider... list) {
		colliders = list;
		minX = colliders[0].minX();
		minY = colliders[0].minY();
		maxX = colliders[0].maxX();
		maxY = colliders[0].maxY();
		int t = 1;
		while(t < colliders.length){
			if(colliders[t].minX() < minX)
				minX = colliders[t].minX();
			if(colliders[t].minY() < minY)
				minY = colliders[t].minY();
			if(colliders[t].maxX() > maxX)
				maxX = colliders[t].maxX();
			if(colliders[t].maxY() > maxY)
				maxY = colliders[t].maxY();
			++t;
		}
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
		if(other instanceof ColliderNull || minX > other.maxX() || maxX < other.minX() || minY > other.maxY() || maxY < other.minY())
			return false;
		int t = 0;
		while(t < colliders.length){
			if(colliders[t].hit(other))
				return true;
			++t;
		}
		return false;
	}

	@Override
	public boolean equals(Collider other) {
		if(other instanceof ColliderList){
			ColliderList list = (ColliderList) other;
			if(list.colliders.length == colliders.length){
				int t = 0;
				while(t < colliders.length){
					if(!colliders[t].equals(list.colliders[t]))
						return false;
					++t;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public double distanceTo(Collider other) {
		double distance = colliders[0].distanceTo(other);
		int t = 1;
		while(t < colliders.length){
			double d = colliders[t].distanceTo(other);
			if(d < distance)
				distance = d;
			if(distance == 0)
				return distance;
			++t;
		}
		return distance;
	}

	@Override
	public boolean isInCollider(Double position) {
		if(position.x >= minX && position.x <= maxX && position.y >= minY && position.y <= maxY){
			int t = 0;
			while(t < colliders.length){
				if(colliders[t].isInCollider(position))
					return true;
				++t;
			}
		}
		return false;
	}

}
