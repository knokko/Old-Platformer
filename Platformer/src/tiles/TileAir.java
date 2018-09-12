package tiles;

import java.awt.Graphics;

import colliders.Collider;
import colliders.ColliderNull;

public class TileAir extends Tile {

	public TileAir() {
		super("");
	}
	
	@Override
	public Collider getCollider(int x, int y){
		return new ColliderNull();
	}
	
	@Override
	public void paint(Graphics gr, int x, int y){}
}
