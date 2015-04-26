package XML;

/**
 * This class holds bank personal fields
 * 
 * @author Livne Lang & Uriel Chechique
 */
public class BankIsraelCurrency
{
	private String currencyName;
	private int unit;
	private String currencyCode;
	private String country;
	private double rate;
	private double changeValue;
	
	
	/**
	 * This function is the bank constructor
	 * 
	 * @param currencyName
	 * @param unit
	 * @param currencyCode
	 * @param country
	 * @param rate
	 * @param changeValue
	 */
	public BankIsraelCurrency(String currencyName, int unit, String currencyCode, String country, double rate, double changeValue) 
	{
		super();
		this.currencyName = currencyName;
		this.unit = unit;
		this.currencyCode = currencyCode;
		this.country = country;
		this.rate = rate;
		this.changeValue = changeValue;
	}
	
	/**
	 * This function construct only details relevant to our txt file
	 * 
	 * @param currencyName
	 * @param rate
	 */
	public BankIsraelCurrency(String currencyName, double rate)   // Constructor For Text Details
	{
		super();
		this.currencyName = currencyName;
		this.rate = rate;
	}
	
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public int getUnit() {
		return unit;
	}
	public void setUnit(int unit) {
		this.unit = unit;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getChangeValue() {
		return changeValue;
	}
	public void setChangeValue(double changeValue) {
		this.changeValue = changeValue;
	}
	@Override
	public String toString() 
	{
		return "BankIsraelCurrency [currencyName=" + currencyName + ", unit="
				+ unit + ", currencyCode=" + currencyCode + ", country="
				+ country + ", rate=" + rate + ", changeValue=" + changeValue
				+ "]";
	}
	
	
	
}
