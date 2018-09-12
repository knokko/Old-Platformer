package entities.ai;

import java.awt.geom.Point2D;

import entities.Entity;

public class EntityAIFollowFlying extends EntityAI {
	
	public Entity target;
	public int range;

	public EntityAIFollowFlying(Entity follower, Entity followed, int bestRange) {
		super(follower);
		target = followed;
		range = bestRange;
	}
	
	@Override
	public void update(){
		Point2D.Double p = target.position;
		double speed = (Double) getMethod("getSpeed");
		double distance = entity.getCollider().distanceTo(target.getCollider());
		double angle = Math.atan2(p.y - entity.position.y, p.x - entity.position.x);
		if(distance > range){
			entity.moveX = Math.cos(angle) * speed;
			entity.moveY = Math.sin(angle) * speed;
		}
		else if(distance < range){
			entity.moveX = -Math.cos(angle) * speed;
			entity.moveY = -Math.sin(angle) * speed;
		}
		if(distance - entity.moveX >= range && distance < range)
			entity.moveX = (int) -(range - distance);
		if(distance + entity.moveX <= range && distance > range)
			entity.moveX = (int) (range - distance);
		if(distance - entity.moveY >= range && distance < range)
			entity.moveY = (int) -(range - distance);
		if(distance + entity.moveY <= range && distance > range)
			entity.moveY = (int) (range - distance);
	}
}
