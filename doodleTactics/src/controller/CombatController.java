package controller;

import java.util.HashMap;
import java.util.List;

import main.DoodleTactics;
import main.GameScreen;
import map.*;
import character.Character;

/**
 * 
 * @author rroelke
 * abstract interface for regulating combat
 */
public abstract class CombatController extends GameScreenController {
	protected List<Character> _characters;
	protected GameScreen _gameScreen;
	protected HashMap<Character, Boolean> _moved;
	
	protected Map _map;
	
	public CombatController(DoodleTactics dt) {
		super(dt);
	}
	
	public void move(Character c, List<Tile> path){
		//TODO draw the character moving along the path of tiles
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
