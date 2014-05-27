/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 20/04/2014
 * 
 */
package asgn2Simulators;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Log;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;

/**
 * @author Ishaan
 * 
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable, ActionListener {
	// Declaring all variables for use in this class

	private JTextField seedText, carProbabilityText, scProbabilityText,
			mcProbabilityText, intendedStayMeanText, stdDeviationText,
			maxCSpacesText, maxSCSpacesText, maxMCSpacesText, maxQueueSizeText;

	private int seed, maxCSpaces, maxSCSpaces, maxMCSpaces, maxQueueSize;
	private double carProb, scProb, mcProb, intendedStay, stdDeviation;
	private JPanel mainPanel, logPanel, barChartPanel, timePanel;
	private JFrame mainFrame, logFrame;
	private JLabel seedLabel, cProbLabel, scProbLabel, mcProbLabel,
			intendedStayLabel, stdDeviationLabel, maxCSpaceLabel,
			maxSCSpaceLabel, maxMCSpaceLabel, maxQueueSizeLabel;

	private JTabbedPane tabs;
	private static GUISimulator gui;
	private JButton btnStartSim;
	private JTextArea logText;
	private JScrollPane scrollBar;
	private Log log;
	private CarPark carPark;
	private Simulator sim;
	private boolean inputIString;

	private ChartPanel charts;

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @author Ishaan 08647917 
	 * This method creates the GUI with a mainframe and
	 * includes layoutPanel method to set up all widgets.
	 */
	private void createGUI() {

		gui.mainFrame = gui.createFrame(300, 400);

		gui.mainPanel = gui.createPanel();

		gui.layoutPanel();

		gui.mainFrame.getContentPane().add(gui.mainPanel, BorderLayout.CENTER);

	}

	/**
	 * @author Ishaan 08647917 
	 * This method displays the interface to the user.
	 */
	private void showGUI() {

		gui.mainFrame.setVisible(true);

	}

	/**
	 * @author Ishaan 08647917 
	 * Creates the frame with set properties and returns
	 * the frame.
	 */
	private JFrame createFrame(int width, int height) {

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("CarParkSimulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(width, height));

		frame.setLocation(new Point(500, 200));
		frame.setLayout(new BorderLayout());
		frame.setResizable(true);
		frame.pack();

		return frame;

	};

	/**
	 * @author Ishaan 08647917 
	 * Creates a label. Method is used to create
	 * multiple labels. Takes in a String to set the text on the label.
	 */
	private JLabel createLabel(String s) {

		JLabel label = new JLabel(s + ": \n");
		return label;

	};

	/**
	 * @author Ishaan 08647917 
	 * Creates and returns a Text field. Used to create
	 * multiple text fields.
	 */
	private JTextField createTextField() {

		JTextField textfield = new JTextField(10);
		textfield.setAlignmentY(LEFT_ALIGNMENT);
		return textfield;

	}

	/**
	 * @author Ishaan 08647917 
	 * Creates and returns a text area.
	 */
	private JTextArea createTextArea() {

		JTextArea textArea = new JTextArea(500, 250);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Arial", Font.BOLD, 15));
		textArea.setBounds(0, 12, 300, 50);
		textArea.setBorder(BorderFactory.createEtchedBorder());

		return textArea;
	}

	/**
	 * @author Ishaan 08647917 
	 * Creates and returns a button. Takes in a String
	 * and sets the text on the button.
	 */
	private JButton createButton(String s) {
		JButton button = new JButton();
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.addActionListener(this);
		button.setLocation(200, 100);
		button.setSize(20, 15);
		button.setText(s);
		return button;
	}

	/**
	 * @author Ishaan 08647917 
	 * Creates and returns a panel.
	 */
	private JPanel createPanel() {

		JPanel panel = new JPanel();
		return panel;

	}

	/**
	 * @author Ishaan 08647917 
	 * Creates the tabs for the 3 categories of data.
	 * Carpark Log, Bar chart and Time chart.
	 */
	private void createTabs() {

		tabs = new JTabbedPane();
		tabs.addTab("Carpark Log", null, scrollBar, "Does nothing");
		tabs.addTab("Bar Chart", null, barChartPanel, "Does nothing");
		tabs.addTab("Time Chart", null, timePanel, "Does nothing");

	}

	/**
	 * @author Ishaan 08647917 
	 * layoutPanel method does most of the creation and
	 * setup of the whole gui simulation.
	 */
	private void layoutPanel() {

		// Creates a new instance of GridBagLayout
		GridBagLayout gbLayout = new GridBagLayout();
		mainPanel.setLayout(gbLayout);

		// Creates a new instance of constraints and sets the basic properties
		// for these constraints.
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = 100;
		constraints.weighty = 100;

		// This section deals with creation of button, textfields and labels.
		btnStartSim = createButton("Start Simulation");
		seedText = createTextField();
		carProbabilityText = createTextField();
		scProbabilityText = createTextField();
		mcProbabilityText = createTextField();
		intendedStayMeanText = createTextField();
		stdDeviationText = createTextField();
		maxCSpacesText = createTextField();
		maxSCSpacesText = createTextField();
		maxMCSpacesText = createTextField();
		maxQueueSizeText = createTextField();

		seedLabel = createLabel("Random Seed");
		cProbLabel = createLabel("Car Probability");
		scProbLabel = createLabel("Small Car Probability");
		mcProbLabel = createLabel("MotorCycle Probability");
		intendedStayLabel = createLabel("Intended Stay Mean");
		stdDeviationLabel = createLabel("Standard Deviation");
		maxCSpaceLabel = createLabel("Max Car Spaces");
		maxSCSpaceLabel = createLabel("Max Small Car Spaces");
		maxMCSpaceLabel = createLabel("Max MotorCycle Spaces");
		maxQueueSizeLabel = createLabel("Max Queue Size");

		// The created labels and corresponding text fields are added to the
		// panel using addToPanel method.
		addToPanel(mainPanel, seedLabel, constraints, 0, 1, 2, 1);
		addToPanel(mainPanel, seedText, constraints, 2, 1, 2, 1);

		addToPanel(mainPanel, cProbLabel, constraints, 0, 2, 2, 1);
		addToPanel(mainPanel, carProbabilityText, constraints, 2, 2, 2, 1);

		addToPanel(mainPanel, scProbLabel, constraints, 0, 3, 2, 1);
		addToPanel(mainPanel, scProbabilityText, constraints, 2, 3, 2, 1);

		addToPanel(mainPanel, mcProbLabel, constraints, 0, 4, 2, 1);
		addToPanel(mainPanel, mcProbabilityText, constraints, 2, 4, 2, 1);

		addToPanel(mainPanel, intendedStayLabel, constraints, 0, 5, 2, 1);
		addToPanel(mainPanel, intendedStayMeanText, constraints, 2, 5, 2, 1);

		addToPanel(mainPanel, stdDeviationLabel, constraints, 0, 6, 2, 1);
		addToPanel(mainPanel, stdDeviationText, constraints, 2, 6, 2, 1);

		addToPanel(mainPanel, maxCSpaceLabel, constraints, 0, 7, 2, 1);
		addToPanel(mainPanel, maxCSpacesText, constraints, 2, 7, 2, 1);

		addToPanel(mainPanel, maxSCSpaceLabel, constraints, 0, 8, 2, 1);
		addToPanel(mainPanel, maxSCSpacesText, constraints, 2, 8, 2, 1);

		addToPanel(mainPanel, maxMCSpaceLabel, constraints, 0, 9, 2, 1);
		addToPanel(mainPanel, maxMCSpacesText, constraints, 2, 9, 2, 1);

		addToPanel(mainPanel, maxQueueSizeLabel, constraints, 0, 10, 2, 1);
		addToPanel(mainPanel, maxQueueSizeText, constraints, 2, 10, 2, 1);

		// Finally it adds the "Start Simuation" button to the panel.
		addToPanel(mainPanel, btnStartSim, constraints, 2, 15, 2, 1);
	}

	/**
	 * @author Ishaan 08647917 
	 * This method takes in a few parameters and sets
	 * the constraint properties accordingly. Eventually it adds the
	 * constraints and the component to the panel.
	 */
	private void addToPanel(JPanel jp, Component c,
			GridBagConstraints constraints, int x, int y, int w, int h) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		jp.add(c, constraints);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	/**
	 * @author Ishaan 08647917 
	 * This method gets executed when the user clicks on
	 * "Start Simuation" button. It gets the user input from the text
	 * fields and checks it for any invalid data. Once deemed that the
	 * data inserted is legitimate, it tries to create a new instance of
	 * the simulator and carpark and parse these values from the gui
	 * back to the classes for log computation.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// this loop tests the code for any string inserted instead of a number
		// (Double or int).
		while (inputIString == false) {

			try {
				// Gets all the values from the gui and stores them in
				// variables.
				seed = Integer.parseInt(seedText.getText());
				intendedStay = Double.parseDouble(intendedStayMeanText
						.getText());
				stdDeviation = Double.parseDouble(stdDeviationText.getText());
				carProb = Double.parseDouble(carProbabilityText.getText());
				scProb = Double.parseDouble(scProbabilityText.getText());
				mcProb = Double.parseDouble(mcProbabilityText.getText());
				maxCSpaces = Integer.parseInt(maxCSpacesText.getText());
				maxSCSpaces = Integer.parseInt(maxSCSpacesText.getText());
				maxMCSpaces = Integer.parseInt(maxMCSpacesText.getText());
				maxQueueSize = Integer.parseInt(maxQueueSizeText.getText());

				// Tests to ensure that maxCSpaces, maxMCSpaces and maxQueueSize
				// are not negative.
				if (maxCSpaces < 0 || maxMCSpaces < 0 || maxQueueSize < 0) {

					JOptionPane.showMessageDialog(null,
							"Invalid Input. Negative value entered.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// Tests to ensure that maxSmallCarSpaces is not negative and
				// not greater than maxCarSpaces.
				if (maxSCSpaces < 0 || maxSCSpaces > maxCSpaces) {
					JOptionPane
							.showMessageDialog(
									null,
									"Invalid Input. 0 <= maxSmallCarSpaces <= maxCarSpaces",
									"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// Creates a new instance of Simulator and carPark and parses
				// all gui values for computing results.
				try {
					sim = new Simulator(seed, intendedStay, stdDeviation,
							carProb, scProb, mcProb);

					carPark = new CarPark(maxCSpaces, maxSCSpaces, maxMCSpaces,
							maxQueueSize);
				} catch (SimulationException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				inputIString = true;

			} catch (Exception exception) {

				JOptionPane.showMessageDialog(null,
						"Invalid Input. Enter a valid number.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

		}

		// this step runs the simulation after creating simulator and carpark
		// instances.
		try {
			gui.runSimulation();
		} catch (VehicleException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SimulationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// A new log frame, panels is generated so as to display the results
		// comprising of the Log, Bar Chart and Time Chart.
		logFrame = createFrame(800, 500);
		logPanel = createPanel();
		barChartPanel = createPanel();
		timePanel = createPanel();

		// The initial user input screen is set to false to make it disappear.
		mainFrame.setVisible(false);
		// The new display screen with results is set to true and it appears.
		logFrame.setVisible(true);

		// A Bar chart is added to the panel barChartPanel.
		barChartPanel.add(charts.createBarChart());
		// A Time chart is added to the panel timePanel.
		timePanel.add(charts.createTimeSeriesChart());
		// A Textarea is added to to the panel logPanel.
		logPanel.add(logText);

		// A scrollbar is created to browse data on display.
		scrollBar = new JScrollPane(logPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// this method creates the tabs needed.
		createTabs();
		// The created tabs are added to the logFrame.
		logFrame.getContentPane().add(tabs);

	}

	/**
	 * @author Ishaan 08647917 
	 * This method has been adapted from
	 * SimulationRunner class file.
	 */
	private void runSimulation() throws VehicleException, SimulationException,
			IOException {

		charts = new ChartPanel();

		logText = createTextArea();

		log.initialEntry(carPark, sim);
		for (int time = 0; time <= Constants.CLOSING_TIME; time++) {
			// queue elements exceed max waiting time
			if (!carPark.queueEmpty()) {
				carPark.archiveQueueFailures(time);
			}
			// vehicles whose time has expired
			if (!carPark.carParkEmpty()) {
				// force exit at closing time, otherwise normal
				boolean force = (time == Constants.CLOSING_TIME);
				carPark.archiveDepartingVehicles(time, force);
			}
			// attempt to clear the queue
			if (!carPark.carParkFull()) {
				carPark.processQueue(time, sim);
			}
			// new vehicles from minute 1 until the last hour
			if (newVehiclesAllowed(time)) {
				carPark.tryProcessNewVehicles(time, sim);
			}

			String status = carPark.getStatus(time);

			charts.updateStatistics(status, time);

			// Log progress
			log.writer.write(status);

			logText.append(status);

		}
		log.finalise(carPark);
	}

	/**
	 * @author Ishaan 08647917
	 */
	private boolean newVehiclesAllowed(int time) {
		boolean allowed = (time >= 1);
		return allowed && (time <= (Constants.CLOSING_TIME - 60));
	}

	/**
	 * @author Ishaan 08647917
	 */
	public static void main(String[] args) {

		// creates a new instance of GUISimulator class.
		gui = new GUISimulator("CarParkSimulator");
		// executes the createGUI method.
		gui.createGUI();

		try {
			// creates a new instance of the log class.
			gui.log = new Log();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this part checks if there are 10 command line arguments parsed into
		// the program. If yes, all 10 arguments are stored in appropriate text
		// fields.
		if (args.length == 11) {

			try {

				gui.seedText.setText(args[1]);
				gui.carProbabilityText.setText(args[2]);
				gui.scProbabilityText.setText(args[3]);
				gui.mcProbabilityText.setText(args[4]);
				gui.intendedStayMeanText.setText(args[5]);
				gui.stdDeviationText.setText(args[6]);
				

				gui.maxCSpacesText.setText(args[7]);
				gui.maxSCSpacesText.setText(args[8]);
				gui.maxMCSpacesText.setText(args[9]);
				gui.maxQueueSizeText.setText(args[10]);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
		// if command line arguments parsed are less than 11 or zero, default
		// constant values are used as input to the simulator.
		if (args.length != 11) {

			try {

				gui.seedText.setText(Integer.toString(Constants.DEFAULT_SEED));
				gui.intendedStayMeanText.setText(Double
						.toString(Constants.DEFAULT_INTENDED_STAY_MEAN));
				gui.stdDeviationText.setText(Double
						.toString(Constants.DEFAULT_INTENDED_STAY_SD));
				gui.carProbabilityText.setText(Double
						.toString(Constants.DEFAULT_CAR_PROB));
				gui.scProbabilityText.setText(Double
						.toString(Constants.DEFAULT_SMALL_CAR_PROB));
				gui.mcProbabilityText.setText(Double
						.toString(Constants.DEFAULT_MOTORCYCLE_PROB));
				gui.maxCSpacesText.setText(Integer
						.toString(Constants.DEFAULT_MAX_CAR_SPACES));
				gui.maxSCSpacesText.setText(Integer
						.toString(Constants.DEFAULT_MAX_SMALL_CAR_SPACES));
				gui.maxMCSpacesText.setText(Integer
						.toString(Constants.DEFAULT_MAX_MOTORCYCLE_SPACES));
				gui.maxQueueSizeText.setText(Integer
						.toString(Constants.DEFAULT_MAX_QUEUE_SIZE));

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		// this method displays the gui program.
		gui.showGUI();
	}

}
