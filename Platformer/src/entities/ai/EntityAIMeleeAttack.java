package entities.ai;

import render.Model;
import entities.Entity;

public class EntityAIMeleeAttack extends EntityAI {
	
	public Entity target;
	public int range;
	public boolean status;

	public EntityAIMeleeAttack(Entity attacker, Entity enemy, int requiredRange) {
		super(attacker);
		target = enemy;
		range = requiredRange;
	}
	
	@Override
	public void update(){
		if(entity.position.x > target.position.x)
			entity.facingLeft = true;
		else
			entity.facingLeft = false;
		if(entity.getCollider().distanceTo(target.getCollider()) <= range){
			Model arm = (Model) getMethod("arm2");
			int speed = (Integer) getMethod("swingSpeed");
			if(!status){
				if(arm.rotation < 50 + speed && arm.rotation > 50 - speed){
					arm.rotation = 50;
					target.attack(entity.position, (Integer) getMethod("getPower"));
					status = true;
				}
				else if(arm.rotation > 50)
					arm.rotation -= speed;
				else
					arm.rotation += speed;
			}
			else if(status){
				if(arm.rotation > -50){
					arm.rotation -= speed;
				}
				else {
					target.attack(entity.position, (Integer) getMethod("getPower"));
					status = false;
				}
			}
		}
		else
			status = false;
	}
}
