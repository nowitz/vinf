package cz.zcu.sp;

/**
 * Data class stores information about the county for one year
 * @author Jan Novák & Marek Šimůnek 
 */
public class Data {
	
	/** Value **/
	private double value;
	
	/** Year **/
	private int year;
	
	/**
	 * Constructor for object Data
	 * @param year year
	 * @param value value
	 */
	public Data(int year, int value)
	{
		this.year = year;
		this.value = value;
	}
	
	/**
	 * Getter value
	 * @return value
	 */
	public double getValue()
	{
		return this.value;
	}
	
	/**
	 * Getter year
	 * @return year
	 */
	public int getYear()
	{
		return this.year;
	}
	
	/**
	 * Method for adjust valu
	 * @param value value
	 * @return ajust value
	 */
	public double Procenta(double value)
	{
		if(value == 0)
			return 0;
		//TODO opraviv vzorec pri data ... nejspis typu 0-9,25 (napr.:1,24)	
		return (((this.value)*100.0)/value);
	}
	
}
