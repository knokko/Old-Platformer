package world.gravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D.Double;

import main.Game;
import util.PointUtils;
import colliders.ColliderBox;

public class GravityField implements IGravity{
	
	public final Game game;
	
	public final Double min;
	public final Double max;
	
	public Color color;
	
	public double power;
	public double rotation;

	public GravityField(Game theGame, Double minPoint, Double maxPoint, Color paintColor, double gravityPower, double gravityDirection) {
		min = minPoint;
		max = maxPoint;
		color = paintColor;
		power = gravityPower;
		rotation = gravityDirection;
		game = theGame;
	}
	
	public ColliderBox getCollider(){
		return new ColliderBox(min, max);
	}
	
	public void paint(Graphics gr){
		Point minScreen = PointUtils.gameToScreenPoint(game, min);
		Point maxScreen = PointUtils.gameToScreenPoint(game, max);
		gr.setColor(color);
		gr.fillRect(minScreen.x, minScreen.y, maxScreen.x - minScreen.x, maxScreen.y - minScreen.y);
	}

	public Double min() {
		return min;
	}

	public Double max() {
		return max;
	}

	public double getRotation(Double location) {
		return rotation;
	}

	public double getAccelleration(Double location) {
		return power;
	}

	public Color color() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getAccelleration() {
		return power;
	}

	public void setAccelleration(double accelleration) {
		power = accelleration;
	}
}
