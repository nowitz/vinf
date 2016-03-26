package cz.zcu.sp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *Region class serves as a model class for region (domain object)
 * 
 * @author Jan Novák & Marek Šimůnek
 */
public class Region {
	// ZKRATKA:ST,NAZEV:Stredocesky kraj
	String shortName;
	String name;
	MapRegion mapRegion;

	/**
	 * Data for region
	 */
	ArrayList<Data> dataList;

	//TODO netusim jestli to tu bude pak a bude se vyuzivat
	/** Actual infomations **/
	int actualInfo;

	/** list informations **/
	ArrayList<String> informations;

	/**
	 * Constructor for create region
	 * 
	 * @param shortName shortcut region
	 * @param name name region
	 * @param points coordinates region
	 */
	public Region(String shortName, String name, Point2D.Double points[]) {
		this.shortName = shortName;
		this.name = name;
		this.mapRegion = new MapRegion(points);

		this.actualInfo = 0;
		this.informations = new ArrayList<String>();

		dataList = new ArrayList<Data>();
	}

	/**
	 * Render regions at those coordinates
	 * 
	 * @param g2 Graphics2D
	 * @param ratio ratio for render region
	 * @param methodTypeData methodTypeData visualisation
	 * @param year choose year
	 */
	public void DrawRegion(Graphics2D g2, double ratio, String methodTypeData, int year) {
		Color color = Color.GRAY;
		for (Data data : dataList) {
			if (data.getYear() == year) {
				//TODO tady bude nejakej if kterej rozhodne jak se to ma vykreslot podle methodTypeData
				//double proc = data.Procenta(this.getDataValue(year));
				double proc = getDataValue(year);
				if (proc <= 1.0)
					color = new Color(255, 255, 204);
				else if (proc > 1.0 && proc <= 1.5)
					color = new Color(229, 255, 204);
				else if (proc > 1.5 && proc <= 2.0)
					color = new Color(204, 255, 153);
				else if (proc > 2.0 && proc <= 2.5)
					color = new Color(178, 255, 102);
				else if (proc > 2.5 && proc <= 3.0)
					color = new Color(153, 255, 51);
				else if (proc > 3.0 && proc <= 3.5)
					color = new Color(128, 255, 0);
				else if (proc > 3.5 && proc <= 4.0)
					color = new Color(102, 204, 0);
				else if (proc > 4 && proc <= 5.0)
					color = new Color(76, 153, 0);
				else if (proc > 5.0 && proc <= 7.0)
					color = new Color(51, 102, 0);
				else
					color = new Color(25, 51, 0);
			}
		}

		mapRegion.setColor(color);
		mapRegion.setOhraniceni(Color.BLACK);
		mapRegion.draw(g2, ratio, shortName);
	}

	/**
	 * Method for highlighting selected regio
	 * 
	 * @param g2 Graphics2D
	 * @param ratio ratio for render region
	 */
	public void DrawThisRegion(Graphics2D g2, double ratio) {
		mapRegion.setOhraniceni(Color.RED);
		mapRegion.draw(g2, ratio, shortName);
	}

	/**
	 * Method for return shortcut name region after click mouse
	 * 
	 * @param point coordinates region
	 * @return shortcut name region
	 */
	public String isInArea(Point point) {
		if (mapRegion.isInArea(point))
			return this.shortName;
		else
			return "";
	}

	/**
	 * Method taht saves data for region
	 * 
	 * @param data data for region
	 */
	public void addData(String[] data) {
		// TODO opravit vstupni year
		String[] _data = Arrays.copyOf(data, data.length);
		int year = 1999;
		for (int i = 1; i < _data.length; i++) {
			this.dataList.add(new Data(year + i, Integer.parseInt(_data[i])));
		}
	}

	/**
	 * Method that return value for current year
	 * 
	 * @param year selected year
	 * @return value value
	 */
	public double getDataValue(int year) {
		for (Data data : dataList) {
			if (data.getYear() == year) {
				return data.getValue();
			}
		}
		return 0;
	}

	/**
	 * Method for saves additional information on the list of information for the region
	 * 
	 * @param otherInfo
	 */
	public void ulozDalsiInfo(ArrayList<String> otherInfo) {
		if (otherInfo.size() > 0)
			for (int i = 0; i < otherInfo.size(); i++) {
				informations.add(otherInfo.get(i));
			}
	}

	/**
	 * Method for obtaining information for the region
	 * 
	 * @return other informations 
	 */
	public String getInformaci() {
		if (this.informations.size() > 0)
			return this.informations.get(actualInfo);
		else
			return "Information not available";
	}

}