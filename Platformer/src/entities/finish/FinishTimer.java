package entities.finish;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import main.Game;
import util.Factors;
import world.World;
import colliders.Collider;
import colliders.ColliderNull;
import entities.Entity;

public class FinishTimer extends Entity implements IFinish {
	
	public int timer;

	public FinishTimer(Game game, World world, int time) {
		super(game, world, new Point2D.Double());
		timer = time;
	}
	
	public boolean isFinished() {
		return timer <= 0;
	}
	
	@Override
	public void update(){
		if(timer > 0)
			--timer;
	}
	
	@Override
	public Collider getCollider() {
		return new ColliderNull();
	}
	
	@Override
	public void paint(Graphics gr){
		gr.setFont(new Font("TimesRoman", 1, 30));
		gr.setColor(Color.GREEN);
		gr.drawString("Finish Timer: " + (timer / 50), Factors.factorX(43), Factors.factorY(24));
		gr.setColor(Color.YELLOW);
		gr.drawRect(Factors.factorX(40), Factors.factorY(20), Factors.factorX(20), Factors.factorY(5));
	}
	
	@Override
	public double getUpdateRange(){
		return Double.POSITIVE_INFINITY;
	}

}
