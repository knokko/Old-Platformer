package particles;

import java.awt.*;
import java.awt.geom.Point2D;

import world.World;

public final class Particles {
	
	public static final void addBlood(World world, Color color, Point2D.Double start){
		int t = 0;
		while(t <= 315){
			world.particles.add(new ParticleBlood(new Point2D.Double(start.x, start.y), color, t));
			t += 45;
		}
	}
}
