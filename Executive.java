package arduinoGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gnu.io.SerialPort;

/*
 * Executive class - the driver class. creates the Graphical User 
 * Interface for the FIH Tester with methods to update the GUI based on
 * data received by the arduino
 */
public class Executive extends JFrame 
{
	
	
	/*
	 * initialization of necessary variables
	 */
	public SerialPort spt;
	public InputStream in;
	public OutputStream out;

	private String comm;
	private String setting;
	int creditVal = 0;

	int colorCount = 0;
	int monoCount = 0;
	int totalCount = 0;
	int creditCount = 0;

	String onoffSelection = "";

	int mainWidth = 750;
	int mainHeight = 550;

	String initialFillLabel = "";
	String finalFillLabel = "";
	ArrayList<String> typeUpdate = new ArrayList<String>();
	
	

	/*
	 * initialization of GUI elements
	 */
	Color electricBlue = new Color (0,149,217);
	Color gainsboro = new Color (220,220,220);
	Color lightGrey = new Color (211,211,211);

	Container pane = getContentPane();
	JPanel mainPanel = new JPanel();

	JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JPanel loadPanel = new JPanel();
	JPanel operationPanel = new JPanel();
	JPanel comPanel = new JPanel();
	JPanel lccPanel = new JPanel();
	JPanel copyCountPanel = new JPanel();
	JPanel opPanel = new JPanel();
	JPanel cpPanel = new JPanel();

	JPanel southPanel = new JPanel();

	JPanel westPanel = new JPanel();
	JPanel westTopPanel = new JPanel();
	JPanel westCenterPanel = new JPanel();
	JPanel westBottomPanel = new JPanel();

	JPanel topNumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JPanel bottomNumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JPanel initialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JPanel finalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	JPanel monoDisplayPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JPanel colorDisplayPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JPanel totalDisplayPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	JPanel monoCountPanel = new JPanel();
	JPanel colorCountPanel = new JPanel();
	JPanel totalCountPanel = new JPanel();

	JPanel eastPanel = new JPanel();
	JPanel eastTopPanel = new JPanel();
	JPanel eastCenterPanel = new JPanel();
	JPanel eastBottomPanel = new JPanel();

	JPanel creditPanel = new JPanel();
	JPanel creditCountPanel = new JPanel();

	JLabel loadCopyCounter = new JLabel();
	JLabel operationModeLabel = new JLabel();
	JLabel comPortLabel = new JLabel();

	JLabel finalNumberDisplay = new JLabel();
	JLabel initialNumberDisplay = new JLabel();
	JLabel initialTypeDisplay = new JLabel();
	JLabel finalTypeDisplay = new JLabel();

	JLabel monoCopyDisplay = new JLabel();
	JLabel colorCopyDisplay = new JLabel();
	JLabel totalCopyDisplay = new JLabel();

	JLabel monoCopyTotal = new JLabel();
	JLabel colorCopyTotal = new JLabel();
	JLabel copyTotal = new JLabel();

	JLabel availableCreditDisplay = new JLabel();
	JLabel creditCountLabel = new JLabel();

	JTextField inputField = new JTextField();

	JButton loadButton = new JButton();

	JButton enableButton = new JButton();
	JButton copyPulseButton = new JButton();
	JButton colorButton = new JButton();
	JButton monoButton = new JButton();

	@SuppressWarnings("rawtypes")
	JComboBox operationModeSelect = new JComboBox();
	@SuppressWarnings("rawtypes")
	JComboBox comPortSelect = new JComboBox();

	private static final long serialVersionUID = 1L;


