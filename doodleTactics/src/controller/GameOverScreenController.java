package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import main.GameOverScreen;
import main.Screen;

public class GameOverScreenController extends Controller {
	
	private GameOverScreen _gameOver;

	public GameOverScreenController(DoodleTactics dt, GameOverScreen over) {
		super(dt);
		_gameOver = over;
	}

	@Override
	public GameOverScreen getScreen() {
		return _gameOver;
	}

	@Override
	public void release() { }

	@Override
	public void take() { }

	@Override
	public void mouseClicked(MouseEvent e) {
		MenuItem m = _gameOver.checkContains(e.getPoint());
		if (m != null)
			m.activate(e.getButton());
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
		_gameOver.checkContains(e.getPoint());
	}
}
