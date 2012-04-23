package controller.combatController;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import controller.GameScreenController;

import main.DoodleTactics;

public class CombatOrchestrator extends GameScreenController {
	
	private List<CombatController> _factions;
	private ListIterator<List<CombatController>> _it;
	private boolean _setup;
	
	private int _numUnits;
	
	public CombatOrchestrator(DoodleTactics dt, List<CombatController> enemies, List<CombatController> partners,
				List<CombatController> others, int numUnits) {
		super(dt);
		
		_setup = false;
		
		PlayerCombatController player = new PlayerCombatController(dt);

		if (partners == null)
			partners = new ArrayList<CombatController>();
		partners.add(player);
		
		for (CombatController p : partners)
			p.setEnemyAffiliations(enemies);
		
		for (CombatController e : enemies)
			e.setEnemyAffiliations(partners);
		
		_numUnits = numUnits;
	}

	@Override
	/**
	 * releases control of the game screen
	 * signifies the end of combat, or the beginning of a turn
	 */
	public void release() {
		// TODO
	}

	@Override
	/**
	 * gains control of the game screen
	 * signifies the beginning of combat, or end of a turn
	 */
	public void take() {
		// TODO Auto-generated method stub
		if (_setup) {
			
		}
		else {
			_gameScreen.pushControl(new PlayerSetup(_dt, _gameScreen.getValidSetupTiles(_numUnits)));
		}
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
