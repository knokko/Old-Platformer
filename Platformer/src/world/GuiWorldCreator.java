package world;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import colliders.Collider;
import entities.*;
import entities.finish.*;
import entities.monsters.*;
import entities.special.*;
import entities.special.Coin.*;
import tiles.Tiles;
import util.*;
import world.gravity.*;
import main.Controller;
import main.Game;
import gui.*;
import gui.Button;
import static util.Factors.*;

public class GuiWorldCreator extends Gui{
	
	private static final byte OBJECT_NONE = 0;
	private static final byte OBJECT_TILE = 1;
	private static final byte OBJECT_ENEMY = 2;
	private static final byte OBJECT_OTHER = 3;
	private static final byte OBJECT_START = 4;
	private static final byte OBJECT_GRAVITY = 5;
	private static final byte OBJECT_FINISH = 6;
	
	private static final byte ENCODING_BITMAP = 0;
	private static final byte ENCODING_TO_WIDTH = 1;
	private static final byte ENCODING_TO_HEIGHT = 2;
	
	private static final byte ENCODING_BITMAP_2 = 3;
	private static final byte ENCODING_TO_WIDTH_2 = 4;
	private static final byte ENCODING_TO_HEIGHT_2 = 5;
	
	private static final byte ID_BIRD = 0;
	private static final byte ID_WARRIOR = 1;
	private static final byte ID_VERTICAL_FLAG = 2;
	private static final byte ID_ARCHER = 3;
	private static final byte ID_GRAVITY_FIELD = 4;
	private static final byte ID_CATAPULT = 5;
	private static final byte ID_HEART = 6;
	private static final byte ID_YELLOW_COIN = 7;
	private static final byte ID_WOODEN_DOOR = 8;
	private static final byte ID_HORIZONTAL_FLAG = 9;
	private static final byte ID_BASIC_FLAG = 10;
	private static final byte ID_BATTLE_STAR = 11;
	private static final byte ID_GRAVITY_SPHERE = 12;
	private static final byte ID_RED_PLANET = 13;
	private static final byte ID_TIMER = 14;
	private static final byte ID_VOID_PUNCHER = 15;
	private static final byte ID_VOID_SPAWNER = 16;
	
	private static final byte FILL_TILE = -128;
	
	public ArrayList<Button> gravityButtons = new ArrayList<Button>();
	
	public World world;
	public String name = "level";
	public Font font;
	
	public String gravityAccelleration = "9.8";
	public String airFriction = "1.0";
	private String fieldAccelleration;
	public short gravityDirection = 270;
	
	public short width = 50;
	public short height = 50;
	
	private IGravity selectedField;
	
	private Point2D.Double markedPoint;
	private Point2D.Double gravityMark;
	private Point2D.Double otherMark;
	
	private byte typingButton = -1;
	private byte placingObject;
	private byte currentTile;
	private byte currentEnemy;
	private byte currentEntity;
	private byte currentFinish;
	private byte currentGravity;
	public boolean config = true;
	
	public GuiWorldCreator(Game game) {
		super(game, null, Color.BLUE);
		font = new Font("TimesRoman", 0, Factors.factorX(2));
		updateButtons();
	}
	
	@Override
	public void update(){
		if(!config){
			Controller c = game.controller;
			c.updateZoom();
			if(c.pressedKeys[0])
				game.camera.x -= 5 / game.zoom;
			if(c.pressedKeys[1])
				game.camera.y += 5 / game.zoom;
			if(c.pressedKeys[2])
				game.camera.x += 5 / game.zoom;
			if(c.pressedKeys[3])
				game.camera.y -= 5 / game.zoom;
		}
	}
	
	@Override
	public void paint(Graphics gr){
		int t = 0;
		while(t < 5){
			buttons.get(t).initSize(gr);
			++t;
		}
		if(config){
			super.paint(gr);
			gr.setColor(Color.YELLOW);
			gr.fillRect(factorX(70), 0, factorX(31), game.getHeight());
			gr.setColor(Color.BLACK);
			gr.drawLine(factorX(70), 0, factorX(70), game.getHeight());
			gr.setFont(font);
			gr.drawString("existing levels", factorX(75), factorY(5));
			paintButtons(gr);
			return;
		}
		world.paint(gr);
		if(game.player.position != null)
			game.player.paint(gr);
		gr.setColor(Color.BLACK);
		Point min = PointUtils.tileToScreenPoint(game, new Point(0, 0));
		Point max = PointUtils.tileToScreenPoint(game, new Point(width, height));
		int x = 0;
		while(x < width){
			Point p = PointUtils.tileToScreenPoint(game, new Point(x, 0));
			gr.drawLine(p.x, max.y, p.x, min.y);
			++x;
		}
		int y = 0;
		while(y < height){
			Point p = PointUtils.tileToScreenPoint(game, new Point(0, y));
			gr.drawLine(min.x, p.y, max.x, p.y);
			++y;
		}
		if(markedPoint != null && placingObject == OBJECT_TILE){
			Point sc = PointUtils.tileToScreenPoint(game, new Point((int)(markedPoint.x / 60), (int) (markedPoint.y / 60)));
			Point m = PointUtils.tileToScreenPoint(game, getMouseTile());
			if(sc.x > m.x){
				int sx = sc.x;
				sc.x = m.x;
				m.x = sx;
			}
			if(sc.y > m.y){
				int sy = sc.y;
				sc.y = m.y;
				m.y = sy;
			}
			m.x += (int)(60 * game.zoom);
			sc.y -= (int)(60 * game.zoom);
			gr.setColor(Color.MAGENTA);
			gr.drawRect(sc.x, sc.y, m.x - sc.x, m.y - sc.y);
		}
		if(gravityMark != null && placingObject == OBJECT_GRAVITY){
			Point sc = PointUtils.gameToScreenPoint(game, gravityMark);
			Point m = PointUtils.gameToScreenPoint(game, getMouse());
			if(currentGravity == 0){
				if(sc.x > m.x){
					int sx = sc.x;
					sc.x = m.x;
					m.x = sx;
				}
				if(sc.y > m.y){
					int sy = sc.y;
					sc.y = m.y;
					m.y = sy;
				}
				gr.setColor(Color.MAGENTA);
				gr.drawRect(sc.x, sc.y, m.x - sc.x, m.y - sc.y);
			}
			if(currentGravity == 1){
				int radius = (int) sc.distance(m);
				gr.setColor(Color.MAGENTA);
				gr.drawOval(sc.x - radius, sc.y - radius, radius * 2, radius * 2);
			}
		}
		if(placingObject == OBJECT_ENEMY || placingObject == OBJECT_OTHER){
			if(currentEntity == 3){
				if(otherMark != null){
					gr.setColor(Color.MAGENTA);
					Point p1 = PointUtils.gameToScreenPoint(game, getMouse());
					Point p2 = PointUtils.gameToScreenPoint(game, otherMark);
					int radius = (int) p1.distance(p2);
					gr.drawOval(p2.x - radius, p2.y - radius, 2 * radius, 2 * radius);
					gr.drawOval(p2.x - 2 * radius, p2.y - 2 * radius, 4 * radius, 4 * radius);
				}
			}
			else {
				Class<?> c = getEnemyClass();
				if(placingObject == OBJECT_OTHER)
					c = getEntityClass();
				if(c != null){
					try {
						Entity entity = (Entity) c.getConstructor(Game.class, World.class, Point2D.Double.class).newInstance(game, world, getMouse());
						Collider co = entity.getCollider();
						Point p1 = PointUtils.gameToScreenPoint(game, new Point2D.Double(co.minX(), co.maxY()));
						Point p2 = PointUtils.gameToScreenPoint(game, new Point2D.Double(co.maxX(), co.minY()));
						gr.setColor(Color.MAGENTA);
						gr.drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
					} catch(Exception ex){}
				}
			}
		}
		gr.setColor(Color.RED);
		gr.drawRect(min.x, max.y, max.x - min.x, min.y - max.y);
		gr.setColor(Color.MAGENTA);
		gr.fillRect(0, 0, game.getWidth(), factorY(15));
		gr.setColor(Color.BLACK);
		gr.drawRect(0, 0, game.getWidth(), factorY(15));
		gr.setFont(font);
		Point2D.Double mouse = PointUtils.screenToGamePoint(game, game.getMousePosition());
		Point mouseTile = getMouseTile();
		if(mouse != null && mouseTile != null){
			mouse.x = Utils.cutDecimals(mouse.x, 2);
			mouse.y = Utils.cutDecimals(mouse.y, 2);
			gr.drawString("x: " + mouse.x + " (" + mouseTile.x + ")", factorX(38), factorY(4));
			gr.drawString("y: " + mouse.y + " (" + mouseTile.y + ")", factorX(38), factorY(8));
			gr.drawString("background: ", factorX(38), factorY(13.5));
		}
		gr.drawString("object: " + getObject(), factorX(53), factorY(4));
		gr.drawString(getObjectText(), factorX(53), factorY(8));
		gr.drawString("width: " + width, factorX(80), factorY(4));
		gr.drawString("height: " + height, factorX(80), factorY(8));
		if(selectedField != null){
			gr.setColor(Color.MAGENTA);
			gr.fillRect(0, factorY(90), game.getWidth(), factorY(10));
			gr.setColor(Color.BLACK);
			gr.drawLine(0, factorY(90), game.getWidth(), factorY(90));
		}
		paintButtons(gr);
	}
	
