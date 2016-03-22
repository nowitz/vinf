package cz.zcu.sp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * T��da okres s informacemi a daty, t��da slou�� jako kontejner pro okres, tj. m� sp�e vykresl dan� okres a panel s informacemi
 * @author Michal Proch�zka (A14B0339P) 
 */
public class Okres
{
	//ZKRATKA:DC,NAZEV:D���n
	String zkratka;
	String nazev;
	MapRegion mapRegion;
	
	//Ostatn� data pro tento okres
	ArrayList<Data> POP;
	ArrayList<Data> WRK;
	ArrayList<Data> CRM;
	
	/** Aktu�ln� index informace **/
	int aktualniInfo;
	
	/** seznam informac� **/
	ArrayList<String> informace;
	

	
	/**
	 * Vytvo�� okres s daty
	 * @param zkratka zkrat okresu
	 * @param nazev cel� n�zev okresu
	 * @param points sou�adnice okresu
	 */
	public Okres(String zkratka, String nazev,Point2D.Double points[])
	{
		this.zkratka = zkratka;
		this.nazev = nazev;
	    this.mapRegion = new MapRegion(points);
	    
	    this.aktualniInfo = 0;
	    this.informace = new ArrayList<String>();
	    
	    POP = new ArrayList<Data>();
	    WRK = new ArrayList<Data>();
	    CRM = new ArrayList<Data>();
	}
	
	/**
	 * Vykreslen� okresu na sou�adnic�ch z dat
	 * @param g2 Graphics2D
	 * @param pomer pomer pro vykreslen� okresu
	 * @param metoda metoda vizualizace
	 * @param rok vybran� rok
	 * @param celkem po�et obyvatel celkem k dan�mu roku
	 */
	public void DrawRegion(Graphics2D g2, double pomer, String metoda, int rok, int celkem)
	{	
		Color barva = Color.GRAY;
			for (Data udaje : CRM) {
					if(udaje.Rok() == rok)
					{
						double proc = udaje.Procenta(this.getPOPUdaj(rok));
						if(proc <= 1.0)
							barva = new Color(255,255,204);
						else
							if(proc >1.0 && proc <= 1.5)
								barva = new Color(229,255,204);
							else
								if(proc >1.5 && proc <= 2.0)
									barva = new Color(204,255,153);	
								else
									if(proc >2.0 && proc <= 2.5)
										barva = new Color(178,255,102);	
									else
										if(proc >2.5 && proc <= 3.0)
											barva = new Color(153,255,51);	
										else
											if(proc >3.0 && proc <= 3.5)
												barva = new Color(128,255,0);	
											else
												if(proc >3.5 && proc <= 4.0)
													barva = new Color(102,204,0);	
												else
													if(proc >4 && proc <= 5.0)
														barva = new Color(76,153,0);	
													else
														if(proc >5.0 && proc <= 7.0)
															barva = new Color(51,102,0);	
														else
																barva = new Color(25,51,0);	
					}
				}
			
		mapRegion.setColor(barva);
		mapRegion.setOhraniceni(Color.BLACK);
		mapRegion.draw(g2, pomer, zkratka);
	}
	
	/**
	 * Vykresl� zv�razn�n� okres
	 * @param g2 Graphics2D
	 * @param pomer pomer pro vykreslen� okresu
	 */
	public void DrawThisRegion(Graphics2D g2, double pomer)
	{
		mapRegion.setOhraniceni(Color.RED);
		mapRegion.draw(g2, pomer, zkratka);
	}

