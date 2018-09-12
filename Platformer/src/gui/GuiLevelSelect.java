package gui;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import util.Utils;
import world.GuiWorldCreator;

public class GuiLevelSelect extends Gui {
	
	public ArrayList<File> basicLevels;
	public ArrayList<File> customLevels;

	public GuiLevelSelect() {
		super(Color.BLUE);
	}
	
	@Override
	public void addButtons(){
		basicLevels = Utils.getBasicLevels();
		int t = 0;
		while(t < basicLevels.size()){
			buttons.add(new ButtonLevel(basicLevels.get(t), Color.GREEN, 5, 5 + t * 10, 20, 14 + t * 10));
			++t;
		}
		customLevels = Utils.getCustomLevels();
		t = 0;
		while(t < customLevels.size()){
			buttons.add(new ButtonLevel(customLevels.get(t), Color.YELLOW, 75, 5 + t * 10, 95, 14 + t * 10));
			++t;
		}
	}
	
	@Override
	public void buttonClicked(Button button){
		if(button instanceof ButtonLevel){
			GuiWorldCreator creator = new GuiWorldCreator(game);
			creator.loadLevel(((ButtonLevel) button).level);
			game.buildWorld(creator.world);
		}
	}
}
