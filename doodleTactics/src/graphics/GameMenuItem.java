package graphics;

import javax.swing.JPanel;

import controller.Controller;

import main.DoodleTactics;
import main.Screen;

public class GameMenuItem extends MenuItem {
	
		
		private Controller _control;
		
		public GameMenuItem(JPanel container, String defltPath, String hoveredPath,
				DoodleTactics dt, Screen screen, Controller control) {
			super(container, defltPath, hoveredPath, dt);
			_control = control;
		}

		/**
		 * overrides method in MenuItem
		 */
		@Override
		public void activate() {
			//_dt.pushController(_control);
		}
	}