	/**
	 * Vykreslen� informac� do po�adovan� ���ky "panelu"
	 * @param g2 Graphics2D
	 * @param rok vybran� aktu�ln� rok
	 * @param sirkaInfoPanelu ���ka penalu pro vykreslen�
	 * @param velikostNadpisu velikost p�sma pro nadpis
	 * @param velikostPisma velikost ostatn�ho p�sma
	 * @param metoda metoda vizualizace
	 * @param celkemObyvatel celkov� po�et obyvatel
	 */
	public void drawInfo(Graphics2D g2,int rok, int sirkaInfoPanelu, int velikostNadpisu, int velikostPisma, String metoda, ArrayList<Data> celkemObyvatel)
	{
		AffineTransform puvodniTR = g2.getTransform();
		Font fontNadpis = new Font("Arial", Font.BOLD, velikostNadpisu);
		Font font = new Font("Arial", Font.BOLD, velikostPisma);
	
		g2.setFont(fontNadpis);
		g2.translate(0, 0);
		g2.setColor(Color.BLACK);
		
		TextLayout labelLayout = new TextLayout("Okres: "+nazev, fontNadpis, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(0),(float)(0));
		double vyskaNadpisu = labelLayout.getAscent() + labelLayout.getDescent();
		
		g2.setFont(font);
		//Pocet obyvatel info
		int pocetObyvatel = this.getPOPUdaj(rok);
		labelLayout = new TextLayout("Po�et obyvatel: "+pocetObyvatel, font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(0),(float)(vyskaNadpisu));
		double vyskaOstatni = labelLayout.getAscent() + labelLayout.getDescent();
		
		//Pracovni sila info
		int pracovniSila = this.getWRKUdaj(rok);
		labelLayout = new TextLayout("Pracovn� s�la: "+pracovniSila+ " ("+(pocetObyvatel == 0 ? 0 :((pracovniSila*100)/pocetObyvatel))+" %)", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(0),(float)(vyskaNadpisu+vyskaOstatni));
		
		//Kriminalita info
		int kriminalita = this.getCRMUdaj(rok);
		labelLayout = new TextLayout("Trestn�ch �in�: "+kriminalita+ " ("+(pocetObyvatel == 0 ? 0 :((kriminalita*100)/pocetObyvatel))+" %)", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(0),(float)(vyskaNadpisu+vyskaOstatni*2));
		
	
		labelLayout = new TextLayout("Pr�b�h trestn�ch �in�", font, g2.getFontRenderContext());

				
		
		labelLayout.draw(g2, (float)(0),(float)(vyskaNadpisu+vyskaOstatni*4));
		g2.translate(40, vyskaNadpisu+vyskaOstatni*3+20);
	
    	//Vykreslen� grafu
		g2.setStroke(new BasicStroke((float)1));
		drawChart(g2,sirkaInfoPanelu-50, rok, metoda,celkemObyvatel);
		
		
		//Vykresleni textov�ch informac�
		g2.setTransform(puvodniTR);
		g2.setColor(Color.BLACK);
		labelLayout = new TextLayout("Dal�� informace:", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(0),(float)(sirkaInfoPanelu+80));
		g2.translate(0, sirkaInfoPanelu+vyskaOstatni+70);
		g2.setColor(new Color(80,80,80));
		drawStringBreakLine(g2, getInformaci(), font, sirkaInfoPanelu);
		
	
	}
	
