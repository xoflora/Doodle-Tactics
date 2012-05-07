package controller.combatController;

import java.util.List;

import main.DoodleTactics;

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
		return false;
	}

	@Override
	public void performTileEvents() {
		
	}

	@Override
	public void performTurnUpdate() {

	}
}
