package util;

import java.awt.Point;
import java.awt.geom.Point2D;

import main.Game;

public final class PointUtils {
	
	public static Point gameToScreenPoint(Game game, Point2D.Double point){
		int tx = (int) (((point.x - game.camera.x) * game.zoom) + game.getWidth() / 2);
		int ty = (int) ((-(point.y - game.camera.y) * game.zoom) + game.getHeight() / 2);
		return new Point(tx, ty);
	}
	
	public static Point gameToScreenPoint(Game game, Point point){
		return gameToScreenPoint(game, new Point2D.Double(point.x, point.y));
	}
	
	public static Point2D.Double screenToGamePoint(Game game, Point point) {
		if(point == null)
			return null;
		double differenceX = point.x - game.getWidth() / 2;
		double factorX = differenceX / game.zoom;
		double originalX = factorX + game.camera.x;
		
		double differenceY = -(point.y - game.getHeight() / 2);
		double factorY = differenceY / game.zoom;
		double originalY = factorY + game.camera.y;
		return new Point2D.Double(originalX, originalY);
	}
	
	public static Point tileToScreenPoint(Game game, Point point){
		return gameToScreenPoint(game, new Point(point.x * 60, point.y * 60));
	}
}
