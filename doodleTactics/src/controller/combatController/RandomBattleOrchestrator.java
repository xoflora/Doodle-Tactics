package controller.combatController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import character.Character;
import event.Dialogue;
import event.FortuneDialogue;
import event.InvalidEventException;

import main.DoodleTactics;
import map.Map;
import map.Tile;

public class RandomBattleOrchestrator extends CombatOrchestrator {

	public RandomBattleOrchestrator(DoodleTactics dt,
			List<CombatController> enemies, List<CombatController> partners,
			List<CombatController> others, int numUnits) {
		super(dt, enemies, partners, others, numUnits);
	}

	@Override
	public boolean isLoss() {
		return _gameScreen.getMainChar().getHP() <= 0;
	}

	@Override
	/**
	 * @return whether or not the current combat state is a win condition;
	 * 		the combat state is a win condition when no enemy units remain on the map
	 */
	public boolean isWin() {
		for (CombatController e : _enemies)
			if (!e.getUnits().isEmpty())
				return false;
		
		return true;
	}

	@Override
	/**
	 * @return whether or not the current combat state represents an escape condition;
	 * 		the combat state is an escape condition if all characters are outside the attack
	 * 		range of all enemies
	 */
	public boolean isRun() {
		Map m = _gameScreen.getMap();
		for (Character c : _p.getUnits()) {
			for (CombatController f : _enemies)
				for (Character e : f.getUnits())
					if (m.getAttackRange(f.getTile(e), e.getMovementRange(),
							e.getMinAttackRange(), e.getMaxAttackRange()).contains(_p.getTile(c)))
						return false;
		}
		
		return true;
	}

	@Override
	/**
	 * performs any tile events that are associated with a combat controller;
	 * updates within the map the data revolving around 
	 */
	public void performTileEvents() {
		for (CombatController c : getEnemyAffiliations()) {
			_gameScreen.getMap().updateRandomBattleTiles(c._locations);
		}
		
		HashMap<Character, Tile> toAdd = new HashMap<Character, Tile>();
		Map m = _gameScreen.getMap();
		for (CombatController f : getPartnerAffiliations())
			for (Character c : f.getUnits())
				if (m.generatesRandomBattle(f.getTile(c)))
					for (Character e : m.getRandomEnemies(f.getTile(c)))
						if (!getAllEnemyUnits().contains(e))
							toAdd.put(e, m.getRandomEnemyTile(e));
		_enemies.get(0).addUnits(toAdd);
	}

	@Override
	public void performTurnUpdate() {

	}
	
	@Override
	public boolean canTalk(Character a, Character b) {
		return true;
	}
	
	@Override
	public Dialogue getDialogue(Character a, Character b) {
		Dialogue def = super.getDialogue(a, b);
		if (def == null) {
			try {
				return new FortuneDialogue(_dt, a, b);
			} catch (Exception e) {
				return null;
			}
		}
		else
			return def;
	}
}
