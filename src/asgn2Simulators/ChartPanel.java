package asgn2Simulators;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class ChartPanel {

	final String series1 = "Total";
	final String series2 = "Satisfied";
	final String series3 = "Dissatisfied";

	final String category1 = "Total Vehicles";
	final String category2 = "Big Cars";
	final String category3 = "Small Cars";
	final String category4 = "Motor Cycles";

	private int satTotal, satBC, satSC, satMC;
	private int disTotal, disBC, disSC, disMC;

	int totalNum, totalParked, totalCars, numSC, numMC, numDissatisfied,
			numArchived, numQueued;

	TimeSeriesCollection tsc;
	TimeSeries vehTotal, vehParked, carsParked, scParked, mcParked, vehQueued,
			vehArchived, vehDissatisfied;
	Calendar cal;

	public ChartPanel() {
		initTimeSeriesChart();
	}

	private void initTimeSeriesChart() {

		tsc = new TimeSeriesCollection();
		vehTotal = new TimeSeries("Count (Total)");
		vehParked = new TimeSeries("Parked (Total)");
		carsParked = new TimeSeries("Cars");
		scParked = new TimeSeries("Small Cars");
		mcParked = new TimeSeries("Motor Cycles");
		vehQueued = new TimeSeries("Queue");
		vehArchived = new TimeSeries("Archived");
		vehDissatisfied = new TimeSeries("Dissatisfied");

		cal = GregorianCalendar.getInstance();
	}

	public org.jfree.chart.ChartPanel createBarChart() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(satTotal + disTotal, series1, category1);
		dataset.addValue(satBC + disBC, series1, category2);
		dataset.addValue(satSC + disSC, series1, category3);
		dataset.addValue(satMC + satMC, series1, category4);

		dataset.addValue(satTotal, series2, category1);
		dataset.addValue(satBC, series2, category2);
		dataset.addValue(satSC, series2, category3);
		dataset.addValue(satMC, series2, category4);

		dataset.addValue(disTotal, series3, category1);
		dataset.addValue(disBC, series3, category2);
		dataset.addValue(disSC, series3, category3);
		dataset.addValue(disMC, series3, category4);

		JFreeChart chart = ChartFactory.createBarChart(
				"Car Park Simulator Bar Chart", "Type of Vehicle", "Vehicles",
				dataset, PlotOrientation.VERTICAL, true, true, false);

		org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(
				chart);

		return chartPanel;
	}

	public org.jfree.chart.ChartPanel createTimeSeriesChart() {

		tsc.addSeries(vehTotal);
		tsc.addSeries(vehParked);
		tsc.addSeries(carsParked);
		tsc.addSeries(scParked);
		tsc.addSeries(mcParked);
		tsc.addSeries(vehQueued);
		tsc.addSeries(vehArchived);
		tsc.addSeries(vehDissatisfied);

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Car Park Simulator Time Series Chart", "hh:mm:ss", "Vehicles",
				tsc, true, true, false);

		final XYPlot plot = chart.getXYPlot();
		ValueAxis domain = plot.getDomainAxis();
		domain.setAutoRange(true);
		ValueAxis range = plot.getRangeAxis();
		range.setAutoRange(true);

		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0,
				Color.BLACK);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(1,
				Color.BLUE);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(2,
				Color.CYAN);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(3,
				Color.GRAY);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(4,
				Color.DARK_GRAY);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(5,
				Color.YELLOW);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(6,
				Color.GREEN);
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(7,
				Color.RED);

		org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(
				chart);

		return chartPanel;
	}

	public void updateStatistics(String status, int simTime) {
		Pattern pattern;
		Matcher matcher;

		String vehClass;

		if (status.contains("P>A")) {
			pattern = Pattern.compile("([C|S|M]):P>A");
			matcher = pattern.matcher(status);
			while (matcher.find()) {
				vehClass = matcher.group(1);
				if (vehClass.equals("C"))
					satBC++;
				if (vehClass.equals("S"))
					satSC++;
				if (vehClass.equals("M"))
					satMC++;
				satTotal++;
			}
		}

		if (status.contains("N>A") || status.contains("Q>A")) {
			pattern = Pattern.compile("([C|S|M]):[N|Q]>A");
			matcher = pattern.matcher(status);
			while (matcher.find()) {
				vehClass = matcher.group(1);
				if (vehClass.equals("C"))
					disBC++;
				if (vehClass.equals("S"))
					disSC++;
				if (vehClass.equals("M"))
					disMC++;
				disTotal++;
			}
		}

		cal.set(2014, 0, 1, 6, simTime);
		Date timePoint = cal.getTime();

		pattern = Pattern
				.compile("\\d+::(\\d+)::P:(\\d+)::C:(\\d+)::S:(\\d+)::M:(\\d+)::D:(\\d+)::A:(\\d+)::Q:(\\d+)");
		matcher = pattern.matcher(status);
		while (matcher.find()) {
			totalNum = Integer.parseInt(matcher.group(1));
			totalParked = Integer.parseInt(matcher.group(2));
			totalCars = Integer.parseInt(matcher.group(3));
			numSC = Integer.parseInt(matcher.group(4));
			numMC = Integer.parseInt(matcher.group(5));
			numDissatisfied = Integer.parseInt(matcher.group(6));
			numArchived = Integer.parseInt(matcher.group(7));
			numQueued = Integer.parseInt(matcher.group(8));
		}

		vehTotal.add(new Minute(timePoint), totalNum);
		vehParked.add(new Minute(timePoint), totalParked);
		carsParked.add(new Minute(timePoint), totalCars);
		scParked.add(new Minute(timePoint), numSC);
		mcParked.add(new Minute(timePoint), numMC);
		vehQueued.add(new Minute(timePoint), numQueued);
		vehArchived.add(new Minute(timePoint), numArchived);
		vehDissatisfied.add(new Minute(timePoint), numDissatisfied);
	}
}
