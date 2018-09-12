package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import render.Model;
import colliders.Collider;
import colliders.ColliderBox;
import colliders.ColliderCircle;
import entities.ai.EntityAI;
import entities.projectiles.GrablingHook;
import util.PointUtils;
import world.World;
import main.Game;

public abstract class Entity {
	
	public final Game game;
	public World world;
	
	public Point2D.Double position;
	public ArrayList<Model> models = new ArrayList<Model>();
	public ArrayList<EntityAI> ai = new ArrayList<EntityAI>();
	public GrablingHook hook;
	
	public int width = 60;
	public int height = 60;
	public int cooldown;
	
	public double moveX;
	public double moveY;
	public double motionX;
	public double motionY;
	
	public boolean onGround;
	public boolean facingLeft;
	public boolean inactive;
	
	public static long[][] timeResults = new long[5][1000];
	public static int[] timers = new int[5];
	
	public Entity(Game game, World world, Point2D.Double position) {
		this.game = game;
		this.world = world;
		this.position = position;
	}
	
	public Collider getCollider(Point2D.Double p){
		if(getCollidingShape() == Shape.BOX){
			double w = Math.max(width, height) / 2;
			double h = Math.max(width, height) / 2;
			double sf = (h - w) * Math.abs(Math.sin(Math.toRadians(world.getGravityDirection(position))));
			double cf = (h - w) * Math.abs(Math.cos(Math.toRadians(world.getGravityDirection(position))));
			return new ColliderBox(p.x - w - cf, p.x + w + cf, p.y - w - sf, p.y + w + sf);
		}
		if(getCollidingShape() == Shape.CIRCLE)
			return new ColliderCircle(Math.max(width, height) / 2, p);
		throw new Collider.CollissionException();
	}
	
	public Collider getCollider(){
		return getCollider(position);
	}
	
	public boolean move(double x, double y, boolean isTest, boolean canRetry){
		if(!isTest)
			onGround = false;
		Point2D.Double back = new Point2D.Double(position.x, position.y);
		Point2D.Double t = new Point2D.Double(back.x, back.y);
		while(x != 0 || y != 0){
			double mx = 0;
			if(x > 0){
				if(x >= 1)
					mx = 1;
				else
					mx = x;
			}
			if(x < 0){
				if(x <= -1)
					mx = -1;
				else
					mx = x;
			}
			double my = 0;
			if(y > 0){
				if(y >= 1)
					my = 1;
				else
					my = y;
			}
			if(y < 0){
				if(y <= -1)
					my = -1;
				else
					my = y;
			}
			if(teleport(new Point2D.Double(position.x + mx, position.y), isTest)){
				x -= mx;
				t.x += mx;
			}
			else if(teleport(new Point2D.Double(position.x + mx, position.y + 1), isTest) || teleport(new Point2D.Double(position.x + mx, position.y - 1), isTest)){
				x -= mx;
				t.x += mx;
				if(!isTest){
					double gravity = world.getGravityDirection(position);
					if((((gravity >= -45 && gravity <= 45) || (gravity >= 315 && gravity <= 405)) && mx > 0) || (gravity >= 135 && gravity <= 225 && mx < 0))
						onGround = true;
				}
			}
			else {
				x = 0;
				if(!isTest){
					double gravity = world.getGravityDirection(position);
					if((((gravity >= -45 && gravity <= 45) || (gravity >= 315 && gravity <= 405)) && mx > 0) || (gravity >= 135 && gravity <= 225 && mx < 0))
						onGround = true;
					motionX = 0;
				}
			}
			if(teleport(new Point2D.Double(position.x, position.y + my), isTest)){
				y -= my;
				t.y += my;
			}
			else if(teleport(new Point2D.Double(position.x + 1, position.y + my), isTest) || teleport(new Point2D.Double(position.x - 1, position.y + my), isTest)){
				y -= my;
				t.y += my;
				if(!isTest){
					double gravity = world.getGravityDirection(position);
					if((gravity >= 45 && gravity <= 135 && my > 0) || (gravity >= 225 && gravity <= 315 && my < 0))
						onGround = true;
				}
			}
			else {
				y = 0;
				if(!isTest){
					double gravity = world.getGravityDirection(position);
					if((gravity >= 45 && gravity <= 135 && my > 0) || (gravity >= 225 && gravity <= 315 && my < 0))
						onGround = true;
					motionY = 0;
				}
			}
		}
		return isTest ? !back.equals(t) : !back.equals(position);
	}
	
	public boolean move(double x, double y, boolean isTest){
		return move(x, y, isTest, false);
	}
	
	public boolean move(double x, double y){
		return move(x, y, false);
	}
	
	public boolean teleport(Point2D.Double target, boolean isTest){
		Collider collider = getCollider(target);
		ArrayList<Collider> colliders = world.getColliders(getCollider(target));
		int t = 0;
		while(t < colliders.size()){
			if(collider.hit(colliders.get(t)))
				return false;
			++t;
		}
		double g = world.getGravityDirection(target);
		boolean g2 = world.getGravity(target) == 0;
		if(!g2){
			if((target.x < 0 && (g <= 90 && g >= 0 || g >= 270 && g <= 360)) || (target.x > world.width() * 60 && (g >= 90 && g <= 270)) || (target.y < 0 && (g >= 0 && g <= 180)) || (target.y > world.height() * 60 && (g >= 180 && g <= 360)) && !g2){
				return false;
			}
		}
		if(!isTest)
			position = new Point2D.Double(target.x, target.y);
		return true;
	}
	
