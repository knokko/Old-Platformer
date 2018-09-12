package world.gravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D.Double;

import colliders.Collider;

public interface IGravity {
	
	public double getRotation(Double location);
	
	public double getAccelleration(Double location);
	
	public double getAccelleration();
	
	public Double min();
	
	public Double max();
	
	public Collider getCollider();
	
	public void paint(Graphics gr);
	
	public Color color();
	
	public void setColor(Color color);
	
	public void setAccelleration(double accelleration);
}
