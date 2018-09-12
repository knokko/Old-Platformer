package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import util.Factors;

public class Button {
	
	public Font font;
	public String text;
	public Color borderColor;
	public Color bodyColor;
	public Color textColor;
	
	public int minX;
	public int minY;
	public int maxX;
	public int maxY;

	public Button(Font textFont, String buttonText, Color border, Color body, Color textCol, double xMin, double yMin, double xMax, double yMax) {
		minX = Factors.factorX(xMin);
		minY = Factors.factorY(yMin);
		maxX = Factors.factorX(xMax);
		maxY = Factors.factorY(yMax);
		borderColor = border;
		bodyColor = body;
		textColor = textCol;
		text = buttonText;
		font = textFont;
	}
	
	public Button(String text, Color borderColor, Color bodyColor, Color textColor, double minX, double minY, double maxX, double maxY){
		this(new Font("TimesRoman", Font.PLAIN, -1), text, borderColor, bodyColor, textColor, minX, minY, maxX, maxY);
	}
	
	public Button(String text, Color bodyColor, Color textColor, double minX, double minY, double maxX, double maxY){
		this(text, Color.BLACK, bodyColor, textColor, minX, minY, maxX, maxY);
	}
	
	public Button(String text, Color color, double minX, double minY, double maxX, double maxY){
		this(text, color, Color.BLACK, minX, minY, maxX, maxY);
	}
	
	public void paint(Graphics gr){
		if(font.getSize() == -1)
			initSize(gr);
		int width = maxX - minX;
		int height = maxY - minY;
		gr.setColor(bodyColor);
		gr.fillRect(minX, minY, width, height);
		gr.setColor(borderColor);
		gr.drawRect(minX, minY, width, height);
		gr.setColor(textColor);
		gr.setFont(font);
		gr.drawString(text, minX + width / 20, maxY - height / 3);
	}
	
	public boolean isHit(Point point){
		return point != null && point.x >= minX && point.x <= maxX && point.y >= minY && point.y <= maxY;
	}
	
	public void initSize(Graphics gr){
		int size = 150;
		font = new Font(font.getFontName(), 0, size);
		gr.setFont(font);
		double width = font.getStringBounds(text, ((Graphics2D) gr).getFontRenderContext()).getWidth();
		double height = font.getStringBounds(text, ((Graphics2D) gr).getFontRenderContext()).getHeight();
		while(width >= (maxX - minX) * 0.9 || height >= (maxY - minY) * 0.9){
			--size;
			font = new Font(font.getFontName(), 0, size);
			gr.setFont(font);
			width = font.getStringBounds(text, ((Graphics2D) gr).getFontRenderContext()).getWidth();
			height = font.getStringBounds(text, ((Graphics2D) gr).getFontRenderContext()).getHeight();
		}
	}
}
