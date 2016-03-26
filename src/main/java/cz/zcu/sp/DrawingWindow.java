package cz.zcu.sp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 * Drawing window class for depict on canvas (map, legend)
 * 
 * @author Jan Novák & Marek Šimůnek
 *
 */
public class DrawingWindow extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Actual width canvas
	 */
	private int cWidth;

	/**
	 * Actual height canvas
	 */
	private int cHeight;

	/** Method type data **/
	// TODO nacitat ze vstupních hodnot
	private static String methodTypeData = "POP";

	/** Current year **/
	// TODO nacitat ze vstupních hodnot
	private static int currentYear = 2000;

	/** Hodnota zda je spu�t�na animace **/
	private boolean isRunning = false;

	/** Map CR **/
	private Map map;

	/** Select region **/
	private String selectRegion;

	Timer timerMove;
	TimerTask task;

	/**
	 * Constructor for create drawing canvas
	 */
	public DrawingWindow() {
		this.selectRegion = "PRH";

		this.cHeight = this.getHeight();
		this.cWidth = this.getWidth();

		// Kliknutí na region se zjistí zda je možné obkreslit kraj, pokud ne je
		// zvyrazená Praha
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {

				double posunX = map.getMin().x * map.getRatio();
				double posunY = map.getMin().y * map.getRatio();

				Point pointer = new Point((int) (posunX + e.getPoint().getX()),
						(int) (posunY + e.getPoint().getY()));

				Region select = map.isInArea(pointer);

				if (!selectRegion.equals(select)) {
					System.out.println("DrawingWindow: " + selectRegion);
					selectRegion = select.shortName;
					repaint();
					if (e.getClickCount() >= 2) {
						// TODO modální okno s informace o kraji - budu si
						// vracet celej objekt regionu
						GraphicComponents.showModalWindow(select);

					}
				}
			}

			public void mouseClicked(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		});

	}

	/**
	 * Method for depict map with legend
	 * 
	 * @param g2
	 *            Graphics2D
	 */
	public void depict(Graphics2D g2) {
		g2.setColor(Color.BLACK);

		AffineTransform puvodniTR = g2.getTransform();

		// vykresleni ctverce okolo mapy
		g2.drawRect(0, 0, cWidth - 1, cHeight - 1);

		// System.out.println(cWidth + " "+ cHeight);
		// System.out.println(map.getWidthMap() + " "+ map.getHeightMap());

		// vykreslení legendy
		this.drawLegenda(g2);

		g2.setTransform(puvodniTR);

		// Posun mapy, aby se zobrazovala celá uživateli na plátně
		double posunX = -map.getMin().x;
		double posunY = -map.getMin().y;
		g2.translate(posunX * map.getRatio(), posunY * map.getRatio() + 10);

		// Vykreslení mapy s vybraným rokem
		map.DrawMapa(g2, methodTypeData, currentYear);

		// Zvýraznení vybraného kraje
		map.DrawThisRegion(g2, selectRegion);

		g2.setTransform(puvodniTR);
	}

	/**
	 * When resizing the canvas will again render map
	 * 
	 * @param g
	 *            Graphics
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.cHeight != this.getHeight() || this.cWidth != this.getWidth()) {
			this.cHeight = this.getHeight();
			this.cWidth = this.getWidth();

			this.map = new Map(cWidth, cHeight);
		}
	}

	/**
	 * Drawing legend
	 * 
	 * @param g2
	 *            Graphics2D
	 */
	public void drawLegenda(Graphics2D g2) {
		Font font = new Font("Arial", Font.BOLD, 12);
		Font fontNadpis = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);

		g2.translate(20, this.getHeight() - 100);
		TextLayout labelLayout = new TextLayout("% - Nezaměstnanost v roce "
				+ currentYear + " bla bla bla.", fontNadpis,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (0), (float) (0));

		// <= 1.00%
		g2.setColor(new Color(255, 255, 204));
		g2.fillRect(0, 5, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 5, 20, 10);

		labelLayout = new TextLayout("0.00 <= 1.00", font,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25), (float) (5 + 8));

		// 1-1.5%
		g2.setColor(new Color(229, 255, 204));
		g2.fillRect(0, 20, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 20, 20, 10);

		labelLayout = new TextLayout("1.00 - 1.50", font,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25), (float) (20 + 8));

		// 1.5-2.0%
		g2.setColor(new Color(204, 255, 153));
		g2.fillRect(0, 35, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 35, 20, 10);

		labelLayout = new TextLayout("1.50 - 2.00", font,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25), (float) (35 + 8));

		// 2.0-2.5%
		g2.setColor(new Color(178, 255, 102));
		g2.fillRect(0, 50, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 50, 20, 10);

		labelLayout = new TextLayout("2.00 - 2.50", font,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25), (float) (50 + 8));

		// 2.5-3.0%
		g2.setColor(new Color(153, 255, 51));
		g2.fillRect(0, 65, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 65, 20, 10);

		labelLayout = new TextLayout("2.50 - 3.00", font,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25), (float) (65 + 8));

		// 3.0-3.5%
		g2.setColor(new Color(128, 255, 0));
		g2.fillRect(0, 80, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 80, 20, 10);

		labelLayout = new TextLayout("3.00 - 3.50", font,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25), (float) (80 + 8));

		// 3.5-4.0%
		g2.setColor(new Color(102, 204, 0));
		g2.fillRect(100, 5, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(100, 5, 20, 10);

		labelLayout = new TextLayout("3.50 - 4.00", font,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25 + 100), (float) (5 + 8));

		// 4.0-5.0%
		g2.setColor(new Color(76, 153, 0));
		g2.fillRect(100, 20, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(100, 20, 20, 10);

		labelLayout = new TextLayout("4.00 - 5.00", font,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25 + 100), (float) (20 + 8));

		// 5.0-7.0%
		g2.setColor(new Color(51, 102, 0));
		g2.fillRect(100, 35, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(100, 35, 20, 10);

		labelLayout = new TextLayout("5.00 - 7.00", font,
				g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25 + 100), (float) (35 + 8));

		// > 7%
		g2.setColor(new Color(25, 51, 0));
		g2.fillRect(100, 50, 20, 10);
		g2.setColor(Color.BLACK);
		g2.drawRect(100, 50, 20, 10);

		labelLayout = new TextLayout("> 7.00", font, g2.getFontRenderContext());
		labelLayout.draw(g2, (float) (25 + 100), (float) (50 + 8));

	}

	/**
	 * Drawing to canvas
	 * 
	 * @param g
	 *            Graphics
	 */
	public void paint(Graphics g) {
		super.paint(g);

		if (map == null) {
			this.map = new Map(this.getWidth(), this.getHeight());
		}

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		this.depict(g2);
	}

	/**
	 * Method for change data
	 * 
	 * @param typeData
	 *            - type data
	 */
	public void changeData(String typeData) {
		methodTypeData = typeData;
		repaint();
	}

	/**
	 * Method for set first year
	 */
	public void toStart() {
		currentYear = 2000;
		repaint();
	}

	/**
	 * Method for set last year
	 */
	public void toEnd() {
		currentYear = 2013;
		repaint();
	}

	/**
	 * Method for set the following year and while it is running animations and
	 * turns off at the last frame
	 */
	public void following() {
		if (isRunning)
			if (currentYear + 1 == 2013) {
				timerMove.cancel();
				addActions();
			}

		if (currentYear + 1 <= 2013)
			currentYear++;
		else
			currentYear = 2000;

		repaint();
	}

	/**
	 * Method for set previous year
	 */
	public void previous() {
		if (currentYear - 1 >= 2000)
			currentYear--;
		else
			currentYear = 2013;

		repaint();
	}

	/**
	 * Method for start/stop animation
	 */
	public void play() {
		if (isRunning == false) {
			isRunning = true;
			addActions();
		} else {
			isRunning = false;
			timerMove.cancel();
		}
	}

	/**
	 * Animation
	 */
	void addActions() {
		timerMove = new Timer();

		// Move timer setting timer start in 3s and later repeated with a period
		// of 1250 ms
		timerMove.schedule(new TimerTask() {
			@Override
			public void run() {
				following();
			}
		}, 3000, 1250);
	}
}
