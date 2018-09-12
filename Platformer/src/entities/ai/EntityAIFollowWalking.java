package entities.ai;

import java.awt.geom.Point2D;

import colliders.Collider;
import tiles.Tile;
import tiles.TileAir;
import entities.Entity;

public class EntityAIFollowWalking extends EntityAI {
	
	public Entity target;
	public int range;

	public EntityAIFollowWalking(Entity follower, Entity followed, int requiredRange) {
		super(follower);
		target = followed;
		range = requiredRange;
	}
	
	@Override
	public void update(){
		Point2D.Double p = target.position;
		int speed = 1;
		double rotation = Math.toRadians(entity.world.getGravityDirection(entity.position) + 90);
		Point2D.Double point1 = new Point2D.Double(Math.cos(rotation) + entity.position.x, Math.sin(rotation) + entity.position.y);
		Point2D.Double point2 = new Point2D.Double(-Math.cos(rotation) + entity.position.x, -Math.sin(rotation) + entity.position.y);
		double distance = entity.getCollider().distanceTo(target.getCollider());
		if(distance > range){
			double distance1 = point1.distance(p);
			double distance2 = point2.distance(p);
			double distance0 = entity.position.distance(p);
			if(distance2 < distance0 && distance2 < distance1)
				entity.moveX -= speed;
			if(distance1 < distance0 && distance1 < distance2)
				entity.moveX += speed;
			if(entity.onGround && !entity.move(entity.moveX * Math.cos(rotation), entity.moveX * Math.sin(rotation), true))
				entity.jump((Integer) getMethod("jumpPower"));
			else if(entity.onGround){
				Point2D.Double destination = new Point2D.Double((int) (entity.position.x + entity.motionX), entity.position.y);
				Collider col = entity.getCollider(destination);
				double fw = (col.maxX() - col.minX()) / 2;
				Tile tile1 = entity.world.getTile(col.minX() + fw, destination.y);
				Tile tile2 = entity.world.getTile(col.minX() - 60 * Math.sin(rotation) + fw, destination.y - 60 * Math.cos(rotation));
				Tile tile3 = entity.world.getTile(col.minX() - 120 * Math.sin(rotation) + fw, destination.y - 120 * Math.cos(rotation));
				Tile tile4 = entity.world.getTile(col.maxX() - fw, destination.y);
				Tile tile5 = entity.world.getTile(col.maxX() - 60 * Math.sin(rotation) - fw, destination.y - 60 * Math.cos(rotation));
				Tile tile6 = entity.world.getTile(col.maxX() - 120 * Math.sin(rotation) - fw, destination.y - 120 * Math.cos(rotation));
				if(tile1 instanceof TileAir && tile2 instanceof TileAir && tile3 instanceof TileAir && tile4 instanceof TileAir && tile5 instanceof TileAir && tile6 instanceof TileAir)
					entity.jump((Integer) getMethod("jumpPower"));
			}
		}
		else if(distance < range){
			if(point1.distance(p) < entity.position.distance(p) && point1.distance(p) < point2.distance(p))
				entity.moveX -= speed;
			if(point2.distance(p) < entity.position.distance(p) && point2.distance(p) < point1.distance(p))
				entity.moveX += speed;
		}
		if(distance - entity.moveX >= range && distance < range)
			entity.moveX = (int) -(range - distance);
		if(distance + entity.moveX <= range && distance > range)
			entity.moveX = (int) (range - distance);
	}

}
