package entities.monsters;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D.Double;

import entities.ai.EntityAIBattleStarAttack;
import entities.ai.EntityAIFollowFlying;
import render.Model;
import main.Game;
import world.World;

public class BattleStar extends Monster {

	public BattleStar(Game game, World world, Double position) {
		super(game, world, position);
		models.add(new Model("sprites/entities/enemies/battle star/default.png", new Point(0, 27), new Point(27, 27), this));
		ai.add(new EntityAIFollowFlying(this, game.player, 0));
		ai.add(new EntityAIBattleStarAttack(this, game.player));
	}
	
	@Override
	public void update(){
		super.update();
		body().rotation--;
		if(body().rotation < 0)
			body().rotation += 360;
		if(body().rotation > 360)
			body().rotation -= 360;
	}
	
	@Override
	public boolean canFly(){
		return true;
	}
	
	@Override
	public double getWeight(){
		return 1200;
	}
	
	@Override
	public double getSpeed(){
		return 5000;
	}
	
	@Override
	public Color bloodColor(){
		return null;
	}
	
	@Override
	public Model gravityModel(){
		return null;
	}
	
	public Model body(){
		return models.get(0);
	}
	
	public int getPower(){
		return 1;
	}
}
