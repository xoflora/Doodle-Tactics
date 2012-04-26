package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import main.DoodleTactics;
import main.GameMenuScreen;

/** 
 * PauseControllers handle all interactions with the pause menu
 * @author jeshapir
 */

public class GameMenuController extends Controller {

	private GameMenuScreen _gameMenu;
	
	public GameMenuController(DoodleTactics dt, GameMenuScreen gameMenu) {
		super(dt);
		_gameMenu = gameMenu;
	}
	
	@Override
	public GameMenuScreen getScreen() {
		return _gameMenu;
	}
	
	@Override
	public void take() {
		// TODO
	}
	
	@Override
	public void release() {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		MenuItem _clickedButton = _gameMenu.checkContains(e.getPoint());
		if (_clickedButton != null) {
			System.out.println("click");
			_clickedButton.activate();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			//do stuff
			_gameMenu.switchToGameScreen();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		MenuItem _clickedButton = _gameMenu.checkItemContains(e.getPoint());
		if (!_gameMenu._beingHovered) {
			
		}
//		if (_clickedButton != null) {
//			System.out.println("click");
//			_clickedButton.activate();
//		}
	}

}
