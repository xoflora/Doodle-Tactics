package controller.combatController.player;

public interface CombatMenu {
	
	/**
	 * adds this menu and all associated components to the gameScreen drawing queue
	 */
	public abstract void addToDrawingQueue();
	
	/**
	 * removes this menu and all associated components from the drawing queue
	 */
	public abstract void removeFromDrawingQueue();
	
}
