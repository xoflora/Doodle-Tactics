package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
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
	
	protected Tile _hoveredTile;
	
	public GameScreenController(DoodleTactics dt) {
		super(dt);
		_gameScreen = _dt.getGameScreen();
		
		_draggedx = 0;
		_draggedy = 0;
		
		_hoveredTile = null;
	}
	
	@Override
	public void take() {
		_gameScreen.repaint();
	}
	
	@Override
	public void release() {
		if (_hoveredTile != null)
			_hoveredTile.setHovered(false);
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
	//	System.out.println("***Added Main");
		List<Character> toReturn = Util.union(_gameScreen.getMap().getCharactersToDisplay(), main);
	//	System.out.println(toReturn);
		return toReturn;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		_draggedx = e.getX();
		_draggedy = e.getY();
	}

	
	@Override
	/**
	 * responds to the mouse being dragged to move the camera
	 */
	public void mouseDragged(MouseEvent e) {
		int updatex = (e.getX() - _draggedx)/Tile.TILE_SIZE;
		int updatey = (e.getY() - _draggedy)/Tile.TILE_SIZE;
				
		if (updatex != 0 || updatey != 0) {
			_gameScreen.pan(-updatex, -updatey);
			_draggedx = e.getX();
			_draggedy = e.getY();
		}
	}
	
	/**
	 * updates the hovered tile and emphasizes menu buttons
	 */
	public void mouseMoved(MouseEvent e) {
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null) {
			
			if (t != _hoveredTile) {
				if (_hoveredTile != null)
					_hoveredTile.setHovered(false);
				_hoveredTile = t;
				_hoveredTile.setHovered(true);
				_gameScreen.repaint();
			}
		}
		
		_gameScreen.checkContains(e.getPoint());
	}

	@Override
	/**
	 * clicks on a menu element of the game screen
	 */
	public void mouseClicked(MouseEvent e) {
		List<MenuItem> _clickedButtons = _gameScreen.checkContains(e.getPoint());
		synchronized (_clickedButtons) {
			for (MenuItem m : _clickedButtons)
				m.activate(e.getButton());
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }
	
	@Override
	public void mouseReleased(MouseEvent e) { }
}
