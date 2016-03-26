package cz.zcu.sp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextComponent;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

/**
 * Graphic components class for application (frame, button, etc.)
 * @author Jan Novák & Marek Šimůnek 
 *
 */
public class GraphicComponents extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	/** Max value button **/
	private static final int MAX_VALUE_BUTTON = 40;
	
	/** Initial width frame **/
	private static final int INICIAL_WIDH = 1000;

	/** Initial height frame **/
	private static final int INICIAL_HEIGHT = 800;

	static final JFrame frame = new JFrame();
	static DrawingWindow renderWindow;
	
	
	/**
	 * Method for create button
	 * @return buttonPanel - panel with all buttons
	 */
	private static JPanel createButton(){
		
		JPanel buttonPanel = new JPanel();

		JComboBox<String> selectData = new JComboBox<String>(new String[]{"test","test2"});
		selectData.addActionListener(event -> {
			renderWindow.changeData(selectData.getSelectedItem().toString());
		});
		
		JButton exitButton = new JButton();
		exitButton.setText("Close");
		exitButton.addActionListener(event -> {
			frame.dispose();
		});

		JButton pauseButton = new JButton();
		pauseButton.setText("Run/Pause");
		pauseButton.addActionListener(event -> {
			renderWindow.play();
		});

		JButton startButton = new JButton();
		startButton.setText("|<");
		startButton.addActionListener(event -> {
			renderWindow.toStart();
		});

		JButton endButton = new JButton();
		endButton.setText(">|");
		endButton.addActionListener(event -> {
			renderWindow.toEnd();
		});

		JButton previousButton = new JButton();
		previousButton.setText("<");
		previousButton.addActionListener(event -> {
			renderWindow.previous();
		});

		JButton followingButton = new JButton();
		followingButton.setText(">");
		followingButton.addActionListener(event -> {
			renderWindow.following();
		});

		buttonPanel.setSize(0, MAX_VALUE_BUTTON);
		buttonPanel.add(selectData);
		buttonPanel.add(startButton);
		buttonPanel.add(previousButton);
		buttonPanel.add(followingButton);
		buttonPanel.add(endButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(exitButton);
		
		return buttonPanel;
	}
	
	
	/**
	 * Method for drawing frame with all buttons
	 */
	public static void createFullyFrame() {
		renderWindow = new DrawingWindow();

		frame.setTitle("VINF Jan Novák & Marek Šimůnek");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(renderWindow, BorderLayout.CENTER);
		
		frame.add(createButton(), BorderLayout.SOUTH);

		frame.pack();
		frame.setSize(INICIAL_WIDH, INICIAL_HEIGHT);

		frame.setMinimumSize(new Dimension(INICIAL_WIDH, INICIAL_HEIGHT));

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	/**
	 * Method for create modal window
	 * @param select
	 */
	public static void showModalWindow(Region select){
		JFrame modal = new JFrame(select.name);
		//modal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		modal.setLayout(new BorderLayout());
		
		modal.add(detailRegion(select), BorderLayout.CENTER);
		
		modal.pack();
		modal.setSize(300, 300);

		modal.setMinimumSize(new Dimension(300, 300));

		modal.setLocationRelativeTo(null);
		modal.setVisible(true);
	}
	
	
	private static JPanel detailRegion(Region select){
		JPanel borderPanel = new JPanel();
		JTextArea textArea = new JTextArea(select.dataList.get(2).getValue()+"");
			textArea.setFont(new Font("Serif", Font.ITALIC, 16));
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
		
		borderPanel.add(textArea);
		return borderPanel;
		
	}
	

}