	/*
	 * constructor -- sets up the initial GUI layout
	 */
	@SuppressWarnings("unchecked")
	public Executive()
	{
		setLayout(new BorderLayout());

		loadButton.setBackground(gainsboro);

		comPortSelect.setBackground(Color.WHITE);
		operationModeSelect.setBackground(Color.white);

		creditPanel.setPreferredSize(new Dimension(200,50));

		copyCountPanel.setPreferredSize(new Dimension(150,75));
		copyCountPanel.setBackground(Color.white);

		lccPanel.setPreferredSize(new Dimension(150, 25));
		lccPanel.setBackground(Color.white);

		cpPanel.setPreferredSize(new Dimension(150, 25));
		cpPanel.setBackground(Color.white);

		opPanel.setPreferredSize(new Dimension(150,25));
		opPanel.setBackground(Color.white);

		comPanel.setBackground(electricBlue);
		comPanel.setPreferredSize(new Dimension(190,120));

		operationPanel.setBackground(electricBlue);
		operationPanel.setPreferredSize(new Dimension(190,120));

		loadPanel.setPreferredSize(new Dimension(190,120));
		loadPanel.setBackground(electricBlue);

		eastTopPanel.setPreferredSize(new Dimension(333, 100));
		eastCenterPanel.setPreferredSize(new Dimension(333, 100));
		eastBottomPanel.setPreferredSize(new Dimension(280, 200));

		monoCountPanel.setPreferredSize(new Dimension((280/5), 100/3));
		colorCountPanel.setPreferredSize(new Dimension((280/5), (100/3)));
		totalCountPanel.setPreferredSize(new Dimension((280/5), (100/3)));

		monoDisplayPanel.setPreferredSize(new Dimension(280, (100/3)));

		colorDisplayPanel.setPreferredSize(new Dimension(280, (100/3)));

		totalDisplayPanel.setPreferredSize(new Dimension(280, (100/3)));

		topNumPanel.setPreferredSize(new Dimension((333), 50));
		bottomNumPanel.setPreferredSize(new Dimension((333), 50));

		initialPanel.setPreferredSize(new Dimension((333), 50));
		finalPanel.setPreferredSize(new Dimension((333), 50));

		westTopPanel.setPreferredSize(new Dimension(333, 100));
		westCenterPanel.setPreferredSize(new Dimension(333, 100));
		westBottomPanel.setPreferredSize(new Dimension(280, 120));

		eastPanel.setPreferredSize(new Dimension(333, 480));

		westPanel.setPreferredSize(new Dimension(333, 480));

		southPanel.setPreferredSize(new Dimension(700,480));

		northPanel.setPreferredSize(new Dimension (600,120));
		northPanel.setBackground(electricBlue);

		colorButton.setBackground(Color.white);
		colorButton.setPreferredSize(new Dimension(140,28));
		colorButton.setText("    Color    ");

		monoButton.setBackground(Color.white);
		monoButton.setPreferredSize(new Dimension(140,28));
		monoButton.setText("  Monochrome  ");

		enableButton.setBackground(Color.white);
		enableButton.setPreferredSize(new Dimension(140,28));
		enableButton.setText("   Enable   ");

		copyPulseButton.setBackground(Color.white);
		copyPulseButton.setPreferredSize(new Dimension(140,28));
		copyPulseButton.setText("Copy Pulse");

		loadButton.setText("Load");

		availableCreditDisplay.setFont(new Font("Consolas", Font.PLAIN, 22));
		availableCreditDisplay.setText("Credits: ");		

		monoCopyDisplay.setFont(new Font("Consolas", Font.PLAIN, 22));
		colorCopyDisplay.setFont(new Font("Consolas", Font.PLAIN, 22));
		totalCopyDisplay.setFont(new Font("Consolas", Font.PLAIN, 22));

		monoCopyDisplay.setText(" Mono copies: ");
		colorCopyDisplay.setText("Color copies: ");
		totalCopyDisplay.setText("Total copies: ");

		monoCopyTotal.setFont(new Font("Consolas", Font.PLAIN, 15));
		colorCopyTotal.setFont(new Font("Consolas", Font.PLAIN, 15));
		copyTotal.setFont(new Font("Consolas", Font.PLAIN, 15));

		monoCopyTotal.setText("0");
		colorCopyTotal.setText("0");
		copyTotal.setText("0");

		monoCountPanel.setBackground(Color.white);
		colorCountPanel.setBackground(Color.white);
		totalCountPanel.setBackground(Color.white);

		creditCountPanel.setBackground(Color.white);
		creditCountPanel.setPreferredSize(new Dimension((280/5), (100/3)));

		creditCountLabel.setFont(new Font("Consolas", Font.PLAIN, 15));
		creditCountLabel.setText("N/A");

		initialNumberDisplay.setFont(new Font("Consolas", Font.PLAIN, 25));
		initialNumberDisplay.setText("1 2 3 4 5 6 7 8 9 0");

		finalNumberDisplay.setFont(new Font("Consolas", Font.PLAIN, 25));
		finalNumberDisplay.setText("1 2 3 4 5 6 7 8 9 0");

		operationModeLabel.setText("Operation Mode");
		comPortLabel.setText("         COM Port           ");

		loadCopyCounter.setText("Load Copy Counter");

		inputField.setPreferredSize(new Dimension(50,20));

		operationModeSelect.addItem("                Select             ");
		operationModeSelect.addItem("Always Enabled");
		operationModeSelect.addItem("Always Disabled");
		operationModeSelect.addItem("Copy Counter");

		comPortSelect.addItem("               Select              ");
		comPortSelect.addItem("COM1");
		comPortSelect.addItem("COM2");
		comPortSelect.addItem("COM3");
		comPortSelect.addItem("COM4");
		comPortSelect.addItem("COM5");
		comPortSelect.addItem("COM6");
		comPortSelect.addItem("COM7");
		comPortSelect.addItem("COM8");
		comPortSelect.addItem("COM9");
		comPortSelect.addItem("COM10");

		comPortSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				startConnection();
			}
		});

		mainPanel.add(northPanel, BorderLayout.PAGE_START); 
		mainPanel.add(southPanel, BorderLayout.SOUTH);

		northPanel.add(loadPanel);
		northPanel.add(operationPanel);
		northPanel.add(comPanel);

		southPanel.add(westPanel, BorderLayout.NORTH);
		southPanel.add(eastPanel, BorderLayout.SOUTH);

		cpPanel.add(comPortLabel);

		opPanel.add(operationModeLabel);

		lccPanel.add(loadCopyCounter);

		loadPanel.add(lccPanel);
		loadPanel.add(copyCountPanel);

		operationPanel.add(opPanel);
		operationPanel.add(operationModeSelect, BorderLayout.CENTER);

		comPanel.add(cpPanel);
		comPanel.add(comPortSelect, BorderLayout.SOUTH);

		westPanel.add(westTopPanel);
		westPanel.add(westCenterPanel);
		westPanel.add(westBottomPanel);

		eastPanel.add(eastTopPanel);
		eastPanel.add(eastCenterPanel);
		eastPanel.add(eastBottomPanel);

		eastTopPanel.add(enableButton);

		eastCenterPanel.add(copyPulseButton, BorderLayout.SOUTH);

		eastBottomPanel.add(monoButton);
		eastBottomPanel.add(colorButton);
		eastBottomPanel.add(creditPanel, BorderLayout.SOUTH);

		topNumPanel.add(finalNumberDisplay);
		bottomNumPanel.add(initialNumberDisplay);

		initialPanel.add(initialTypeDisplay);
		finalPanel.add(finalTypeDisplay);

		westTopPanel.add(topNumPanel);
		westTopPanel.add(finalPanel);

		westCenterPanel.add(bottomNumPanel);
		westCenterPanel.add(initialPanel);

		westBottomPanel.add(monoDisplayPanel);
		westBottomPanel.add(colorDisplayPanel);
		westBottomPanel.add(totalDisplayPanel);

		monoDisplayPanel.add(monoCopyDisplay);
		colorDisplayPanel.add(colorCopyDisplay);
		totalDisplayPanel.add(totalCopyDisplay);

		monoDisplayPanel.add(monoCountPanel);
		colorDisplayPanel.add(colorCountPanel);
		totalDisplayPanel.add(totalCountPanel);

		monoCountPanel.add(monoCopyTotal);
		colorCountPanel.add(colorCopyTotal);
		totalCountPanel.add(copyTotal);

		copyCountPanel.add(inputField);
		copyCountPanel.add(loadButton);

		creditPanel.add(availableCreditDisplay);
		creditPanel.add(creditCountPanel);

		creditCountPanel.add(creditCountLabel);

		pane.add(mainPanel);

		setSize(mainWidth, mainHeight);
		setTitle("FIH Tester");
		setBackground(gainsboro);
		getRootPane().setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	

	/*
	 * runs the SerialComm class
	 * sets comm to the selected port
	 */
	private void startConnection()
	{
		setComm(comPortSelect.getSelectedItem().toString());
		getComm();

		SerialComm ser = new SerialComm(this);
		ser.connect();
	}

	/*
	 * gets the operation mode selection
	 * creates a variable for the selection to match the expected arduino code inputs
	 */
	public String commandInput() {
		String initialCommand=operationModeSelect.getSelectedItem().toString();
		String finalCommand = "";
		if (initialCommand == "Copy Counter") {
			finalCommand = "cred ";
		} else if(initialCommand == "Always Enabled") {
			finalCommand = "on";
		} else if(initialCommand == "Always Disabled") {
			finalCommand= "off";
		}
		return finalCommand;
	}

	/*
	 * updates the lights
	 */
	public void updateLights(JButton whichButton, Color whichColor) {
		whichButton.setBackground(whichColor);

	}

	/*
	 * updates the totals of mono, copy, and overall, and the credit counter
	 */
	public void updateCount(String whichCount) {
		if (whichCount=="total") {
			copyTotal.setText(Integer.toString(totalCount));
		} else if (whichCount== "color") {
			colorCopyTotal.setText(Integer.toString(colorCount));
		} else if(whichCount== "mono") {
			monoCopyTotal.setText(Integer.toString(monoCount));
		} else if(whichCount== "credit") {
			creditCountLabel.setText(Integer.toString(creditCount));
			if (creditCount == 0) {
				updateLights(enableButton, Color.WHITE);
				updateLights(copyPulseButton, Color.white);
				updateLights(monoButton, Color.white);
				updateLights(colorButton,Color.WHITE);
			}
		}
		else {
			copyTotal.setText("0");
			colorCopyTotal.setText("0");
			monoCopyTotal.setText("0");
			creditCountLabel.setText("N/A");
		}
	}

	/*
	 * updates the display of M or C as the copies come in
	 * based on the typeUpdate array
	 * that is updated in the SerialComm class based on which type of copy is made
	 */
	public void updateDisplay() {
		if (typeUpdate.size() > 10 && typeUpdate.size() < 21) {
			initialPanel.remove(initialTypeDisplay);
			initialPanel.revalidate();
			initialPanel.repaint();

			finalPanel.remove(finalTypeDisplay);
			finalPanel.revalidate();
			finalPanel.repaint();

			finalFillLabel = initialFillLabel.substring(18)+" "+finalFillLabel;
			initialFillLabel = initialFillLabel.substring(0,17);

			initialFillLabel = typeUpdate.get(typeUpdate.size()-1)+" "+initialFillLabel;
		} else if (typeUpdate.size() > 20) {
			initialPanel.remove(initialTypeDisplay);
			initialPanel.revalidate();
			initialPanel.repaint();

			finalPanel.remove(finalTypeDisplay);
			finalPanel.revalidate();
			finalPanel.repaint();

			finalFillLabel = finalFillLabel.substring(0,17);
			finalFillLabel = initialFillLabel.substring(18)+" "+finalFillLabel;
			initialFillLabel = initialFillLabel.substring(0,17);

			initialFillLabel = typeUpdate.get(typeUpdate.size()-1)+" "+initialFillLabel;
		}
		else {
			if (typeUpdate.size()==1) {
				initialFillLabel = typeUpdate.get(typeUpdate.size()-1);
			} else {
				initialPanel.remove(initialTypeDisplay);
				initialPanel.revalidate();
				initialPanel.repaint();
				initialFillLabel = typeUpdate.get(typeUpdate.size()-1)+" "+initialFillLabel;
			}
		}

		initialTypeDisplay.setText(initialFillLabel);
		initialTypeDisplay.setFont(new Font("Consolas", Font.PLAIN, 25));

		finalTypeDisplay.setText(finalFillLabel);
		finalTypeDisplay.setFont(new Font("Consolas", Font.PLAIN, 25));

		initialPanel.add(initialTypeDisplay);
		finalPanel.add(finalTypeDisplay);
	}

	/*
	 * getters and setters
	 */
	public String getComm() { 
		return comm; 
	}

	public String setComm(final String comm) { 
		this.comm = comm; 
		return comm;
	}

	public String getSetting() { 
		return this.setting; 
	}

	public String setSetting(final String setting) { 
		this.setting = setting; 
		return setting;
	}

	public int getCredits() { 
		return this.creditVal; 
	}

	public int setCredits(final int creditVal) { 
		this.creditVal = creditVal; 
		return creditVal;
	}


	public static void main(String[] args) 
	{
		new Executive();

	}

}