package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import main.Game;

public final class Utils {
	
	public static final short[] BYTES = new short[]{16384,8192,4096,2048,1024,512,256,128,64,32,16,8,4,2,1};

	public static final Image getImage(String url){
		return new ImageIcon(Utils.class.getClassLoader().getResource(url)).getImage();
	}
	
	public static final BufferedImage getBuffImage(String url){
		try {
			return ImageIO.read(Utils.class.getClassLoader().getResource(url.toLowerCase()));
		} catch (Exception e) {
			e.printStackTrace(Game.console);
			Game.console.println("url = " + url);
			return null;
		}
	}
	
	public static final ArrayList<File> getLevels(){
		ArrayList<File> levels = getBasicLevels();
		levels.addAll(getCustomLevels());
		return levels;
	}
	
	public static final ArrayList<File> getBasicLevels(){
		try {
			ArrayList<File> levels = new ArrayList<File>();
			try {
				JarFile jar = new JarFile(new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()));
				Game.console.println("jar = " + jar);
				Enumeration<JarEntry> entries = jar.entries();
		    	while(entries.hasMoreElements()) {
		        	final String name = entries.nextElement().getName();
		        	if (name.startsWith("levels/") && name.length() > 7) {
		        		File file = new File(Utils.class.getClassLoader().getResource(name).getFile());
		            	Game.console.println("file = " + file);
		            	levels.add(file);
		        	}
		    	}
				jar.close();
			} catch(Exception ex){
				File basic = new File(Utils.class.getClassLoader().getResource("levels/").getFile());
				Game.console.println("basic: " + basic + " basic.isDirectory = " + basic.isDirectory());
				int t = 0;
				while(t < basic.listFiles().length){
					levels.add(basic.listFiles()[t]);
					++t;
				}
			}
			//TODO doesn't work outside eclipse...
			
			return levels;
		} catch(Exception ex){
			ex.printStackTrace(Game.console);
			return null;
		}
	}
	
	public static final ArrayList<File> getCustomLevels(){
		try {
			ArrayList<File> levels = new ArrayList<File>();
			File basic = new File("custom levels");
			if(!basic.exists())
				createCustomLevelsFolder();
			basic = new File("custom levels");
			int t = 0;
			while(t < basic.listFiles().length){
				levels.add(basic.listFiles()[t]);
				++t;
			}
			return levels;
		} catch(Exception ex){
			ex.printStackTrace(Game.console);
			return null;
		}
	}
	
	public static void createCustomLevelsFolder(){
		new File("custom levels").mkdir();
	}
	
	public static final double cutDecimals(double d, int maxDecimals){
		String s = d + "";
		if(s.contains(".") && !s.contains("E")){
			int i = s.indexOf(".");
			d = Double.parseDouble(s.substring(0, Math.min(i + 3, s.length())));
		}
		return d;
	}
	
	public static boolean[] byteToBinair(byte b){
		boolean[] bools = new boolean[8];
		if(b >= 0)
			bools[7] = true;
		else {
			b++;
			b *= -1;
		}
		byte t = 8;
		while(t < BYTES.length){
			if(b >= BYTES[t]){
				b -= BYTES[t];
				bools[t - 8] = true;
			}
			++t;
		}
		return bools;
	}
	
	public static boolean[] shortToBinair(short s){
		boolean[] bools = new boolean[16];
		if(s >= 0)
			bools[BYTES.length] = true;
		else {
			s++;
			s *= -1;
		}
		byte t = 0;
		while(t < BYTES.length){
			if(s >= BYTES[t]){
				s -= BYTES[t];
				bools[t] = true;
			}
			++t;
		}
		return bools;
	}
	
	public static short shortFromBinair(boolean... bools){
		short s = 0;
		int t = 0;
		while(t < BYTES.length){
			if(bools[t])
				s += BYTES[t];
			++t;
		}
		if(!bools[15]){
			s *= -1;
			s--;
		}
		return s;
	}
	
	public static byte byteFromBinair(boolean... bools){
		byte b = 0;
		int t = 8;
		while(t < BYTES.length){
			if(bools[t - 8])
				b += BYTES[t];
			++t;
		}
		if(!bools[7]){
			b *= -1;
			b--;
		}
		return b;
	}
	
	public static byte[] shortToBytes(short s){
		boolean[] bools = shortToBinair(s);
		return new byte[]{byteFromBinair(Arrays.copyOfRange(bools, 0, 8)), byteFromBinair(Arrays.copyOfRange(bools, 8, 16))};
	}
	
	public static short bytesToShort(byte... bytes){
		boolean[] bools1 = byteToBinair(bytes[0]);
		boolean[] bools2 = byteToBinair(bytes[1]);
		boolean[] bools = new boolean[16];
		System.arraycopy(bools1, 0, bools, 0, 8);
		System.arraycopy(bools2, 0, bools, 8, 8);
		return shortFromBinair(bools);
	}
}
