package controller;

import main.DoodleTactics;
import main.GameScreen;

/**
 * 
 * @author rroelke
 *
 */
public abstract class GameScreenController extends Controller {
	
	public GameScreenController(DoodleTactics dt) {
		super(dt);
	}

	protected GameScreen _gameScreen;
	
	@Override
	public GameScreen getScreen() {
		return _gameScreen;
	}
}
