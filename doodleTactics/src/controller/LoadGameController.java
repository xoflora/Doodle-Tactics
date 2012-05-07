package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.DoodleTactics;
import main.LoadGameScreen;
import main.Screen;
import main.LoadGameScreen.LoadMenuItem;

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
	public void mouseClicked(MouseEvent e) {
		LoadMenuItem clicked = _loadScreen.checkContainsRadioButtons(e.getPoint());
		if(clicked != null){
			clicked.setHovered();
			_loadScreen.repaint();
		}
		MenuItem button = _loadScreen.checkContainsButton(e.getPoint());
		if(button != null)
			button.activate(e.getButton());
		
	}

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
	public void mouseMoved(MouseEvent e) {
		_loadScreen.checkContainsButton(e.getPoint());
		_loadScreen.repaint();

	}
}