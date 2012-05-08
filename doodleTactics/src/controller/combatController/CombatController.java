package controller.combatController;

import graphics.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import util.Util;

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
		CHARACTER_MOVING,
		CHARACTER_OPTION_MENU,
		SELECTING_ITEM,
		ITEM_SELECTED,
		ATTACKING,
		EVENT_OCCURRING,
		CHARACTER_OPTION_MENU_POST_EVENT;
		
		public String toString() {
			switch (this) {
			case START:
				return "START";
			case CHARACTER_SELECTED:
				return "CHARACTER_SELECTED";
			case CHARACTER_MOVING:
				return "CHARACTER_MOVING";
			case CHARACTER_OPTION_MENU:
				return "CHARACTER_OPTION_MENU";
			case SELECTING_ITEM:
				return "SELECTING_ITEM";
			case ITEM_SELECTED:
				return "ITEM_SELECTED";
			case ATTACKING:
				return "ATTACKING";
			case CHARACTER_OPTION_MENU_POST_EVENT:
				return "CHARACTER_OPTION_MENU_POST_EVENT";
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
	
	private State _state;
	
	protected boolean _cycledLeft;
	protected boolean _cycledRight;
	
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
		_state = State.START;
		
		_cycledLeft = false;
		_cycledRight = false;
	}
	
	protected class SlideTimer implements Runnable{
		private MenuItem _menu;
		private int _stop;
		public SlideTimer(MenuItem menu, int stop){
			_menu = menu;
			_stop = stop;
		}
		@Override
		public void run() {
			while(_menu.getX() > _stop){
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					//Do Nothing
				}
				_menu.setLocation(_menu.getX()  -20,_menu.getY());
				_dt.getGameScreen().repaint();
			}
		}
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
	 * @return the mapping from characters to tiles of this controller
	 */
	public HashMap<Character, Tile> getTileMappings() {
		return _locations;
	}
	
	/**
	 * sets the enemy affiliations of this combat controller
	 * @param aff
	 */
	public void setEnemyAffiliations(List<CombatController> aff) {
		_enemyAffiliations = aff;
	}
	
	/**
	 * @return all combat factions that are enemies of this one
	 */
	public List<CombatController> getEnemyAffiliations() {
		return _enemyAffiliations;
	}
	
	/**
	 * moves a character across a path
	 * @param c the character to move
	 * @param path the path across which to move the character
	 */
	public void move(Character c, Tile source, List<Tile> path) {

		Tile dest = path.get(0);
		for (int i = 0; i < path.size(); i++) {
			dest = path.get(i);
		}
		
		source.setOccupant(null);
		dest.setOccupant(c);
		
		_state = State.CHARACTER_MOVING;
		c.followPath(path);
		
		//c.setLocation(dest.getX(), dest.getY());
		_hasMoved.put(c, true);
		_locations.put(c, path.get(path.size() - 1));
		
	}
	
	/**
	 * signifies that the character has finished moving along its path
	 */
	public void moveComplete() {
		_state = State.CHARACTER_OPTION_MENU;
	}
	
	/**
	 * action corresponding to having a character wait
	 */
	public void characterWait() {
		_state = State.START;
	}
	
	@Override
	/**
	 * takes control of combat
	 * for this particular controller type, represents the beginning of a new turn
	 */
	public void take() {
		super.take();
		
		if (_state != State.ATTACKING && _state != State.EVENT_OCCURRING) {
			_hasMoved.clear();
			_unitCycle = _units.listIterator();
			_cycledLeft = false;
			_cycledRight = false;
		}
		
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
	
	public List<Character> enemyUnits() {
		if (_orch == null)
			return new ArrayList<Character>();
		
		List<Character> enemies = new ArrayList<Character>();
		for (CombatController aff : _enemyAffiliations)
			enemies.addAll(aff.getUnits());
		
		return enemies;
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
			if (!hasNext) {
				wrap = false;
				_unitCycle = _units.listIterator();
			}
			else {
				c = _unitCycle.next();
				if (!hasMoved(c)) {
					_cycledRight = true;
					return c;
				}
				if (!wrap && c == wrapPoint)
					return null;
			}
		}
		
		return null;
	}
	
	/**
	 * @return the previous unit in the controller that has not yet moved
	 * (equivalent to nextUnit() but cycles in the opposite direction)
	 */
	public Character previousUnit() {
		boolean wrap;
		Character wrapPoint;
		
		if (_unitCycle.hasPrevious()) {
			wrap = true;
			wrapPoint = _unitCycle.previous();
			if (!hasMoved(wrapPoint))
				return wrapPoint;
		}
		else {
			wrap = false;
			wrapPoint = null;
			_unitCycle = _units.listIterator(_units.size());
		}
		
		boolean hasPrevoius;
		Character c;
		while ((hasPrevoius = _unitCycle.hasPrevious()) || wrap) {
			if (!hasPrevoius) {
				wrap = false;
				_unitCycle = _units.listIterator(_units.size());
			}
			else {
				c = _unitCycle.previous();
				if (!hasMoved(c)) {
					_cycledLeft = true;
					return c;
				}
				if (!wrap && c == wrapPoint)
					return null;
			}
		}
		
		return null;
	}
	
	/**
	 * causes one character to attack another
	 * @param src offense
	 * @param dest defense
	 */
	public void attack(Tile src, Tile dest) {
		_state = State.ATTACKING;
		
		int xDiff = src.x()-dest.x();
		int yDiff = src.y()-dest.y();
								
		if (Math.abs(xDiff) > Math.abs(yDiff)) {
			if (xDiff > 0) {
				src.getOccupant().setLeft();
			}
			else {
				src.getOccupant().setRight();
			}
		}
		else {
			if (yDiff > 0) {
				src.getOccupant().setUp();
			}
			else {
				src.getOccupant().setDown();
			}
		}
		
		System.out.println("DOING THE ANIMATION " + _state);
		
		_gameScreen.pushControl(new CombatWindowController(_dt, src, dest));
		
	/*	src.attack(dest, r);
		System.out.println(src.getName() + " has " + src.getHP() + " HP remaining.");
		System.out.println(dest.getName() + " has " + dest.getHP() + " HP remaining.");
		
		if (isDefeated())
			defeat();
		else if (dest.getAffiliation().isDefeated()) {
			dest.getAffiliation().defeat();
		}	*/
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
	
	public State getState() {
		return _state;
	}
	public void setState(State st) {
		_state = st;
	}
	
	/**
	 * @param c a character
	 * @return the tile associated with the given character
	 */
	public Tile getTile(Character c) {
		return _locations.get(c);
	}
	
	/**
	 * adds units to this combat controller
	 * @param newUnits the new units to add
	 */
	public void addUnits(HashMap<Character, Tile> newUnits) {
		for (Character c : newUnits.keySet())
			if (!_units.contains(c)) {
				_units.add(c);
				_locations.put(c, newUnits.get(c));
				c.setAffiliation(this);
			}
	}
}