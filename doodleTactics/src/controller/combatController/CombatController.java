package controller.combatController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import controller.GameScreenController;

import main.DoodleTactics;
import character.Character;
import map.*;

/**
 * 
 * @author rroelke
 * abstract interface for regulating combat
 */
public abstract class CombatController extends GameScreenController {
	protected List<Character> _units;
	private ListIterator<Character> _unitCycle;
	protected HashMap<Character, Boolean> _hasMoved;
	
	protected Map _map;
	
	protected List<CombatController> _enemyAffiliations;
	
	public CombatController(DoodleTactics dt, List<Character> units) {
		super(dt);
		
		_units = units;
		_enemyAffiliations = new ArrayList<CombatController>();
		
		for (Character c : _units)
			c.affiliate(this);
	}
	
	/**
	 * @param c the character to check
	 * @return whether the indicated character has moved yet this turn
	 */
	public boolean hasMoved(Character c) {
		Boolean m = _hasMoved.get(c);
		return m != null && m;
	}
	
	/**
	 * sets the enemy affiliations of this combat controller
	 * @param aff
	 */
	public void setEnemyAffiliations(List<CombatController> aff) {
		_enemyAffiliations = aff;
	}
	
	/**
	 * moves a character across a path
	 * @param c the character to move
	 * @param path the path across which to move the character
	 */
	public void move(Character c, List<Tile> path){
		//TODO draw the character moving along the path of tiles
		
		_hasMoved.put(c, true);
	}
	
	@Override
	/**
	 * takes control of combat
	 * for this particular controller type, represents the beginning of a new turn
	 */
	public abstract void take();
	
	@Override
	/**
	 * releases control of combat
	 * for this particular controller type, represents the end of a turn (cleanup step)
	 */
	public abstract void release();
	
	/**
	 * @param c the character to check
	 * @return whether the given character is affiliated with this controller
	 */
	public boolean isAffiliated(Character c) {
		return c.getAffiliation() == this;
	}
	
	/**
	 * @return the next unit for the controller that has not yet moved this turn
	 */
	public Character nextUnit() {
		boolean wrap;
		Character wrapPoint;
		
		if (_unitCycle.hasNext()) {
			wrap = true;
			wrapPoint = _unitCycle.next();
			if (!hasMoved(wrapPoint))
				return wrapPoint;
		}
		else {
			wrap = false;
			wrapPoint = null;
			_unitCycle = _units.listIterator();
		}
		
		boolean hasNext;
		Character c;
		while ((hasNext = _unitCycle.hasNext()) || wrap) {
			c = _unitCycle.next();
			if (!hasMoved(c))
				return c;
			if (!wrap && c == wrapPoint)
				return null;
			if (!hasNext && wrap) {
				wrap = false;
				_unitCycle = _units.listIterator();
			}
		}
		
		return null;
	}
}
