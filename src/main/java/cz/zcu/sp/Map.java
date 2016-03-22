package cz.zcu.sp;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * T��da s mapou �esk� republiky
 * @author Michal Proch�zka (A14B0339P) 
 */
public class Map {

	/** N�zev vstupn�ho souboru pro souradnice okres� **/
	private final String vstupniSouborOkresu = "data/kraje_cr.txt";
	
	/** N�zev vstupn�ho souboru pro statistiky**/
	private final String vstupniPOP_WRK_CRM = "data/statistiky.txt";
	
	/** N�zev vstupn�ho souboru, kde jsou dal�� informace pro ka�d� okres zvl᚝ **/
	private final String vstupniSouborInformaci = "data/DoplnujiciInfo/";
	
	/** ���ka vykreslovac�ho okna **/
	private double widthVykres;
	
	/** V��ka vykreslovac�ho okna **/
	private double heightVykres;
	
	/** ���ka mapy **/
	double widthMap = 0;
	
	/** V��ka mapy **/
	double heightMap = 0;
	
	/** Pom�r pro vykreslen�, aby se vykreslovan� prvek ve�el na pl�tno **/
	double pomer = 0.0;
	
	/** Seznam okres� **/
	ArrayList<Okres> regions;
	
	/** Seznam s po�tem obyvatel �R pro ka�d� rok **/
	ArrayList<Data> POPCelkem;
	
	/** Maximum mapy **/
	Point2D.Double max;
	
	/** Minimum mapy **/
	Point2D.Double min;


	/**
	 * Vytvo�en� mapy �esk� republiky a hodnoty pro jednotliv� okres
	 * @param width ���ka vykreslovac�ho okna
	 * @param height v��ka vykreslovac�ho okna
	 */
	public Map(double width, double height)
	{
		this.widthVykres = width;
		this.heightVykres = height;
		this.regions = new ArrayList<Okres>();
		this.POPCelkem = new ArrayList<Data>();
			
		readData(vstupniSouborOkresu);
		
		this.max = (Point2D.Double)regions.get(0).mapRegion.getMax().clone();
		this.min = (Point2D.Double)regions.get(0).mapRegion.getMin().clone();

		for (Okres okres : regions) {	
			if (okres.mapRegion.getMin().x < min.x) min.x = okres.mapRegion.getMin().x;
			if (okres.mapRegion.getMin().y < min.y) min.y = okres.mapRegion.getMin().y;
			if (okres.mapRegion.getMax().x > max.x) max.x = okres.mapRegion.getMax().x;
			if (okres.mapRegion.getMax().y > max.y) max.y = okres.mapRegion.getMax().y;
		}
		
		widthMap = Math.abs(Math.abs(max.x) - Math.abs(min.x));
		heightMap = Math.abs(Math.abs(max.y) - Math.abs(min.y));
		
		//Spo�ten� pom�ru pro vykreslov�n�
		if(heightMap*(widthVykres/widthMap) < heightVykres)
			pomer = widthVykres/widthMap;
		else
			pomer = heightVykres/heightMap;
		
		///nacteni barev atd...
		//this.readPOP_WRK_CRM(vstupniPOP_WRK_CRM);
		
		for (int i = 2000; i <= 2013; i++) {
			int celkem = 0;
			for (Okres okres : regions) {
				celkem += okres.getPOPUdaj(i);
			}
			POPCelkem.add(new Data(i, celkem));
		}
				
	}
	
	/**
	 * �ten� dat pro vykreslen� mapy
	 * @param path cesta k souboru
	 */
	public void readData(String path) {

		BufferedReader br = null;
		
		try {
			
			String sCurrentLine = "";
			br = new BufferedReader( new InputStreamReader(new FileInputStream(path), "UTF-8"));
 
			try {
				while ((sCurrentLine = br.readLine()) != null) {
					
					String[] data = sCurrentLine.split(",");				
					String lineWithPoints = br.readLine().trim();
					lineWithPoints = lineWithPoints.substring(1, lineWithPoints.length() - 1);
					String pointsString[] = lineWithPoints.split("\\),\\(");
					Point2D.Double points[] = new Point2D.Double[pointsString.length];				
					for (int i = 0; i < points.length; i++) {
						String singlePoint[] = pointsString[i].split(",");
						points[i] = new Point2D.Double(Double.parseDouble(singlePoint[0]),Double.parseDouble(singlePoint[1])*-1);
					}
					
					this.regions.add(
							new Okres(
							data[0].split(":")[1],
							data[1].split(":")[1],  
							points));
				}				
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Soubor neni v predpokladanem formatu. Program bude ukoncen "+ sCurrentLine);
				System.exit(1);
			}
			finally 
			{
				try 
				{
					if (br != null)br.close();
				} 
				catch (Exception ex) 
				{
					ex.printStackTrace();
				}
			}
		}
		catch (Exception e) 
		{
			System.out.println("Soubor se nepodarilo precist. Program bude ukoncen");
			System.exit(1);
		}
		
		
	}
	
