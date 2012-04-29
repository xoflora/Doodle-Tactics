package main;

import graphics.MenuItem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import controller.ErrorScreenController;

public class ErrorScreen extends Screen<ErrorScreenController> {
	
	private MenuItem _title;
	private MenuItem _quit;
	private String _text;
	
	

	
	public ErrorScreen(DoodleTactics dt, String message) {
		super(dt);
		_dt = dt;
		setBackground(Color.GRAY);

		BufferedImage titleImage = dt.importImage("src/graphics/menu/title.png");
		
		_text = message;
		_title = new MenuItem(this, titleImage, titleImage, dt);
		
		_quit = new MenuItem(this, dt.importImage("src/graphics/menu/quit.png"),
				dt.importImage("src/graphics/menu/quit_hovered.png"), dt);

		_title.setSize(_title.getImage().getWidth(), _title.getImage().getHeight());
		_quit.setSize(_quit.getImage().getWidth(), _quit.getImage().getHeight());

		_title.setLocation(((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _title.getImage().getWidth())/2, 50);
		_quit.setLocation(80, 600);



		_title.setVisible(true);
		_quit.setVisible(true);

		setVisible(true);

	}
	
/*	private class ErrorMenu extends MenuItem{
		private BufferedImage _title,_quit,_quitHovered;
		public ErrorMenu(DoodleTactics dt,BufferedImage title,BufferedImage quit, BufferedImage quitHovered) {
			super(dt.getGameScreen(),quit,quitHovered,dt);
			_title = title;
			_quit =
		}
		
		@Override
		public void paint(java.awt.Graphics2D brush,BufferedImage img) {
			super.paint(brush, img);

			_title.paint(brush,);
			paintNext(brush);

		}

		
		
	}*/


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
		super.paint(brush);
		Graphics2D g = (Graphics2D) brush;

		_title.setSize(_title.getImage().getWidth(), _title.getImage().getHeight());
		_quit.setSize(_quit.getImage().getWidth(), _quit.getImage().getHeight());

		_title.paint(g, _title.getImage());
		_quit.paint(g, _quit.getImage());
		brush.setFont(new Font("Dialogue",Font.BOLD,40));
		brush.setColor(new Color(0,0,1));
		brush.drawString("an error occured",350,300);
		brush.setFont(new Font("Dialogue",Font.BOLD,25));
		int curr = 0;
		int count = 1;
		int end;
		int maxPerLine = 60;
		while(curr <= _text.length()){
			end = Math.min(curr + maxPerLine, _text.length() - 1);
			brush.drawString(_text.substring(curr, end), 150,300 + count*50);
			curr += maxPerLine;
			count++;
		}
	}
}
