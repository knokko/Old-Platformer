package entities.ai;

import render.Model;
import entities.Entity;

public class EntityAIBattleStarAttack extends EntityAI {
	
	public Entity target;
	
	public int range;

	public EntityAIBattleStarAttack(Entity attacker, Entity target, int range) {
		super(attacker);
		this.target = target;
		this.range = range;
	}
	
	public EntityAIBattleStarAttack(Entity attacker, Entity target){
		this(attacker, target, 0);
	}
	
	@Override
	public void update(){
		if(entity.getCollider().distanceTo(target.getCollider()) <= range){
			target.attack(entity.position, (Integer) getMethod("getPower"));
			((Model)getMethod("body")).rotation += 5;
		}
	}
}
