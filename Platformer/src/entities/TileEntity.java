package entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;

import main.Game;
import util.PointUtils;
import util.Utils;
import world.World;

public class TileEntity extends Entity {
	
	public int ticksLeft = 50;
	public final Image image;

	public TileEntity(Game game, World world, Point2D.Double position, String image, int width, int height) {
		super(game, world, position);
		this.width = width;
		this.height = height;
		this.image = Utils.getImage(image);
	}
	
	@Override
	public void update(){
		super.update();
		if(isTemporarily()){
			--ticksLeft;
			if(ticksLeft < 0)
				inactive = true;
		}
	}
	
	@Override
	public void paint(Graphics gr){
		Point screen = PointUtils.gameToScreenPoint(game, position);
		gr.drawImage(image, (int)(screen.x - game.zoom * width / 2), (int)(screen.y - game.zoom * height / 2), (int)(width * game.zoom), (int) (height * game.zoom), null);
	}
	
	public boolean isTemporarily(){
		return true;
	}
}
