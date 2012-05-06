package controller.combatController.AIController;

import java.util.ArrayList;

import map.Tile;
import controller.combatController.CombatController;
import character.Character;

/**
 * action class that corresponds to waiting
 * @author rroelke
 *
 */
public class WaitAction extends Action {

	public WaitAction(CombatController src, Character c, Tile t) {
		super(src, c, t);
	}

	@Override
	public void act() {
		System.out.println("HELLO");
		_src.characterWait();
	}

	@Override
	/**
	 * evaluates what happens if the character moves to the given tile
	 */
	public double evaluateMove() {
		return defensiveEval(new ArrayList<Character>());
	}

}
