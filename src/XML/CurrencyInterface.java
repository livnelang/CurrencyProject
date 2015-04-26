package XML;

/**
 * This interface defines essential methods 
 * required to the user.
 *
 *@see Client#check_input(String)
 *@see Client#convert(double, double, double)
 */
public interface CurrencyInterface 
{
	boolean check_input(String input1);							   // Check Validate Input
	double convert(double sum, double rate, double divide_rate);  // Exchange User Currencies
}
