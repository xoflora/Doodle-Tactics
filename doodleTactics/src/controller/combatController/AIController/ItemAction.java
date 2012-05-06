package controller.combatController.AIController;

import map.Tile;
import controller.combatController.CombatController;
import character.Character;

/**
 * action class that corresponds to item use
 * @author rroelke
 *
 */
public class ItemAction extends Action {

	public ItemAction(CombatController src, Character c, Tile t) {
		super(src, c, t);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double evaluateMove() {
		// TODO Auto-generated method stub
		return Double.NEGATIVE_INFINITY;
	}

}
