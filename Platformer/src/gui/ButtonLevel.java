package gui;

import java.awt.Color;
import java.io.File;

public class ButtonLevel extends Button {
	
	public File level;

	public ButtonLevel(File file, Color color, double minX, double minY, double maxX, double maxY) {
		super(file.getName().substring(0, file.getName().length() - 6).replaceAll("_", " "), color, minX, minY, maxX, maxY);
		level = file;
	}

}
