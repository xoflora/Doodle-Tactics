package controller.combatController.AIController;

import java.util.ArrayList;
import java.util.List;

import map.Tile;
import controller.combatController.CombatController;
import character.Character;

/**
 * action class that corresponds to an attack
 * @author rroelke
 *
 */
public class AttackAction extends Action {
	
	private Character _toAttack;

	public AttackAction(CombatController src, Character c, Tile t) {
		super(src, c, t);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * 
	 */
	public double evaluateMove() {
		double eval = 0;
		List<Character> filter = new ArrayList<Character>();
		
		int power = _c.getFullAttackStrength();
		
		for (Tile t : _src.getScreen().getMap().getAttackRange(_destTile, 0,
				_c.getMinAttackRange(), _c.getMaxAttackRange()))
			if (_src.isEnemy(t.getOccupant())) {
				
			}
		
		return eval;
	}

}
