package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.Game;

public class Gui {
	
	public final Game game;
	
	public ArrayList<Button> buttons = new ArrayList<Button>();
	
	public Image backGroundImage;
	public Color backGroundColor;
	
	public Gui(Game theGame, Image backGround, Color backGroundCol){
		game = theGame;
		backGroundImage = backGround;
		backGroundColor = backGroundCol;
		addButtons();
	}

	public Gui(Image backGroundImage, Color backGroundColor) {
		this(Game.game, backGroundImage, backGroundColor);
	}
	
	public Gui(Image backGround){
		this(backGround, null);
	}
	
	public Gui(Color backGround){
		this(null, backGround);
	}
	
	public void update(){}
	
	public void paint(Graphics gr){
		if(backGroundColor != null){
			gr.setColor(backGroundColor);
			gr.fillRect(0, 0, game.getWidth(), game.getHeight());
		}
		if(backGroundImage != null)
			gr.drawImage(backGroundImage, 0, 0, game.getWidth(), game.getHeight(), null);
		paintButtons(gr);
	}
	
	public void click(Point mouse){
		int t = 0;
		while(t < buttons.size()){
			if(buttons.get(t).isHit(mouse))
				buttonClicked(buttons.get(t));
			++t;
		}
	}
	
	public void rightClick(Point mouse){}
	
	public void keyTyped(KeyEvent event){}
	
	public void buttonClicked(Button button){}
	
	public void addButtons(){}
	
	protected void paintButtons(Graphics gr){
		int t = 0;
		while(t < buttons.size()){
			buttons.get(t).paint(gr);
			++t;
		}
	}
}
