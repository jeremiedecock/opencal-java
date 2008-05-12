/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view.swt.stats;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jdhp.opencal.controller.stats.StatsController;
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
public class StatsView {

	final private Composite parentComposite;
	
	final private TimeSeriesCollection dataset;
	
	final private JFreeChart chart;
	
	/**
	 * 
	 * @param parentComposite
	 */
	public StatsView(Composite parentComposite) {
		this.parentComposite = parentComposite;

		///////////////////////////////////////////////////////////////////////
		// Make statComposite /////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		
		this.parentComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		// ********** //
		
		TimeSeries s1 = new TimeSeries("Card created per day", Day.class);
		TreeMap<Date, Integer> cardCreationStats = StatsController.getCardCreationStats();
		Set entries = cardCreationStats.entrySet();
		Iterator<Set> it = entries.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Date date = (Date) entry.getKey();
			Integer value = (Integer) entry.getValue();
			s1.add(new Day(date), value);
		}

		TimeSeries s2 = new TimeSeries("Revision per day", Day.class);
		TreeMap<Date, Integer> revisionStats = StatsController.getRevisionStats();
		entries = revisionStats.entrySet();
		it = entries.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Date date = (Date) entry.getKey();
			Integer value = (Integer) entry.getValue();
			s2.add(new Day(date), value);
		}

		this.dataset = new TimeSeriesCollection();
		dataset.addSeries(s1);
		dataset.addSeries(s2);

		// ********** //
		
		this.chart = ChartFactory.createTimeSeriesChart("Statistics", "", "", dataset, true, true, false);
		this.chart.setBackgroundPaint(new Color(this.parentComposite.getBackground().getRed(), this.parentComposite.getBackground().getGreen(), this.parentComposite.getBackground().getBlue()));
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
	 */
	public void updateChart() {
//		TimeSeries s1 = new TimeSeries("Card created per day", Day.class);
//		TreeMap<Date, Integer> cardCreationStats = StatsController.getCardCreationStats();
//		Set entries = cardCreationStats.entrySet();
//		Iterator<Set> it = entries.iterator();
//		while(it.hasNext()) {
//			Map.Entry entry = (Map.Entry) it.next();
//			Date date = (Date) entry.getKey();
//			Integer value = (Integer) entry.getValue();
//			s1.add(new Day(date), value);
//		}
//
//		TimeSeries s2 = new TimeSeries("Revision per day", Day.class);
//		TreeMap<Date, Integer> revisionStats = StatsController.getRevisionStats();
//		entries = revisionStats.entrySet();
//		it = entries.iterator();
//		while(it.hasNext()) {
//			Map.Entry entry = (Map.Entry) it.next();
//			Date date = (Date) entry.getKey();
//			Integer value = (Integer) entry.getValue();
//			s2.add(new Day(date), value);
//		}
//
//		this.dataset.removeAllSeries();
//		this.dataset.addSeries(s1);
//		this.dataset.addSeries(s2);
//
//		// ********** //
//		
//		this.chart.setNotify(true);
	}
	
}