	/**
	 * Vykreslen� grafu pro pr�b�h dat pro v�echny roky okresu
	 * @param g2 Graphics2D
	 * @param sirkaInfoPanelu ���ka palenu pro informace
	 * @param aktualniRok vybran� aktu�ln� rok 
	 * @param metoda metoda vizualizace
	 * @param celkemObyvatel celkov� po�et obyvatel cr
	 */
	public void drawChart(Graphics2D g2, int sirkaInfoPanelu, int aktualniRok, String metoda, ArrayList<Data> celkemObyvatel)
	{
		Font font = new Font("Arial", Font.BOLD,10);
		g2.setFont(font);
		
		double vyskaGrafu = sirkaInfoPanelu;
		int pocetRoku = this.POP.size();
		double pocetBlokuProcenta = 10.0;
		
		double velikostBlokuY = 0;
		double velikostBlokuX = sirkaInfoPanelu/1;
		
		
		pocetBlokuProcenta = 10;


		velikostBlokuY = vyskaGrafu/pocetBlokuProcenta;
		
		//Vykresleni os X a Y
		g2.setColor(Color.BLACK);
		g2.drawLine(0, (int)vyskaGrafu, sirkaInfoPanelu, (int)vyskaGrafu);
		g2.drawLine(0, (int)vyskaGrafu, 0, 0);
		AffineTransform puvodniTR;
		for (int i = 1; i <= pocetRoku; i++) {
			puvodniTR = g2.getTransform();
			g2.translate(i*velikostBlokuX, vyskaGrafu);
			g2.drawLine(0, 0-5, 0, 0+5);
			
			AffineTransform tr = g2.getTransform();
			tr.rotate((Math.PI/180)*90);
			g2.setTransform(tr);
			g2.drawString(1999+i+"", 10 , 3);
			g2.setTransform(puvodniTR);
		}
		
		if(metoda.equals("WRK"))
			for (int i = 1; i <= pocetBlokuProcenta; i++) {
				g2.drawLine(-5, (int)(vyskaGrafu-(i*velikostBlokuY)), 5,(int)( vyskaGrafu-(i*velikostBlokuY)));
				g2.drawString(10*(i)+"%", -35 , (int)(vyskaGrafu-(i*velikostBlokuY)+3));
			}
		else
			for (double i = 1; i <= pocetBlokuProcenta; i+=1) {
				g2.drawLine(-5, (int)(vyskaGrafu-(i*velikostBlokuY)), 5,(int)( vyskaGrafu-(i*velikostBlokuY)));
				g2.drawString(String.format("%.0f", (1*i))+"%", -35 , (int)(vyskaGrafu-(i*velikostBlokuY)+3));
			}
			
		
		//Vykreslen� hodnot
		puvodniTR = g2.getTransform();
		double xpuvodni = 0;
		double ypuvodni = 0;
		
		for (int i = 1; i <= pocetRoku; i++) {
			
			double pop = 0;
			if(!metoda.equals("POP"))
				pop = getPOPUdaj(1999+i);
			else
			{
				for (Data udaje : celkemObyvatel) {
					if(udaje.Rok() == 1999+i)
					{
						pop = udaje.Udaj();
						break;
					}
				}
			}
			
			double udaj = 0;
			if(metoda.equals("WRK"))
				udaj = getWRKUdaj(1999+i);
			else
				if(metoda.equals("CRM"))
					udaj = getCRMUdaj(1999+i)*10;
				else
					if(metoda.equals("POP"))
						udaj = getPOPUdaj(1999+i)*10;
			
			double proc = 0.0;
			if(pop != 0)
				proc = (udaj*100.0)/pop;
			
			
			
			if(i == 1)
			{
				//g2.drawLine(velikostBlokuX, 0, velikostBlokuX*i, 0);
				xpuvodni = velikostBlokuX;
				ypuvodni = vyskaGrafu- ((vyskaGrafu*proc)/(pocetBlokuProcenta*10));
			}
			
			if(i > 1)
			{
				g2.drawLine((int)xpuvodni, (int)ypuvodni, (int)(velikostBlokuX*i), (int)(vyskaGrafu- ((vyskaGrafu*proc)/(pocetBlokuProcenta*10))));
				xpuvodni = velikostBlokuX*i;
				ypuvodni = vyskaGrafu- ((vyskaGrafu*proc)/(pocetBlokuProcenta*10));
			}		
			
			if(1999+i == aktualniRok)
			{	
				g2.setColor(Color.RED);
				g2.drawOval((int)xpuvodni-4,(int)ypuvodni-4, 8, 8);
				g2.setColor(Color.BLUE);
				g2.drawOval((int)xpuvodni-2,(int) ypuvodni-2, 4, 4);
				g2.setColor(Color.BLACK);
			}
			
		}
		g2.setTransform(puvodniTR);
			
		
	}

	/**
	 * Vykreslen� textu tak aby se ve�el do svolen� ���ky panelu
	 * @param g2 Graphics2D
	 * @param str dal�� �daj
	 * @param fnt font 
	 * @param sirkaInfoPanelu ���ka kam se m� vykreslit dal�� informace
	 */
	private void drawStringBreakLine(Graphics2D g2, String str, Font fnt, int sirkaInfoPanelu) {
		// nastaveni y-ove souradnice a barvy vykreslovani
		int curX = 0;
		int curY = 0;
		
		// AttributedString umoznuje pridat retezci atributy
		AttributedString attrStr = new AttributedString(str);
		
		// nastaveni fontu pro vykreslovani retezce
		attrStr.addAttribute(TextAttribute.FONT, fnt);

		// iterator pro pruchod retezcem
		AttributedCharacterIterator text = attrStr.getIterator();
		
		// vytvoreni renderovaciho kontextu
		// pro prevod typografickych bodu na pixely
		FontRenderContext frc = g2.getFontRenderContext();

		// pomocna trida pro vykreslovani zalamovaneho textu
		LineBreakMeasurer measurer = new LineBreakMeasurer(text, frc);
		
		// nastaveni sirky zalamovani
		float wrappingWidth = sirkaInfoPanelu - 2 * curX;

		// pruchod celym retezcem
		while (measurer.getPosition() < text.getEndIndex()) {
			// ziskani casti textu, ktery se vejde do sirky wrappingWidth
			TextLayout layout = measurer.nextLayout(wrappingWidth);

			// zmena y-ove souradnice o vysky ascentu (horni dotaznice)
			curY += (layout.getAscent());

			// vykresleni casti textu
			layout.draw(g2, curX, curY);
			
			// posunuti o velikost descentu (dolni dotaznice) 
			// a o mezeru mezi radky (leading)
			curY += layout.getDescent() + layout.getLeading();
		}

	}	
	
