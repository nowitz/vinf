package cz.zcu.sp;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

/**
 * Tato t��da byla vytvo�ena ji� pro cvi�en�, z mal� ��sti byla tato t��da upravena pro pot�eby SP. Resp. bylo p�id�no mal� mno�stv� ��dk� k�du
 * @author ZCU/KIV/UPG
 *
 */
public class MapRegion {
	
	Color color;
	Color ohraniceni;
	Point2D.Double points[];
	Point2D.Double pointsTransformed[];
	Point2D.Double centroidTransformed;

	// teziste polygonu - budeme tam umistovat text
	//Point2D.Double centroid; 
	// Maxima a minima datove mnoziny
	Point2D.Double max;
	Point2D.Double min;
	// polygon vytvoreny ze zadanych bodu
	Path2D.Double regionPath;

	double width = 0;
	double height = 0;
	

	public MapRegion(Point2D.Double points[]) {
		this.color = Color.GRAY;
		this.ohraniceni = Color.BLACK;
		
		this.points = Arrays.copyOf(points, points.length);
		
		calculateLimits();  // spocte bounding box
		
		this.pointsTransformed = new Point2D.Double[points.length];
		
		this.centroidTransformed = new Point2D.Double();
		
	}
	

	/**
	 * Urci maxima a minima teto datove mnoziny
	 */
	private void calculateLimits() {
		// zjistime maxima a minima dat abychom zvolil spravne meritko
		min = (Point2D.Double) points[0].clone();
		max = (Point2D.Double) points[0].clone();
		for (int i = 1; i < points.length; i++) {
			if (points[i].x < min.x) min.x = points[i].x;
			if (points[i].y < min.y) min.y = points[i].y;
			if (points[i].x > max.x) max.x = points[i].x;
			if (points[i].y > max.y) max.y = points[i].y;
		}
		
		width = Math.abs(Math.abs(max.x) - Math.abs(min.x));
		height = Math.abs(Math.abs(max.y) - Math.abs(min.y));
		
	}

	
	
	public void draw(Graphics2D g2,	double pomer, String zkratka) {
	
		this.createTransformedRegionPath(pomer);
		this.drawRegion(g2,zkratka);
	}
	
	
	/**
	 * Transformuje souradnice vsech bodu do souradnicoveho systemu vykreslovaci oblasti
	 * a vygeneruje regionPath pro vykresleni. 
	 * @param width Sirka oblasti v pixelech, do ktereho se budou data vykreslovat
	 * @param height Vyska oblasti v pixelech, do ktereho se budou data vykreslovat
	 */
	private void createTransformedRegionPath(double pomer) {
		

		
		for (int j = 0; j < points.length; j++) {
			pointsTransformed[j] = new Point2D.Double(points[j].x*pomer, points[j].y*pomer);
		}
		for (int i = 0; i < pointsTransformed.length; i++) {
			this.centroidTransformed.x += pointsTransformed[i].x;
			this.centroidTransformed.y += pointsTransformed[i].y;
		}
		this.centroidTransformed.x /= pointsTransformed.length;
		this.centroidTransformed.y /= pointsTransformed.length;
		

		this.regionPath = new Path2D.Double();
		this.regionPath.moveTo(pointsTransformed[0].x, pointsTransformed[0].y);
		for (int i = 1; i < points.length; i++) 
			this.regionPath.lineTo(pointsTransformed[i].x, pointsTransformed[i].y);
		this.regionPath.closePath();
	}
	
	/**
	 * Vykresli region s prislusnym popiskem.
	 * @param g2 Graficky kontext
	 * @param width Sirka vykreslovaci oblasti v pixelech
	 * @param height Vyska vykreslovaci oblasti v pixelech
	 */
	private void drawRegion(Graphics2D g2, String regionName) {		
		
		Font font = new Font("Arial", Font.BOLD, 8);
		g2.setFont(font);
		
		if(this.ohraniceni == Color.BLACK)
		g2.setStroke(new BasicStroke(1));
		else
			g2.setStroke(new BasicStroke(2));	
		
		// vyplnime zadanou barvou
		g2.setColor(this.color);
		g2.fill(regionPath);
		// vykreslime obrys
		g2.setColor(this.ohraniceni);
		g2.draw(regionPath);

		TextLayout labelLayout = new TextLayout(regionName, font, g2.getFontRenderContext());
		double labelHeight = labelLayout.getAscent() + labelLayout.getDescent();
		Area k = new Area(this.regionPath);
		Rectangle2D.Double r = new Rectangle2D.Double(centroidTransformed.x,centroidTransformed.y - labelHeight,labelLayout.getBounds().getWidth(), labelLayout.getBounds().getHeight());
	//	g2.setColor(Color.BLACK);
		if (!k.contains(r)) {
			
			labelLayout.draw(g2, (float)(centroidTransformed.x),(float)( centroidTransformed.y));
			//
			//
			//
			//�e�en�ch je mnoho, dod�lat pokud bude �as...
			//
			//
			//
		}
		else
			labelLayout.draw(g2, (float)(centroidTransformed.x),(float)( centroidTransformed.y));
	}

	
	public boolean isInArea(Point point)
	{
		Area k = new Area(this.regionPath);
		if (k.contains(point)) 
			return true;
		else
			return false;
		
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getOhraniceni() {
		return ohraniceni;
	}

	public void setOhraniceni(Color color) {
		this.ohraniceni = color;
	}


	public Point2D.Double getMax() {
		return max;
	}

	public void setMax(Point2D.Double max) {
		this.max = max;
	}

	public Point2D.Double getMin() {
		return min;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getWidth() {
		return width;
	}

	public void setMin(Point2D.Double min) {
		this.min = min;
	}

	public Path2D.Double getRegionPath() {
		return regionPath;
	}

	public void setPolygon(Path2D.Double regionPath) {
		this.regionPath = regionPath;
	}
	
	public Point2D.Double[] getPoints() {
		return points;
	}


	public void setPoints(Point2D.Double[] points) {
		this.points = points;
	}


	public Point2D.Double[] getPointsTransformed() {
		return pointsTransformed;
	}


	public void setPointsTransformed(Point2D.Double[] pointsTransformed) {
		this.pointsTransformed = pointsTransformed;
	}

}
