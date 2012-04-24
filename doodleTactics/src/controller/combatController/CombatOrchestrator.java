package controller.combatController;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import util.Util;
import controller.GameScreenController;
import main.DoodleTactics;
import character.Character;

public class CombatOrchestrator extends GameScreenController {
	
	private List<CombatController> _factions;
	private ListIterator<CombatController> _factionCycle;
	private boolean _setup;
	
	private int _numUnits;
	
	private PlayerCombatController _p;
	
	public CombatOrchestrator(DoodleTactics dt, List<CombatController> enemies, List<CombatController> partners,
				List<CombatController> others, int numUnits) {
		super(dt);
		
		_factions = new ArrayList<CombatController>();
		_setup = false;
		
		PlayerCombatController player = new PlayerCombatController(dt);
		_p = player;

		if (partners == null)
			partners = new ArrayList<CombatController>();
		partners.add(player);
		
		for (CombatController p : partners)
			p.setEnemyAffiliations(enemies);
		
		for (CombatController e : enemies)
			e.setEnemyAffiliations(partners);
		
		_numUnits = numUnits;
		_factionCycle = _factions.listIterator(); 
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
		if (_setup) {	//swap factions
			if (_factionCycle.hasNext())
				_gameScreen.pushControl(_factionCycle.next());
		}
		else {
		//	_gameScreen.pushControl(new PlayerSetup(_dt, _gameScreen.getValidSetupTiles(_numUnits)));
			_gameScreen.pushControl(_p);
		}
	}
	
	/**
	 * @return a list containing all the characters involved in the battle
	 */
	public List<Character> getAllCombatants() {
		List<List<Character>> c = new ArrayList<List<Character>>();
		for (CombatController faction : _factions)
			c.add(faction.getUnits());
		return Util.union(c);
	}

	
	/*
	 * the orchestrator does not respond to user inputs since it just switches between combat controllers 
	 */
	
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
