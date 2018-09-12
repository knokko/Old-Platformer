package entities.ai;

import entities.Entity;

public abstract class EntityAI {
	
	public Entity entity;

	public EntityAI(Entity target) {
		entity = target;
	}
	
	public void update(){}
	
	protected Object getMethod(String method){
		try {
			return entity.getClass().getMethod(method).invoke(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	protected Object getMethod(String method, Class<?>[] classParameters, Object... parameters){
		try {
			return entity.getClass().getMethod(method, classParameters).invoke(entity, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
}
