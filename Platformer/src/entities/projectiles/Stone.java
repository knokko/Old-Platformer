package entities.projectiles;

import java.awt.geom.Point2D.Double;

import entities.Entity;

public class Stone extends Projectile {

	public Stone(Entity shooter, Double target, boolean player) {
		super(shooter, target, player);
	}

	public Stone(Entity shooter, Double target) {
		super(shooter, target);
	}

	@Override
	public double getSpeed() {
		return 5000000;
	}

	@Override
	public String getName() {
		return "Stone";
	}

	@Override
	public int getPower() {
		return 20;
	}
	
	@Override
	public double getWeight(){
		return 2000;
	}
	
	@Override
	public double frictionMultiplier(){
		return 0;
	}
	
	public boolean test(){
		return false;
	}

}
