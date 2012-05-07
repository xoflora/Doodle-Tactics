package controller.combatController;

import java.util.List;

import main.DoodleTactics;

public class RandomBattleOrchestrator extends CombatOrchestrator {

	public RandomBattleOrchestrator(DoodleTactics dt,
			List<CombatController> enemies, List<CombatController> partners,
			List<CombatController> others, int numUnits) {
		super(dt, enemies, partners, others, numUnits);
	}

}
