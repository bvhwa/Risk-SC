
public class User {
	
	String firstName, lastName, userName, image;
	boolean isHost;
	int territories, troops;
	
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
	
}
