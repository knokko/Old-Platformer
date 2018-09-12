package entities.ai;

import entities.Entity;

public class EntityAIRangedAttack extends EntityAI {
	
	public final Entity target;
	public final int range;
	public final int baseCooldown;
	public int cooldown;

	public EntityAIRangedAttack(Entity attacker, Entity attackTarget, int requiredRange, int fireCooldown) {
		super(attacker);
		target = attackTarget;
		range = requiredRange;
		cooldown = baseCooldown = fireCooldown;
	}
	
	@Override
	public void update(){
		if(entity.position.x > target.position.x)
			entity.facingLeft = true;
		else
			entity.facingLeft = false;
		if(cooldown <= 0){
			if(entity.getCollider().distanceTo(target.getCollider()) <= range){
				entity.world.spawnEntity((Entity) getMethod("projectile", new Class[]{Entity.class}, target));
				entity.cooldown = cooldown = baseCooldown;
			}
		}
		else
			--cooldown;
	}
}
