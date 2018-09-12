package entities.monsters;

import java.awt.Point;
import java.awt.geom.Point2D;

import entities.Entity;
import entities.ai.EntityAIFollowFlying;
import entities.ai.EntityAIMoveArms;
import entities.ai.EntityAIMoveWings;
import entities.ai.EntityAIRangedAttack;
import entities.projectiles.FireBall;
import render.Model;
import main.Game;
import world.World;

public class Bird extends Monster {

	public Bird(Game game, World world, Point2D.Double position) {
		super(game, world, position);
		Model body = new Model("sprites/entities/enemies/bird/body.png", new Point(-1, 20), new Point(5, 10), this);
		models.add(new Model("sprites/entities/enemies/bird/wing.png", new Point(-1, 5), new Point(23, 4), this).setParent(body));
		models.add(new Model("sprites/entities/enemies/bird/leg.png", new Point(-4, 8), new Point(1, 1), this).setParent(body));
		models.add(body);
		models.add(new Model("sprites/entities/enemies/bird/leg.png", new Point(-4, 8), new Point(1, 1), this).setParent(body));
		models.add(new Model("sprites/entities/enemies/bird/wing.png", new Point(-1, 5), new Point(23, 4), this).setParent(body));
		ai.add(new EntityAIFollowFlying(this, game.player, 250));
		ai.add(new EntityAIRangedAttack(this, game.player, 350, 100));
		ai.add(new EntityAIMoveWings(this));
		ai.add(new EntityAIMoveArms(this));
	}
	
	public double getWeight(){
		return 60;
	}
	
	@Override
	public double getSpeed(){
		return 50;
	}
	
	@Override
	public Model gravityModel(){
		return body();
	}
	
	@Override
	public boolean canFly(){
		return true;
	}
	
	public Model wing1(){
		return models.get(0);
	}
	
	public Model wing2(){
		return models.get(4);
	}
	
	public Model body(){
		return models.get(2);
	}
	
	public Model arm1(){
		return models.get(1);
	}
	
	public Model arm2(){
		return models.get(3);
	}
	
	public FireBall projectile(Entity target){
		return new FireBall(this, target.position);
	}
}
