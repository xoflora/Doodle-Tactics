package controller.combatController;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import main.DoodleTactics;
import graphics.MenuItem;
import graphics.Rectangle;

/**
 * selection window for the series of options that a unit has after moving
 * @author rroelke
 *
 */
public class CombatOptionWindow extends Rectangle {
	
	private class CombatOption extends MenuItem {

		public CombatOption(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt) {
			super(container, defltPath, hoveredPath, dt);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	private BufferedImage _img;
	private List<CombatOption> _options;
	
	public CombatOptionWindow(JPanel container, boolean attack, boolean item, boolean talk) {
		super(container);
		_options = new ArrayList<CombatOption>();
		
		if (attack) {
			
		}
		if (item) {
			
		}
		if (talk) {
			
		}
		
	}

	@Override
	/**
	 * @return the image corresponding to the CombatOptionWindow
	 */
	public BufferedImage getImage() {
		return _img;
	}

}
