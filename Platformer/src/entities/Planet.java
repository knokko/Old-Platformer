package entities;

import java.awt.Color;
import java.awt.geom.Point2D.Double;

import main.Game;
import world.World;
import world.gravity.GravitySphere;

public class Planet extends TileEntity {

	public Planet(Game game, World world, Double position, String image, int radius, boolean addGravityField) {
		super(game, world, position, image, 2 * radius, 2 * radius);
		if(addGravityField)
			world.gravityFields.add(new GravitySphere(position, new Color(0, 0, 255), radius * 2, 9.8, true));
	}
	
	@Override
	public Shape getCollidingShape(){
		return Shape.CIRCLE;
	}
	
	@Override
	public boolean isTemporarily(){
		return false;
	}
}
