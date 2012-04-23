package controller;

import main.GameScreen;

/**
 * 
 * @author rroelke
 *
 */
public abstract class GameScreenController extends Controller {
	protected GameScreen _gameScreen;
	
	@Override
	public GameScreen getScreen() {
		return _gameScreen;
	}
}
