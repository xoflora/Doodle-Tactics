package controller.combatController;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
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
	
	private static final String MENU_IMAGE_PATH = "";
	private static final String ATTACK_IMAGE = "";
	private static final String ATTACK_HOVER = "";
	private static final String SPECIAL_IMAGE = "";
	private static final String SPECIAL_HOVER = "";
	private static final String ITEM_IMAGE = "";
	private static final String ITEM_HOVER = "";
	private static final String TALK_IMAGE = "";
	private static final String TALK_HOVER = "";
	private static final String WAIT_IMAGE = "";
	private static final String WAIT_HOVER = "";
	
	private class CombatOption extends MenuItem {
		
		private PlayerCombatController _source;

		public CombatOption(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt, PlayerCombatController source) {
			super(container, defltPath, hoveredPath, dt);
			_source = source;
		}
		
		@Override
		public void activate(int type) {
			if (type != MouseEvent.BUTTON1)
				return;
		}
	}
	
	private BufferedImage _img;
	private List<CombatOption> _options;
	
	public CombatOptionWindow(JPanel container, boolean attack, boolean special, boolean item, boolean talk) {
		super(container);
		_options = new ArrayList<CombatOption>();
		
		if (attack) {
			
		}
		if (special) {
			
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
