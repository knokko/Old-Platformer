package gui;

import java.awt.Color;

import world.GuiWorldCreator;
import main.Game;

public class GuiMainMenu extends Gui {

	public GuiMainMenu(Game game) {
		super(game, null, Color.GREEN);
	}
	
	@Override
	public void addButtons(){
		buttons.add(new Button("Play Game", Color.BLUE, 37.5, 20, 62.5, 35));
		buttons.add(new Button("Level Designer", Color.BLUE, 37.5, 40, 62.5, 55));
		buttons.add(new Button("Quit Game", Color.RED, 37.5, 60, 62.5, 75));
	}
	
	@Override
	public void buttonClicked(Button button){
		if(button.text.matches("Play Game")){
			//game.buildWorld(new World(game));
			game.openGui(new GuiLevelSelect());
		}
		if(button.text.matches("Level Designer"))
			game.openGui(new GuiWorldCreator(game));
		if(button.text.matches("Quit Game"))
			game.dispose();
	}
}
