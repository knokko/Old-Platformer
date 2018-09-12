package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import entities.ai.EntityAIMoveArms;
import entities.ai.EntityAIMoveLegs;
import particles.Particles;
import render.Model;
import main.Game;
import util.Factors;
import world.World;

public class Player extends Entity {
	
	public Model body;
	
	public double goalAngle;
	public int health = 100;
	public int axes = 2;
	public int mana = 100;
	public boolean cooldown2;

	public Player(Game game, World world, Point2D.Double position) {
		super(game, world, position);
		height = 57;
		width = 57;
		body = new Model("sprites/entities/player/body.png", new Point(0, 19), new Point(5, 19), this);
		models.add(new Model("sprites/entities/player/leg.png", new Point(1, 19), new Point(4, 1), this).setParent(body));
		models.add(new Model("sprites/entities/player/arm.png", new Point(4, -18), new Point(3, 26), this).setParent(body));
		models.add(body);
		models.add(new Model("sprites/entities/player/head.png", new Point(0, -10), new Point(5, 10), this).setParent(body));
		models.add(new Model("sprites/entities/player/arm.png", new Point(4, -18), new Point(3, 26), this).setParent(body));
		models.add(new Model("sprites/entities/player/leg.png", new Point(1, 19), new Point(4, 1), this).setParent(body));
		ai.add(new EntityAIMoveLegs(this));
		ai.add(new EntityAIMoveArms(this));
	}
	
	@Override
	public void death(){
		game.backToMenu();
	}
	
	@Override
	public void update(){
		if(mana < 100)
			++mana;
		if(cooldown2 && onGround)
			cooldown2 = false;
		if(goalAngle != 4321){
			if(arm2().rotation > goalAngle)
				arm2().rotation -= 5;
			if(arm2().rotation < goalAngle)
				arm2().rotation += 5;
			if(Math.abs(arm2().rotation - goalAngle) < 10){
				arm2().rotation = goalAngle;
				goalAngle = 4321;
			}
		}
		super.update();
		double wx = (game.getWidth() / 2) / game.zoom;
		if(position.x >= wx && position.x <= (world.width() * 60) - wx)
			game.camera.x = position.x;
		else if(position.x > wx)
			game.camera.x = world.width() * 60 - wx;
		else
			game.camera.x = wx;
		double wy = (game.getHeight() / 2) / game.zoom;
		if(position.y >= wy && position.y <= (world.height() * 60) - wy)
			game.camera.y = position.y;
		else if(position.y > wy)
			game.camera.y = world.height() * 60 - wy;
		else
			game.camera.y = wy;
	}
	
	@Override
	public Shape getCollidingShape(){
		return Shape.CIRCLE;
	}
	
	public Model body(){
		return models.get(2);
	}
	
	public Model head(){
		return models.get(3);
	}
	
	public Model arm1(){
		return models.get(1);
	}
	
	public Model arm2(){
		return models.get(4);
	}
	
	public Model leg1(){
		return models.get(0);
	}
	
	public Model leg2(){
		return models.get(5);
	}
	
	public void setWeapon(String weapon){
		arm2().setImage("sprites/entities/player/" + weapon + ".png");
	}
	
	public String getWeapon(){
		return arm2().name;
	}
	
	public boolean hasWeapon(String weapon){
		return getWeapon().matches(weapon);
	}
	
	public void setSpell(String spell){
		arm1().setImage("sprites/entities/player/" + spell + ".png");
	}
	
	public String getSpell(){
		return arm1().name;
	}
	
	public boolean hasSpell(String spell){
		return getSpell().matches(spell);
	}
	
	@Override
	public void attack(Point2D.Double source, int damage){
		health -= damage;
		Particles.addBlood(world, Color.RED, new Point2D.Double(position.x, position.y));
		addAimedForce(Math.toDegrees(Math.atan2(source.y - position.y, source.x - position.x)), damage * -10000);
		if(health <= 0)
			death();
	}
	
	@Override
	public void paint(Graphics gr){
		super.paint(gr);
		int fy5 = Factors.factorY(5);
		int fx5 = Factors.factorX(5);
		int fx10 = Factors.factorX(10);
		int fx20 = Factors.factorX(20);
		gr.setColor(Color.GREEN);
		int paintHealth = (int) ((health / 100.0) * fx10);
		gr.fillRect(fx5, fy5, paintHealth, fy5);
		gr.setColor(new Color(255, 0, 255));
		int paintMana = (int) ((mana / 100.0) * fx10);
		gr.fillRect(fx20, fy5, paintMana, fy5);
		gr.setColor(Color.RED);
		gr.fillRect(fx5 + paintHealth, fy5, fx10 - paintHealth, fy5);
		gr.fillRect(fx20 + paintMana, fy5, fx10 - paintMana, fy5);
		gr.setColor(Color.BLACK);
		gr.drawRect(fx5, fy5, fx10, fy5);
		gr.drawRect(fx20, fy5, fx10, fy5);
	}
	
	@Override
	public double getWeight(){
		return 80;
	}
	
	@Override
	public double getSpeed(){
		return 2000;
	}
	
	@Override
	public Model gravityModel(){
		return body();
	}
}
