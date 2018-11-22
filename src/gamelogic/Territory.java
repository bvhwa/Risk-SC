package gamelogic;

public class Territory {
	
	/*
	 * Private Instance Variables
	 */
	private String name;
	private int id, occupier, troops;
	
	/*
	 * Constructors	
	 */
	
	/**
	 * Default Constructor
	 */
	public Territory()	{
		this.name = "";
		this.id = -1;
		this.occupier = -1;
		this.troops = 0;
	}
	
	/**
	 * 
	 * @param name the name of the territory
	 * @param id the id of the territory
	 * @param occupier the player id of the player that controls this territory
	 * @param troops the number of troops belonging to this territory
	 */
	public Territory(String name, int id, int occupier, int troops)	{
		this.name = name;
		this.id = id;
		this.occupier = occupier;
		this.troops = troops;
	}

	/*
	 * Getters and Setters of Private Instance Variables
	 */
	
	/**
	 * 
	 * @return the name of the territory
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return the territory ID 
	 */
	public int getID()	{
		return this.id;
	}

	/**
	 * 
	 * @return occupier
	 */
	public int getOccupier() {
		return occupier;
	}

	/**
	 * 
	 * @param occupier the occupier to set
	 */
	public void setOccupier(int occupier) {
		this.occupier = occupier;
	}

	/**
	 * 
	 * @return the number of troops residing upon this territory
	 */
	public int getTroops() {
		return troops;
	}

	/**
	 * 
	 * @param troops the troops to set
	 */
	public void setTroops(int troops) {
		this.troops = troops;
	}
	
	/**
	 * 
	 * @param troops the troops to add
	 */
	public void addTroops(int troops)	{
		this.troops += troops;
	}
	
	/**
	 * Decrements the amount of troops in the territory by troops
	 * @param troops the amount of troops to remove
	 */
	public void removeTroops(int troops)	{
		this.troops -= troops;
	}
	
	/**
	 * Decrements the amount of troops in the territory by one
	 */
	public void removeTroops()	{
		this.troops--;
	}
	
	@Override
	/**
	 * Creates and returns a clone of the current Territory object
	 * @return a cloned Territory object
	 */ 
	public Territory clone() {
		return new Territory(this.name, this.id, this.occupier, this.troops);
	}

	/**
	 * @return the string printed out in human-readable format
	 */
	public String toString()	{
		return this.name + " is occupied by Player " + this.occupier + " and has " + this.troops + " troops.";
	}
	
	
}
