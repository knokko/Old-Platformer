package entities.finish;

import java.awt.Point;
import java.awt.geom.Point2D.Double;

import render.Model;
import main.Game;
import world.World;

public class Door extends Finish {

	public Door(Game game, World world, Double position, String sprite, int width, int height) {
		super(game, world, position);
		this.width = width;
		this.height = height;
		models.add(new Model("sprites/entities/finish/" + sprite + ".png", new Point(0, height / 2), new Point(width / 2, height / 2), this));
	}
	
	@Override
	public boolean isFinished(){
		return game.player.getCollider().hit(getCollider());
	}
	
	@Override
	public Model gravityModel(){
		return models.get(0);
	}
}