	public boolean teleport(Point2D.Double target){
		return teleport(target, false);
	}
	
	public void paintIfInRange(Graphics gr){
		Collider c = getCollider();
		Point screen = PointUtils.gameToScreenPoint(game, new Point2D.Double(c.minX(), c.minY()));
		Point screen2 = PointUtils.gameToScreenPoint(game, new Point2D.Double(c.maxX(), c.maxY()));
		if(screen.x <= game.getWidth() && screen2.x >= 0 && screen2.y <= game.getHeight() && screen.y >= 0)
			paint(gr);
	}
	
	public void paint(Graphics gr){
		int t = 0;
		while(t < models.size()){
			models.get(t).paint((Graphics2D) gr);
			++t;
		}
	}
	
	public void updateIfInRange(){
		if(position.distance(game.player.position) <= getUpdateRange())
			update();
	}
	
	public void update(){
		updateAI();
		if(gravityModel() != null && world.getGravity(position) != 0)
			gravityModel().rotation = 270 - world.getGravityDirection(position);
		if(cooldown > 0){
			moveX /= 3;
			moveY /= 3;
		}
		if(canFly())
			addForce(moveX, moveY);
		else
			addAimedForce(world.getGravityDirection(position) + 90, onGround ? moveX * getSpeed() : (world.getGravity(position) != 0 ? moveX * 0.02 * getSpeed() : 0));
		move(motionX, motionY);
		if(cooldown <= 0){
			if(moveX > 0)
				facingLeft = false;
			if(moveX < 0)
				facingLeft = true;
			moveX = 0;
			moveY = 0;
		}
		else
			--cooldown;
		applyGravityAndFriction();
		if(hook != null && hook.blocked){
			motionX /= 1.01;
			motionY /= 1.01;
			hook.blocked = false;
		}
		if(hook != null && hook.inactive)
			hook = null;
		if((position.y < -120) || (position.y > world.height() * 60 + 120) || (position.x < -120) || (position.x > world.width() * 60 + 120))
			death();
	}
	
	public void death(){
		inactive = true;
	}
	
	public double getWeight(){
		return 2;
	}
	
	public void attack(Point2D.Double position, int damage){}
	
	protected void paintCollider(Graphics gr){
		gr.setColor(Color.RED);
		Collider c = getCollider();
		Point p1 = PointUtils.gameToScreenPoint(game, new Point2D.Double(c.minX(), c.minY()));
		Point p2 = PointUtils.gameToScreenPoint(game, new Point2D.Double(c.maxX(), c.maxY()));
		gr.drawRect(p1.x, p2.y, p2.x - p1.x, p1.y - p2.y);
	}
	
	public void updateAI(){
		int t = 0;
		while(t < ai.size()){
			ai.get(t).update();
			++t;
		}
	}
	
	public void applyGravityAndFriction(){
		if(!canFly())
			addAimedForce(world.getGravityDirection(position), world.getGravity(position) * getWeight());
		if(onGround && !canFly()){
			motionX *= 0.9;
			motionY *= 0.9;
		}
		double friction = world.getAirFriction(position) * frictionMultiplier();
		double speedX = pttoms(motionX);
		double speedY = pttoms(motionY);
		if(motionX > 0)
			addForce(-friction * speedX * speedX, 0);
		else
			addForce(friction * speedX * speedX, 0);
		if(motionY > 0)
			addForce(0, -friction * speedY * speedY);
		else
			addForce(0, friction * speedY * speedY);
	}
	
	public double frictionMultiplier(){
		return 2;
	}
	
	public void jump(int power){
		if(onGround)
			addAimedForce(world.getGravityDirection(position) + 180, power);
	}
	
	public void addForce(double fx, double fy){
		motionX += ntopt(fx);
		motionY += ntopt(fy);
	}
	
	public void addAimedForce(double direction, double force){
		addForce(Math.cos(Math.toRadians(direction)) * force, Math.sin(Math.toRadians(direction)) * force);
	}
	
	public double getSpeed(){
		return 1;
	}
	
	public Shape getCollidingShape(){
		return Shape.BOX;
	}
	
	public Model gravityModel(){
		return null;
	}
	
	public boolean canFly(){
		return false;
	}
	
	public double getUpdateRange(){
		return 1800;
	}
	
	public double ntomss(double n){
		return n / getWeight();
	}
	
	public double msston(double mss){
		return mss * getWeight();
	}
	
	public double ntoms(double n){
		return msstoms(ntomss(n));
	}
	
	public double mston(double ms){
		return msston(mstomss(ms));
	}
	
	public double ntopt(double n){
		return mstopt(ntoms(n));
	}
	
	public double ptton(double pt){
		return mston(pttoms(pt));
	}
	
	public static double msstoms(double mss){
		return mss / 50;
	}
	
	public static double mstomss(double ms){
		return ms * 50;
	}
	
	/**
	 * converts a speed in meter/second to a speed in pixel/tick
	 * @param ms the speed in meters per second
	 * @return the speed in pixels per tick
	 */
	public static double mstopt(double ms){
		return ms * 30 / 50;
	}
	
	/**
	 * converts a speed in pixel/tick to a speed in meter/second
	 * @param pt the speed in pixels per tick
	 * @return the speed in meters per second
	 */
	public static double pttoms(double pt){
		return pt / 30 * 50;
	}
}
