package character;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import main.DoodleTactics;
import map.Tile;
import event.Dialogue;
import event.Event;
import event.InvalidEventException;
public class MapCharacter extends Character{
	/**
	 * @author czchapma
	 * represents a character that exists on the map that you can talk to
	 */
	
	Dialogue _dialogue;
	BufferedImage _profile, _down;
	
	public MapCharacter(DoodleTactics dt,Tile[][] allTiles, int xLoc, int yLoc, String dialogueFile,BufferedImage profile,BufferedImage mapImage)
	throws IOException,InvalidEventException{
		//Create Dialogue Event
		_dialogue = new Dialogue(dt,dialogueFile,dt.getCharacterMap());
				
		//Set all adjacent tiles to trigger dialogue event
		if(yLoc < allTiles[0].length - 1)
			allTiles[xLoc][yLoc + 1].setTriggersDialogue(_dialogue);
		
		this.setSize(_down.getWidth(), _down.getHeight());
		int overflow = 0;
		if(_down.getWidth() - Tile.TILE_SIZE <= 25.0)
			overflow = (_down.getWidth() - Tile.TILE_SIZE) / 2;
		this.setLocation(x - overflow,y - _down.getHeight() + Tile.TILE_SIZE);

	}
}
