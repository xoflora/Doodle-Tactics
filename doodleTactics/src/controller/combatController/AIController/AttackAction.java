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
	
	private static final double MINOR_DAMAGE_MULTIPLIER = 3;
	private static final double DAMAGING_MULTIPLIER = 6;
	private static final double CRIPPLING_MULTIPLIER = 12;
	private static final double DEFEAT_MULTIPLIER = 24;
	
	private static final double DEFEAT_VALUE = 1000;
	
	private Character _toAttack;
	private int _distance;

	public AttackAction(CombatController src, Character c, Tile t) {
		super(src, c, t);
		_distance = -1;
	}

	@Override
	public void act() {
		System.out.println("EWOI");
		if (_toAttack != null)
			_src.attack(_c, _toAttack, _distance);
		else {
			_src.characterWait();
		}
	}

	@Override
	/**
	 * evaluates moving to this tile and attacking an enemy character
	 */
	public double evaluateMove() {
		List<Character> filter = new ArrayList<Character>();
		
		int power = _c.getFullAttackStrength();
		
		double bestAttack = 0;
		for (Tile t : _src.getScreen().getMap().getAttackRange(_destTile, 0,
				_c.getMinAttackRange(), _c.getMaxAttackRange()))
			if (_src.isEnemy(t.getOccupant())) {
				Character other = t.getOccupant();
				int damage = power - other.getFullDefense();
				
				if (other.getHP() == 0) {
					bestAttack = DEFEAT_VALUE;
					_toAttack = other;
				}
				else if (damage/other.getHP() > bestAttack) {
					bestAttack = damage/other.getHP();
					_toAttack = other;
					_distance = _destTile.gridDistanceToTile(t);
				}
			}
		
		double eval;
		if (bestAttack >= 1) {
			filter.add(_toAttack);
			eval = DEFEAT_MULTIPLIER;
		}
		else if (bestAttack >= .75) {
			filter.add(_toAttack);
			eval = CRIPPLING_MULTIPLIER;
		}
		else if (bestAttack >= .4)
			eval = DAMAGING_MULTIPLIER;
		else {
			eval = MINOR_DAMAGE_MULTIPLIER;
		}
		
		return eval*bestAttack + defensiveEval(filter);
	}
}