	@Override
	public void addButtons(){
		addConfigButtons();
	}
	
	@Override
	public void click(Point mouse){
		typingButton = -1;
		super.click(mouse);
		if(selectedField != null && mouse.y >= factorY(90)){
			int t = 0;
			while(t < gravityButtons.size()){
				if(gravityButtons.get(t).isHit(mouse)){
					buttonClicked(gravityButtons.get(t));
					return;
				}
				++t;
			}
			return;
		}
		updateButtons();
		if(config || game.getMousePosition().y <= Factors.factorY(10))
			return;
		Point t = getMouseTile();
		if(placingObject == OBJECT_TILE){
			if(markedPoint == null)
				world.setTile(Tiles.fromId(currentTile), t.x, t.y);
			else {
				Point m = new Point((int)(markedPoint.x / 60), (int) (markedPoint.y / 60));
				world.fillTiles(Tiles.fromId(currentTile), m.x < t.x ? m.x : t.x, m.y < t.y ? m.y : t.y, m.x > t.x ? m.x : t.x, m.y > t.y ? m.y : t.y);
				markedPoint = null;
			}
		}
		if(placingObject == OBJECT_ENEMY){
			Class<?> enemy = getEnemyClass();
			if(enemy != null){
				try {
					world.spawnEntity((Entity) enemy.getConstructor(Game.class, World.class, Point2D.Double.class).newInstance(game, world, getMouse()));
				} catch (Exception e) {
					e.printStackTrace(Game.console);
				}
			}
		}
		if(placingObject == OBJECT_OTHER){
			if(currentEntity == 3){
				if(otherMark == null)
					otherMark = getMouse();
				else {
					world.spawnEntity(new Planet(game, world, otherMark, "sprites/tiles/red planet.png", (int) otherMark.distance(getMouse()), true));
					otherMark = null;
				}
			}
			if(currentEntity == 4){
				if(otherMark == null)
					otherMark = getMouse();
				else {
					world.spawnEntity(new VoidSpawner(game, world, otherMark, (int) otherMark.distance(getMouse())));
					otherMark = null;
				}
			}
			Class<?> entity = getEntityClass();
			if(entity != null){
				try {
					world.spawnEntity((Entity) entity.getConstructor(Game.class, World.class, Point2D.Double.class).newInstance(game, world, getMouse()));
				} catch (Exception e) {
					e.printStackTrace(Game.console);
				}
			}
		}
		if(placingObject == OBJECT_START){
			game.player.position = getMouse();
			game.player.world = world;
			world.spawnX = getMouse().x;
			world.spawnY = getMouse().y;
		}
		if(placingObject == OBJECT_GRAVITY){
			if(currentGravity == 0){
				if(gravityMark == null)
					gravityMark = getMouse();
				else {
					Point2D.Double location = getMouse();
					Point2D.Double min = new Point2D.Double(location.x > gravityMark.x ? gravityMark.x : location.x, location.y > gravityMark.y ? gravityMark.y : location.y);
					Point2D.Double max = new Point2D.Double(location.x > gravityMark.x ? location.x : gravityMark.x, location.y > gravityMark.y ? location.y : gravityMark.y);
					world.gravityFields.add(new GravityField(game, min, max, Color.RED, 9.8, 270));
					gravityMark = null;
				}
			}
			if(currentGravity == 1){
				if(gravityMark == null)
					gravityMark = getMouse();
				else {
					Point2D.Double target = getMouse();
					world.gravityFields.add(new GravitySphere(gravityMark, Color.RED, target.distance(gravityMark), 9.8, true));
					gravityMark = null;
				}
			}
		}
		if(placingObject == OBJECT_FINISH){
			Entity finish = getFinish();
			if(finish != null)
				world.spawnEntity(finish);
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void rightClick(Point mouse){
		markedPoint = getMouse();
		if(placingObject == OBJECT_ENEMY){
			int t = 0;
			while(t < world.monsters.size()){
				if(world.monsters.get(t).getCollider().isInCollider(markedPoint)){
					world.entities.remove(world.monsters.get(t));
					world.monsters.remove(t);
				}
				++t;
			}
		}
		if(placingObject == OBJECT_OTHER){
			int t = 0;
			while(t < world.entities.size()){
				if(world.entities.get(t).getCollider().isInCollider(markedPoint))
					world.entities.remove(t);
				++t;
			}
		}
		if(placingObject == OBJECT_FINISH){
			int t = 0;
			while(t < world.finishes.size()){
				if(world.finishes.get(t).getCollider().isInCollider(markedPoint)){
					world.entities.remove(world.finishes.get(t));
					world.finishes.remove(t);
				}
				++t;
			}
			if(mouse.x >= Factors.factorX(40) && mouse.x <= Factors.factorX(60) && mouse.y >= Factors.factorY(20) && mouse.y <= Factors.factorY(25)){
				t = 0;
				while(t < world.finishes.size()){
					if(world.finishes.get(t) instanceof FinishTimer){
						world.entities.remove(world.finishes.get(t));
						world.finishes.remove(t);
					}
					++t;
				}
			}
		}
		if(placingObject == OBJECT_GRAVITY){
			selectedField = null;
			gravityMark = null;
			gravityButtons = new ArrayList<Button>();
			int t = 0;
			while(t < world.gravityFields.size()){
				if(world.gravityFields.get(t).getCollider().isInCollider(getMouse())){
					selectedField = world.gravityFields.get(t);
					addGravityButtons();
				}
				++t;
			}
		}
	}
	
	@Override
	public void buttonClicked(Button button){
		if(button instanceof ButtonLevel){
			loadLevel(((ButtonLevel) button).level);
			updateButtons();
			return;
		}
		if(button.text.matches("quit"))
			game.openGui(new GuiMainMenu(game));
		if(button.text.matches("next"))
			stopConfig();
		if(button.text.startsWith("width: "))
			typingButton = 1;
		if(button.text.startsWith("height: "))
			typingButton = 2;
		if(button.text.startsWith("name: "))
			typingButton = 0;
		if(button.text.startsWith("gravity accelleration: "))
			typingButton = 3;
		if(button.text.startsWith("gravity direction: "))
			typingButton = 4;
		if(button.text.startsWith("air friction: "))
			typingButton = 5;
		if(button.text.startsWith(" red: "))
			typingButton = 6;
		if(button.text.startsWith(" green: "))
			typingButton = 7;
		if(button.text.startsWith(" blue: "))
			typingButton = 8;
		if(button.text.startsWith("accelleration: "))
			typingButton = -2;
		if(button.text.startsWith("direction: ")){
			if(selectedField instanceof GravityField)
				typingButton = -3;
			if(selectedField instanceof GravitySphere)
				((GravitySphere)selectedField).pull = !((GravitySphere)selectedField).pull;
		}
		if(button.text.startsWith("red: "))
			typingButton = -4;
		if(button.text.startsWith("green: "))
			typingButton = -5;
		if(button.text.startsWith("blue: "))
			typingButton = -6;
		if(button.text.startsWith("alpha: "))
			typingButton = -7;
		if(button.text.matches("delete") && selectedField != null){
			world.gravityFields.remove(selectedField);
			selectedField = null;
			addBuildButtons();
		}
		if(button.text.matches("done") && selectedField != null){
			typingButton = -1;
			updateButtons();
			selectedField = null;
			addBuildButtons();
		}
		if(button.text.matches("change")){
			if(placingObject < OBJECT_FINISH)
				++placingObject;
			else
				placingObject = OBJECT_NONE;
		}
		if(button.text.equals("+")){
			if(button.minX <= Factors.factorX(72)){
				if(placingObject == OBJECT_TILE){
					if(currentTile < Tiles.tiles() - 1)
						++currentTile;
					else
						currentTile = 0;
				}
				if(placingObject == OBJECT_ENEMY){
					if(currentEnemy < 6)
						++currentEnemy;
					else
						currentEnemy = 0;
				}
				if(placingObject == OBJECT_OTHER){
					if(currentEntity < 4)
						++currentEntity;
					else
						currentEntity = 0;
				}
				if(placingObject == OBJECT_GRAVITY){
					if(currentGravity < 1)
						++currentGravity;
					else
						currentGravity = 0;
				}
				if(placingObject == OBJECT_FINISH){
					if(currentFinish < 4)
						++currentFinish;
					else
						currentFinish = 0;
				}
			}
			else if(button.minX == Factors.factorX(74)){
				if(button.minY == Factors.factorY(5.5)){
					byte[][] tiles = world.tiles;
					int t = 0;
					while(t < width){
						byte[] n = new byte[height + 1];
						System.arraycopy(tiles[t], 0, n, 1, height);
						tiles[t] = n;
						++t;
					}
					height++;
					world.height++;
					t = 0;
					while(t < world.entities.size()){
						world.entities.get(t).position.y += 60;
						++t;
					}
					t = 0;
					while(t < world.gravityFields.size()){
						world.gravityFields.get(t).min().y += 60;
						world.gravityFields.get(t).max().y += 60;
						++t;
					}
					game.player.position.y += 60;
					world.spawnY += 60;
				}
				else if(button.minY == Factors.factorY(1.5)){
					byte[][] n = new byte[width + 1][height];
					System.arraycopy(world.tiles, 0, n, 1, width);
					world.tiles = n;
					int t = 0;
					while(t < world.entities.size()){
						world.entities.get(t).position.x += 60;
						++t;
					}
					game.player.position.x += 60;
					world.spawnX += 60;
					++width;
					++world.width;
				}
			}
			else if(button.minX == Factors.factorX(93)){
				if(button.minY == Factors.factorY(5.5)){
					byte[][] tiles = world.tiles;
					int t = 0;
					while(t < width){
						byte[] n = new byte[height + 1];
						System.arraycopy(tiles[t], 0, n, 0, height);
						tiles[t] = n;
						++t;
					}
					height++;
					world.height++;
				}
				else if(button.minY == Factors.factorY(1.5)){
					byte[][] n = new byte[width + 1][height];
					System.arraycopy(world.tiles, 0, n, 0, width);
					world.tiles = n;
					width++;
					world.width++;
				}
			}
		}
		if(button.text.equals("-")){
			if(button.minX <= Factors.factorX(72)){
				if(placingObject == OBJECT_TILE){
					if(currentTile > 0)
						--currentTile;
					else
						currentTile = (byte) (Tiles.tiles() - 1);
				}
				if(placingObject == OBJECT_ENEMY){
					if(currentEnemy > 0)
						--currentEnemy;
					else
						currentEnemy = 6;
				}
				if(placingObject == OBJECT_OTHER){
					if(currentEntity > 0)
						--currentEntity;
					else
						currentEntity = 4;
				}
				if(placingObject == OBJECT_GRAVITY){
					if(currentGravity > 0)
						--currentGravity;
					else
						currentGravity = 1;
				}
				if(placingObject == OBJECT_FINISH){
					if(currentFinish > 0)
						--currentFinish;
					else
						currentFinish = 4;
				}
			}
			else if(button.minX == Factors.factorX(77)){
				if(button.minY == Factors.factorY(5.5)){
					byte[][] tiles = world.tiles;
					int t = 0;
					while(t < width){
						byte[] n = new byte[height - 1];
						System.arraycopy(tiles[t], 1, n, 0, height - 1);
						tiles[t] = n;
						++t;
					}
					height--;
					world.height--;
					t = 0;
					while(t < world.entities.size()){
						world.entities.get(t).position.y -= 60;
						++t;
					}
					t = 0;
					while(t < world.gravityFields.size()){
						world.gravityFields.get(t).min().y -= 60;
						world.gravityFields.get(t).max().y -= 60;
						++t;
					}
					game.player.position.y -= 60;
					world.spawnY -= 60;
				}
				else if(button.minY == Factors.factorY(1.5)){
					byte[][] n = new byte[width - 1][height];
					System.arraycopy(world.tiles, 1, n, 0, width - 1);
					world.tiles = n;
					int t = 0;
					while(t < world.entities.size()){
						world.entities.get(t).position.x -= 60;
						++t;
					}
					game.player.position.x -= 60;
					world.spawnX -= 60;
					--width;
					--world.width;
				}
			}
			else if(button.minX == Factors.factorX(90)){
				if(button.minY == Factors.factorY(5.5)){
					byte[][] tiles = world.tiles;
					int t = 0;
					while(t < width){
						byte[] n = new byte[height - 1];
						System.arraycopy(tiles[t], 0, n, 0, height - 1);
						tiles[t] = n;
						++t;
					}
					height--;
					world.height--;
				}
				else if(button.minY == Factors.factorY(1.5)){
					byte[][] n = new byte[width - 1][height];
					try {
						System.arraycopy(world.tiles, 0, n, 0, width - 1);
					} catch(Exception ex){
						System.out.println("world.tiles.length = " + world.tiles.length + " n.length = " + n.length + " width = " + width);
						ex.printStackTrace();
						return;
					}
					world.tiles = n;
					width--;
					world.width--;
				}
			}
		}
		if(button.text.equals("quit"))
			game.openGui(new GuiMainMenu(game));
		if(button.text.matches("save"))
			saveLevel();
		if(button.text.matches("test")){
			saveLevel();
			recover();
			game.buildWorld(world);
			game.exitGui = this;
		}
		updateButtons();
	}
	
	@Override
	public void keyTyped(KeyEvent event){
		if(typingButton != -1){
			char c = event.getKeyChar();
			try {
				if((byte)c == 8){
					if(typingButton == 0){
						if(name.length() > 0)
							name = name.substring(0, name.length() - 1);
					}
					if(typingButton == 1){
						String w = width + "";
						width = Short.decode(w.substring(0, w.length() - 1));
					}
					if(typingButton == 2){
						String h = height + "";
						height = Short.decode(h.substring(0, h.length() - 1));
					}
					if(typingButton == 3){
						String a = gravityAccelleration;
						gravityAccelleration = a.substring(0, a.length() - 1);
					}
					if(typingButton == 4){
						String d = gravityDirection + "";
						gravityDirection = Short.decode(d.substring(0, d.length() - 1));
					}
					if(typingButton == 5){
						String f = airFriction;
						airFriction = f.substring(0, f.length() - 1);
					}
					if(typingButton == 6){
						String r = world.backGroundColor.getRed() + "";
						world.backGroundColor = new Color(Short.decode(r.substring(0, r.length() - 1)), world.backGroundColor.getGreen(), world.backGroundColor.getBlue());
					}
					if(typingButton == 7){
						String g = world.backGroundColor.getGreen() + "";
						world.backGroundColor = new Color(world.backGroundColor.getRed(), Short.decode(g.substring(0, g.length() - 1)), world.backGroundColor.getBlue());
					}
					if(typingButton == 8){
						String b = world.backGroundColor.getBlue() + "";
						world.backGroundColor = new Color(world.backGroundColor.getRed(), world.backGroundColor.getGreen(), Short.decode(b.substring(0, b.length() - 1)));
					}
					if(typingButton == -2 && selectedField != null){
						String a = fieldAccelleration;
						fieldAccelleration = a.substring(0, a.length() - 1);
					}
					if(typingButton == -3 && selectedField instanceof GravityField){
						String d = (short)((GravityField)selectedField).rotation + "";
						((GravityField)selectedField).rotation = Short.decode(d.substring(0, d.length() - 1));
					}
					if(typingButton == -4 && selectedField != null && selectedField.color() != null){
						String r = selectedField.color().getRed() + "";
						selectedField.setColor(new Color(Short.decode(r.substring(0, r.length() - 1)), selectedField.color().getGreen(), selectedField.color().getBlue(), selectedField.color().getAlpha()));
					}
					if(typingButton == -5 && selectedField != null && selectedField.color() != null){
						String g = selectedField.color().getGreen() + "";
						selectedField.setColor(new Color(selectedField.color().getRed(), Short.decode(g.substring(0, g.length() - 1)), selectedField.color().getBlue(), selectedField.color().getAlpha()));
					}
					if(typingButton == -6 && selectedField != null && selectedField.color() != null){
						String b = selectedField.color().getBlue() + "";
						selectedField.setColor(new Color(selectedField.color().getRed(), selectedField.color().getGreen(), Short.decode(b.substring(0, b.length() - 1)), selectedField.color().getAlpha()));
					}
					if(typingButton == -7 && selectedField != null){
						String a = selectedField.color().getAlpha() + "";
						selectedField.setColor(new Color(selectedField.color().getRed(), selectedField.color().getGreen(), selectedField.color().getBlue(), Short.decode(a.substring(0, a.length() - 1))));
					}
				}
				else if(typingButton == 0){
					name += c;
					name.replace(' ', '_');
				}
				else if((byte)c >= 48 && (byte)c <= 58 || c == '.' && (typingButton == 3 || typingButton == -2 || typingButton == 5)){
					if(typingButton == 1){
						String w = width + "" + c;
						if(w.startsWith("0"))
							w = w.substring(1);
						width = Short.decode(w);
					}
					if(typingButton == 2){
						String h = height + "" + c;
						if(h.startsWith("0"))
							h = h.substring(1);
						height = Short.decode(h);
					}
					if(typingButton == 3){
						if(c == '.' && gravityAccelleration.contains("."))
							return;
						String a = gravityAccelleration + c;
						gravityAccelleration = a;
					}
					if(typingButton == 4){
						String d = gravityDirection + "" + c;
						if(d.startsWith("0"))
							d = d.substring(1);
						gravityDirection = Short.decode(d);
						if(gravityDirection > 360)
							gravityDirection = 360;
					}
					if(typingButton == 5){
						if(c == '.' && airFriction.contains("."))
							return;
						String f = airFriction + c;
						airFriction = f;
					}
					if(typingButton == 6){
						String r = world.backGroundColor.getRed() + "" + c;
						if(r.startsWith("0"))
							r = r.substring(1);
						int red = Short.decode(r);
						if(red > 255)
							red = 255;
						world.backGroundColor = new Color(red, world.backGroundColor.getGreen(), world.backGroundColor.getBlue());
					}
					if(typingButton == 7){
						String g = world.backGroundColor.getGreen() + "" + c;
						if(g.startsWith("0"))
							g = g.substring(1);
						int green = Short.decode(g);
						if(green > 255)
							green = 255;
						world.backGroundColor = new Color(world.backGroundColor.getRed(), green, world.backGroundColor.getBlue());
					}
					if(typingButton == 8){
						String b = world.backGroundColor.getBlue() + "" + c;
						if(b.startsWith("0"))
							b = b.substring(1);
						int blue = Short.decode(b);
						if(blue > 255)
							blue = 255;
						world.backGroundColor = new Color(world.backGroundColor.getRed(), world.backGroundColor.getGreen(), blue);
					}
					if(typingButton == -2){
						if(c == '.' && fieldAccelleration.contains("."))
							return;
						String a = fieldAccelleration + c;
						fieldAccelleration = a;
					}
					if(typingButton == -3 && selectedField instanceof GravityField){
						GravityField field = (GravityField) selectedField;
						String d = (short)field.rotation + "" + c;
						if(d.startsWith("0"))
							d = d.substring(1);
						field.rotation = Short.decode(d);
						if(field.rotation > 360)
							field.rotation = 360;
					}
					if(typingButton == -4 && selectedField != null && selectedField.color() != null){
						String r = selectedField.color().getRed() + "" + c;
						if(r.startsWith("0"))
							r = r.substring(1);
						int red = Short.decode(r);
						if(red > 255)
							red = 255;
						selectedField.setColor(new Color(red, selectedField.color().getGreen(), selectedField.color().getBlue(), selectedField.color().getAlpha()));
					}
					if(typingButton == -5 && selectedField != null && selectedField.color() != null){
						String g = selectedField.color().getGreen() + "" + c;
						if(g.startsWith("0"))
							g = g.substring(1);
						int green = Short.decode(g);
						if(green > 255)
							green = 255;
						selectedField.setColor(new Color(selectedField.color().getRed(), green, selectedField.color().getBlue(), selectedField.color().getAlpha()));
					}
					if(typingButton == -6 && selectedField != null && selectedField.color() != null){
						String b = selectedField.color().getBlue() + "" + c;
						if(b.startsWith("0"))
							b = b.substring(1);
						int blue = Short.decode(b);
						if(blue > 255)
							blue = 255;
						selectedField.setColor(new Color(selectedField.color().getRed(), selectedField.color().getGreen(), blue, selectedField.color().getAlpha()));
					}
					if(typingButton == -7 && selectedField != null && selectedField.color() != null){
						String a = selectedField.color().getAlpha() + "" + c;
						if(a.startsWith("0"))
							a = a.substring(1);
						int alpha = Short.decode(a);
						if(alpha > 255)
							alpha = 255;
						selectedField.setColor(new Color(selectedField.color().getRed(), selectedField.color().getGreen(), selectedField.color().getBlue(), alpha));
					}
				}
			} catch(Exception ex){
				if(ex.getMessage() != null && ex.getMessage().matches("Zero length string")){
					if(typingButton == 1)
						width = 0;
					if(typingButton == 2)
						height = 0;
					if(typingButton == 3)
						gravityAccelleration = "0";
					if(typingButton == 4)
						gravityDirection = 0;
					if(typingButton == 5)
						airFriction = "0";
					if(typingButton == 6)
						world.backGroundColor = new Color(0, world.backGroundColor.getGreen(), world.backGroundColor.getBlue());
					if(typingButton == 7)
						world.backGroundColor = new Color(world.backGroundColor.getRed(), 0, world.backGroundColor.getBlue());
					if(typingButton == 8)
						world.backGroundColor = new Color(world.backGroundColor.getRed(), world.backGroundColor.getGreen(), 0);
					if(typingButton == -2)
						fieldAccelleration = "0";
					if(typingButton == -3 && selectedField instanceof GravityField)
						((GravityField)selectedField).rotation = 0;
					if(typingButton <= -4 && typingButton >= -7){
						int red = selectedField.color().getRed();
						int green = selectedField.color().getGreen();
						int blue = selectedField.color().getBlue();
						int alpha = selectedField.color().getAlpha();
						if(typingButton == -4)
							red = 0;
						if(typingButton == -5)
							green = 0;
						if(typingButton == -6)
							blue = 0;
						if(typingButton == -7)
							alpha = 0;
						selectedField.setColor(new Color(red, green, blue, alpha));
					}
				}
				else
					ex.printStackTrace(Game.console);
			}	
		}
		updateButtons();
	}

	public void loadLevel(File file){
		try {
			stopConfig();
			name = file.getName().substring(0, file.getName().length() - 6);
			InputStream stream = getClass().getClassLoader().getResourceAsStream("levels/" + file.getName());
			if(stream == null)
				stream = new FileInputStream(file.getPath());
			Game.console.println("stream = " + stream + " file.length() returns " + file.length() + " stream.available returns " + stream.available());
			Game.console.println(file.getAbsolutePath());
			byte[] bytes;
			/*
			if(file.length() > 0){
				bytes = new byte[(int) file.length()];
				stream.read(bytes);
			}
			else {*/
				bytes = new byte[0];
				while(stream.available() > 0){
					bytes = Arrays.copyOf(bytes, bytes.length + stream.available());
					stream.read(bytes);
					Thread.sleep(20);
				}
			//}
			stream.close();
			Game.console.println("bytes = " + bytes.length);
			byte encoding = bytes[0];
			width = Utils.bytesToShort(bytes[1], bytes[2]);
			height = Utils.bytesToShort(bytes[3], bytes[4]);
			short spawnX = Utils.bytesToShort(bytes[5], bytes[6]);
			short spawnY = Utils.bytesToShort(bytes[7], bytes[8]);
			game.player.world = world;
			game.player.position = new Point2D.Double(spawnX, spawnY);
			world = new World(game, width, height);
			world.spawnX = spawnX;
			world.spawnY = spawnY;
			short accel = Utils.bytesToShort(bytes[9], bytes[10]);
			float accelleration = accel / 100.0F;
			world.gravity = accelleration;
			gravityAccelleration = Utils.cutDecimals(accelleration, 2) + "";
			world.gravityDirection = gravityDirection = Utils.bytesToShort(bytes[11], bytes[12]);
			int t = 13;
			if(encoding == ENCODING_BITMAP_2 || encoding == ENCODING_TO_WIDTH_2 || encoding == ENCODING_TO_HEIGHT_2){
				short fric = Utils.bytesToShort(bytes[13], bytes[14]);
				float friction = fric / 1000.0F;
				world.airFriction = friction;
				airFriction = Utils.cutDecimals(friction, 2) + "";
				int red = bytes[15] + 128;
				int green = bytes[16] + 128;
				int blue = bytes[17] + 128;
				world.backGroundColor = new Color(red, green, blue);
				t += 5;
			}
			if(encoding == ENCODING_BITMAP || encoding == ENCODING_BITMAP_2){
				int x = 0;
				while(x < width){
					int y = 0;
					while(y < height){
						world.tiles[x][y] = bytes[t];
						++y;
						++t;
					}
					++x;
				}
			}
			if(encoding == ENCODING_TO_WIDTH || encoding == ENCODING_TO_HEIGHT || encoding == ENCODING_TO_WIDTH_2 || encoding == ENCODING_TO_HEIGHT_2){
				byte[] tiles = new byte[width * height];
				int t1 = 0;
				while(t1 < tiles.length){
					if(bytes[t] == FILL_TILE){
						short length = Utils.bytesToShort(bytes[t + 1], bytes[t + 2]);
						byte tile = bytes[t + 3];
						int l = 0;
						while(l < length){
							if(t1 + l < tiles.length)
								tiles[t1 + l] = tile;
							++l;
						}
						t1 += length;
						t += 4;
					}
					else {
						if(t1 < tiles.length)
							tiles[t1] = bytes[t];
						++t1;
						++t;
					}
				}
				if(encoding == ENCODING_TO_WIDTH || encoding == ENCODING_TO_WIDTH_2){
					int y = 0;
					int t2 = 0;
					while(y < height){
						int x = 0;
						while(x < width){
							world.tiles[x][y] = tiles[t2];
							++x;
							++t2;
						}
						++y;
					}
				}
				if(encoding == ENCODING_TO_HEIGHT || encoding == ENCODING_TO_HEIGHT_2){
					int x = 0;
					int t2 = 0;
					while(x < width){
						int y = 0;
						while(y < height){
							world.tiles[x][y] = tiles[t2];
							++y;
							++t2;
						}
						++x;
					}
				}
			}
			while(t + 4 < bytes.length){
				byte id = bytes[t];
				if(id == ID_GRAVITY_FIELD){
					short minX = Utils.bytesToShort(bytes[t + 1], bytes[t + 2]);
					short minY = Utils.bytesToShort(bytes[t + 3], bytes[t + 4]);
					short maxX = Utils.bytesToShort(bytes[t + 5], bytes[t + 6]);
					short maxY = Utils.bytesToShort(bytes[t + 7], bytes[t + 8]);
					double accell = Utils.bytesToShort(bytes[t + 9], bytes[t + 10]) / 100.0;
					short rot = Utils.bytesToShort(bytes[t + 11], bytes[t + 12]);
					int red = bytes[t + 13] + 128;
					int green = bytes[t + 14] + 128;
					int blue = bytes[t + 15] + 128;
					int alpha = bytes[t + 16] + 128;
					world.gravityFields.add(new GravityField(game, new Point2D.Double(minX, minY), new Point2D.Double(maxX, maxY), new Color(red, green, blue, alpha), accell, rot));
					t += 17;
				}
				else if(id == ID_GRAVITY_SPHERE){
					short cenX = Utils.bytesToShort(bytes[t + 1], bytes[t + 2]);
					short cenY = Utils.bytesToShort(bytes[t + 3], bytes[t + 4]);
					boolean pull = bytes[t + 5] == 1;
					double accell = Utils.bytesToShort(bytes[t + 9], bytes[t + 10]) / 100.0;
					short rad = Utils.bytesToShort(bytes[t + 11], bytes[t + 12]);
					int red = bytes[t + 13] + 128;
					int green = bytes[t + 14] + 128;
					int blue = bytes[t + 15] + 128;
					int alpha = bytes[t + 16] + 128;
					world.gravityFields.add(new GravitySphere(new Point2D.Double(cenX, cenY), new Color(red, green, blue, alpha), rad, accell, pull));
					t += 17;
				}
				else if(id == ID_RED_PLANET){
					short entityX = Utils.bytesToShort(bytes[t + 1], bytes[t + 2]);
					short entityY = Utils.bytesToShort(bytes[t + 3], bytes[t + 4]);
					short radius = Utils.bytesToShort(bytes[t + 5], bytes[t + 6]);
					world.spawnEntity(new Planet(game, world, new Point2D.Double(entityX, entityY), "sprites/tiles/red planet.png", radius, false));
					t += 7;
				}
				else if(id == ID_VOID_SPAWNER){
					short entityX = Utils.bytesToShort(bytes[t + 1], bytes[t + 2]);
					short entityY = Utils.bytesToShort(bytes[t + 3], bytes[t + 4]);
					short radius = Utils.bytesToShort(bytes[t + 5], bytes[t + 6]);
					world.spawnEntity(new VoidSpawner(game, world, new Point2D.Double(entityX, entityY), radius));
					t += 7;
				}
				else {
					short entityX = Utils.bytesToShort(bytes[t + 1], bytes[t + 2]);
					short entityY = Utils.bytesToShort(bytes[t + 3], bytes[t + 4]);
					if(id == ID_BIRD)
						world.spawnEntity(new Bird(game, world, new Point2D.Double(entityX, entityY)));
					if(id == ID_WARRIOR)
						world.spawnEntity(new Warrior(game, world, new Point2D.Double(entityX, entityY)));
					if(id == ID_ARCHER)
						world.spawnEntity(new Archer(game, world, new Point2D.Double(entityX, entityY)));
					if(id == ID_VOID_PUNCHER)
						world.spawnEntity(new VoidPuncher(game, world, new Point2D.Double(entityX, entityY)));
					if(id == ID_BATTLE_STAR)
						world.spawnEntity(new BattleStar(game, world, new Point2D.Double(entityX, entityY)));
					if(id == ID_CATAPULT)
						world.spawnEntity(new Catapult(game, world, new Point2D.Double(entityX, entityY)));
					if(id == ID_HEART)
						world.spawnEntity(new Heart(game, world, new Point2D.Double(entityX, entityY)));
					if(id == ID_YELLOW_COIN)
						world.spawnEntity(new Yellow(game, world, new Point2D.Double(entityX, entityY)));
					if(id == ID_VERTICAL_FLAG)
						world.spawnEntity(new Flag(game, world, new Point2D.Double(entityX, entityY), false, true));
					if(id == ID_HORIZONTAL_FLAG)
						world.spawnEntity(new Flag(game, world, new Point2D.Double(entityX, entityY), true, false));
					if(id == ID_BASIC_FLAG)
						world.spawnEntity(new Flag(game, world, new Point2D.Double(entityX, entityY), false, false));
					if(id == ID_TIMER)
						world.spawnEntity(new FinishTimer(game, world, 15000));
					if(id == ID_WOODEN_DOOR)
						world.spawnEntity(new Door(game, world, new Point2D.Double(entityX, entityY), "wooden door", 120, 180));
					t += 5;
				}
			}
		} catch (Exception e) {
			e.printStackTrace(Game.console);
		}
	}
	
	public void saveLevel(){
		byte[] bitmap;
		byte[] toWidth;
		byte[] toHeight;
		if(Float.parseFloat(airFriction) == 1 && world.backGroundColor.equals(new Color(0, 0, 200))){
			bitmap = saveLevel(ENCODING_BITMAP);
			toWidth = saveLevel(ENCODING_TO_WIDTH);
			toHeight = saveLevel(ENCODING_TO_HEIGHT);
		}
		else {
			bitmap = saveLevel(ENCODING_BITMAP_2);
			toWidth = saveLevel(ENCODING_TO_WIDTH_2);
			toHeight = saveLevel(ENCODING_TO_HEIGHT_2);
		}
		byte[] best;
		if(bitmap.length <= toWidth.length && bitmap.length <= toHeight.length)
			best = bitmap;
		else if(toWidth.length <= bitmap.length && toWidth.length <= bitmap.length)
			best = toWidth;
		else
			best = toHeight;
		byte[] bytes = new byte[0];
		int t = 0;
		int b = 0;
		while(t < world.entities.size()){
			Entity entity = world.entities.get(t);
			bytes = Arrays.copyOf(bytes, bytes.length + 5);
			byte id = ID_BIRD;
			if(entity instanceof Warrior)
				id = ID_WARRIOR;
			if(entity instanceof Archer)
				id = ID_ARCHER;
			if(entity instanceof VoidPuncher)
				id = ID_VOID_PUNCHER;
			if(entity instanceof BattleStar)
				id = ID_BATTLE_STAR;
			if(entity instanceof Catapult)
				id = ID_CATAPULT;
			if(entity instanceof Heart)
				id = ID_HEART;
			if(entity instanceof Yellow)
				id = ID_YELLOW_COIN;
			if(entity instanceof VoidSpawner)
				id = ID_VOID_SPAWNER;
			if(entity instanceof Flag){
				Flag flag = (Flag) entity;
				if(flag.verticalFinish)
					id = ID_VERTICAL_FLAG;
				else if(flag.horizontalFinish)
					id = ID_HORIZONTAL_FLAG;
				else
					id = ID_BASIC_FLAG;
			}
			if(entity instanceof FinishTimer)
				id = ID_TIMER;
			if(entity instanceof Door)
				id = ID_WOODEN_DOOR;
			if(entity instanceof Planet)
				id = ID_RED_PLANET;
			bytes[b] = id;
			byte[] x = Utils.shortToBytes((short) entity.position.x);
			byte[] y = Utils.shortToBytes((short) entity.position.y);
			bytes[b + 1] = x[0];
			bytes[b + 2] = x[1];
			bytes[b + 3] = y[0];
			bytes[b + 4] = y[1];
			if(entity instanceof Planet || entity instanceof VoidSpawner){
				bytes = Arrays.copyOf(bytes, bytes.length + 2);
				byte[] radius = Utils.shortToBytes((short) (entity.width / 2));
				bytes[b + 5] = radius[0];
				bytes[b + 6] = radius[1];
				b += 2;
			}
			++t;
			b += 5;
		}
		int i = 0;
		while(i < world.gravityFields.size()){
			IGravity grav = world.gravityFields.get(i);
			if(grav instanceof GravityField){
				GravityField field = (GravityField) grav;
				bytes = Arrays.copyOf(bytes, bytes.length + 17);
				bytes[i * 17 + b] = ID_GRAVITY_FIELD;
				byte[] minX = Utils.shortToBytes((short) field.min.x);
				byte[] minY = Utils.shortToBytes((short) field.min.y);
				byte[] maxX = Utils.shortToBytes((short) field.max.x);
				byte[] maxY = Utils.shortToBytes((short) field.max.y);
				bytes[i * 17 + 1 + b] = minX[0];
				bytes[i * 17 + 2 + b] = minX[1];
				bytes[i * 17 + 3 + b] = minY[0];
				bytes[i * 17 + 4 + b] = minY[1];
				bytes[i * 17 + 5 + b] = maxX[0];
				bytes[i * 17 + 6 + b] = maxX[1];
				bytes[i * 17 + 7 + b] = maxY[0];
				bytes[i * 17 + 8 + b] = maxY[1];
				byte[] accelleration = Utils.shortToBytes((short)(field.power * 100));
				byte[] direction = Utils.shortToBytes((short) field.rotation);
				bytes[i * 17 + 9 + b] = accelleration[0];
				bytes[i * 17 + 10 + b] = accelleration[1];
				bytes[i * 17 + 11 + b] = direction[0];
				bytes[i * 17 + 12 + b] = direction[1];
				bytes[i * 17 + 13 + b] = (byte) (field.color.getRed() - 128);
				bytes[i * 17 + 14 + b] = (byte) (field.color.getGreen() - 128);
				bytes[i * 17 + 15 + b] = (byte) (field.color.getBlue() - 128);
				bytes[i * 17 + 16 + b] = (byte) (field.color.getAlpha() - 128);
			}
			if(grav instanceof GravitySphere){
				GravitySphere sphere = (GravitySphere) grav;
				bytes = Arrays.copyOf(bytes, bytes.length + 17);
				bytes[i * 17 + b] = ID_GRAVITY_SPHERE;
				byte[] minX = Utils.shortToBytes((short) sphere.center.x);
				byte[] minY = Utils.shortToBytes((short) sphere.center.y);
				bytes[i * 17 + 1 + b] = minX[0];
				bytes[i * 17 + 2 + b] = minX[1];
				bytes[i * 17 + 3 + b] = minY[0];
				bytes[i * 17 + 4 + b] = minY[1];
				bytes[i * 17 + 5 + b] = (byte) (sphere.pull ? 1 : 0);
				byte[] accelleration = Utils.shortToBytes((short)(sphere.accelleration * 100));
				byte[] radius = Utils.shortToBytes((short) sphere.radius);
				bytes[i * 17 + 9 + b] = accelleration[0];
				bytes[i * 17 + 10 + b] = accelleration[1];
				bytes[i * 17 + 11 + b] = radius[0];
				bytes[i * 17 + 12 + b] = radius[1];
				bytes[i * 17 + 13 + b] = (byte) (sphere.color.getRed() - 128);
				bytes[i * 17 + 14 + b] = (byte) (sphere.color.getGreen() - 128);
				bytes[i * 17 + 15 + b] = (byte) (sphere.color.getBlue() - 128);
				bytes[i * 17 + 16 + b] = (byte) (sphere.color.getAlpha() - 128);
			}
			++i;
		}
		try {
			Utils.createCustomLevelsFolder();
			FileOutputStream stream = new FileOutputStream(new File("custom levels/" + name + ".level"));
			stream.write(best);
			stream.write(bytes);
			stream.close();
		} catch(Exception ex){
			ex.printStackTrace(Game.console);
		}
	}
	
	public byte[] saveLevel(byte encoding){
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		bytes.add(encoding);
		byte[] w = Utils.shortToBytes(width);
		bytes.add(w[0]);
		bytes.add(w[1]);
		byte[] h = Utils.shortToBytes(height);
		bytes.add(h[0]);
		bytes.add(h[1]);
		byte[] spawnX = Utils.shortToBytes((short) game.player.position.x);
		byte[] spawnY = Utils.shortToBytes((short) game.player.position.y);
		bytes.add(spawnX[0]);
		bytes.add(spawnX[1]);
		bytes.add(spawnY[0]);
		bytes.add(spawnY[1]);
		short accel = (short)(Float.parseFloat(gravityAccelleration) * 100);
		byte[] baccel = Utils.shortToBytes(accel);
		bytes.add(baccel[0]);
		bytes.add(baccel[1]);
		byte[] direc = Utils.shortToBytes(gravityDirection);
		bytes.add(direc[0]);
		bytes.add(direc[1]);
		if(encoding == ENCODING_BITMAP_2 || encoding == ENCODING_TO_WIDTH_2 || encoding == ENCODING_TO_HEIGHT_2){
			byte[] friction = Utils.shortToBytes((short) (Float.parseFloat(airFriction) * 1000));
			bytes.add(friction[0]);
			bytes.add(friction[1]);
			bytes.add((byte) (world.backGroundColor.getRed() - 128));
			bytes.add((byte) (world.backGroundColor.getGreen() - 128));
			bytes.add((byte) (world.backGroundColor.getBlue() - 128));
		}
		if(encoding == ENCODING_BITMAP || encoding == ENCODING_BITMAP_2){
			int x = 0;
			while(x < width){
				int y = 0;
				while(y < height){
					bytes.add(world.tiles[x][y]);
					++y;
				}
				++x;
			}
		}
		if(encoding == ENCODING_TO_WIDTH || encoding == ENCODING_TO_WIDTH_2){
			byte[] tiles = new byte[width * height];
			int y = 0;
			while(y < height){
				int x = 0;
				while(x < width){
					tiles[width * y + x] = world.tiles[x][y];
					++x;
				}
				++y;
			}
			int t = 0;
			while(t < tiles.length){
				byte tile = tiles[t];
				short t1 = 1;
				while(t + t1 < tiles.length && tiles[t + t1] == tile && t1 < Short.MAX_VALUE){
					t1++;
				}
				if(t1 > 4){
					bytes.add(FILL_TILE);
					byte[] size = Utils.shortToBytes(t1);
					bytes.add(size[0]);
					bytes.add(size[1]);
					bytes.add(tile);
					t += t1 - 1;
				}
				else
					bytes.add(tile);
				++t;
			}
		}
		if(encoding == ENCODING_TO_HEIGHT || encoding == ENCODING_TO_HEIGHT_2){
			byte[] tiles = new byte[width * height];
			int x = 0;
			while(x < width){
				int y = 0;
				while(y < height){
					tiles[height * x + y] = world.tiles[x][y];
					++y;
				}
				++x;
			}
			int t = 0;
			while(t < tiles.length){
				byte tile = tiles[t];
				short t1 = 1;
				while(t + t1 < tiles.length && tiles[t + t1] == tile && t1 < Short.MAX_VALUE){
					t1++;
				}
				if(t1 > 4){
					bytes.add(FILL_TILE);
					byte[] size = Utils.shortToBytes(t1);
					bytes.add(size[0]);
					bytes.add(size[1]);
					bytes.add(tile);
					t += t1 - 1;
				}
				else
					bytes.add(tile);
				++t;
			}
		}
		byte[] b = new byte[bytes.size()];
		int t = 0;
		while(t < b.length){
			b[t] = bytes.get(t);
			++t;
		}
		return b;
	}
	
	public void recover(){
		loadLevel(new File("custom levels/" + name + ".level"));
		game.player.health = 100;
		game.player.hook = null;
		game.player.motionX = 0;
		game.player.motionY = 0;
	}
	
	@Override
	protected void paintButtons(Graphics gr){
		int t = 0;
		while(t < buttons.size()){
			Button button = buttons.get(t);
			if(!((placingObject == OBJECT_NONE || placingObject == OBJECT_START) && (button.text.equals("+") || button.text.equals("-")) && button.minX < Factors.factorX(73)))
				button.paint(gr);
			++t;
		}
		if(selectedField != null){
			t = 0;
			while(t < gravityButtons.size()){
				gravityButtons.get(t).paint(gr);
				++t;
			}
		}
	}
	
	protected void updateButtons(){
		buttons.get(0).text = "name: " + name;
		if(config){
			buttons.get(1).text = "width: " + width;
			buttons.get(2).text = "height: " + height;
		}
		buttons.get(3).text = "gravity accelleration: " + gravityAccelleration;
		buttons.get(4).text = "gravity direction: " + gravityDirection;
		if(!config){
			buttons.get(5).text = "air friction: " + airFriction;
			buttons.get(6).text = " red: " + world.backGroundColor.getRed();
			buttons.get(7).text = " green: " + world.backGroundColor.getGreen();
			buttons.get(8).text = " blue: " + world.backGroundColor.getBlue();
		}
		int t = 0;
		while(t < 9){
			if(typingButton == t && !(t >= 6 && t <= 8 && !config))
				buttons.get(t).bodyColor = Color.GREEN;
			else if(!((t == 1 || t == 2) && !config)){
				if(!((t >= 5 && t <= 8) && config) && !(t >= 6 && t <= 8 && !config))
					buttons.get(t).bodyColor = Color.YELLOW;
				if(t == 3)
					gravityAccelleration = Float.parseFloat(gravityAccelleration) + "";
				if(t == 5)
					airFriction = Float.parseFloat(airFriction) + "";
			}
			++t;
		}
		if(selectedField != null){
			if(fieldAccelleration == null)
				fieldAccelleration = selectedField.getAccelleration() + "";
			gravityButtons.get(0).text = "accelleration: " + fieldAccelleration;
			if(selectedField instanceof GravityField)
				gravityButtons.get(1).text = "direction: " + ((GravityField)selectedField).rotation;
			if(selectedField instanceof GravitySphere)
				gravityButtons.get(1).text = "direction: " + (((GravitySphere)selectedField).pull ? "pull" : "push");
			gravityButtons.get(2).text = "red: " + selectedField.color().getRed();
			gravityButtons.get(3).text = "green: " + selectedField.color().getGreen();
			gravityButtons.get(4).text = "blue: " + selectedField.color().getBlue();
			gravityButtons.get(5).text = "alpha: " + selectedField.color().getAlpha();
			t = 0;
			while(t < gravityButtons.size()){
				Button b = gravityButtons.get(t);
				if(-typingButton - 2 == t)
					b.borderColor = Color.WHITE;
				else {
					b.borderColor = Color.BLACK;
					if(t == 0)
						selectedField.setAccelleration(Float.parseFloat(fieldAccelleration));
				}
				++t;
			}
		}
	}
	
	protected void addConfigButtons(){
		buttons = new ArrayList<Button>();
		buttons.add(new Button("name: " + name, Color.YELLOW, 10, 15, 50, 25));
		buttons.add(new Button("width: " + width, Color.YELLOW, 10, 30, 30, 40));
		buttons.add(new Button("height: " + height, Color.YELLOW, 10, 45, 30, 55));
		buttons.add(new Button("gravity accelleration: " + gravityAccelleration, Color.YELLOW, 10, 60, 50, 70));
		buttons.add(new Button("gravity direction: " + gravityDirection, Color.YELLOW, 10, 75, 50, 85));
		buttons.add(new Button("quit", Color.RED, 53, 55, 59, 65));
		buttons.add(new Button("next", Color.GREEN, 53, 35, 59, 45));
		ArrayList<File> levels = Utils.getCustomLevels();
		int t = 0;
		while(t < levels.size()){
			buttons.add(new ButtonLevel(levels.get(t), Color.MAGENTA, 75, 15 + t * 15, 95, 25 + t * 15));
			++t;
		}
	}
	
	protected void addBuildButtons(){
		buttons = new ArrayList<Button>();
		buttons.add(new Button("name: " + name, Color.YELLOW, 1, 1, 14, 5));
		buttons.add(new Button("test", Color.GREEN, 95.5, 1, 99.5, 5));
		buttons.add(new Button("save", Color.GREEN, 95.5, 5.5, 99.5, 9.5));
		buttons.add(new Button("gravity accelleration: " + gravityAccelleration, Color.YELLOW, 15, 1, 35, 5));
		buttons.add(new Button("gravity direction: " + gravityDirection, Color.YELLOW, 15, 5.5, 35, 9.5));
		buttons.add(new Button("air friction: " + airFriction, Color.YELLOW, 15, 10.5, 35, 14.5));
		buttons.add(new Button(" red: " + world.backGroundColor.getRed(), Color.RED, 52, 10.5, 59, 14.5));
		buttons.add(new Button(" green: " + world.backGroundColor.getGreen(), Color.GREEN, 59.5, 10.5, 66.5, 14.5));
		buttons.add(new Button(" blue: " + world.backGroundColor.getBlue(), Color.BLUE, 67, 10.5, 74, 14.5));
		buttons.add(new Button("change", Color.BLUE, 68, 1, 73, 5));
		buttons.add(new Button("-", Color.BLUE, 68, 6, 70, 9));
		buttons.add(new Button("+", Color.BLUE, 71, 6, 73, 9));
		buttons.add(new Button("+", Color.YELLOW, 74, 1.5, 76, 4.5));
		buttons.add(new Button("-", Color.YELLOW, 77, 1.5, 79, 4.5));
		buttons.add(new Button("+", Color.YELLOW, 74, 5.5, 76, 8.5));
		buttons.add(new Button("-", Color.YELLOW, 77, 5.5, 79, 8.5));
		buttons.add(new Button("-", Color.YELLOW, 90, 1.5, 92, 4.5));
		buttons.add(new Button("+", Color.YELLOW, 93, 1.5, 95, 4.5));
		buttons.add(new Button("-", Color.YELLOW, 90, 5.5, 92, 8.5));
		buttons.add(new Button("+", Color.YELLOW, 93, 5.5, 95, 8.5));
		buttons.add(new Button("quit", Color.RED, 1, 5.5, 5, 9.5));
	}
	
	protected void addGravityButtons(){
		gravityButtons = new ArrayList<Button>();
		gravityButtons.add(new Button("accelleration: " + selectedField.getAccelleration(), Color.YELLOW, 15, 91, 30, 95));
		if(selectedField instanceof GravityField)
			gravityButtons.add(new Button("direction: " + ((GravityField)selectedField).rotation, Color.YELLOW, 15, 95.5, 30, 99.5));
		if(selectedField instanceof GravitySphere)
			gravityButtons.add(new Button("direction: " + (((GravitySphere)selectedField).pull ? "pull" : "push"), Color.YELLOW, 15, 95.5, 30, 99.5));
		gravityButtons.add(new Button("red: " + selectedField.color().getRed(), Color.RED, 35, 91, 42, 95));
		gravityButtons.add(new Button("green: " + selectedField.color().getGreen(), Color.GREEN, 35, 95.5, 42, 99.5));
		gravityButtons.add(new Button("blue: " + selectedField.color().getBlue(), Color.BLUE, 45, 91, 52, 95));
		gravityButtons.add(new Button("alpha: " + selectedField.color().getAlpha(), Color.WHITE, 45, 95.5, 52, 99.5));
		gravityButtons.add(new Button("delete", Color.RED, 90, 91, 95, 95));
		gravityButtons.add(new Button("done", Color.GREEN, 90, 95.5, 95, 99.5));
	}
	
	protected void stopConfig(){
		config = false;
		world = new World(game, width, height);
		addBuildButtons();
		world.gravity = Float.parseFloat(gravityAccelleration);
		world.gravityDirection = gravityDirection;
		world.airFriction = Float.parseFloat(airFriction);
		Game.console.println("world.airFriction = " + world.airFriction + " and airFriction = " + airFriction);
		game.player.position = new Point2D.Double(120, 300);
		game.player.world = world;
		world.spawnX = 120;
		world.spawnY = 300;
	}
	
	protected Point2D.Double getMouse(){
		return PointUtils.screenToGamePoint(game, game.getMousePosition());
	}
	
	protected Point getMouseTile(){
		Point2D.Double mouse = getMouse();
		if(mouse != null)
			return new Point((int) (mouse.x / 60), (int) (mouse.y / 60));
		return null;
	}
	
	private String getObject(){
		if(placingObject == OBJECT_TILE)
			return "tile";
		if(placingObject == OBJECT_ENEMY)
			return "enemy";
		if(placingObject == OBJECT_OTHER)
			return "other";
		if(placingObject == OBJECT_START)
			return "startpoint";
		if(placingObject == OBJECT_GRAVITY)
			return "gravity";
		if(placingObject == OBJECT_FINISH)
			return "finish";
		return "none";
	}
	
	private String getObjectText(){
		String object = getObject();
		if(object.matches("tile"))
			object += ": " + Tiles.getName(Tiles.fromId(currentTile));
		if(object.matches("enemy"))
			object += ": " + (getEnemyClass() != null ? getEnemyClass().getSimpleName() : "none");
		if(object.matches("other"))
			object += ": " + (getEntityClass() != null ? getEntityClass().getSimpleName() : "none");
		if(object.matches("startpoint")){
			if(game.player.position != null)
				object += ":(x: " + (int) game.player.position.x + ", y: " + (int) game.player.position.y + ")";
			else
				object += ": none";
		}
		if(object.matches("gravity")){
			if(currentGravity == 0)
				object += ": box";
			if(currentGravity == 1)
				object += ": sphere";
		}
		if(object.matches("finish")){
			if(currentFinish == 0)
				object += ": basic flag";
			if(currentFinish == 1)
				object += ": vertical flag";
			if(currentFinish == 2)
				object += ": horizontal flag";
			if(currentFinish == 3)
				object += ": wooden door";
			if(currentFinish == 4)
				object += " finish timer";
		}
		return object;
	}

	private Class<?> getEnemyClass(){
		if(currentEnemy == 1)
			return entities.monsters.Bird.class;
		if(currentEnemy == 2)
			return entities.monsters.Warrior.class;
		if(currentEnemy == 3)
			return entities.monsters.Archer.class;
		if(currentEnemy == 4)
			return entities.monsters.Catapult.class;
		if(currentEnemy == 5)
			return entities.monsters.BattleStar.class;
		if(currentEnemy == 6)
			return entities.monsters.VoidPuncher.class;
		return null;
	}
	
	private Class<?> getEntityClass(){
		if(currentEntity == 1)
			return entities.special.Heart.class;
		if(currentEntity == 2)
			return entities.special.Coin.Yellow.class;
		if(currentEntity == 3)
			return entities.Planet.class;
		if(currentEntity == 4)
			return entities.special.VoidSpawner.class;
		return null;
	}
	
	private Entity getFinish(){
		if(currentFinish == 0)
			return new Flag(game, world, getMouse(), false, false);
		if(currentFinish == 1)
			return new Flag(game, world, getMouse(), false, true);
		if(currentFinish == 2)
			return new Flag(game, world, getMouse(), true, false);
		if(currentFinish == 3)
			return new Door(game, world, getMouse(), "wooden door", 120, 180);
		if(currentFinish == 4)
			return new FinishTimer(game, world, 15000);
		return null;
	}
}
