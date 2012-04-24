package controller.combatController;

import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;

import graphics.Rectangle;
import character.Character;

/**
 * a unit pool is a graphical interface that allows players to select units
 * @author rroelke
 *
 */
public class UnitPool extends Rectangle {
	
	private List<Character> _unitPool;
	
	public UnitPool(JPanel container, List<Character> units) {
		super(container);
	}

	public UnitPool(JPanel container, int priority, List<Character> units) {
		super(container, priority);
	}
	
	@Override
	public void paint(Graphics2D brush) {
		super.paint(brush);
	}

}
