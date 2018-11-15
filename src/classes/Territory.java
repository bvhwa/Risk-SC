package classes;

public class Territory {
	
	/*
	 * Private Instance Variables
	 */
	private String name;
	private int occupier, troops;
	
	/*
	 * Constructors	
	 */
	public Territory()	{
		this.name = "";
		this.occupier = -1;
		this.troops = 0;
	}
	
	public Territory(String name, int occupier, int troops)	{
		this.name = name;
		this.occupier = occupier;
		this.troops = troops;
	}

	/*
	 * Getters and Setters of Private Instance Variables
	 */
	
	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return troops
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
	 * @param troops
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
	
	
	/**
	 * @return the string printed out in human-readable format
	 */
	public String toString()	{
		return this.name + " is occupied by Player " + this.occupier + " and has " + this.troops + " troops.";
	}
	
	
}
