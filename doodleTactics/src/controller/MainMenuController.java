package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import main.MainMenuScreen;

public class MainMenuController extends Controller {

	private MainMenuScreen _mainMenu;
	
	public MainMenuController(DoodleTactics dt, MainMenuScreen gameMenu) {
		super(dt);
		_mainMenu = gameMenu;
	}
	
	public MainMenuScreen getScreen() {
		return _mainMenu;
	}
	
	@Override
	public void take() { }
	
	@Override
	public void release() { }

	@Override
	/**
	 * activate the clicked component of the menu screen
	 */
	public void mouseClicked(MouseEvent e) {
		MenuItem _clickedButton = _mainMenu.checkContains(e.getPoint());
		if (_clickedButton != null)
			_clickedButton.activate(e.getButton());
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) { }

	@Override
	public void mouseMoved(MouseEvent e) {
		_mainMenu.checkContains(e.getPoint());
	}
}
