package controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import main.LoadGameScreen;
import main.Screen;

public class LoadGameController extends Controller{

	private LoadGameScreen _loadScreen;
	public LoadGameController(DoodleTactics dt,LoadGameScreen lgs) {
		super(dt);
		_loadScreen = lgs;
	}

	@Override
	public Screen<? extends Controller> getScreen() {
		return _loadScreen;
	}

	@Override
	public void take() {
		_loadScreen.repaint();
	}

	@Override
	public void release() {}


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