package controller.combatController.AIController;

import java.util.ArrayList;
import java.util.List;

import map.Tile;
import controller.combatController.CombatController;
import character.Character;

/**
 * action class that corresponds to waiting
 * @author rroelke
 *
 */
public class WaitAction extends Action {

	public WaitAction(CombatController src, Tile t) {
		super(src, t);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int evaluateMove() {
		List<Character> aggressors = new ArrayList<Character>();
		
		for (CombatController aff : _src.getEnemyAffiliations())
			for (Character c : aff.getUnits()) {
				if (_src.getScreen().getMap().getAttackRange(aff.getTileMappings().get(c),
						c.getMovementRange(), c.getMinAttackRange(),
						c.getMaxAttackRange()).contains(_destTile)) {
					
				}
			}
		
		return 0;
	}

}
