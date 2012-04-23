package controller;

import java.util.HashMap;
import java.util.List;

import main.DoodleTactics;
import main.GameScreen;
import character.Character;
import map.*;

/**
 * 
 * @author rroelke
 * abstract interface for regulating combat
 */
public abstract class CombatController extends GameScreenController {
	protected List<Character> _units;
	protected GameScreen _gameScreen;
	protected HashMap<Character, Boolean> _hasMoved;
	
	protected Map _map;
	
	protected List<CombatController> _enemyAffiliations;
	
	public CombatController(DoodleTactics dt) {
		super(dt);
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
}
