package controller.combatController.AIController;

import character.Character;
import controller.combatController.CombatController;
import map.Tile;

/**
 * an action type wrapper of the AI class
 * @author rroelke
 *
 */
public abstract class Action implements Comparable<Action> {
	protected CombatController _src;
	protected Character _c;
	protected Tile _destTile;
	private double _value;
	
	public Action(CombatController src, Character c, Tile t) {
		_src = src;
		_c = c;
		_destTile = t;
		_value = evaluateMove();
	}
	
	/**
	 * @return the destination tile corresponding to this action
	 */
	public Tile getTile() {
		return _destTile;
	}
	
	public double getValue() {
		return _value;
	}
	
	/**
	 * @return the value of the move corresponding to this action
	 */
	public abstract double evaluateMove();
	
	/**
	 * compares two actions
	 * @return a positive number if this action is better
	 * 		zero if they are equal
	 * 		a negative number if the other action is better
	 */
	public int compareTo(Action other) {
		System.out.println("comparing " + _value + " to " + other._value);
		if (_value < other._value)
			return 1;
		else if (_value == other._value)
			return 0;
		else
			return -1;
	}
	
	/**
	 * pushes an action to the combat controller
	 */
	public abstract void act();
}
