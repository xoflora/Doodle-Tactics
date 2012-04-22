package controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.GameScreen;
import map.Tile;

public class OverworldController extends Controller {

	protected GameScreen _gameScreen;
	
	public OverworldController(GameScreen game) {
		_gameScreen = game;
	}
	
	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		
		System.out.println("Mouse pos: " + e.getX() + " " + e.getY());
		System.out.println("Map pos: " + _gameScreen.getMapX(e.getX()) + " " + _gameScreen.getMapY(e.getY()));
		
		if (t != null)
			System.out.println(t.x() + " " + t.y());

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
		
		System.out.println("isAnimating:" + _gameScreen.isAnimating());
		
		if(!_gameScreen.isAnimating()) {
		
			switch(e.getKeyChar()) {
			
			case 'w':
				System.out.println("w");
				_gameScreen.getMainChar().setUp();
				_gameScreen.mapUpdate(0, -1);
				break;
			case 'a':
				System.out.println("a");
				_gameScreen.getMainChar().setLeft();
				_gameScreen.mapUpdate(-1, 0);
				break;
			case 's':
				System.out.println("s");
				_gameScreen.getMainChar().setDown();
				_gameScreen.mapUpdate(0, 1);
				break;
			case 'd':
				System.out.println("d");
				_gameScreen.getMainChar().setRight();
				_gameScreen.mapUpdate(1, 0);
				break;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
}
