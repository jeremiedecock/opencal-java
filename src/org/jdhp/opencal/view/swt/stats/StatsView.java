/*
 * OpenCAL version 3.0
 * Copyright (c) 2007 Jérémie Decock (http://www.jdhp.org)
 */

package org.jdhp.opencal.view.swt.stats;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
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

		TimeSeries s1 = new TimeSeries("Card Created Per Day", Day.class);
		s1.add(new Day(2, 2, 2001), 18);
		s1.add(new Day(3, 2, 2001), 6);
		s1.add(new Day(4, 2, 2001), 15);
		s1.add(new Day(5, 2, 2001), 16);
		s1.add(new Day(6, 2, 2001), 5);
		s1.add(new Day(7, 2, 2001), 14);
		s1.add(new Day(8, 2, 2001), 15);

		TimeSeries s2 = new TimeSeries("Revision Per Day", Day.class);
		s2.add(new Day(2, 2, 2001), 12);
		s2.add(new Day(3, 2, 2001), 2);
		s2.add(new Day(4, 2, 2001), 11);
		s2.add(new Day(5, 2, 2001), 12);
		s2.add(new Day(6, 2, 2001), 10);
		s2.add(new Day(7, 2, 2001), 1);
		s2.add(new Day(8, 2, 2001), 11);

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(s1);
		dataset.addSeries(s2);

		// ********** //
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart("Statistics", "", "", dataset, true, true, false);
		chart.setBackgroundPaint(new Color(this.parentComposite.getBackground().getRed(), this.parentComposite.getBackground().getGreen(), this.parentComposite.getBackground().getBlue()));
		chart.setPadding(new RectangleInsets(10.0, 10.0, 10.0, 10.0));

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
		
		// ********** //
		
		Composite awtComp = new Composite(this.parentComposite, SWT.EMBEDDED);
		Frame chartFrame = SWT_AWT.new_Frame(awtComp);
		
		chartFrame.setLayout(new GridLayout());
		ChartPanel cp = new ChartPanel(chart);
		chartFrame.add(cp);
		
	}
	
}
