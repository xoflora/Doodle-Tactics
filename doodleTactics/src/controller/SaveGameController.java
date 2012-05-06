package controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import main.SaveGameScreen;
import main.Screen;

public class SaveGameController extends Controller {

	private SaveGameScreen _screen;
	public SaveGameController(DoodleTactics dt, SaveGameScreen saveGameScreen) {
		super(dt);
		_screen = saveGameScreen;

	}

	@Override
	public Screen<? extends Controller> getScreen() {
		return _screen;
	}

	@Override
	public void release() {}

	@Override
	public void take() {
		_screen.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

}
