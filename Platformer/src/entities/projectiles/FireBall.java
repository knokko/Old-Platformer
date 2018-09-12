package entities.projectiles;

import java.awt.geom.Point2D;

import entities.Entity;

public class FireBall extends Projectile {

	public FireBall(Entity shooter, Point2D.Double target, boolean player) {
		super(shooter, target, player);
	}

	public FireBall(Entity shooter, Point2D.Double target) {
		super(shooter, target);
	}

	@Override
	public double getSpeed() {
		return 10;
	}

	@Override
	public String getName() {
		return "Fire Ball";
	}

	@Override
	public int getPower() {
		return 5;
	}
	
	@Override
	public double getWeight(){
		return 0.01;
	}
	
	@Override
	public double frictionMultiplier(){
		return 0;
	}
}
