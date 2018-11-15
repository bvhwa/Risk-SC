package classes;

public class Territory {
	
	/*
	 * Private Instance Variables
	 */
	String name;
	int occupier, troops;
	
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOccupier() {
		return occupier;
	}

	public void setOccupier(int occupier) {
		this.occupier = occupier;
	}

	public int getTroops() {
		return troops;
	}

	public void setTroops(int troops) {
		this.troops = troops;
	}
	
	public String toString()	{
		return this.name + " is occupied by Player " + this.occupier + " and has " + this.troops + " troops.";
	}
	
	
}