	/**
	 * Vr�t� n�zev m�sta pokud bude na n�j kliknut, jinak vr�t� hodnotu Plze�-m�sto
	 * @param point sou�adnice kliknut� my�i
	 * @return n�zev m�sta
	 */
	public String isInArea(Point point)
	{
		if(mapRegion.isInArea(point))
			return this.zkratka;
		else
			return "";
	}
	
	/**
	 * Vlo�� data do listu po�tu obyvatel
	 * @param data po�et obyvatel pro v�echny roky
	 */
	public void AddPOP(String[] data )
	{
		String[] _data = Arrays.copyOf(data, data.length);
		int rok = 1999;
		for (int i = 1; i < _data.length; i++) {
			this.POP.add(new Data(rok+i, Integer.parseInt(_data[i])));
		}
	}
	
	/**
	 * Vlo�� data do listu pracovn� s�ly
	 * @param data pracovn� s�la pro v�echny roky
	 */
	public void AddWRK(String[] data )
	{
		String[] _data = Arrays.copyOf(data, data.length);
		int rok = 1999;
		for (int i = 1; i < _data.length; i++) {
			this.WRK.add(new Data(rok+i, Integer.parseInt(_data[i])));
		}
	}
	
	/**
	 * Vlo�� data do listu kriminality
	 * @param data kriminalita pro v�echny roky
	 */
	public void AddCRM(String[] data )
	{
		String[] _data = Arrays.copyOf(data, data.length);
		int rok = 1999;
		for (int i = 1; i < _data.length; i++) {
			this.CRM.add(new Data(rok+i, Integer.parseInt(_data[i])));
		}
	}
	
	/**
	 * Vr�t� hodnotu pracovn� s�ly v dan�m roce
	 * @param rok vybran� rok
	 * @return pracovn� sila v dan�m roce
	 */
	public int getWRKUdaj(int rok)
	{
		for (Data udaje : WRK) {
			if(udaje.Rok() == rok)
			{
				return udaje.Udaj();
			}
		}
		
		return 0;
	}
	
	/**
	 * Vr�t� hodnotu kriminality v dan�m roce
	 * @param rok vybran� rok
	 * @return kriminalita v dan�m roce
	 */
	public int getCRMUdaj(int rok)
	{
		for (Data udaje : CRM) {
			if(udaje.Rok() == rok)
			{
				return udaje.Udaj();
			}
		}
		
		return 0;
	}
	
	/**
	 * Vr�t� hodnotu po�et obyvatel v dan�m roce
	 * @param rok vybran� rok
	 * @return po�et obyvatel v dan�m roce
	 */
	public int getPOPUdaj(int rok)
	{
		for (Data udaje : POP) {
			if(udaje.Rok() == rok)
			{
				return udaje.Udaj();
			}
		}
		
		return 0;
	}
	
	/**
	 * Ulo�� dal�� informace do seznamu informac� pro okres
	 * @param dalsiInfo
	 */
	public void ulozDalsiInfo(ArrayList<String> dalsiInfo)
	{
		if(dalsiInfo.size() > 0)
		for (int i = 0; i < dalsiInfo.size(); i++) {
			informace.add(dalsiInfo.get(i));
		}
	}
	
	
	
	/**
     * Nastav� dal�� informaci v po�ad� (to�en� infromac� z listu)
	 */
	public void dalsiInfo()
	{
		if((aktualniInfo+1) == informace.size())
			aktualniInfo = 0;
		else
			aktualniInfo = aktualniInfo+1;
	}
	
	

	/**
	 * Z�sk� informaci pro mesto v po�ad�
	 * @return dal�i informace
	 */
	public String getInformaci()
	{
		if(this.informace.size() >0)
			return this.informace.get(aktualniInfo);
		else
			return "Informace nejsou k dispozici";
	}
	
}