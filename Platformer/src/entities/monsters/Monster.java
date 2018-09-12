package entities.monsters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import particles.Particles;
import main.Game;
import util.PointUtils;
import world.World;
import entities.Entity;
import entities.Shape;

public class Monster extends Entity {
	
	public int health = 2000;
	public int maxHealth = 2000;
	
	public byte bloodCooldown;

	public Monster(Game game, World world, Point2D.Double position) {
		super(game, world, position);
	}
	
	@Override
	public double getSpeed(){
		return 2;
	}
	
	@Override
	public Shape getCollidingShape(){
		return Shape.CIRCLE;
	}
	
	public int jumpPower(){
		return 10;
	}
	
	public int getPower(){
		return 1;
	}
	
	@Override
	public void attack(Point2D.Double source, int damage){
		if(bloodColor() != null && bloodCooldown <= 0){
			Particles.addBlood(world, bloodColor(), new Point2D.Double(position.x, position.y));
			bloodCooldown = 10;
		}
		addAimedForce(Math.toDegrees(Math.atan2(source.y - position.y, source.x - position.x)), damage * -100);
		health -= damage;
		if(health <= 0)
			death();
	}
	
	@Override
	public void paint(Graphics gr){
		super.paint(gr);
		if(health > 0){
			Point screen = PointUtils.gameToScreenPoint(game, new Point2D.Double(getCollider().minX(), getCollider().maxY()));
			gr.setColor(Color.RED);
			gr.fillRect(screen.x, (int)(screen.y - 14 * game.zoom), (int)(width * game.zoom), (int) (10 * game.zoom));
			gr.setColor(Color.GREEN);
			gr.fillRect(screen.x, (int)(screen.y - 14 * game.zoom), (int) (((double)health / maxHealth) * width * game.zoom), (int) (10 * game.zoom));
		}
	}
	
	@Override
	public void update(){
		super.update();
		if(bloodCooldown > 0)
			--bloodCooldown;
	}
	
	@Override
	public double getUpdateRange(){
		return 2000;
	}
	
	public Color bloodColor(){
		return Color.RED;
	}
}
