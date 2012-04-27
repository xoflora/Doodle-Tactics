package graphics;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import controller.Controller;

import main.DoodleTactics;
import main.Screen;

public class GameMenuItem extends MenuItem {
	
		
		private Controller _control;
		
		public GameMenuItem(JPanel container, BufferedImage defltPath, BufferedImage hoveredPath,
				DoodleTactics dt, Screen screen, Controller control) {
			super(container, defltPath, hoveredPath, dt);
			_control = control;
		}

		/**
		 * overrides method in MenuItem
		 */
		@Override
		public void activate(int type) {
			//_dt.pushController(_control);
		}
	}
