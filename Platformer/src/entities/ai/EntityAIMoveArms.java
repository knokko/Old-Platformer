package entities.ai;

import render.Model;
import entities.Entity;

public class EntityAIMoveArms extends EntityAI {
	
	protected boolean arm;

	public EntityAIMoveArms(Entity target) {
		super(target);
	}
	
	@Override
	public void update(){
		if(entity.cooldown <= 0){
			double mx = Math.abs(entity.motionX * Math.sin(Math.toRadians(entity.world.getGravityDirection(entity.position))) + entity.motionY * Math.cos(Math.toRadians(entity.world.getGravityDirection(entity.position)))) / 2;
			Model arm1 = (Model) getMethod("arm1");
			Model arm2 = (Model) getMethod("arm2");
			if(arm){
				arm1.rotation += mx;
				arm2.rotation -= mx;
			}
			else {
				arm1.rotation -= mx;
				arm2.rotation += mx;
			}
			if(arm1.rotation > 30 || arm2.rotation > 30){
				arm = !arm;
			}
			if(entity.moveX != 0){
				if(arm2.rotation > 30){
					arm2.rotation = 30;
					arm1.rotation = -30;
				}
				if(arm2.rotation < -30){
					arm2.rotation = -30;
					arm1.rotation = 30;
				}
				if(arm1.rotation > 30){
					arm1.rotation = 30;
					arm2.rotation = -30;
				}
				if(arm1.rotation < -30){
					arm1.rotation = -30;
					arm2.rotation = 30;
				}
			}
		}
	}
}
