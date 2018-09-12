package entities.finish;

import java.awt.Point;
import java.awt.geom.Point2D.Double;

import render.Model;
import main.Game;
import world.World;

public class Flag extends Finish {
	
	public boolean horizontalFinish;
	public boolean verticalFinish;

	public Flag(Game game, World world, Double position, boolean finishHorizontal, boolean finishVertical) {
		super(game, world, position);
		models.add(new Model("sprites/entities/finish/flag/stick.png", new Point(5, 15), new Point(5, 40), this));
		models.add(new Model("sprites/entities/finish/flag/flag.png", new Point(5, 0), new Point(10, 0), this).setParent(stick()));
		horizontalFinish = finishHorizontal;
		verticalFinish = finishVertical;
	}
	
	@Override
	public boolean isFinished(){
		if(position.distance(game.player.position) <= 60)
			return true;
		if(verticalFinish && Math.abs(position.x - game.player.position.x) <= 120)
			return true;
		if(horizontalFinish && Math.abs(position.y - game.player.position.y) <= 120)
			return true;
		return false;
	}
	
	@Override
	public Model gravityModel(){
		return stick();
	}
	
	public Model stick(){
		return models.get(0);
	}
	
	public Model flag(){
		return models.get(1);
	}
}
