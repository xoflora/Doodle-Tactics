package controller.combatController.AIController;

import map.Tile;
import controller.combatController.CombatController;
import character.Character;

/**
 * action class that corresponds to an attack
 * @author rroelke
 *
 */
public class AttackAction extends Action {

	public AttackAction(CombatController src, Character c, Tile t) {
		super(src, c, t);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double evaluateMove() {
	//	List<Tile> attackSpots
		return Double.NEGATIVE_INFINITY;
	}

}
