package entities.projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import util.PointUtils;
import entities.Entity;

public class GrablingHook extends Projectile {
	
	public double maxLength;
	public final Entity owner;
	public Entity victim;
	
	public boolean inTile;
	public boolean blocked;

	public GrablingHook(Entity shooter, Point2D.Double target, boolean player) {
		super(shooter, target, player);
		owner = shooter;
		owner.hook = this;
	}

	public GrablingHook(Entity shooter, Point2D.Double target) {
		super(shooter, target);
		owner = shooter;
		owner.hook = this;
	}

	@Override
	public double getSpeed() {
		return 2000;
	}

	@Override
	public String getName() {
		return "grabling hook";
	}

	@Override
	public int getPower() {
		return 100;
	}
	
	@Override
	public void update(){
		if(inTile){
			motionX = 0;
			motionY = 0;
		}
		if(victim != null){
			position = victim.position;
			if(victim.inactive)
				inactive = true;
		}
		super.update();
		double back = maxLength;
		if(position.distance(owner.position) > maxLength && inTile){
			pull((int) (position.distance(owner.position) - maxLength));
			blocked = true;
		}
		maxLength = back;
	}
	
	@Override
	public void paint(Graphics gr){
		gr.setColor(Color.BLACK);
		Point p = PointUtils.gameToScreenPoint(game, position);
		Point o = PointUtils.gameToScreenPoint(game, owner.position);
		gr.drawLine(p.x, p.y, o.x, o.y);
		super.paint(gr);
	}
	
	@Override
	public void tryAttack(){
		if(isPlayerOwned && !inactive && !inTile){
			int t = 0;
			while(t < world.monsters.size()){
				if(getCollider().hit(world.monsters.get(t).getCollider())){
					victim = world.monsters.get(t);
					victim.attack(position, getPower());
					position = victim.position;
					maxLength = position.distance(owner.position);
					inTile = true;
					return;
				}
				++t;
			}
		}
	}
	
	@Override
	public boolean removeOnGround(){
		maxLength = position.distance(owner.position);
		inTile = true;
		return false;
	}
	
	@Override
	public void paintIfInRange(Graphics gr){
		paint(gr);
	}
	
	public void pull(int power){
		double rotation = Math.atan2(position.y - owner.position.y, position.x - owner.position.x);
		if(victim != null){
			owner.addAimedForce(Math.toDegrees(rotation), power * 100);
			victim.addAimedForce(Math.toDegrees(rotation) - 180, power * 100);
		}
		else if(inTile)
			owner.addAimedForce(Math.toDegrees(rotation), power * 500);
		else {
			owner.addAimedForce(Math.toDegrees(rotation), power * 100);
			addAimedForce(Math.toDegrees(rotation) - 180, power * 100);
		}
		maxLength = position.distance(owner.position);
	}
}
