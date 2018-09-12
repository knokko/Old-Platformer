package entities.monsters;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D.Double;

import render.Model;
import entities.ai.EntityAIFollowWalking;
import entities.ai.EntityAIMeleeAttack;
import entities.ai.EntityAIMoveArms;
import entities.ai.EntityAIMoveLegs;
import main.Game;
import util.Random;
import world.World;

public class VoidPuncher extends Monster {

	public VoidPuncher(Game game, World world, Double position) {
		super(game, world, position);
		Model body = new Model("sprites/entities/enemies/void puncher/body.png", new Point(0, 20), new Point(5, 10), this);
		models.add(new Model("sprites/entities/enemies/void puncher/leg.png", new Point(0, 17), new Point(5, 3), this).setParent(body ));
		models.add(new Model("sprites/entities/enemies/void puncher/arm.png", new Point(5, 2), new Point(2, 3), this).setParent(body));
		models.add(body);
		models.add(new Model("sprites/entities/enemies/void puncher/head.png", new Point(1, -14), new Point(4, 14), this).setParent(body));
		models.add(new Model("sprites/entities/enemies/void puncher/leg.png", new Point(0, 17), new Point(5, 3), this).setParent(body));
		models.add(new Model("sprites/entities/enemies/void puncher/arm.png", new Point(5, 2), new Point(2, 3), this).setParent(body));
		ai.add(new EntityAIFollowWalking(this, game.player, 0));
		ai.add(new EntityAIMoveLegs(this));
		ai.add(new EntityAIMoveArms(this));
		ai.add(new EntityAIMeleeAttack(this, game.player, 0));
	}
	
	@Override
	public int jumpPower(){
		return 7000;
	}
	
	@Override
	public double getWeight(){
		return 15;
	}
	
	@Override
	public double getSpeed(){
		return 300;
	}
	
	@Override
	public Model gravityModel(){
		return body();
	}
	
	@Override
	public Color bloodColor(){
		return new Color(Random.getInt(0, 50), Random.getInt(0, 25), 0, Random.getInt(100, 255));
	}
	
	@Override
	public double getUpdateRange(){
		return java.lang.Double.POSITIVE_INFINITY;
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
