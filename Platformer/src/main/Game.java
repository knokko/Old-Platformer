package main;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JFrame;

import entities.Entity;
import entities.Player;
import entities.monsters.Catapult;
import gui.Gui;
import gui.GuiLevelSelect;
import gui.GuiMainMenu;
import world.GuiWorldCreator;
import world.World;

public class Game extends JFrame {

	private static final long serialVersionUID = 4192979835196130185L;
	public static final Game game = new Game();
	
	public final Board board;
	public final Controller controller;
	public final Player player;
	
	public World world;
	public Gui gui;
	public GuiWorldCreator exitGui;
	public static PrintWriter console;
	public Point2D.Double camera = new Point2D.Double(800, 450);
	
	public double zoom = 1;
	public boolean pause;
	
	private long ticks;
	private long updateTime;
	private long renderTime;

	public static void main(String[] args){
		game.init();
		game.run();
	}
	
	public Game(){
		board = new Board(this);
		player = new Player(this, null, null);
		controller = new Controller(this, player);
	}
	
	public void init(){
		add(board);
		setUndecorated(true);
		setSize(1600, 900);
		setTitle("platformer");
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addKeyListener(controller);
		addMouseListener(controller);
		addMouseMotionListener(controller);
		addMouseWheelListener(controller);
		openGui(new GuiMainMenu(this));
		try {
			console = new PrintWriter("console.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void buildWorld(World world){
		double g;
		if(this.world != null)
			g = this.world.gravity;
		else
			g = Double.NaN;
		this.world = world;
		player.position = world.spawnPoint();
		player.world = world;
		player.health = 100;
		player.motionX = 0;
		player.motionY = 0;
		openGui(null);
		if(world.gravity != g)
			Catapult.updateDirectionTable(this, world);
		world.build();
	}
	
	public void backToMenu(){
		if(exitGui != null){
			openGui(exitGui);
			exitGui.recover();
			exitGui = null;
		}
		else {
			if(gui instanceof GuiLevelSelect)
				openGui(new GuiMainMenu(this));
			if(gui instanceof GuiWorldCreator && !((GuiWorldCreator) gui).config){
				((GuiWorldCreator) gui).saveLevel();
				openGui(new GuiMainMenu(this));
			}
			if(gui == null)
				openGui(new GuiMainMenu(this));
		}
		world = null;
	}
	
	public void openGui(Gui guiToOpen){
		gui = guiToOpen;
		pause = gui != null;
	}
	
	public void run(){
		try {
			while(true){
				long startTime = System.nanoTime();
				long endTime = 20000000 + startTime;
				update();
				long l = System.nanoTime();
				long extraUpdateTime = (l - startTime) / 1000;
				board.repaint();
				long extraRenderTime = (System.nanoTime() - l) / 1000;
				long skipTime = (endTime - System.nanoTime()) / 1000000;
				if(skipTime > 0)
					Thread.sleep(skipTime);
				else {
					System.out.println("Game is lagging... skipTime = " + skipTime);
					updateTime += extraUpdateTime;
					renderTime += extraRenderTime;
					ticks++;
				}
			}
		} catch(Exception ex){
			ex.printStackTrace(console);
		}
	}
	
	public void update(){
		controller.update();
		if(!pause && world != null){
			player.update();
			if(world == null)
				return;
			world.update();
		}
		if(gui != null)
			gui.update();
	}
	
	@Override
	public void dispose(){
		int t = 0;
		long total = 0;
		while(t < 1000){
			total += Entity.timeResults[1][t];
			++t;
		}
		total /= 1000;
		System.out.println("average player ai time = " + total);
		total = 0;
		t = 0;
		while(t < 1000){
			total += Entity.timeResults[2][t];
			++t;
		}
		total /= 1000;
		System.out.println("average warrior ai time = " + total);
		total = 0;
		t = 0;
		while(t < 1000){
			total += Entity.timeResults[3][t];
			++t;
		}
		total /= 1000;
		System.out.println("average archer ai time = " + total);
		total = 0;
		t = 0;
		while(t < 1000){
			total += Entity.timeResults[4][t];
			++t;
		}
		total /= 1000;
		System.out.println("average catapult ai time = " + total);
		if(ticks > 0){
			System.out.println("average update time was " + (updateTime / ticks) + " microseconds");
			System.out.println("average render time was " + (renderTime / ticks) + " microseconds");
		}
		console.close();
		System.exit(0);
	}

	public void finishLevel() {
		backToMenu();
	}
}
