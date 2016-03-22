package cz.zcu.sp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Point;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.swing.JPanel;


/**
 * T��da vykresluj�c� d�n� na pl�tno
 * @author Michal Proch�zka (A14B0339P) 
 */
public class Grawing extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH_INFO = 250; // ���ka pro prav� panel informac�
	private static final int HEIGHT_LEGE = 100; // V��ka pro pruh s legendou
	private int cWidth; // Aktu�ln� ���ka pl�tna
	private int cHeight; // Aktu�ln� v��ka pl�tna
	
	/** Typ metody **/
	private static String metoda = "POP";
	
	/** VřchozÝ rok **/
	private static int vychoziRok = 2000;
	
	
	/** Hodnota zda je spu�t�na animace **/
	private boolean isRunning = false;
		
	/** Mapa �esk� republiky i s informacemi **/
	private Map mapa;
	
	/** Aktu�ln� v�b�r m�sta **/
	private String vyberMesta; 
	
	
	Timer timerMove;
	TimerTask task;
	
	/**
	 * Vytvo�en� ktesl�c�ho pl�tna
	 * @param vychoziRok 
	 * @param metoda 
	 * @param metoda typ spu�t�n� metody
	 * @param vychoziRok v�choz� rok
	 */
	public Grawing(String metoda, int vychoziRok)
	{
		this.vyberMesta = "PRH";
		
		this.cHeight = this.getHeight();
		this.cWidth = this.getWidth();
		
		// Kliknutí na region a zjistí zda je možné obkreslit kraj, pokud ne je zv�razn�n okres Plze�-m�sto
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {

				double posunX = mapa.getMin().x * mapa.getPomer();
				double posunY = mapa.getMin().y * mapa.getPomer();
						
				Point pointer = new Point(
						(int)(posunX + e.getPoint().getX()),
						(int)(posunY + e.getPoint().getY()));
					
				String vyber = mapa.isInArea(pointer);
				
				if(!vyberMesta.equals(vyber))
					{
						vyberMesta = vyber;
						repaint();
					}
				//if (task != null && task.scheduledExecutionTime() > 0) {
				//	task.cancel();
				//}

				//task = new TimerTask() {
				//@Override
					//public void run() {
				
						//repaint();
					//}
				//};
			}

			public void mouseClicked(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
	}

	/**
	 * P�i zm�n� velikosti pl�tna se na�tou data znovu a vypo��t� se velikost mapy
	 * @param g Graphics
	 */
	@Override
	public void paintComponent(Graphics g) {
	     super.paintComponent(g);
	     
	     if(this.cHeight != this.getHeight() || this.cWidth != this.getWidth())
	     {
	       this.cHeight = this.getHeight();
	       this.cWidth = this.getWidth();
	       
	       this.mapa = new Map(
	    		   cWidth - WIDTH_INFO,
	    		   cHeight - HEIGHT_LEGE);
	     }
	}
	
	/**
	 * Kreslen� na pl�tno
	 * @param g Graphics
	 */
	public void paint(Graphics g) {
		super.paint(g);
		
		if(mapa == null)
		{
			 this.mapa = new Map(
		    		 this.getWidth() - WIDTH_INFO,
		    		 this.getHeight() - HEIGHT_LEGE);
		}
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.vykreslit(g2);
	}

	
	/**
	 * Vykreslení legendy
	 * @param g2 Graphics2D
	 */
	public void drawLegendaCRM(Graphics2D g2)
	{
		Font font = new Font("Arial", Font.BOLD, 8);
		Font fontNadpis = new Font("Arial", Font.BOLD, 10);
		g2.setFont(font);
		
		g2.translate(20, this.getHeight()-100);
		TextLayout labelLayout = new TextLayout("% - Kriminalita v jednotliv�ch okresech v roce "+vychoziRok+" vzhledem k celkov�mu po�tu obyvatel v okresu.", fontNadpis, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(0),(float)(0));
		
				
		// <= 1.00%
		g2.setColor(new Color(255,255,204));
		g2.fillRect(0, 5, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 5, 20, 10);
		
		labelLayout = new TextLayout("0.00 <= 1.00", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25),(float)(5+8));
		
		// 1-1.5%
		g2.setColor(new Color(229,255,204));
		g2.fillRect(0, 20, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 20, 20, 10);
		
		labelLayout = new TextLayout("1.00 - 1.50", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25),(float)(20+8));
		
		// 1.5-2.0%
		g2.setColor(new Color(204,255,153));
		g2.fillRect(0, 35, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 35, 20, 10);
		
		labelLayout = new TextLayout("1.50 - 2.00", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25),(float)(35+8));
		
		// 2.0-2.5%
		g2.setColor(new Color(178,255,102));
		g2.fillRect(0, 50, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 50, 20, 10);
		
		labelLayout = new TextLayout("2.00 - 2.50", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25),(float)(50+8));
		
		// 2.5-3.0%
		g2.setColor(new Color(153,255,51));
		g2.fillRect(0, 65, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 65, 20, 10);
		
		labelLayout = new TextLayout("2.50 - 3.00", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25),(float)(65+8));
		
		// 3.0-3.5%
		g2.setColor(new Color(128,255,0));
		g2.fillRect(0, 80, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 80, 20, 10);
		
		labelLayout = new TextLayout("3.00 - 3.50", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25),(float)(80+8));
		
		// 3.5-4.0%
		g2.setColor(new Color(102,204,0));
		g2.fillRect(80, 5, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(80, 5, 20, 10);
		
		labelLayout = new TextLayout("3.50 - 4.00", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25+80),(float)(5+8));
		
		// 4.0-5.0%
		g2.setColor(new Color(76,153,0));
		g2.fillRect(80, 20, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(80, 20, 20, 10);
		
		labelLayout = new TextLayout("4.00 - 5.00", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25+80),(float)(20+8));
		
		// 5.0-7.0%
		g2.setColor(new Color(51,102,0));
		g2.fillRect(80, 35, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(80, 35, 20, 10);
		
		labelLayout = new TextLayout("5.00 - 7.00", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25+80),(float)(35+8));
		
		// > 7%
		g2.setColor(new Color(25,51,0));
		g2.fillRect(80, 50, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(80, 50, 20, 10);
		
		labelLayout = new TextLayout("> 7.00", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float)(25+80),(float)(50+8));
		
	}
	
	
	

	/**
	 * Metoda pro vykreslení mapy
	 * @param g2 Graphics2D
	 */
	public void vykreslit(Graphics2D g2)
	{		
		g2.setColor(Color.BLACK);
		
		AffineTransform puvodniTR = g2.getTransform();
		
		//vykreslení legendy
		this.drawLegendaCRM(g2);	
		
		g2.setTransform(puvodniTR);
		
		
		//Posun mapy, aby se zobrazovala celá uživateli  na plátně
		double posunX = -mapa.getMin().x;
		double posunY = -mapa.getMin().y;	
		g2.translate(posunX * mapa.getPomer(),posunY * mapa.getPomer());
		
		//Vykreslen� mapy s vybran�m rokem
		mapa.DrawMapa(g2, metoda, vychoziRok);
		
		//Zv�razn�n� vybran�ho m�sta
		mapa.DrawThisRegion(g2, vyberMesta);
		g2.setTransform(puvodniTR);
		
		//Vykreslen� hodnot s grafem a dal�� informac�
		//g2.translate(getWidth()-WIDTH_INFO,0+30);
		//mapa.DrawInfo(g2, vychoziRok, WIDTH_INFO, 18, 11, vyberMesta, metoda);

		
		g2.setTransform(puvodniTR);
	}
	
	
	/**
	 * Nastav� se prvn� rok v seznamu
	 */
	public void nazacatek()
	{
		this.vychoziRok = 2000;
		repaint();
	}
	
	/**
	 * Nastav� se posledn� rok v seznamu
	 */
	public void nakonec()
	{
		this.vychoziRok = 2013;
		repaint();
	}
	
	/**
	 * Nastav� se dal�� rok v po�ad� a z�rove� pokud b�� animace tak se vypne na posledn�m sn�mku a op�t zapne
	 * t�m se po�k� 5000ms a za�ne se op�t p�ehr�vat po 1250ms
	 */
	public void dalsi()
	{
		if(isRunning)
			if(vychoziRok+1 == 2013)
			{
				timerMove.cancel();
				addActions();
			}
		
		if(vychoziRok+1 <= 2013)
		this.vychoziRok++;
		else
			this.vychoziRok = 2000;
		
		repaint();
	}
	
	/**
	 * Nastav� se p�edchoz� rok
	 */
	public void predchozi()
	{
		if(vychoziRok-1 >= 2000)
		this.vychoziRok--;
		else
			this.vychoziRok = 2013;
		
		repaint();
	}
	
	/**
	 * Spust� se animace / Zastav� se animace
	 */
	public void play()
	{
		if(isRunning == false)
		{
			isRunning = true;
			addActions();
		}
		else
		{
			isRunning = false;
			timerMove.cancel();
		}
	}
	
	/**
	 * P�id� se akce pro animaci
	 */
	void addActions() {		
		timerMove = new Timer();

		// nastavime casovaci timerMove start za 5s a pote opakovat s periodou 1250 ms 
		timerMove.schedule(new TimerTask() {
			@Override
			public void run() {
				dalsi();
			}
		}, 5000, 1250);
	}
		
	/**
	 * Zobraz� dal�� informaci o dan�m vybran�m okrese
	 */
	public void dalsiInfo()
	{
		mapa.dalsiInfo(vyberMesta);
		repaint();
	}
	
	
}
