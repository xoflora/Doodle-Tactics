package controller.combatController;

public enum ActionType {
	
	SPECIAL,
	ATTACK,
	EQUIP,
	ITEM,
	TALK,
	TRADE,
	WAIT;
	
	public String toString() {
		switch (this) {
			case ATTACK:
				return "ATTACK";
			case ITEM:
				return "ITEM";
			case TALK:
				return "TALK";
			case SPECIAL:
				return "SPECIAL";
			case TRADE:
				return "TRADE";
			case WAIT:
				return "WAIT";
			default:
				return "";
		}
	}
}
