package entities.special;

import java.awt.Point;
import java.awt.geom.Point2D.Double;

import render.Model;
import main.Game;
import world.World;
import entities.Entity;

public class Heart extends Entity {

	public Heart(Game game, World world, Double position) {
		super(game, world, position);
		models.add(new Model("sprites/entities/special/heart.png", new Point(0, 30), new Point(30, 30), this));
	}
	
	@Override
	public void update(){
		super.update();
		if(game.player.health < 100 && game.player.getCollider().hit(getCollider()))
			game.player.health++;
	}
	
	@Override
	public Model gravityModel(){
		return models.get(0);
	}
}
