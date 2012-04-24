package controller;

import java.util.LinkedList;

import main.DoodleTactics;
import main.GameScreen;
import character.Character;
/**
 * 
 * @author rroelke
 *
 */
public abstract class GameScreenController extends Controller {
	
	protected GameScreen _gameScreen;
	
	public GameScreenController(DoodleTactics dt) {
		super(dt);
		_gameScreen = _dt.getGameScreen();
	}
	
	@Override
	public GameScreen getScreen() {
		return _gameScreen;
	}
	
	public abstract LinkedList<Character> getCharactersToDisplay();
}
