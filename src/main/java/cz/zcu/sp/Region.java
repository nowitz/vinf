package cz.zcu.sp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * T��da okres s informacemi a daty, t��da slou�� jako kontejner pro okres, tj.
 * m� sp�e vykresl dan� okres a panel s informacemi
 * 
 * @author Jan Novák & Marek Šimůnek
 */
public class Region {
	// ZKRATKA:ST,NAZEV:Stredocesky kraj
	String shortName;
	String name;
	MapRegion mapRegion;

	// Ostatn� data pro tento okres
	ArrayList<Data> dataList;

	/** Aktu�ln� index informace **/
	int aktualniInfo;

	/** seznam informac� **/
	ArrayList<String> informace;

	/**
	 * Vytvo�� okres s daty
	 * 
	 * @param zkratka
	 *            zkrat okresu
	 * @param nazev
	 *            cel� n�zev okresu
	 * @param points
	 *            sou�adnice okresu
	 */
	public Region(String shortName, String name, Point2D.Double points[]) {
		this.shortName = shortName;
		this.name = name;
		this.mapRegion = new MapRegion(points);

		this.aktualniInfo = 0;
		this.informace = new ArrayList<String>();

		dataList = new ArrayList<Data>();
	}

	/**
	 * Vykreslen� okresu na sou�adnic�ch z dat
	 * 
	 * @param g2
	 *            Graphics2D
	 * @param pomer
	 *            pomer pro vykreslen� okresu
	 * @param metoda
	 *            metoda vizualizace
	 * @param year
	 *            vybran� year
	 */
	public void DrawRegion(Graphics2D g2, double pomer, String metoda, int year) {
		Color barva = Color.GRAY;
		for (Data data : dataList) {
			if (data.getYear() == year) {
				//double proc = data.Procenta(this.getDataValue(year));
				double proc = getDataValue(year);
				if (proc <= 1.0)
					barva = new Color(255, 255, 204);
				else if (proc > 1.0 && proc <= 1.5)
					barva = new Color(229, 255, 204);
				else if (proc > 1.5 && proc <= 2.0)
					barva = new Color(204, 255, 153);
				else if (proc > 2.0 && proc <= 2.5)
					barva = new Color(178, 255, 102);
				else if (proc > 2.5 && proc <= 3.0)
					barva = new Color(153, 255, 51);
				else if (proc > 3.0 && proc <= 3.5)
					barva = new Color(128, 255, 0);
				else if (proc > 3.5 && proc <= 4.0)
					barva = new Color(102, 204, 0);
				else if (proc > 4 && proc <= 5.0)
					barva = new Color(76, 153, 0);
				else if (proc > 5.0 && proc <= 7.0)
					barva = new Color(51, 102, 0);
				else
					barva = new Color(25, 51, 0);
			}
		}

		mapRegion.setColor(barva);
		mapRegion.setOhraniceni(Color.BLACK);
		mapRegion.draw(g2, pomer, shortName);
	}

	/**
	 * Vykresl� zv�razn�n� okres
	 * 
	 * @param g2
	 *            Graphics2D
	 * @param pomer
	 *            pomer pro vykreslen� okresu
	 */
	public void DrawThisRegion(Graphics2D g2, double pomer) {
		mapRegion.setOhraniceni(Color.RED);
		mapRegion.draw(g2, pomer, shortName);
	}

	/**
	 * Vr�t� n�zev m�sta pokud bude na n�j kliknut, jinak vr�t� hodnotu
	 * Plze�-m�sto
	 * 
	 * @param point
	 *            sou�adnice kliknut� my�i
	 * @return n�zev m�sta
	 */
	public String isInArea(Point point) {
		if (mapRegion.isInArea(point))
			return this.shortName;
		else
			return "";
	}

	/**
	 * Vlo�� data do listu po�tu obyvatel
	 * 
	 * @param data
	 *            po�et obyvatel pro v�echny yeary
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
	 * Vr�t� hodnotu po�et obyvatel v dan�m roce
	 * 
	 * @param year
	 *            vybran� year
	 * @return po�et obyvatel v dan�m roce
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
	 * Ulo�� dal�� informace do seznamu informac� pro okres
	 * 
	 * @param dalsiInfo
	 */
	public void ulozDalsiInfo(ArrayList<String> dalsiInfo) {
		if (dalsiInfo.size() > 0)
			for (int i = 0; i < dalsiInfo.size(); i++) {
				informace.add(dalsiInfo.get(i));
			}
	}

	/**
	 * Z�sk� informaci pro mesto v po�ad�
	 * 
	 * @return dal�i informace
	 */
	public String getInformaci() {
		if (this.informace.size() > 0)
			return this.informace.get(aktualniInfo);
		else
			return "Informace nejsou k dispozici";
	}

}