package world.gravity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D.Double;

import main.Game;
import util.PointUtils;
import colliders.Collider;
import colliders.ColliderCircle;

public class GravitySphere implements IGravity {
	
	public Color color;
	public Double center;
	
	public double radius;
	public double accelleration;
	
	public boolean pull;

	public GravitySphere(Double location, Color color, double radius, double accelleration, boolean pull) {
		center = new Double(location.x, location.y);
		this.color = color;
		this.radius = radius;
		this.accelleration = accelleration;
		this.pull = pull;
	}

	public double getRotation(Double location) {
		double rotation = Math.toDegrees(Math.atan2(center.y - location.y, center.x - location.x));
		return pull ? rotation : 180 + rotation;
	}

	public double getAccelleration(Double location) {
		return accelleration;
	}

	public Double min() {
		return new Double(center.x - radius, center.y - radius);
	}

	public Double max() {
		return new Double(center.x + radius, center.y + radius);
	}

	public Collider getCollider() {
		return new ColliderCircle(radius, center);
	}

	public void paint(Graphics gr) {
		gr.setColor(color);
		Point screenMin = PointUtils.gameToScreenPoint(Game.game, min());
		Point screenMax = PointUtils.gameToScreenPoint(Game.game, max());
		gr.fillOval(screenMin.x, screenMax.y, screenMax.x - screenMin.x, screenMin.y - screenMax.y);
	}

	public Color color() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getAccelleration() {
		return accelleration;
	}

	public void setAccelleration(double accelleration) {
		this.accelleration = accelleration;
	}
}
