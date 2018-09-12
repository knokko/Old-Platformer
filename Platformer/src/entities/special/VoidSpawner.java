package entities.special;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D.Double;

import colliders.Collider;
import main.Game;
import util.PointUtils;
import util.Random;
import world.World;
import entities.Entity;
import entities.Shape;
import entities.projectiles.VoidBall;

public class VoidSpawner extends Entity {

	public VoidSpawner(Game game, World world, Double position, int radius) {
		super(game, world, position);
		this.width = radius * 2;
	}
	
	@Override
	public boolean canFly(){
		return true;
	}
	
	@Override
	public void update(){
		if(Random.chance(50)){
			Collider c = getCollider();
			world.spawnEntity(new VoidBall(this, new Double(c.minX() + Random.getInt(width), c.minY() + Random.getInt(height)), false));
		}
	}
	
	@Override
	public double getUpdateRange(){
		return java.lang.Double.POSITIVE_INFINITY;
	}
	
	@Override
	public Shape getCollidingShape(){
		return Shape.CIRCLE;
	}
	
	@Override
	public void paint(Graphics gr){
		Collider c = getCollider();
		Point min = PointUtils.gameToScreenPoint(game, new Double(c.minX(), c.maxY()));
		Point max = PointUtils.gameToScreenPoint(game, new Double(c.maxX(), c.minY()));
		Point center = PointUtils.gameToScreenPoint(game, position);
		int radius = center.x - min.x;
		int x = min.x;
		while(x <= max.x){
			int y = min.y;
			while(y <= max.y){
				if(new Point(x, y).distance(center) <= radius){
					gr.setColor(new Color(Random.getInt(100), 0, Random.getInt(100), Random.getInt(150, 255)));
					gr.fillRect(x, y, 1, 1);
				}
				++y;
			}
			++x;
		}
	}
}
