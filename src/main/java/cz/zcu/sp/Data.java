package cz.zcu.sp;

/**
 * Třída uchovávající informace o daném kraji pro jeden rok
 * @author Michal Proch�zka (A14B0339P) 
 */
public class Data {
	
	/** Udaj (opo�et obyvatel, kriminalita...) **/
	private int udaj;
	/** Rok pro udaj **/
	private int rok;
	
	/**
	 * Vytvo�� objekt udaje
	 * @param rok rok
	 * @param udaj udaj/hodnota
	 */
	public Data(int rok, int udaj)
	{
		this.rok = rok;
		this.udaj = udaj;
	}

	/**
	 * Vrati udaj pro tento rok
	 * @return udaj/hodnota
	 */
	public int Udaj()
	{
		return this.udaj;
	}
	
	/**
	 * Vrat� rok pro tento udaj
	 * @return rok
	 */
	public int Rok()
	{
		return this.rok;
	}
	
	/**
	 * Vrati procentualn� hodnotu vzhledem k celkov� hodnot�
	 * @param celkove hodnota
	 * @return procentuln� hodnota
	 */
	public double Procenta(int celkove)
	{
		if(celkove == 0)
			return 0;
			
		return ((double)(this.udaj)*100.0)/((double)(celkove));
	}
	
	/**
	 * Prida extra hodnotu k ji� vytvo�enemu udaji
	 * @param data extra data
	 */
	public void Add(int data)
	{
		this.udaj += data;
	}
}
