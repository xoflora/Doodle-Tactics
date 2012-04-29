package main;

import graphics.MenuItem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import controller.ErrorScreenController;

public class ErrorScreen extends Screen<ErrorScreenController> {
	
	private MenuItem _title;
	private MenuItem _quit;

	public ErrorScreen(DoodleTactics dt) {
		super(dt);

		try {
			setBackground(Color.GRAY);

			BufferedImage titleImage = ImageIO.read(new File("src/graphics/menu/title.png"));

			_title = new MenuItem(this, titleImage, titleImage, dt);

			_quit = new MenuItem(this, ImageIO.read(new File("src/graphics/menu/quit.png")),
					ImageIO.read(new File("src/graphics/menu/quit_hovered.png")), dt);
			
			_title.setLocation(((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _title.getImage().getWidth())/2, 50);
			_quit.setLocation(320, 240);

		} catch(IOException e) {
			System.out.println("Fatal error.");
			System.exit(1);
		}	
	}

	@Override
	protected ErrorScreenController defaultController() {
		return new ErrorScreenController(_dt, this);
	}
	
	public MenuItem checkContains(java.awt.Point point) {
		_title.setDefault();
		_quit.setDefault();
		
		if (_title.contains(point)) {
			_title.setHovered();
			repaint();
			return _title;
		}
		else if (_quit.contains(point)) {
			_quit.setHovered();
			repaint();
			return _quit;
		}
		
		return null;
	}
	
	@Override
	public void paint(Graphics brush) {
		Graphics2D g = (Graphics2D) brush;
		
		_title.paint(g, _title.getImage());
		_quit.paint(g, _quit.getImage());
	}
}
