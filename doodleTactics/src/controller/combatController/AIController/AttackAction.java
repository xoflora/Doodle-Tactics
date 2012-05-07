package controller.combatController.AIController;

import java.util.ArrayList;
import java.util.List;

import map.Tile;
import controller.combatController.ActionType;
import controller.combatController.CombatController;
import character.Character;

/**
 * action class that corresponds to an attack
 * @author rroelke
 *
 */
public class AttackAction extends Action {
	
	private static final int ATTACK_POINTLESS = 0;
	private static final int MINOR_DAMAGE = 1;
	private static final int REASONABLE_DAMAGE = 2;
	private static final int GOOD_DAMAGE = 3;
	private static final int CRIPPLING_DAMAGE = 4;
	private static final int LETHAL_BLOW = 5;
	
	private static final double[] TIERED_MULTIPLIERS = {-1, 3, 6, 12, 24, 100};
	private static final double[] TIER_THRESHOLDS = {0, .1, .2, .4, .7, 1};
	
	private static final double DEFEAT_VALUE = 1000;
	
	private Tile _toAttack;

	public AttackAction(CombatController src, Character c, Tile t) {
		super(src, c, t);
		_type = ActionType.ATTACK;
	}

	@Override
	public void act() {
		if (_toAttack != null) {
			_src.attack(_destTile, _toAttack);
		}
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
				
				System.out.println("DAMAGE " + damage);
				
				if (other.getHP() == 0) {
					bestAttack = DEFEAT_VALUE;
					_toAttack = t;
				}
				else if ((double)damage/(double)other.getHP() > bestAttack) {
					bestAttack = damage/other.getHP();
					_toAttack = t;
				}
			}
		
		if (_toAttack == null)
			return Double.NEGATIVE_INFINITY;
		
		double eval;
		if (bestAttack >= TIER_THRESHOLDS[LETHAL_BLOW]) {
			filter.add(_toAttack.getOccupant());
			eval = TIERED_MULTIPLIERS[LETHAL_BLOW];
		}
		else if (bestAttack >= TIER_THRESHOLDS[CRIPPLING_DAMAGE]) {
			filter.add(_toAttack.getOccupant());
			eval = TIERED_MULTIPLIERS[CRIPPLING_DAMAGE];
		}
		else if (bestAttack >= TIER_THRESHOLDS[GOOD_DAMAGE])
			eval = TIERED_MULTIPLIERS[GOOD_DAMAGE];
		else if (bestAttack > TIER_THRESHOLDS[REASONABLE_DAMAGE])
			eval = TIERED_MULTIPLIERS[REASONABLE_DAMAGE];
		else if (bestAttack > TIER_THRESHOLDS[MINOR_DAMAGE])
			eval = TIERED_MULTIPLIERS[MINOR_DAMAGE];
		else
			eval = TIERED_MULTIPLIERS[ATTACK_POINTLESS];
		
		return eval*bestAttack + defensiveEval(filter);
	}
}
