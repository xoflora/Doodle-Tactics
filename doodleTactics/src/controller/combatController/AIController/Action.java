package controller.combatController.AIController;

import java.util.List;

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
	
	
	/**
	 * @return a defensive evaluation of the destination tile
	 */
	public double defensiveEval(List<Character> filter) {
		double eval = 0;
		for (CombatController aff : _src.getEnemyAffiliations())
			for (Character c : aff.getUnits()) {				
				if (_src.isEnemy(c) && !filter.contains(c) &&
						_src.getScreen().getMap().getAttackRange(aff.getTileMappings().get(c),
						c.getMovementRange(), c.getMinAttackRange(),
						c.getMaxAttackRange()).contains(_destTile)) {
					int defense = _c.getFullDefense() + _destTile.getDefense();
					if (c.getHitChance(_c) > 30 && c.getFullAttackStrength() > defense)
						if (c.getCriticalChance(_c) > 30)
							eval -= Character.CRITICAL_MULTIPLIER*
								(c.getFullAttackStrength() - defense);
						else
							eval -= c.getFullAttackStrength() - defense;
					eval += defense;
				}
			}
		return eval;
	}
}
