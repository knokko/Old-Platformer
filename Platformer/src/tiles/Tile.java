package tiles;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import colliders.Collider;
import colliders.ColliderBox;
import main.Game;
import util.PointUtils;
import util.Utils;

public class Tile {
	
	public final Image image;
	public final byte id;
	
	private static byte nextId;
	
	Tile(String name) {
		if(name != null && !name.isEmpty())
			image = Utils.getImage("sprites/tiles/" + name + ".png");
		else
			image = null;
		id = nextId;
		Tiles.tiles.add(this);
		nextId++;
	}
	
	public void paint(Graphics gr, int tileX, int tileY){
		Point screen = PointUtils.tileToScreenPoint(Game.game, new Point(tileX, tileY));
		double z = Game.game.zoom;
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		Game g = Game.game;
		if(screen.x + z * w >= g.getX() && screen.y >= g.getY() && screen.x <= g.getX() + g.getWidth() && screen.y - z * h<= g.getY() + g.getHeight()){
			int hz = (int) (h * z);
			if(h * z != hz)
				++hz;
			int wz = (int) (w * z);
			if(w * z != wz)
				++wz;
			gr.drawImage(image, screen.x, (int) (screen.y - h * z), wz, hz, null);
		}
	}
	
	public Collider getCollider(double x, double y){
		return getCollider((int)x, (int)y);
	}
	
	public Collider getCollider(int x, int y){
		x = x / 60 * 60;
		y = y / 60 * 60;
		return new ColliderBox(x, x + 60, y, y + 60);
	}
}
