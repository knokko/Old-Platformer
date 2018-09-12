package entities.projectiles;

import java.awt.geom.Point2D.Double;

import entities.Entity;

public class Arrow extends Projectile {
	
	public static int[] results = new int[360];

	public Arrow(Entity shooter, Double target, boolean player) {
		super(shooter, target, player);
	}

	public Arrow(Entity shooter, Double target) {
		super(shooter, target);
	}

	@Override
	public double getSpeed() {
		return 5000;
	}

	@Override
	public String getName() {
		return "Arrow";
	}

	@Override
	public int getPower() {
		return 5;
	}

}
