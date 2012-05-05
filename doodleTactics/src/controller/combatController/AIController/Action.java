package controller.combatController.AIController;

import controller.combatController.CombatController;
import map.Tile;

/**
 * an action type wrapper of the AI class
 * @author rroelke
 *
 */
public abstract class Action implements Comparable<Action> {
	protected CombatController _src;
	protected Tile _destTile;
	private int _value;
	
	public Action(CombatController src, Tile t) {
		_src = src;
		_destTile = t;
		_value = evaluateMove();
	}
	
	/**
	 * @return the value of the move corresponding to this action
	 */
	public abstract int evaluateMove();
	
	/**
	 * compares two actions
	 * @return a positive number if this action is better
	 * 		zero if they are equal
	 * 		a negative number if the other action is better
	 */
	public int compareTo(Action other) {
		return _value - other._value;
	}
	
	/**
	 * pushes an action to the combat controller
	 */
	public abstract void act();
}
