package event;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import main.DoodleTactics;

public class CombatEvent extends Event{

	public CombatEvent(DoodleTactics dt, boolean hasOccured) {
		super(dt, hasOccured);
	}


	public enum WinCondition{
		ROOT,
		SURVIVE
	}
	/**
	 * 
	 * @param filepath - file to parse
	 * @return the CombatEvent represented by the file
	 * 
	 * File:
	 * <Win Condition, Root or Survive> 
	 */
	public CombatEvent parseEvent(String filepath) throws InvalidEventException{
		WinCondition wc;
		LinkedList<Character> _combatants;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String line = br.readLine();
			//Chec
			int numRounds = -1;
			if(line.startsWith("root")){
				wc = WinCondition.ROOT;
			} else if(line.startsWith("survive")){
				wc = WinCondition.SURVIVE;
				numRounds = Integer.parseInt(line.split(",")[1]);
			}
			
			//Looping through types:
		/*	HashMap<String,Type>
			
			//Loop through Characters
			line = br.readLine();*/
			while(line != null){
				//Map Character
				if(!line.contains(",")){
					
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			throw new InvalidEventException(filepath + "could not be opened");
		} catch (IOException e) {
			throw new InvalidEventException("Something went wrong while reading " + filepath);

		}
		
		return null;
	}


	@Override
	public String save() {
		// TODO Auto-generated method stub
		return null;
	}

}
