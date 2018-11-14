package classes;

public class Territory {
	
	/*
	 * Private Instance Variables
	 */
	String name, occupier;
	int troops;
	
	/*
	 * Constructors	
	 */
	public Territory()	{
		this.name = "";
		this.occupier = "";
		this.troops = 0;
	}
	
	public Territory(String name, String occupier, int troops)	{
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

	public String getOccupier() {
		return occupier;
	}

	public void setOccupier(String occupier) {
		this.occupier = occupier;
	}

	public int getTroops() {
		return troops;
	}

	public void setTroops(int troops) {
		this.troops = troops;
	}
	
	
}
