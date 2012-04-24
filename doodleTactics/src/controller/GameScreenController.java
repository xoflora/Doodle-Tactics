package controller;

import java.util.LinkedList;
import java.util.List;

import util.Util;

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
	
	/**
	 * @return a list of characters to display in the game screen
	 */
	public List<Character> getCharactersToDisplay() {
		List<Character> main = new LinkedList<Character>();
		main.add(_gameScreen.getMainChar());
		return Util.union(_gameScreen.getMap().getCharactersToDisplay(), main);
	}
}
