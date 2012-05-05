package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.ClassChooserScreen;
import main.DoodleTactics;
import main.Screen;

public class ClassChooserScreenController extends Controller {

	DoodleTactics _dt;
	ClassChooserScreen _classChooser;
	
	public ClassChooserScreenController(DoodleTactics dt, ClassChooserScreen screen) {
		super(dt);
		_dt = dt;
		_classChooser = screen;
	}

	@Override
	public Screen<? extends Controller> getScreen() {
		// TODO Auto-generated method stub
		return _classChooser;
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}//parse party

	@Override
	public void take() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		_classChooser.checkClicked(e.getPoint());
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
		// TODO Auto-generated method stub
		_classChooser.checkContains(e.getPoint());
	}

}
