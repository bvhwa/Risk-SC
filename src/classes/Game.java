package classes;

public class Game {
	
	private Territory[] map = new Territory[Adjacencies.getTerritories().length];
	
	public Game(int playerNum)	{
		
		init(playerNum);
		
	}
	
	private void init(int playerNum)	{
		initMap(playerNum);
	}
	
	private void initMap(int playerNum)	{
		
		int territoriesToAllocate[] = new int[playerNum];
		for (int i = 0; i < playerNum; i++)
			territoriesToAllocate[i] = this.map.length/playerNum;
		
		for (int i = 0; i < this.map.length; i++)	{
			int temp = (int) (Math.random()*playerNum);
			while (territoriesToAllocate[temp] == 0)
				temp = (int) (Math.random()*playerNum);
			
			this.map[i] = new Territory(Adjacencies.getTerritory(i), temp, (Math.random() < 0.5) ? 1 : 2);
			territoriesToAllocate[temp]--;
		}
		
		for (int i = 1; i < this.map.length; i++)	{
			// Select a random value below the current value
			int randIndex = (int) (Math.random()*i);
			
			// Swap the randomly selected value with one below it
			int temp = this.map[i].getOccupier();
			this.map[i].setOccupier(this.map[randIndex].getOccupier());
			this.map[randIndex].setOccupier(temp);
		}
	}

	/**
	 * @return the map
	 */
	public Territory[] getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Territory[] map) {
		this.map = map;
	}
	
	
}
