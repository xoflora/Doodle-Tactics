package controller.combatController;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.DoodleTactics;
import graphics.MenuItem;
import graphics.Rectangle;

/**
 * selection window for the series of options that a unit has after moving
 * @author rroelke
 *
 */
public class CombatOptionWindow extends MenuItem {
	
	private static final String MENU_IMAGE_PATH = "src/graphics/characters/pokeball.png";
	private static final String ATTACK_IMAGE = "src/graphics/menu/attack.png";
	private static final String ATTACK_HOVER = "src/graphics/menu/attack_hovered.png";
	private static final String SPECIAL_IMAGE = "src/graphics/menu/special.png";
	private static final String SPECIAL_HOVER = "src/graphics/menu/special_hovered.png";
	private static final String ITEM_IMAGE = "src/graphics/menu/item.png";
	private static final String ITEM_HOVER = "src/graphics/menu/item_hovered.png";
	private static final String TALK_IMAGE = "";
	private static final String TALK_HOVER = "";
	private static final String WAIT_IMAGE = "src/graphics/menu/wait.png";
	private static final String WAIT_HOVER = "src/graphics/menu/wait_hovered.png";
	
	private class CombatOption extends MenuItem {
		
		private PlayerCombatController _source;
		private ActionType _action;

		public CombatOption(JPanel container, BufferedImage defltPath, BufferedImage hoveredPath,
				DoodleTactics dt, PlayerCombatController source, ActionType action) {
			super(container, defltPath, hoveredPath, dt);
			_source = source;
			_action = action;
		}
		
		@Override
		public void activate(int type) {
			if (type != MouseEvent.BUTTON1)
				return;
			_source.pushAction(_action);
		}
	}
	
	private List<CombatOption> _options;
	
	public CombatOptionWindow(DoodleTactics dt, JPanel container, boolean attack, boolean special, boolean item, boolean talk,
			PlayerCombatController source) throws IOException {
		
		super(container, ImageIO.read(new File(MENU_IMAGE_PATH)), ImageIO.read(new File(MENU_IMAGE_PATH)), dt);
		_options = new ArrayList<CombatOption>();
		
		if (attack) {
			_options.add(new CombatOption(container, ImageIO.read(new File(ATTACK_IMAGE)),
					ImageIO.read(new File(ATTACK_HOVER)), dt, source, ActionType.ATTACK));
		}
		if (special) {
			_options.add(new CombatOption(container, ImageIO.read(new File(SPECIAL_IMAGE)),
					ImageIO.read(new File(SPECIAL_HOVER)), dt, source, ActionType.SPECIAL));
		}
		if (item) {
			_options.add(new CombatOption(container, ImageIO.read(new File(ITEM_IMAGE)),
					ImageIO.read(new File(ITEM_HOVER)), dt, source, ActionType.ITEM));
		}
		if (talk) {
			_options.add(new CombatOption(container, ImageIO.read(new File(TALK_IMAGE)),
					ImageIO.read(new File(TALK_HOVER)), dt, source, ActionType.TALK));
		}
		_options.add(new CombatOption(container, ImageIO.read(new File(WAIT_IMAGE)),
				ImageIO.read(new File(WAIT_HOVER)), dt, source, ActionType.WAIT));
	}
}
