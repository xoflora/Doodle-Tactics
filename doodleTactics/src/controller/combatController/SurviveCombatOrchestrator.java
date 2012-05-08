package controller.combatController;

import java.util.List;

import main.DoodleTactics;

public class SurviveCombatOrchestrator extends CombatOrchestrator {
	
	private int _numTurns;

	public SurviveCombatOrchestrator(DoodleTactics dt,
			List<CombatController> enemies, List<CombatController> partners,
			List<CombatController> others, int numUnits, int numTurns) {
		super(dt, enemies, partners, others, numUnits);
		_numTurns = numTurns;
	}

	@Override
	public boolean isLoss() {
		return _gameScreen.getMainChar().getHP() <= 0;
	}

	@Override
	public boolean isRun() {
		return false;
	}

	@Override
	/**
	 * @return whether or not the current game state represents a win condition;
	 * 		this occurs if all enemies are defeated, or the player has survived
	 * 		for the appropriate number of turns
	 */
	public boolean isWin() {
		if (_numTurns <= 0)
			return true;
		for (CombatController e : _enemies)
			if (!e.getUnits().isEmpty())
				return false;
		
		return true;
	}

	@Override
	public void performTileEvents() {
		
	}

	@Override
	public void performTurnUpdate() {
		_numTurns--;
	}
}
