package controller;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ListIterator;

import main.DoodleTactics;

public class CombatOrchestrator extends GameScreenController {
	
	private List<CombatController> _factions;
	private ListIterator<List<CombatController>> _it;
	private boolean _begin;
	
	public CombatOrchestrator(DoodleTactics dt) {
		super(dt);
	}

	@Override
	/**
	 * releases control of the game screen
	 * signifies the end of combat
	 */
	public void release() {
		// TODO
	}

	@Override
	/**
	 * gains control of the game screen
	 * signifies the beginning of combat
	 */
	public void take() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

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
	public void mouseMoved(MouseEvent e) { }
}
