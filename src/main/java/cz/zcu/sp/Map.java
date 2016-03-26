package cz.zcu.sp;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Map class processes the input data and create object Map
 * @author Jan Novák & Marek Šimůnek 
 */
public class Map {

	/** Input file with data map **/
	private static final String INPUT_FILE_DATA_MAP = "data/kraje_cr.txt";
	
	/** Input file with data region **/
	private static final String INPUT_FILE_DATA_REGION = "data/DoplnujiciInfo/";
	
	/** width map **/
	double widthMap = 0;
	
	/** height map **/
	double heightMap = 0;
	
	/** Ration for render map **/
	double ratio = 0.0;
	
	/** List reions **/
	ArrayList<Region> regions;
	
	/** Max map **/
	Point2D.Double max;
	
	/** Min map **/
	Point2D.Double min;
	
	/** Magic constant for height map **/
	public static final double MAGIC_CONSTANT = 1.3;


	/**
	 * Constructor for create map with regions
	 * @param width width canvas
	 * @param height canvas
	 */
	public Map(double widthCanvas, double heightCanvas)
	{
		this.regions = new ArrayList<Region>();
		
		readDataMap(INPUT_FILE_DATA_MAP);
		
		this.max = (Point2D.Double)regions.get(0).mapRegion.getMax().clone();
		this.min = (Point2D.Double)regions.get(0).mapRegion.getMin().clone();

		for (Region region : regions) {	
			if (region.mapRegion.getMin().x < min.x) min.x = region.mapRegion.getMin().x;
			if (region.mapRegion.getMin().y < min.y) min.y = region.mapRegion.getMin().y;
			if (region.mapRegion.getMax().x > max.x) max.x = region.mapRegion.getMax().x;
			if (region.mapRegion.getMax().y > max.y) max.y = region.mapRegion.getMax().y;
		}
		
		//slouzi pro vypocitavani ratiou podle toho jak se ma roztahovat okno
		widthMap = Math.abs(Math.abs(max.x) - Math.abs(min.x));
		heightMap = Math.abs(Math.abs(max.y) - Math.abs(min.y));
		
		//Spčtení poměru pro vykreslovací vlákno
		if(heightMap*(widthCanvas/widthMap) < heightCanvas)
			ratio = widthCanvas/widthMap;
		else
			ratio = heightCanvas/heightMap;
	
		//TODO doresit nacteni a obarvovani dat
		//nacteni barev atd...
		//this.readData(INPUT_FILE_DATA_REGION);
		//to tu pak nebude....
		for (Region region : regions) {
			region.addData(new String[]{"1","2","3","4","5"});
		}
				
	}
	
	/**
	 * Method for rotation input data about -90°
	 * @param pointX
	 * @param pointY
	 * @return rotation point
	 */
	public Point2D.Double rotationPoint(double pointX, double pointY){
		double angle = -90 * Math.PI / 180;
	    double cos = Math.cos(angle);
	    double sin = Math.sin(angle);
	    double dx = pointX - 63.116994844800004;
	    double dy = pointY - 12.091104331;
	    double x = cos * dx - sin * dy + 63.116994844800004;
	    double y = sin * dx + cos * dy + 63.116994844800004;
	    
	    Point2D.Double rotated = new Point2D.Double(x, y);
		return rotated;	
	}
	
	/**
	 * Method for obtaining data from a file to map
	 * @param path url to file
	 */
	public void readDataMap(String path) {

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
					for (int i = 0; i < points.length; i++) { //nebo tady otocit kazdy bod o 90° (mapRegion.getMin()
						String singlePoint[] = pointsString[i].split(",");
						points[i] = rotationPoint(Double.parseDouble(singlePoint[0])*MAGIC_CONSTANT,Double.parseDouble(singlePoint[1]));
					}
					
					this.regions.add(new Region(data[0].split(":")[1],data[1].split(":")[1], points));
				}				
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("File is not in the anticipated format. The program will be terminated "+ sCurrentLine);
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
			System.out.println("The file could not be read. The program will be terminated.");
			System.exit(1);
		}
		
		
	}
	
	/**
	 * Method for obtaining data from a file to region
	 * @param path url to file
	 */
	public void readData(String path) {

		BufferedReader br = null;
		
		try {
			
			String sCurrentLine = "";
			br = new BufferedReader( new InputStreamReader(new FileInputStream(path)));
			
			try {
		
				while ((sCurrentLine = br.readLine()) != null) {

					String name = sCurrentLine.trim();					
					String value = br.readLine().trim();
					
					boolean entered = false;
					for (Region region : regions) {
						if(region.name.toLowerCase().equals(name.toLowerCase()))
						{
							String[] data = value.split(";");
							region.addData(data);										
							entered = true;			
							break;
						}
					}
					
					if(!entered)
						System.out.println("Error writing region:"+name);					
				}				
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("File is not in the anticipated format. The program will be terminated "+ sCurrentLine);
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
			System.out.println("The file could not be read. The program will be terminated.");
			System.exit(1);
		}
		
	}
		
	/**
	 * Depict map
	 * @param g2 Graphics2D
	 * @param methodTypeDataTypeData methodTypeDataTypeData what to render (by the methodTypeDataTypeData - the color scale)
	 * @param year current year
	 */
	public void DrawMapa (Graphics2D g2, String methodTypeDataTypeData, int year)
	{
		System.out.println("MAP: " + methodTypeDataTypeData +" - " + year);
	
		for (Region region : regions) {
			region.DrawRegion(g2, ratio, methodTypeDataTypeData, year);
		}
	}
	
	/**
	 * Depict highlight region
	 * @param g2 Graphics2D
	 * @param selectRegion select region for highlight
	 */
	public void DrawThisRegion(Graphics2D g2, String selectRegion)
	{
		for (Region region : regions) {
			if(region.shortName.equals(selectRegion))
			{
				region.DrawThisRegion(g2, ratio);
				break;
			}
		}
	}
	
	/**
	 * Method for return name region after click
	 * @param point coordinate click
	 * @return name region
	 */
	public Region isInArea(Point point)
	{
		String selectRegion = "";
		//TODO pokud region == PRH tak konkroluj 1. protoze jinak to najde stredocesky kraj
		if("PRH".contentEquals(regions.get(12).isInArea(point))){
			return regions.get(12);
		}else{
			for (Region region : regions) {
				selectRegion = region.isInArea(point);
				
				if(selectRegion != "")
					return region;
			}
		}	
		return regions.get(12);
	}
	
	
	/**
	 * Getter max map
	 * @return max map
	 */
	public Point2D.Double getMax()
	{
		return this.max;
	}
	
	/**
	 * Getter min map
	 * @return min map
	 */
	public Point2D.Double getMin()
	{
		return this.min;
	}	
	
	/**
	 * Getter ratio render
	 * @return ratio render
	 */
	public double getRatio()
	{
		return this.ratio;
	}
		
	/**
	 * Getter widht map
	 * @return width map
	 */
	public double getWidthMap()
	{
		return this.widthMap;
	}
	
	/**
	 * Getter heigh map
	 * @return heigh map
	 */
	public double getHeightMap()
	{
		return this.heightMap;
	}
	
}
