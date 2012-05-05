package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;


import util.Util;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;
import character.Character;
/**
 * 
 * @author rroelkedww
 *
 */
public abstract class GameScreenController extends Controller {
	
	protected GameScreen _gameScreen;
	private int _draggedx;
	private int _draggedy;
	
	protected Tile _hoveredTile;
	protected UnitStatWindow _unitStats;
	
	public GameScreenController(DoodleTactics dt) {
		super(dt);
		_gameScreen = _dt.getGameScreen();
		
		_draggedx = 0;
		_draggedy = 0;
		
		_hoveredTile = null;
		_unitStats = null;
	}
	
	@Override
	public void take() {
		_gameScreen.repaint();
	}
	
	@Override
	public void release() {
		if (_hoveredTile != null)
			_hoveredTile.setHovered(false);
		if (_unitStats != null) {
			_unitStats.removeFromDrawingQueue();
			_unitStats = null;
		}
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
		List<Character> toReturn = Util.union(_gameScreen.getMap().getCharactersToDisplay(), main);
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

		//	if (updatex != 0 || updatey != 0) {
		_gameScreen.pan(e.getX() - _draggedx, e.getY() - _draggedy);
		_draggedx = e.getX();
		_draggedy = e.getY();
		_gameScreen.repaint();
		//	}

		if (_unitStats != null) {
			_unitStats.removeFromDrawingQueue();
			_unitStats = null;
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
			
			if (t.isOccupied()) {
				Character c = t.getOccupant();
				
					if (_unitStats != null) {
						_unitStats.removeFromDrawingQueue();
						_unitStats = null;
					}
				
					_unitStats = new UnitStatWindow(_gameScreen, _dt, c, c.getX() + c.getWidth()/2 > _gameScreen.getWidth()/2);
					_gameScreen.addMenuItem(_unitStats);
					_unitStats.setVisible(true);
			} else {
				if (_unitStats != null) {
					_unitStats.removeFromDrawingQueue();
					_unitStats = null;
				}
			}
		}
		
		_gameScreen.checkContains(e.getPoint());
	}

	@Override
	/**
	 * clicks on a menu element of the game screen
	 */
	public void mouseClicked(MouseEvent e) {
	/*	List<MenuItem> _clickedButtons = _gameScreen.checkContains(e.getPoint());
		synchronized (_clickedButtons) {
			for (MenuItem m : _clickedButtons)
				m.activate(e.getButton());
		}	*/
		
		MenuItem m = _gameScreen.checkContains(e.getPoint());
		if (m != null)
			m.activate(e.getButton());
		
	/*	Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null)
			System.out.println("Tile " + t + ", Occupant " + t.getOccupant());	*/
	}
	
/*	public void removeUnitStats() {
		if(_unitStats != null) {
			_gameScreen.removeMenuItem(_unitStats);
			_unitStats.setVisible(false);
			_unitStats = null;
			_gameScreen.repaint();
		}
	}	*/
	
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
