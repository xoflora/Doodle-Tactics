package event;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import controller.combatController.CombatController;
import controller.combatController.CombatOrchestrator;
import controller.combatController.AIController.AICombatController;

import main.DoodleTactics;
import map.Tile;
import character.Archer;
import character.Character;
import character.Mage;
import character.Thief;
import character.Warrior;

public class CombatEvent extends Event{

	private CombatOrchestrator _co;
	private WinCondition _winConditions;
	private int _roundsToWin;
	public CombatEvent(DoodleTactics dt, boolean hasOccured,WinCondition type, int numRounds, CombatOrchestrator co) {
		super(dt, hasOccured);
		_co = co;
		_winConditions = type;
		_roundsToWin = numRounds;
	}


	public enum WinCondition{
		ROOT,
		SURVIVE
	}

	public enum FactionType{
		Enemy,
		Neutral,
		Partner

	}
	/**
	 * 
	 * @param filepath - file to parse
	 * @return the CombatEvent represented by the file
	 * 
	 * File:
	 * <Win Condition, Root or Survive> 
	 */
	public static CombatEvent parseEvent(DoodleTactics dt,String filepath) throws InvalidEventException{
		WinCondition wc = null;
		int numUnits;
		HashMap<String,FactionType> groupNameToType = new HashMap<String,FactionType>();
		HashMap<String,HashMap<Character,Tile>> nameToCharacters = new HashMap<String,HashMap<Character,Tile>>();
		int numRounds = -1;

		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			numUnits = Integer.parseInt(br.readLine());
			String line = br.readLine();

			//Check Win Conditions
			if(line.startsWith("root")){
				wc = WinCondition.ROOT;
			} else if(line.startsWith("survive")){
				wc = WinCondition.SURVIVE;
				numRounds = Integer.parseInt(line.split(",")[1]);
			}

			//Get faction types
			String[] split = br.readLine().split(",");
			while(split.length == 2){
				groupNameToType.put(split[1], getFactionType(split[0]));
				nameToCharacters.put(split[1], new HashMap<Character,Tile>());
				split = br.readLine().split(",");
			}

			//Loop through Characters
			line = br.readLine();
			while(line != null){
				split = line.split(",");
				
				//Map Character:
				//faction,name,tileX,tileY
				if(split.length == 4){
					String group = split[0];
					Character toAdd = dt.getCharacter(split[1]);
					Tile t = dt.getGameScreen().getMap().getTile(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
					nameToCharacters.get(group).put(toAdd, t);
				} else if(split.length == 8){
					String group = split[0];
					String type = split[0];
					int x = Integer.parseInt(split[7]);
					int y = Integer.parseInt(split[8]);
					Character c;
					if(type.equalsIgnoreCase("mage"))
						c = new Mage(dt,dt.getGameScreen(),split[2],split[3],split[4],split[5],split[6],"Mage",x,y);
					else if(type.equalsIgnoreCase("warrior"))
						c = new Warrior(dt,dt.getGameScreen(),split[2],split[3],split[4],split[5],split[6],"Warrior",x,y);
					else if(type.equalsIgnoreCase("archer"))
						c = new Archer(dt,dt.getGameScreen(),split[2],split[3],split[4],split[5],split[6],"Archer",x,y);
					else if(type.equalsIgnoreCase("thief"))
						c = new Thief(dt,dt.getGameScreen(),split[2],split[3],split[4],split[5],split[6],"Mage",x,y);
					else
						throw new InvalidEventException("CombatEvent- expected Character class, received: " + type);
					Tile t = dt.getGameScreen().getMap().getTile(x, y);
					nameToCharacters.get(group).put(c, t);
				} else{
					throw new InvalidEventException("CombatEvent- incorrect number of items on line");
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			throw new InvalidEventException(filepath + "could not be opened");
		} catch (IOException e) {
			throw new InvalidEventException("Something went wrong while reading " + filepath);
		}
		
		
		
		//Create CombatControllers
		List<CombatController> enemies = new LinkedList<CombatController>();
		List<CombatController> partners = new LinkedList<CombatController>();
		List<CombatController> neutrals = new LinkedList<CombatController>();
		
		
		for(String s: nameToCharacters.keySet()){
			AICombatController toAdd = new AICombatController(dt, nameToCharacters.get(s));
			if(groupNameToType.get(s) == FactionType.Enemy)
				enemies.add(toAdd);
			else if(groupNameToType.get(s) == FactionType.Neutral)
				neutrals.add(toAdd);
			else
				partners.add(toAdd);
		}
		return new CombatEvent(dt,false, wc,numRounds, new CombatOrchestrator(dt,enemies,partners,neutrals,numUnits));
	}


	public static FactionType getFactionType(String s){
		if(s.equalsIgnoreCase("enemy"))
			return FactionType.Enemy;
		else if(s.equalsIgnoreCase("partner"))
			return FactionType.Enemy;
		else if (s.equalsIgnoreCase("neutral"))
			return FactionType.Neutral;
		else
			return null;
	}
	
	@Override
	public void take(){
		System.out.println("COMBAT EVENT!");
		_gameScreen.pushControl(_co);
	}


	@Override
	public String save() {
		// TODO Auto-generated method stub
		return null;
	}

}
