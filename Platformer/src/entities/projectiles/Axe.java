package entities.projectiles;

import java.awt.geom.Point2D;

import entities.Entity;
import entities.Player;

public class Axe extends Projectile {
	
	public final Entity user;
	public int ticks;

	public Axe(Entity shooter, Point2D.Double target, boolean player) {
		super(shooter, target, player);
		user = shooter;
	}

	public Axe(Entity shooter, Point2D.Double target) {
		super(shooter, target);
		user = shooter;
	}

	@Override
	public double getSpeed() {
		return 500;
	}

	@Override
	public String getName() {
		return "Axe";
	}
	
	@Override
	public int getPower(){
		return 1;
	}
	
	@Override
	public void update(){
		double distanceX = user.position.x - position.x;
		double distanceY = user.position.y - position.y;
		double distance = Math.hypot(distanceX, distanceY);
		double mx = (distanceX / distance) * 10;
		double my = (distanceY / distance) * 10;
		if(mx == mx)
			addForce(mx, 0);
		if(my == my)
			addForce(0, my);
		model().rotation += Math.hypot(motionX, motionY);
		super.update();
		++ticks;
	}
	
	@Override
	public double getWeight(){
		return 0.1;
	}
	
	@Override
	public boolean autoRotate(){
		return false;
	}
	
	@Override
	public boolean removeOnGround(){
		return false;
	}
	
	@Override
	public boolean removeOnHit(){
		return false;
	}
	
	@Override
	public void death(){
		super.death();
		if(user instanceof Player)
			++((Player)user).axes;
	}
	
	protected void tryAttack(){
		super.tryAttack();
		if(ticks > 20 && getCollider().hit(user.getCollider()) && !inactive){
			inactive = true;
			if(user instanceof Player){
				++((Player)user).axes;
				if(((Player) user).arm2().name.matches("arm"))
					((Player) user).setWeapon("axe");
			}
		}
	}
}
