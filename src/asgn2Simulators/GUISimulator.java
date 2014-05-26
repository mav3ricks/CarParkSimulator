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

	private void createGUI() {

		gui.mainFrame = gui.createFrame(300, 400);

		gui.mainPanel = gui.createPanel();

		gui.layoutPanel();

		gui.mainFrame.getContentPane().add(gui.mainPanel, BorderLayout.CENTER);

	}

	private void showGUI() {
		gui.mainFrame.setVisible(true);
	}

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

	private JLabel createLabel(String s) {
		JLabel label = new JLabel(s + ": \n");
		return label;

	};

	private JTextField createTextField() {
		JTextField textfield = new JTextField(10);
		textfield.setAlignmentY(LEFT_ALIGNMENT);
		return textfield;
	}

	private JTextArea createTextArea() {
		JTextArea textArea = new JTextArea(500, 250);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Arial", Font.BOLD, 15));
		textArea.setBounds(0, 12, 300, 50);
		// textArea.setText(s);
		textArea.setBorder(BorderFactory.createEtchedBorder());
		return textArea;
	}

	private JButton createButton(String s) {
		JButton button = new JButton();
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.addActionListener(this);
		button.setLocation(200, 100);
		button.setSize(20, 15);
		button.setText(s);
		return button;
	}

	private JPanel createPanel() {
		JPanel panel = new JPanel();
		return panel;

	}

	private void createTabs() {
		tabs = new JTabbedPane();
		tabs.addTab("Carpark Log", null, scrollBar, "Does nothing");
		tabs.addTab("Bar Chart", null, barChartPanel, "Does nothing");
		tabs.addTab("Time Chart", null, timePanel, "Does nothing");

	}

	private void layoutPanel() {
		GridBagLayout gbLayout = new GridBagLayout();
		mainPanel.setLayout(gbLayout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = 100;
		constraints.weighty = 100;

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
		addToPanel(mainPanel, btnStartSim, constraints, 2, 15, 2, 1);
	}

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

	@Override
	public void actionPerformed(ActionEvent e) {

		while (inputIString == false) {
			try {

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

				if (maxCSpaces < 0 || maxMCSpaces < 0 || maxQueueSize < 0) {

					JOptionPane.showMessageDialog(null,
							"Invalid Input. Negative value entered.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (maxSCSpaces < 0 || maxSCSpaces > maxCSpaces) {
					JOptionPane
							.showMessageDialog(
									null,
									"Invalid Input. 0 <= maxSmallCarSpaces <= maxCarSpaces",
									"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

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

		logFrame = createFrame(800, 500);
		logPanel = createPanel();
		barChartPanel = createPanel();
		timePanel = createPanel();
		mainFrame.setVisible(false);
		logFrame.setVisible(true);

		barChartPanel.add(charts.createBarChart());

		timePanel.add(charts.createTimeSeriesChart());

		logPanel.add(logText);
		scrollBar = new JScrollPane(logPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		createTabs();

		logFrame.getContentPane().add(tabs);

	}

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

	private boolean newVehiclesAllowed(int time) {
		boolean allowed = (time >= 1);
		return allowed && (time <= (Constants.CLOSING_TIME - 60));
	}

	public static void main(String[] args) {
		gui = new GUISimulator("CarParkSimulator");

		gui.createGUI();

		try {

			gui.log = new Log();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (args.length == 11) {

			try {

				gui.seedText.setText(args[1]);
				gui.intendedStayMeanText.setText(args[2]);
				gui.stdDeviationText.setText(args[3]);
				gui.carProbabilityText.setText(args[4]);
				gui.scProbabilityText.setText(args[5]);
				gui.mcProbabilityText.setText(args[6]);

				gui.maxCSpacesText.setText(args[7]);
				gui.maxSCSpacesText.setText(args[8]);
				gui.maxMCSpacesText.setText(args[9]);
				gui.maxQueueSizeText.setText(args[10]);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
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
		gui.showGUI();
	}

}
