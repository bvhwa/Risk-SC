import java.math.BigInteger;

public class Authentication {

	public static String hashString(String string)	{

		BigInteger value = new BigInteger("0");
		
		for (int i = 0; i < string.length(); i++)
			value.add(new BigInteger(Double.toString(string.charAt(i)*Math.pow(31, string.length() - (i + 1)))));
		
		return value.toString();
	}
	
}