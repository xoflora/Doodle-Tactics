package main;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import graphics.MenuItem;

public class QuitMenuButton extends MenuItem {

	public QuitMenuButton(JPanel container, BufferedImage defltPath,
			BufferedImage hoveredPath, DoodleTactics dt) {
		super(container, defltPath, hoveredPath, dt);
	}

	@Override
	public void activate(int type) {
		System.exit(0);
	}
	
	@Override
	public void setDefault() {
		super.setDefault();
	}
}
