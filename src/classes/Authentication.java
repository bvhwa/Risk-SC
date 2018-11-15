package classes;
import java.math.BigInteger;

public class Authentication {

	
	/*
	 * Given: A raw-text password
	 * Returns; Hashed password
	 */
	public static String hashString(String string)	{

		BigInteger value = new BigInteger("0");
		
		for (int i = 0; i < string.length(); i++)   {
			value = value.add(new BigInteger(String.format("%d",Math.round(string.charAt(i)*Math.pow(31, string.length() - (i + 1))))));
		}
		return value.toString();
	}
	
}