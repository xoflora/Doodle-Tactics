package controller.combatController;

import java.util.List;

import main.DoodleTactics;

public class EventCombatOrchestrator extends CombatOrchestrator {

	public EventCombatOrchestrator(DoodleTactics dt,
			List<CombatController> enemies, List<CombatController> partners,
			List<CombatController> others, int numUnits) {
		super(dt, enemies, partners, others, numUnits);
	}

}
