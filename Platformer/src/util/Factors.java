package util;

import main.Game;

public final class Factors {

	public static final int factorX(double percent){
		return (int)(percent * (Game.game.getWidth() / 100.0));
	}
	
	public static final int factorY(double percent){
		return (int)(percent * (Game.game.getHeight() / 100.0));
	}
}
