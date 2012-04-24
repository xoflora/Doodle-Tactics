package controller;

import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import util.Util;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;
import character.Character;
/**
 * 
 * @author rroelke
 *
 */
public abstract class GameScreenController extends Controller {
	
	protected GameScreen _gameScreen;
		
	private int _draggedx;
	private int _draggedy;
	
	public GameScreenController(DoodleTactics dt) {
		super(dt);
		_gameScreen = _dt.getGameScreen();
		
		_draggedx = 0;
		_draggedy = 0;
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
	
	
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		_draggedx = e.getX();
		_draggedy = e.getY();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) { }
	
	@Override
	/**
	 * responds to the mouse being dragged to move the camera
	 */
	public void mouseDragged(MouseEvent e) {
		int updatex = (e.getX() - _draggedx)/Tile.TILE_SIZE;
		int updatey = (e.getY() - _draggedy)/Tile.TILE_SIZE;
		
		if (updatex != 0 || updatey != 0) {
			_gameScreen.mapUpdate(-updatex, -updatey);
			_draggedx = e.getX();
			_draggedy = e.getY();
		}
	}
}
