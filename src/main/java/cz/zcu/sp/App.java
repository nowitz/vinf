package cz.zcu.sp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



/**
 * @author Jan Novák & Marek Šimůnek
 * Hlavní třída celého projektu
 */
public class App{
	

	/** Konstanta maximálí velikosti talčítek **/
	private static final int MAXBUTTONVEL = 40;
	
	/** Konstanta po��te�n� velikosti ���ky **/
	private static final int POCATECNIWIDTH = 800;
	
	/** Konstanta po��te�n� velikosti v��ky **/
	private static final int POCATECNIHEIGHT = 600;
	
	static final JFrame frame = new JFrame();
	static Grawing oknoVykres;
	
	/** Typ metody **/
	private static String metoda = "POP";
	
	/** VřchozÝ rok **/
	private static int vychoziRok = 2000;
	
	/**
	 * Kontrola a parametrů
	 * @param args vstupní parametry
	 * @return true/false 
	 */
	private static boolean checkParametrs(String[] args)
	{
		if (args.length == 0) 
		{
			System.out.println("Program musí být spouštěn s parametry!");
			return false;
		}
		else{
			return true;			
		}	
		
	}
	
	/**
	 * Metoda pro vytvoření tlačítek
	 */
	private static void vytvorTlacitka() {

		JPanel buttonPanel = new JPanel();
		
		JButton exitButton = new JButton();
		exitButton.setText("Close");
		exitButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
				
		JButton infoButton = new JButton();
		infoButton.setText("Další info");
		infoButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				oknoVykres.dalsiInfo();
			}
		});	
		
		JButton pauseButton = new JButton();
		pauseButton.setText("Run/Pause");
		pauseButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				oknoVykres.play();
			}
		});		
			
		
		JButton zacatekButton = new JButton();
		zacatekButton.setText("|<");
		zacatekButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				oknoVykres.nazacatek();
			}
		});	
		
		JButton konecButton = new JButton();
		konecButton.setText(">|");
		konecButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				oknoVykres.nakonec();
			}
		});	
		
		JButton predchoziButton = new JButton();
		predchoziButton.setText("<");
		predchoziButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				oknoVykres.predchozi();
			}
		});	
		
		JButton dalsiButton = new JButton();
		dalsiButton.setText(">");
		dalsiButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				oknoVykres.dalsi();
			}
		});	
		
		
		buttonPanel.setSize(0, MAXBUTTONVEL);
		buttonPanel.add(zacatekButton);
		buttonPanel.add(predchoziButton);
		buttonPanel.add(dalsiButton);
		buttonPanel.add(konecButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(infoButton);
		buttonPanel.add(exitButton);
		
		frame.add(buttonPanel, BorderLayout.SOUTH);	
	}
	
	/**
	 * Metoda která vytvoří a  vykreslí okno s tlačítky
	 */
	private static void oknoVykresu()
	{
		oknoVykres = new Grawing(metoda, vychoziRok);
	
		frame.setTitle("VINF Jan Novák & Marek Šimůnek");		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(oknoVykres, BorderLayout.CENTER);

		vytvorTlacitka();

		frame.pack(); // nastavi velikost okna podle prefferedSize subkomponent
		frame.setSize(POCATECNIWIDTH, POCATECNIHEIGHT);
		
		frame.setMinimumSize(new Dimension(POCATECNIWIDTH, POCATECNIHEIGHT));
		
		frame.setLocationRelativeTo(null);	
		frame.setVisible(true);
	}
	
	
	/**
	 * Hlavní souštěcí metoda
	 * @param args zadané parametry
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{	
		String [] args2  = {"ste"};
		
		if(checkParametrs(args2))
		{
			oknoVykresu();
		}
		else
		{
			System.out.println("Zvolené parametry nejsou správně! Zkuste to znovu!");
		}
	
	}

}
