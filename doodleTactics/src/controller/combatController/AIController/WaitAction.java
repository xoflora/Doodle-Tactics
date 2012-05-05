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

	public WaitAction(CombatController src, Character c, Tile t) {
		super(src, c, t);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * evaluates what happens if the character moves to the given tile
	 */
	public double evaluateMove() {
	//	List<Character> aggressors = new ArrayList<Character>();
		double eval = 0;
		
		for (CombatController aff : _src.getEnemyAffiliations())
			for (Character c : aff.getUnits()) {
				if (_src.getScreen().getMap().getAttackRange(aff.getTileMappings().get(c),
						c.getMovementRange(), c.getMinAttackRange(),
						c.getMaxAttackRange()).contains(_destTile)) {
					if (c.getHitChance(_c) > 30 && c.getFullAttackStrength() > _c.getFullDefense())
						if (c.getCriticalChance(_c) > 30)
							eval -= Character.CRITICAL_MULTIPLIER*
								(c.getFullAttackStrength() - _c.getFullDefense());
						else
							eval -= c.getFullAttackStrength() - _c.getFullDefense();
				}
			}
		
		return eval;
	}

}