	/**
	 * �ten� dat a ulo�en� do jednotliv�ch okres�
	 * @param path cesta k souboru
	 */
	public void readPOP_WRK_CRM(String path) {

		BufferedReader br = null;
		
		try {
			
			String sCurrentLine = "";
			br = new BufferedReader( new InputStreamReader(new FileInputStream(path)));
			
			try {
		
				while ((sCurrentLine = br.readLine()) != null) {

					String nazev = sCurrentLine.trim();					
					String pop = br.readLine().trim();
					String wrk = br.readLine().trim();
					String crm = br.readLine().trim();
					
					
					boolean zapsano = false;
					for (Okres okres : regions) {
						if(okres.nazev.toLowerCase().equals(nazev.toLowerCase()))
						{
							String[] data = pop.split(";");
							okres.AddPOP(data);
							
							data = wrk.split(";");
							okres.AddWRK(data);
							
							data = crm.split(";");
							okres.AddCRM(data);
							
							okres.ulozDalsiInfo(readDalsiInfo(nazev));
							
							zapsano = true;
							
							
							break;
						}
					}
					
					if(!zapsano)
						System.out.println("Chyba zaps�n� m�sta:"+nazev);					
				}				
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Soubor neni v predpokladanem formatu. Program bude ukoncen "+ sCurrentLine);
				System.exit(1);
			}
			finally 
			{
				try 
				{
					if (br != null)br.close();
				} 
				catch (Exception ex) 
				{
					ex.printStackTrace();
				}
			}
		}
		catch (Exception e) 
		{
			System.out.println("Soubor se nepodarilo precist. Program bude ukoncen");
			System.exit(1);
		}
		
	}
	
	/**
	 * �ten� dal��ch informaci pro dan� okres
	 * @param nazevSouboruInformaci n�zev souboru 
	 * @return seznam s informacemi
	 */
	public ArrayList<String> readDalsiInfo(String nazevSouboruInformaci) {

		ArrayList<String> dalsiInfo = new ArrayList<String>();
		BufferedReader brinfo = null;
		
		try {
			
			String sCurrentLine = "";
			brinfo = new BufferedReader( new InputStreamReader(new FileInputStream(vstupniSouborInformaci+nazevSouboruInformaci+".txt")));
			
			try {
		
				while ((sCurrentLine = brinfo.readLine()) != null) {
					dalsiInfo.add(sCurrentLine);					
				}				
			}
			catch (Exception e) {
				//System.out.println("Soubor neni v predpokladanem formatu. Program bude ukoncen "+ sCurrentLine);
			}
			finally 
			{
				try 
				{
					if (brinfo != null)brinfo.close();
				} 
				catch (Exception ex) 
				{
				}
			}
		}
		catch (Exception e) 
		{
			//System.out.println("Soubor se nepodarilo precist nebo neexistuje.");
		}
		
		return dalsiInfo;
		
	}
	
	/**
	 * Vykresl� panel s informacemi
	 * @param g2 Graphics2D
	 * @param rok vybran� rok
	 * @param sirkaInfoPanelu ���ka panelu s informacemi
	 * @param velikostNadpisu velikost p�sma pro nadpis
	 * @param velikostPisma velikost ostan�ho p�sma
	 * @param vyberMesta vybrn� m�sto pro kter� se maj� informace vykreslit
	 * @param metoda metoda vizualizace
	 */
	public void DrawInfo(Graphics2D g2, int rok, int sirkaInfoPanelu, int velikostNadpisu, int velikostPisma, String vyberMesta, String metoda)
	{
		for (Okres okres : regions) {
			if(okres.zkratka.equals(vyberMesta))
			{
				okres.drawInfo(g2, rok, sirkaInfoPanelu, velikostNadpisu, velikostPisma, metoda, POPCelkem);
				break;
			}
		}
	}
	
	/**
	 * Vykreslen� mapy
	 * @param g2 Graphics2D
	 * @param metoda metoda co se m� vykreslovat (podle metody - barevn� �k�la)
	 * @param rok vybran� rok
	 */
	public void DrawMapa (Graphics2D g2, String metoda, int rok)
	{
		int celkem  = 0;
		for (Data udaje : POPCelkem) {
			if(udaje.Rok() == rok)
				celkem = udaje.Udaj();
		}
		
		for (Okres okres : regions) {
			okres.DrawRegion(g2, pomer, metoda, rok, celkem);
		}
	}
	
	/**
	 * Vykresl� zv�razn�n� okres
	 * @param g2 Graphics2D
	 * @param vyberMesta vybran� m�sto pro zv�razn�n�
	 */
	public void DrawThisRegion(Graphics2D g2, String vyberMesta)
	{
		for (Okres okres : regions) {
			if(okres.zkratka.equals(vyberMesta))
			{
				okres.DrawThisRegion(g2, pomer);
				break;
			}
		}
	}
	
	/**
	 * Vr�t� n�zev m�sta pokud bude na n�j kliknut, jinak vr�t� hodnotu Plze�-m�sto
	 * @param point sou�adnice kliknut� my�i
	 * @return n�zev m�sta
	 */
	public String isInArea(Point point)
	{
		String vyberMesta = "";
		for (Okres okres : regions) {
			vyberMesta = okres.isInArea(point);
			
			if(vyberMesta != "")
				return vyberMesta;
		}
		
		return "PM";
	}
	
	/**
	 * Maximum mapy
	 * @return maximum mapy
	 */
	public Point2D.Double getMax()
	{
		return this.max;
	}
	
	/**
	 * Minimum mapy
	 * @return minimum mapy
	 */
	public Point2D.Double getMin()
	{
		return this.min;
	}	
	
	/**
	 * Pom�r vykreslen�
	 * @return pom�r vykreslen�
	 */
	public double getPomer()
	{
		return this.pomer;
	}
		
	/**
	 * ���ka mapy
	 * @return ���ka mapy
	 */
	public double getWidthMap()
	{
		return this.widthMap;
	}
	
	/**
	 * Velikost mapy
	 * @return velikost mapy
	 */
	public double getHeightMap()
	{
		return this.heightMap;
	}

	/**
	 * Nastav� dal�� infro pro vykreslen�
	 */
	public void dalsiInfo(String vyberMesta)
	{
		for (Okres okres : regions) {
			if(okres.zkratka.equals(vyberMesta))
			{
				okres.dalsiInfo();
			}
		}
	}
	
}
