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
		// TODO determine whether the combat is a win or a loss and act accordingly
		//	if combat is a loss, swap screens for the "game over" screen
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
			else if (!_factions.isEmpty()) {	//INCORRECT CONDITION - swap for not win/loss condition
				_factionCycle = _factions.listIterator();
				take();
			}
			else	//combat has ended - win/loss condition
				release();
		}
		else {
			_gameScreen.pushControl(new PlayerSetup(_dt, _gameScreen.getValidSetupTiles(_numUnits)));
		//	_gameScreen.pushControl(_p);
			_setup = true;
		}
	}
	
	/**
	 * @return a list containing all the characters involved in the battle
	 */
	public List<Character> getCharactersToDisplay() {
		List<List<Character>> c = new ArrayList<List<Character>>();
		c.add(super.getCharactersToDisplay());
		for (CombatController faction : _factions)
			c.add(faction.getUnits());
		return Util.union(c);
	}
}
