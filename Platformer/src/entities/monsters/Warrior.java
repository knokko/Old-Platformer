package entities.monsters;

import java.awt.Point;
import java.awt.geom.Point2D;

import entities.ai.EntityAIFollowWalking;
import entities.ai.EntityAIMeleeAttack;
import entities.ai.EntityAIMoveArms;
import entities.ai.EntityAIMoveLegs;
import render.Model;
import main.Game;
import world.World;

public class Warrior extends Monster {

	public Warrior(Game game, World world, Point2D.Double position) {
		super(game, world, position);
		Model body = new Model("sprites/entities/enemies/warrior/body.png", new Point(0, 20), new Point(5, 10), this);
		models.add(new Model("sprites/entities/enemies/warrior/leg.png", new Point(0, 17), new Point(5, 3), this).setParent(body ));
		models.add(new Model("sprites/entities/enemies/warrior/shield arm.png", new Point(5, -3), new Point(3, 12), this).setParent(body));
		models.add(body);
		models.add(new Model("sprites/entities/enemies/warrior/head.png", new Point(1, -14), new Point(4, 14), this).setParent(body));
		models.add(new Model("sprites/entities/enemies/warrior/leg.png", new Point(0, 17), new Point(5, 3), this).setParent(body));
		models.add(new Model("sprites/entities/enemies/warrior/sword arm.png", new Point(5, -8), new Point(3, 17), this).setParent(body));
		ai.add(new EntityAIFollowWalking(this, game.player, 0));
		ai.add(new EntityAIMoveLegs(this));
		ai.add(new EntityAIMoveArms(this));
		ai.add(new EntityAIMeleeAttack(this, game.player, 0));
	}
	
	@Override
	public int jumpPower(){
		return 70000;
	}
	
	@Override
	public double getWeight(){
		return 150;
	}
	
	@Override
	public double getSpeed(){
		return 3000;
	}
	
	@Override
	public Model gravityModel(){
		return body();
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
	
	public int swingSpeed(){
		return 5;
	}
}
