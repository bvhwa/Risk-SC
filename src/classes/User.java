package classes;

public class User {
	
	/*
	 * Private Instance Variables
	 */
	
	String firstName, lastName, userName, image;
	boolean isHost;
	int territories, troops;
	
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	public int getTerritories() {
		return territories;
	}

	public void setTerritories(int territories) {
		this.territories = territories;
	}

	public int getTroops() {
		return troops;
	}

	public void setTroops(int troops) {
		this.troops = troops;
	}
	
}
