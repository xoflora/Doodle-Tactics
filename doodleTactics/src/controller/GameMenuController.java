package controller;

import graphics.MenuItem;

import items.Item;

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
	private State _mode;
	public GameMenuController(DoodleTactics dt, GameMenuScreen gameMenu) {
		super(dt);
		_gameMenu = gameMenu;
		_mode = State.LISTEN;
	}

	public enum State{
		RECORD,
		LISTEN
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
			//			_clickedButton.activate();
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
		if(_mode == State.LISTEN){
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				//do stuff
				_gameMenu.setDefault();
				_gameMenu.removeAll();
				_gameMenu.switchToGameScreen();
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				_gameMenu.increaseCurrOption();
				_gameMenu.repaint();
			} else if(e.getKeyCode() == KeyEvent.VK_UP){
				_gameMenu.decreaseCurrOption();
				_gameMenu.repaint();
			} else if(e.getKeyCode() == KeyEvent.VK_ENTER){
				_mode = State.RECORD;
			}
		} else if(_mode == State.RECORD){
			_gameMenu.assignKey(e.getKeyCode());
			_mode = State.LISTEN;
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
	}

}
