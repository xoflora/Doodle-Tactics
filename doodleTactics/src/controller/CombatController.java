package controller;

import java.util.List;

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
	
	protected Map _map;
	
	public void move(Character c, List<Tile> path){
		//TODO draw the character moving along the path of tiles
	}
	
	@Override
	public void take() {
		// TODO
	}
	
	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param c the character to check
	 * @return whether the given character is affiliated with this controller
	 */
	public boolean isAffiliated(Character c) {
		return c.getAffiliation() == this;
	}
}
