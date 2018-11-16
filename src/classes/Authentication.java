package classes;
import java.math.BigInteger;

public class Authentication {
	
	/**
	 * 
	 * @param string raw-text password
	 * @return a string of the hashed password
	 */
	public static String hashString(String string)	{

		BigInteger value = new BigInteger("0");
		
		for (int i = 0; i < string.length(); i++)   {
			value = value.add(new BigInteger(String.format("%d",Math.round(string.charAt(i)*Math.pow(31, string.length() - (i + 1))))));
		}
		return value.toString();
	}
	
}