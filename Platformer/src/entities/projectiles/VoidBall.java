package entities.projectiles;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import entities.Entity;
import entities.monsters.VoidPuncher;

public class VoidBall extends Projectile {

	public VoidBall(Entity shooter, Double target, boolean player) {
		super(shooter, target, player);
	}

	public VoidBall(Entity shooter, Double target) {
		super(shooter, target);
	}

	@Override
	public double getSpeed() {
		return 10;
	}

	@Override
	public String getName() {
		return "Void Ball";
	}

	@Override
	public int getPower() {
		return 10;
	}
	
	@Override
	public double frictionMultiplier(){
		return 0;
	}
	
	@Override
	public double getWeight(){
		return 0.01;
	}
	
	@Override
	public boolean removeOnGround(){
		if(!inactive)
			world.spawnEntity(new VoidPuncher(game, world, new Point2D.Double(position.x, getCollider().maxY() + 30)));
		return true;
	}
}
