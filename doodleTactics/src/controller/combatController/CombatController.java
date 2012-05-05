package controller.combatController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

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
	
	protected enum State {
		START,
		CHARACTER_SELECTED,
		CHARACTER_OPTION_MENU,
		SELECTING_ITEM,
		ITEM_MENU;
		
		public String toString() {
			switch (this) {
			case START:
				return "START";
			case CHARACTER_SELECTED:
				return "CHARACTER_SELECTED";
			case CHARACTER_OPTION_MENU:
				return "CHARACTER_OPTION_MENU";
			case SELECTING_ITEM:
				return "SELECTING_ITEM";
			case ITEM_MENU:
				return "ITEM_MENU";
			default:
				return "";
			}
		}
	}
	
	protected List<Character> _units;
	private ListIterator<Character> _unitCycle;
	protected HashMap<Character, Boolean> _hasMoved;
	protected HashMap<Character, Tile> _locations;
	protected CombatOrchestrator _orch;
	
	protected List<CombatController> _enemyAffiliations;
	
	private Random r;
	
	public CombatController(DoodleTactics dt, HashMap<Character, Tile> units) {
		super(dt);
		
		_locations = units;
		_units = new ArrayList<Character>();
		for (Character c : _locations.keySet())
			_units.add(c);
		
		_enemyAffiliations = new ArrayList<CombatController>();
		_hoveredTile = null;
		_hasMoved = new HashMap<Character, Boolean>();
		_locations = units;
		
		r = new Random();
		
		for (Character c : _units)
			c.affiliate(this);
		
		_orch = null;
	}
	
	/**
	 * @param c a character
	 * @return whether the given character is affiliated with this combat controller
	 */
	public boolean containsCharacter(Character c) {
		return _units.contains(c);
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
	 * @return the list of units affiliated with this combat controller
	 */
	public List<Character> getUnits() {
		return _units;
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
	public void move(Character c, Tile source, List<Tile> path) {
		//TODO draw the character moving along the path of tiles instead of just switching

		Tile dest = path.get(0);
		for (int i = 0; i < path.size(); i++) {
			dest = path.get(i);
		}
		
		source.setOccupant(null);
		dest.setOccupant(c);
		
		c.followPath(path);
		
		//c.setLocation(dest.getX(), dest.getY());
		_hasMoved.put(c, true);
		_locations.put(c, path.get(path.size() - 1));
	}
	
	@Override
	/**
	 * takes control of combat
	 * for this particular controller type, represents the beginning of a new turn
	 */
	public void take() {
		super.take();
		_hasMoved.clear();
		
		if (_orch == null)
			_dt.error("Combat error: orchestrator unassigned.");
	}
	
	@Override
	/**
	 * releases control of combat
	 * for this particular controller type, represents the end of a turn (cleanup step)
	 */
	public void release() {
		super.release();
	}
	
	/**
	 * @param c the character to check
	 * @return whether the given character is affiliated with this controller
	 */
	public boolean isAffiliated(Character c) {
		return c.getAffiliation() == this;
	}
	
	/**
	 * checks whether the given character is an enemy of this controller
	 * @param c the character to check
	 * @return whether the character is an enemy of this controller
	 */
	public boolean isEnemy(Character c) {
		if (c == null)
			return false;
		for (CombatController affiliation : _enemyAffiliations)
			if (affiliation.isAffiliated(c))
				return true;
		return false;
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
	
	/**
	 * causes one character to attack another
	 * @param src offense
	 * @param dest defense
	 */
	public void attack(Character src, Character dest) {
		src.attack(dest, r);
		System.out.println(src.getName() + " has " + src.getHP() + " HP remaining.");
		System.out.println(dest.getName() + " has " + dest.getHP() + " HP remaining.");
		
		if (isDefeated())
			defeat();
		else if (dest.getAffiliation().isDefeated()) {
			dest.getAffiliation().defeat();
		}
	}
	
	/**
	 * removes a unit from this combat controller; it can no longer be used in the battle
	 */
	public void removeUnit(Character c) {
		System.out.println("removeing" + c + " from tile " + _locations.get(c));
		_units.remove(c);
		_gameScreen.removeCharacter(c);
		System.out.println(_locations == null);
		System.out.println(c == null);
		System.out.println(_locations.get(c) == null);
		_locations.get(c).setOccupant(null);
		_locations.remove(c);
	///	if (t.occupant() == c)
	//		t.setOccupant(null);
	}
	
	/**
	 * sets the orchestrator of this combat controller
	 * @param o
	 */
	public void setOrchestrator(CombatOrchestrator o) {
		_orch = o;
	}
	
	/**
	 * @return whether or not this combat controller has any units remaining
	 */
	public boolean isDefeated() {
		return _units.isEmpty();
	}
	
	/**
	 * defeat action of this combat controller
	 * calls whatever events or other functions are appropriate
	 */
	public void defeat() {
		System.out.println("defeated");
	}
}