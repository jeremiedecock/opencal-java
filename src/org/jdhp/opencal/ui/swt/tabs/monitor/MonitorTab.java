/*
 * OpenCAL version 3.0
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock
 */

package org.jdhp.opencal.ui.swt.tabs.monitor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jdhp.opencal.OpenCAL;
import org.jdhp.opencal.model.cardcollection.CardCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 * 
 * @author Jérémie Decock
 *
 */
public class MonitorTab {

	final private Composite parentComposite;
	
	final private TimeSeriesCollection dataset;
	
	final private JFreeChart chart;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public MonitorTab(Composite parentComposite) {
		this.parentComposite = parentComposite;

		this.parentComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		// ********** //
		
		this.dataset = new TimeSeriesCollection();
		
		this.chart = ChartFactory.createTimeSeriesChart("Statistics", "", "", dataset, true, true, false);
		this.chart.setBackgroundPaint(new Color(this.parentComposite.getBackground().getRed(),
				                                this.parentComposite.getBackground().getGreen(),
				                                this.parentComposite.getBackground().getBlue()));
		this.chart.setPadding(new RectangleInsets(10.0, 10.0, 10.0, 10.0));

		XYPlot plot = (XYPlot) this.chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
		
		this.chart.getLegend().setMargin(new RectangleInsets(10.0, 0.0, 5.0, 0.0));
		this.chart.getLegend().setItemFont(new Font("Monospaced", Font.PLAIN, 9));
		
		// ********** //
		
		Composite awtComp = new Composite(this.parentComposite, SWT.EMBEDDED);
		Frame chartFrame = SWT_AWT.new_Frame(awtComp);
		
		chartFrame.setLayout(new GridLayout());
		ChartPanel cp = new ChartPanel(this.chart);
		chartFrame.add(cp);
	}
	
	/**
	 * 
	 * Update the dataset
	 */
	public void update() {
		int numberOfDays = 30;
		
		TimeSeries s1 = new TimeSeries("Card created per day", Day.class);
		TimeSeries s2 = new TimeSeries("Revision per day", Day.class);
		
		int[] cardCreationStats = OpenCAL.cardCollection.getCardCreationStats(numberOfDays);
		int[] revisionStats = OpenCAL.cardCollection.getRevisionStats(numberOfDays);

		GregorianCalendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, -numberOfDays+1);
		
		for(int i=0 ; i<cardCreationStats.length ; i++) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			
			s1.addOrUpdate(new Day(date.get(Calendar.DAY_OF_MONTH),
					               date.get(Calendar.MONTH) + 1,
					               date.get(Calendar.YEAR)),
					       cardCreationStats[i]);
			
			s2.addOrUpdate(new Day(date.get(Calendar.DAY_OF_MONTH),
		                           date.get(Calendar.MONTH) + 1,
		                           date.get(Calendar.YEAR)),
		                   revisionStats[i]);
		}

		this.dataset.removeAllSeries();
		this.dataset.addSeries(s1);
		this.dataset.addSeries(s2);

		if(this.chart != null) this.chart.setNotify(true);
	}
	
}
