package entities.projectiles;

import java.awt.geom.Point2D;

import entities.Entity;

public class Spike extends Projectile {

	public Spike(Entity shooter, Point2D.Double target) {
		super(shooter, target);
	}

	@Override
	public double getSpeed() {
		return 5000;
	}

	@Override
	public String getName() {
		return "spike";
	}

	@Override
	public int getPower() {
		return 100;
	}

}
