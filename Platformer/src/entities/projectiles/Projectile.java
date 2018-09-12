package entities.projectiles;

import java.awt.Point;
import java.awt.geom.Point2D;

import entities.Entity;
import entities.Player;
import render.Model;

public abstract class Projectile extends Entity {
	
	public boolean isPlayerOwned;
	
	protected double startRotation;
	protected double spawnX;
	
	public Projectile(Entity shooter, Point2D.Double target, boolean player) {
		super(shooter.game, shooter.world, shooter.position);
		spawnX = position.x;
		models.add(new Model("sprites/entities/projectiles/" + getName() + ".png", new Point(), new Point(), this));
		isPlayerOwned = player;
		double distanceX = target.x - position.x;
		double distanceY = target.y - position.y;
		double distance = Math.hypot(distanceX, distanceY);
		addForce((distanceX / distance) * getSpeed(), (distanceY / distance) * getSpeed());
		width = model().image.getWidth();
		height = model().image.getHeight();
		model().rotationPoint = new Point(width / 2, height / 2);
		startRotation = model().rotation = (int) Math.toDegrees(Math.atan2(-motionY, motionX));
	}
	
	public Projectile(Entity shooter, Point2D.Double target){
		this(shooter, target, shooter instanceof Player);
	}
	
	public abstract double getSpeed();
	public abstract String getName();
	public abstract int getPower();
	
	@Override
	public void update(){
		if(autoRotate())
			model().rotation = (int) Math.toDegrees(Math.atan2(-motionY, motionX));
		super.update();
	}
	
	public Model model(){
		return models.get(0);
	}
	
	public double getWeight(){
		return 2;
	}
	
	@Override
	public boolean teleport(Point2D.Double point, boolean test){
		if(super.teleport(point, test)){
			if(!test)
				tryAttack();
			if(inactive)
				return false;
			return true;
		}
		else {
			if(removeOnGround() && !test){
				inactive = true;
			}
			return false;
		}
	}
	
	@Override
	public boolean teleport(Point2D.Double point){
		return teleport(point, false);
	}
	
	@Override
	public double frictionMultiplier(){
		return 0.001;
	}
	
	@Override
	public double getUpdateRange(){
		return Double.POSITIVE_INFINITY;
	}
	
	protected void tryAttack(){
		if(isPlayerOwned && !inactive){
			int t = 0;
			while(t < world.monsters.size()){
				if(getCollider().hit(world.monsters.get(t).getCollider())){
					world.monsters.get(t).attack(position, getPower());
					if(removeOnHit()){
						inactive = true;
						return;
					}
				}
				++t;
			}
		}
		else if(!inactive){
			if(getCollider().hit(game.player.getCollider())){
				game.player.attack(position, getPower());
				if(removeOnHit()){
					inactive = true;
					return;
				}
			}
		}
	}
	
	public boolean autoRotate(){
		return true;
	}
	
	public boolean removeOnGround(){
		return true;
	}
	
	public boolean removeOnHit(){
		return true;
	}
}
