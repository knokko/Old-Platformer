package entities.monsters;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import render.Model;
import entities.Entity;
import entities.Shape;
import entities.ai.EntityAIRangedAttack;
import entities.projectiles.Stone;
import main.Game;
import world.World;
import static java.lang.Math.*;

public class Catapult extends Monster {
	
	private static short[][][] directionTable;
	
	public double goalRotation = 255;

	public Catapult(Game game, World world, Double position) {
		super(game, world, position);
		models.add(new Model("sprites/entities/enemies/catapult/catapult.png", new Point(0, 60), new Point(90, 75), this));
		models.add(new Model("sprites/entities/enemies/catapult/thrower.png", new Point(151, -30), new Point(14, 110), this).setParent(base()));
		ai.add(new EntityAIRangedAttack(this, game.player, 10000, 300));
		width = 180;
		height = 150;
	}
	
	@Override
	public void update(){
		super.update();
		if(goalRotation != 255 && stoneThrower().rotation != goalRotation){
			stoneThrower().rotation += 5;
			if(stoneThrower().rotation > 360)
				stoneThrower().rotation -= 360;
			if(Math.abs(stoneThrower().rotation - goalRotation) < 5){
				stoneThrower().rotation = goalRotation;
				goalRotation = 255;
				if(goalRotation < 0)
					goalRotation += 360;
			}
		}
		else if(stoneThrower().rotation != goalRotation){
			stoneThrower().rotation -= 2;
			if(stoneThrower().rotation < 0)
				stoneThrower().rotation += 360;
			if(Math.abs(stoneThrower().rotation - goalRotation) < 2){
				stoneThrower().rotation = goalRotation;
			}
		}
	}
	
	public Stone projectile(Entity target){
		Point2D.Double t = target.position;
		double distanceX = t.x - position.x;
		double distanceY = t.y - position.y;
		Stone stone = new Stone(this, t);
		double direction = getTable((int)(distanceX), (int) (distanceY));
		System.out.println("direction = " + direction);
		double startForce = stone.getSpeed();
		double dir = toRadians(direction);
		double startForceX = cos(dir) * startForce;
		double ticks = distanceX / stone.ntopt(startForceX);
		Stone test = new Stone(this, new Point2D.Double(position.x + cos(dir) * (distanceX >= 0 ? 10 : -10), position.y + sin(dir) * 10)){
			
			private boolean test;
			
			@Override
			public int getPower() {
				test = true;
				return 0;
			}
			
			@Override
			public boolean test(){
				return test;
			}
		};
		int i = 0;
		while(i < Math.abs(ticks) + 50){
			test.update();
			++i;
		}
		if(!test.test()){
			direction = getTable((int)(distanceX), (int) (distanceY), 1);
			if(direction == 0)
				direction = getTable((int)distanceX, (int)distanceY);
			dir = toRadians(direction);
			startForceX = cos(dir) * startForce;
			ticks = distanceX / stone.ntopt(startForceX);
		}
		if(direction != 0)
			goalRotation = 360 - direction - base().rotation;
		if(goalRotation < 0)
			goalRotation += 360;
		System.out.println("direction = " + direction + " and goalRotation = " + goalRotation);
		return new Stone(this, new Point2D.Double(position.x + cos(dir) * (distanceX >= 0 ? 10 : -10), position.y + sin(dir) * 10));
	}
	
	@Override
	public void paint(Graphics gr){
		if(facingLeft)
			stoneThrower().rotation *= -1;
		super.paint(gr);
		if(facingLeft)
			stoneThrower().rotation *= -1;
	}
	
	@Override
	public Model gravityModel(){
		return base();
	}
	
	@Override
	public Shape getCollidingShape(){
		return Shape.BOX;
	}
	
	@Override
	public double getUpdateRange(){
		return 12000;
	}
	
	public static void updateDirectionTable(Game game, World world){
		directionTable = new short[1000][400][2];
		double rotation = 270;
		Stone stone = new Stone(new Catapult(game, world, new Point2D.Double()), new Point2D.Double());
		double gravityForce = stone.getWeight() * world.gravity;
		double startForce = stone.getSpeed();
		while(rotation <= 360 && rotation >= 270 || rotation >= 0 && rotation < 90){
			int x = 0;
			while(x < 10000){
				double startForceY = sin(toRadians(rotation)) * startForce;
				double startForceX = cos(toRadians(rotation)) * startForce;
				double ticks = x / stone.ntopt(startForceX);
				int y = (int) (stone.ntopt(startForceY) * ticks - 0.5 * stone.ntopt(gravityForce) * ticks * ticks);
				if(y > -600 && y < 3400)
					setTable(x, y, rotation);
				x += 10;
			}
			rotation += 0.01;
			if(rotation >= 360)
				rotation = 0;
		}
	}
	
	private static void setTable(int x, int y, double direction){
		byte b = 0;
		if(direction > 45 && direction < 270)
			b = 1;
		if(direction >= 270)
			direction -= 180;
		else
			direction = -direction;
		directionTable[x / 10][y / 10 + 60][b] = (short) (direction * 100);
	}
	
	private static double getTable(int x, int y, int index3){
		try {
			if(x < 0)
				x = -x;
			double table = directionTable[x / 10][y / 10 + 60][index3];
			if(index3 == 1 && table == 0){
				table = directionTable[(x + 10)/ 10][y / 10 + 60][index3];
				if(table == 0)
					table = directionTable[(x - 10)/ 10][y / 10 + 60][index3];
				if(table == 0)
					table = directionTable[(x + 20)/ 10][y / 10 + 60][index3];
				if(table == 0)
					table = directionTable[(x - 20)/ 10][y / 10 + 60][index3];
				if(table == 0)
					table = directionTable[x / 10][y / 10 + 61][index3];
				if(table == 0)
					table = directionTable[x / 10][y / 10 + 59][index3];
				if(table == 0)
					table = directionTable[x / 10][y / 10 + 62][index3];
				if(table == 0)
					table = directionTable[x / 10][y / 10 + 58][index3];
			}
			table /= 100;
			if(table >= 90)
				table += 180;
			else
				table = -table;
		return table;
		} catch(Exception ex){
			return 0;
		}
	}
	
	private static double getTable(int x, int y){
		return getTable(x, y, 0);
	}
	
	public Model base(){
		return models.get(0);
	}
	
	public Model stoneThrower(){
		return models.get(1);
	}
}
