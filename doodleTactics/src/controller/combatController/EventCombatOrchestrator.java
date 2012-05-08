package controller.combatController;

import java.util.List;

import character.Character;

import main.DoodleTactics;

public class EventCombatOrchestrator extends CombatOrchestrator {

	public EventCombatOrchestrator(DoodleTactics dt,
			List<CombatController> enemies, List<CombatController> partners,
			List<CombatController> others, int numUnits) {
		super(dt, enemies, partners, others, numUnits);
	}

	@Override
	public boolean isLoss() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRun() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWin() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void performTileEvents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performTurnUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canTalk(Character a, Character b) {
		return false;
	}
}
