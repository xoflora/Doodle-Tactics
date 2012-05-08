package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import main.GameMenuScreen;
import main.GameMenuScreen.LoadMenuItem;

/**
 * PauseControllers handle all interactions with the pause menu
 * 
 * @author jeshapir
 */

public class GameMenuController extends Controller {

	private GameMenuScreen _gameMenu;
	private State _mode;

	public GameMenuController(DoodleTactics dt, GameMenuScreen gameMenu) {
		super(dt);
		_gameMenu = gameMenu;
		_mode = State.LISTEN;
	}

	public enum State {
		RECORD, LISTEN
	}

	@Override
	public GameMenuScreen getScreen() {
		return _gameMenu;
	}

	@Override
	public void take() {
	}

	@Override
	public void release() {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		MenuItem _clickedButton = _gameMenu.checkContains(e.getPoint());

		LoadMenuItem clicked = _gameMenu
				.checkContainsRadioButtons(e.getPoint());
		if (clicked != null) {
			_gameMenu.repaint();
		}
		_gameMenu.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (_mode == State.LISTEN) {
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				// do stuff
				_gameMenu.setDefault();
				_gameMenu.removeAll();
				_gameMenu.switchToGameScreen();
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				_gameMenu.increaseCurrOption();
				_gameMenu.repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				_gameMenu.decreaseCurrOption();
				_gameMenu.repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				_mode = State.RECORD;
			}
		} else if (_mode == State.RECORD) {
			_gameMenu.assignKey(e);
			_mode = State.LISTEN;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (_gameMenu.getSaveMenuItem().contains(e.getPoint())
				&& (_gameMenu.getSaveMenuItem().containsText() || (!_gameMenu
						.getSaveMenuItem().getVisible()) && _gameMenu.getCurrSelected() != null)) {
			_gameMenu.getSaveMenuItem().setHovered();

		} else {
			_gameMenu.getSaveMenuItem().setDefault();
		}

		_gameMenu.repaint();
	}
}
