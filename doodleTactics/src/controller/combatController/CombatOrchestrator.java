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
	
	private enum State {
		SETUP,
		SETUP_COMPLETE,
		BATTLING
	}
	
	private List<CombatController> _factions;
	private ListIterator<CombatController> _factionCycle;
	private State _state;
	
	private List<CombatController> _enemies;
	private List<CombatController> _partners;
	private List<CombatController> _others;
	
	private int _numUnits;
	private static final int NUM_EXTRA_SETUP_SPACES = 0;
	
	private PlayerCombatController _p;
	
	public CombatOrchestrator(DoodleTactics dt, List<CombatController> enemies, List<CombatController> partners,
				List<CombatController> others, int numUnits) {
		super(dt);
				
		_factions = null;
		_factionCycle = null;
		_state = State.SETUP;
		_numUnits = numUnits;
		
		_enemies = enemies;
		_partners = partners;
		_others = others;		
		
		_p = null;
	}

	@Override
	/**
	 * releases control of the game screen
	 * signifies the end of combat, or the beginning of a turn
	 */
	public void release() {
		// TODO determine whether the combat is a win or a loss and act accordingly
		//	if combat is a loss, swap screens for the "game over" screen
		System.out.println("Orchestrator releasing for some reason");
		super.release();
	}

	@Override
	/**
	 * gains control of the game screen
	 * signifies the beginning of combat, or end of a turn
	 */
	public void take() {
		super.take();
		
		if (_state == State.BATTLING) {	//swap factions
		/*	while (!_factionCycle.hasNext()) {
				if (!_factions.isEmpty())
					_factionCycle = _factions.listIterator();
				else {
					
				}
			}	*/
			
			
			
			if (_factionCycle.hasNext()) {
				_gameScreen.pushControl(_factionCycle.next());
			}
			else if (!_factions.isEmpty()) {	//INCORRECT CONDITION - swap for not win/loss condition
				_factionCycle = _factions.listIterator();
				take();
			}
	//		else	//combat has ended - win/loss condition
	//			_gameScreen.popControl();
		}
		else if (_state == State.SETUP_COMPLETE) {
			
			if (_p == null)
				_p = new PlayerCombatController(_dt, _dt.getParty());
			
			if (_partners == null)
				_partners = new ArrayList<CombatController>();
			_partners.add(_p);
			
			for (CombatController p : _partners)
				p.setEnemyAffiliations(_enemies);
			
			for (CombatController e : _enemies)
				e.setEnemyAffiliations(_partners);
			
			List<List<CombatController>> zip = new ArrayList<List<CombatController>>();
			
			zip.add(_partners);
			if (_enemies != null)
				zip.add(_enemies);
			if (_others != null)
				zip.add(_others);
			
			_factions = Util.zip(zip);
			
			
			_factionCycle = _factions.listIterator();
			_state = State.BATTLING;
			take();
		}
		else if (_state == State.SETUP){
			_gameScreen.pushControl(new PlayerSetup(_dt,
					_gameScreen.getValidSetupTiles(_numUnits + NUM_EXTRA_SETUP_SPACES), this));
			_state = State.SETUP_COMPLETE;
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

	/**
	 * set the player combat controller to have the given units
	 * @param units the unit list of the player combat controller
	 */
	public void setPlayerUnits(List<Character> units) {
		units.add(_gameScreen.getMainChar());
		_p = new PlayerCombatController(_dt, units);
	}
}
