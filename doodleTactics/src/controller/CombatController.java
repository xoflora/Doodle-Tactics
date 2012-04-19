package controller;

import java.util.List;

import main.GameScreen;
import map.*;

/**
 * 
 * @author rroelke
 *
 */
public abstract class CombatController extends Controller {
	protected List<Character> _characters;
	protected GameScreen _gameScreen;
	
	protected Map _map;
	
	public void move(Character c, Tile start, Tile destination){
		//TODO 
	}
	
	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}
}
