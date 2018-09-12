package entities.finish;

import java.awt.geom.Point2D.Double;

import main.Game;
import world.World;
import entities.Entity;

public class Finish extends Entity implements IFinish{

	public Finish(Game game, World world, Double position) {
		super(game, world, position);
	}
	
	@Override
	public void update(){
		super.update();
		if(isFinished())
			game.finishLevel();
	}
	
	@Override
	public double getUpdateRange(){
		return java.lang.Double.POSITIVE_INFINITY;
	}
	
	public boolean isFinished(){
		return false;
	}
	
	public boolean canFly(){
		return true;
	}
}
