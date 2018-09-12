package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import actions.Gadget;
import actions.Spell;
import particles.Particle;
import colliders.Collider;
import entities.*;
import entities.finish.*;
import entities.monsters.*;
import tiles.Tile;
import tiles.Tiles;
import world.gravity.IGravity;
import main.Game;

public class World {
	
	public final Game game;
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Entity> monsters = new ArrayList<Entity>();
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<TileEntity> tileEntities = new ArrayList<TileEntity>();
	public ArrayList<IGravity> gravityFields = new ArrayList<IGravity>();
	public ArrayList<IFinish> finishes = new ArrayList<IFinish>();
	
	protected byte[][] tiles;
	
	public Color backGroundColor;
	
	public double gravity;
	public double gravityDirection;
	public double airFriction;
	public double spawnX;
	public double spawnY;
	protected int width;
	protected int height;
	
	private boolean[] allowedGadgets = new boolean[Gadget.values().length];
	private boolean[] allowedSpells = new boolean[Spell.values().length];
	
	public World(Game game, int worldWidth, int worldHeight) {
		this.game = game;
		width = worldWidth;
		height = worldHeight;
		tiles = new byte[width()][height()];
		gravity = 9.8;
		gravityDirection = 270;
		airFriction = 1;
		backGroundColor = new Color(0, 0, 200);
		Arrays.fill(allowedGadgets, true);
		Arrays.fill(allowedSpells, true);
	}
	
	public void paint(Graphics gr){
		gr.setColor(backGroundColor);
		gr.fillRect(0, 0, game.getWidth(), game.getHeight());
		int t = 0;
		while(t < gravityFields.size()){
			gravityFields.get(t).paint(gr);
			++t;
		}
		int x = 0;
		while(x < tiles.length){
			int y = 0;
			while(y < tiles[x].length){
				Tiles.fromId(tiles[x][y]).paint(gr, x, y);
				++y;
			}
			++x;
		}
		t = 0;
		while(t < entities.size()){
			entities.get(t).paintIfInRange(gr);
			++t;
		}
		t = 0;
		while(t < particles.size()){
			particles.get(t).paint(gr);
			++t;
		}
	}
	
	public void update(){
		if(checkFinish())
			return;
		int t = 0;
		while(t < tileEntities.size()){
			if(tileEntities.get(t).inactive)
				tileEntities.remove(t);
			++t;
		}
		t = 0;
		while(t < entities.size()){
			if(entities.get(t).inactive)
				entities.remove(t);
			else
				entities.get(t).updateIfInRange();
			++t;
		}
		t = 0;
		while(t < monsters.size()){
			if(monsters.get(t).inactive)
				monsters.remove(t);
			++t;
		}
		t = 0;
		while(t < particles.size()){
			if(particles.get(t).inactive)
				particles.remove(t);
			else
				particles.get(t).update();
			++t;
		}
	}
	
	public Tile getTile(double x, double y){
		return getTile((int)x, (int)y);
	}
	
	public Tile getTile(int x, int y){
		try {
			return Tiles.fromId(tiles[x / 60][y / 60]);
		}catch(Exception ex){
			return Tiles.air;
		}
	}
	
	public ArrayList<Collider> getColliders(Collider collider){
		ArrayList<Collider> colliders = new ArrayList<Collider>();
		double x = collider.minX();
		while(x <= collider.maxX()){
			double y = collider.minY();
			while(y <= collider.maxY()){
				Collider c = getTile(x, y).getCollider(x, y);
				if(!colliders.contains(c))
					colliders.add(c);
				y += 60;
				if(y > collider.maxY() && y < collider.maxY() + 60){
					y = collider.maxY();
				}
			}
			x += 60;
			if(x > collider.maxX() && x < collider.maxX() + 60){
				x = collider.maxX();
			}
		}
		int t = 0;
		while(t < tileEntities.size()){
			if(tileEntities.get(t).getCollider().hit(collider))
				colliders.add(tileEntities.get(t).getCollider());
			++t;
		}
		return colliders;
	}
	
	public int width(){
		return width;
	}
	
	public int height(){
		return height;
	}
	
	public void spawnEntity(Entity entity){
		entities.add(entity);
		if(entity instanceof Monster)
			monsters.add(entity);
		if(entity instanceof TileEntity)
			tileEntities.add((TileEntity) entity);
		if(entity instanceof IFinish)
			finishes.add((IFinish) entity);
	}
	
	public Point2D.Double spawnPoint(){
		return new Point2D.Double(spawnX, spawnY);
	}
	
	public void setTile(int x, int y, Tile tile){
		try {
			tiles[x / 60][y / 60] = tile.id;
		} catch(Exception ex){}
	}
	
	protected void setTile(Tile tile, int x, int y){
		try {
			tiles[x][y] = tile.id;
		} catch(Exception ex){}
	}
	
	protected void fillTiles(Tile tile, int minX, int minY, int maxX, int maxY){
		try {
			int x = minX;
			while(x <= maxX){
				int y = minY;
				while(y <= maxY){
					tiles[x][y] = tile.id;
					++y;
				}
				++x;
			}
		} catch(Exception ex){}
	}
	
	public void build(){
		game.player.setSpell("arm");
		game.player.setWeapon("arm");
	}
	
	public double getAirFriction(Point2D.Double position) {
		return airFriction;
	}
	
	public double getGravity(Point2D.Double position){
		IGravity g = getGravityField(position);
		if(g != null)
			return g.getAccelleration(position);
		return gravity;
	}
	
	public double getGravityDirection(Point2D.Double position){
		IGravity g = getGravityField(position);
		if(g == null)
			return gravityDirection;
		double dir = g.getRotation(position);
		if(dir > 360)
			dir -= 360;
		if(dir < 0)
			dir += 360;
		return dir;
	}
	
	public IGravity getGravityField(Point2D.Double position){
		int t = gravityFields.size() - 1;
		while(t >= 0){
			IGravity g = gravityFields.get(t);
			if(g.getCollider().isInCollider(position))
				return g;
			--t;
		}
		return null;
	}
	
	public boolean checkFinish(){
		int t = 0;
		while(t < finishes.size()){
			if(finishes.get(t).isFinished()){
				game.finishLevel();
				return true;
			}
			++t;
		}
		return false;
	}
	
	public boolean isAllowed(Gadget gadget){
		return allowedGadgets[gadget.ordinal()];
	}
	
	public boolean isAllowed(Spell spell){
		return allowedSpells[spell.ordinal()];
	}
}
