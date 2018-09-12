package tiles;

import java.lang.reflect.Field;
import java.util.ArrayList;

public final class Tiles {
	
	static final ArrayList<Tile> tiles = new ArrayList<Tile>();
	
	public static final TileAir air = new TileAir();
	public static final Tile grass = new Tile("grass");
	public static final Tile dirt = new Tile("dirt");
	public static final Tile red = new Tile("red");
	public static final Tile stonebrick = new Tile("stone brick");
	public static final Tile redcarpet = new Tile("red carpet");
	public static final TileTriangle stonestair1 = new TileTriangle("stone stair 1", 0, 0, 60, 30, true);
	public static final TileTriangle stonestair2 = new TileTriangle("stone stair 2", 0, 30, 60, 60, true);
	public static final Tile stonebrickcon = new Tile("stone brick con");
	public static final TileTriangle stonestair1r = new TileTriangle("stone stair 1r", 0, 0, 60, 30, false);
	public static final TileTriangle stonestair2r = new TileTriangle("stone stair 2r", 0, 30, 60, 60, false);
	public static final Tile stonebrickconr = new Tile("stone brick conr");
	public static final Tile gray = new Tile("gray");
	public static final Tile purpleSphere = new TileSphere("purple sphere", 30);
	public static final Tile blue = new Tile("blue");
	
	public static final Tile fromId(int id){
		return tiles.get(id) != null ? tiles.get(id) : air;
	}
	
	public static final int tiles(){
		return tiles.size();
	}
	
	public static final String getName(Tile tile){
		try {
			Field[] fields = Tiles.class.getFields();
			int t = 0;
			while(t < fields.length){
				if(tile == fields[t].get(null))
					return fields[t].getName();
				++t;
			}
		}catch(Exception ex){}
		return null;
	}
}
