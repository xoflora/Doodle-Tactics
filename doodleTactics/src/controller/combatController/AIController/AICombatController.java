package controller.combatController.AIController;

import java.util.HashMap;

import main.DoodleTactics;
import map.Tile;
import character.Character;
import controller.combatController.CombatController;

public abstract class AICombatController extends CombatController {

	public AICombatController(DoodleTactics dt, HashMap<Character, Tile> units) {
		super(dt, units);
	}

	
}
