package entities.special;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D.Double;

import render.Model;
import main.Game;
import world.World;
import entities.Entity;
import entities.Shape;

public abstract class Coin extends Entity {
	
	public int heal;

	public Coin(Game game, World world, Double position, String color, int healAmount) {
		super(game, world, position);
		width = 20;
		height = 20;
		models.add(new Model("sprites/entities/special/" + color + " ring.png", new Point(0, 10), new Point(10, 10), this));
		heal = healAmount;
	}
	
	@Override
	public void update(){
		if(getCollider().hit(game.player.getCollider())){
			game.player.health += heal;
			if(game.player.health > 100)
				game.player.health = 100;
			inactive = true;
		}
	}
	
	@Override
	public void paint(Graphics gr){
		models.get(0).paintSimple((Graphics2D) gr);
	}
	
	@Override
	public Shape getCollidingShape(){
		return Shape.CIRCLE;
	}
	
	public static class Yellow extends Coin {

		public Yellow(Game game, World world, Double position) {
			super(game, world, position, "yellow", 1);
		}
		
	}
}
