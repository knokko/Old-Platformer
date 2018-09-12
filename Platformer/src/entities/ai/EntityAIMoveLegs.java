package entities.ai;

import render.Model;
import entities.Entity;

public class EntityAIMoveLegs extends EntityAI {
	
	protected boolean leg;

	public EntityAIMoveLegs(Entity target) {
		super(target);
	}
	
	@Override
	public void update(){
		Model leg1 = (Model) getMethod("leg1");
		Model leg2 = (Model) getMethod("leg2");
		double mx = 1.5 * Math.abs(entity.motionX * Math.sin(Math.toRadians(entity.world.getGravityDirection(entity.position))) + entity.motionY * Math.cos(Math.toRadians(entity.world.getGravityDirection(entity.position))));
		if(entity.cooldown > 0)
			mx /= 2;
		if(!entity.onGround)
			mx /= 3;
		if(leg){
			leg1.rotation += mx;
			leg2.rotation -= mx;
		}
		else {
			leg1.rotation -= mx;
			leg2.rotation += mx;
		}
		if(leg1.rotation > 45 || leg2.rotation > 45){
			if(leg){
				leg1.rotation = 45;
				leg2.rotation = -45;
			}
			else {
				leg1.rotation = -45;
				leg2.rotation = 45;
			}
			leg = !leg;
		}
	}

}
