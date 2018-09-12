package main;

import java.awt.*;

import javax.swing.JPanel;

public class Board extends JPanel {

	private static final long serialVersionUID = -1604814511280438526L;
	public final Game game;

	public Board(Game theGame) {
		game = theGame;
	}
	
	@Override
	public void paint(Graphics gr){
		if(game.world != null){
			game.world.paint(gr);
			game.player.paint(gr);
		}
		if(game.gui != null)
			game.gui.paint(gr);
	}
}
