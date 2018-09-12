package tiles;

import colliders.Collider;
import colliders.ColliderBox;
import colliders.ColliderList;
import colliders.ColliderTriangle;

public class TileTriangle extends Tile {
	
	int minX;
	int minY;
	int maxX;
	int maxY;
	
	boolean left;

	public TileTriangle(String name, int minX, int minY, int maxX, int maxY, boolean openLeft) {
		super(name);
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		left = openLeft;
	}
	
	public Collider getCollider(int x, int y){
		x = x / 60 * 60;
		y = y / 60 * 60;
		if(minY == 0)
			return new ColliderTriangle(x + minX, y + minY, x + maxX, y + maxY, left);
		else
			return new ColliderList(new ColliderTriangle(x + minX, y + minY, x + maxX, y + maxY, left), new ColliderBox(x + minX, x + maxX, y, y + minY));
	}
}
