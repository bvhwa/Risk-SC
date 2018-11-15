package classes;

public class User {
	
	/*
	 * Private Instance Variables
	 */
	
	private String firstName;
	private String lastName;
	private String userName;
	private String image;
	private boolean isHost;
	private int territories;
	private int troops;
	
	/*
	 * Constructors
	 */
	
	public User()	{
		this.firstName = "";
		this.lastName = "";
		this.userName = "";
		this.image = "https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwiD1OuxvtLdAhWNB3wKHd7NBDYQjRx6BAgBEAU&url=https%3A%2F%2Fcidco-smartcity.niua.org%2Fno-image-found%2F&psig=AOvVaw32m7njsNn-ln0B9xy6xvru&ust=1537838875192818";
		this.isHost = false;
		this.territories = 0;
		this.troops = 0;
	}
	
	public User(String firstName, String lastName, String userName, boolean isHost)	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.isHost = isHost;
		this.image = "https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwiD1OuxvtLdAhWNB3wKHd7NBDYQjRx6BAgBEAU&url=https%3A%2F%2Fcidco-smartcity.niua.org%2Fno-image-found%2F&psig=AOvVaw32m7njsNn-ln0B9xy6xvru&ust=1537838875192818";
		this.territories = 0;
		this.troops = 0;
	}
	
	/*
	 * Getters and Setters for the Private Instance Variables
	 */
	
	/**
	 * @return first name of the user
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * @param firstName the first name to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * 
	 * @return last name of the user
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * 
	 * @param lastName the last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	/**
	 * 
	 * @return user name of the user
	 */
	public String getUserName() {
		return userName;
	}

	
	/**
	 * 
	 * @param userName the user name to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * @return image of the user
	 */
	public String getImage() {
		return image;
	}

	/**
	 * 
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 
	 * @return if the user is a host
	 */
	public boolean isHost() {
		return isHost;
	}

	/**
	 * 
	 * @param isHost set whether the user is a host
	 */
	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	/**
	 * 
	 * @return the number of territories held by the user
	 */
	public int getTerritories() {
		return territories;
	}

	/**
	 * 
	 * @param territories the amount of territories to set
	 */
	public void setTerritories(int territories) {
		this.territories = territories;
	}

	/**
	 * 
	 * @return the number of troops held by the user
	 */
	public int getTroops() {
		return troops;
	}

	/**
	 * 
	 * @param troops the amount of troops to set
	 */
	public void setTroops(int troops) {
		this.troops = troops;
	}

	
	/**
	 * Increment the amount of territories held by the user
	 */
	public void addTerritory() {
		this.territories++;
	}
	
	/**
	 * Decrement the amount of territories held by the user
	 */
	public void removeTerritory()	{
		this.territories--;
	}
	
}
