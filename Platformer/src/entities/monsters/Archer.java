package entities.monsters;

import java.awt.Point;
import java.awt.geom.Point2D.Double;

import render.Model;
import entities.Entity;
import entities.ai.*;
import entities.projectiles.Arrow;
import main.Game;
import world.World;

public class Archer extends Monster {

	public Archer(Game game, World world, Double position) {
		super(game, world, position);
		Model body = new Model("sprites/entities/enemies/archer/body.png", new Point(0, 20), new Point(5, 10), this);
		models.add(new Model("sprites/entities/enemies/archer/leg.png", new Point(0, 17), new Point(5, 3), this).setParent(body));
		models.add(new Model("sprites/entities/enemies/archer/arm.png", new Point(5, -3), new Point(3, 12), this).setParent(body));
		models.add(body);
		models.add(new Model("sprites/entities/enemies/archer/head.png", new Point(1, -14), new Point(4, 14), this).setParent(body));
		models.add(new Model("sprites/entities/enemies/archer/leg.png", new Point(0, 17), new Point(5, 3), this).setParent(body));
		models.add(new Model("sprites/entities/enemies/archer/bow arm.png", new Point(5, -8), new Point(3, 17), this).setParent(body));
		ai.add(new EntityAIMoveLegs(this));
		ai.add(new EntityAIMoveArms(this));
		ai.add(new EntityAIFollowWalking(this, game.player, 400));
		ai.add(new EntityAIRangedAttack(this, game.player, 600, 100));
	}
	
	@Override
	public void update(){
		super.update();
		Arrow projectile = new Arrow(this, game.player.position);
		arm1().rotation = arm2().rotation = projectile.model().rotation - body().rotation;
		if(facingLeft){
			arm2().rotation -= 180;
			arm1().rotation -= 180;
		}
		if(cooldown > 0){
			if(position.x > game.player.position.x)
				facingLeft = true;
			else
				facingLeft = false;
		}
	}
	
	@Override
	public double getSpeed(){
		return 2000;
	}
	
	@Override
	public double getWeight(){
		return 80;
	}
	
	@Override
	public int jumpPower(){
		return 45000;
	}
	
	@Override
	public Model gravityModel(){
		return body();
	}
	
	public Arrow projectile(Entity target){
		return new Arrow(this, target.position);
	}
	
	public Model leg1(){
		return models.get(0);
	}
	
	public Model leg2(){
		return models.get(4);
	}
	
	public Model body(){
		return models.get(2);
	}
	
	public Model arm1(){
		return models.get(1);
	}
	
	public Model arm2(){
		return models.get(5);
	}
	
	public Model head(){
		return models.get(3);
	}
}
