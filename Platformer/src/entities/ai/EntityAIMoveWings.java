package entities.ai;

import render.Model;
import entities.Entity;

public class EntityAIMoveWings extends EntityAI {
	
	protected boolean wing;

	public EntityAIMoveWings(Entity target) {
		super(target);
	}
	
	@Override
	public void update(){
		Model wing1 = (Model) getMethod("wing1");
		Model wing2 = (Model) getMethod("wing2");
		double mx = Math.hypot(entity.motionX > 0 ? entity.motionX : -entity.motionX, entity.motionY > 0 ? entity.motionY : -entity.motionY) / 2;
		if(entity.cooldown > 0)
			mx /= 2;
		if(wing){
			wing1.rotation += mx;
			wing2.rotation -= mx;
		}
		else {
			wing1.rotation -= mx;
			wing2.rotation += mx;
		}
		int dis = 10;
		if(wing1.rotation > dis || wing2.rotation > dis){
			if(wing){
				wing1.rotation = dis;
				wing2.rotation = -dis;
			}
			else {
				wing1.rotation = -dis;
				wing2.rotation = dis;
			}
			wing = !wing;
		}
	}
}
