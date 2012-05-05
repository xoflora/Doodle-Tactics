package controller.combatController.AIController;

import map.Tile;
import controller.combatController.CombatController;

/**
 * action class that corresponds to an attack
 * @author rroelke
 *
 */
public class AttackAction extends Action {

	public AttackAction(CombatController src, Tile t) {
		super(src, t);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int evaluateMove() {
	//	List<Tile> attackSpots
		return 0;
	}

}
