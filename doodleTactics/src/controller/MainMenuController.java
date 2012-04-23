package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.MainMenuScreen;

public class MainMenuController extends Controller {

	private MainMenuScreen _mainMenu;
	
	public MainMenuController(MainMenuScreen gameMenu) {
		_mainMenu = gameMenu;
		System.out.println(gameMenu);
	}
	
	public MainMenuScreen getScreen() {
		return _mainMenu;
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
		MenuItem _clickedButton = _mainMenu.checkContains(e.getPoint());
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
		_mainMenu.checkContains(e.getPoint());
	}
}
