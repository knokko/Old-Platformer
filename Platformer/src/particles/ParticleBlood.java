package particles;

import java.awt.*;
import java.awt.geom.Point2D;

import main.Game;
import util.PointUtils;

public class ParticleBlood extends Particle {
	
	public Color color;
	
	public double motionX;
	public double motionY;
	public int ticks = 10;
	
	public ParticleBlood(Point2D.Double point, Color bloodColor, int rotation) {
		super(point);
		motionX = Math.sin(Math.toRadians(rotation)) * 5 * Math.random();
		motionY = Math.cos(Math.toRadians(rotation)) * 5 * Math.random();
		color = bloodColor;
	}

	@Override
	public void update() {
		position.x += motionX;
		position.y += motionY;
		--ticks;
		if(ticks <= 0)
			inactive = true;
	}

	@Override
	public void paint(Graphics gr) {
		Point screen = PointUtils.gameToScreenPoint(Game.game, position);
		Point screen2 = PointUtils.gameToScreenPoint(Game.game, new Point2D.Double(position.x + 2, position.y + 2));
		gr.setColor(color);
		gr.fillRect(screen.x, screen.y, screen2.x - screen.x, screen2.y - screen.y);
	}

}
