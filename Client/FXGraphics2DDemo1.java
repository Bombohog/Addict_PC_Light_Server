/* =================
 * FXGraphics2DDemo1
 * =================
 * 
 * Copyright (c) 2014-2021, Object Refinery Limited.
 * All rights reserved.
 *
 * https://github.com/jfree/jfree-fxdemos
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   - Neither the name of the Object Refinery Limited nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL OBJECT REFINERY LIMITED BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.jfree.fx.FXGraphics2D;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * A demo showing the display of JFreeChart within a JavaFX application.
 * Note that this demo is for illustration only...the JFreeChart distribution
 * (from version 1.0.18 onwards) incorporates FXGraphics2D directly and 
 * provides a {@code ChartViewer} control that supports tooltips, panning,
 * zooming, mouse events and a context menu.
 * 
 * The ChartCanvas code is based on: 
 * http://dlemmermann.wordpress.com/2014/04/10/javafx-tip-1-resizable-canvas/
 * 
 */
public class FXGraphics2DDemo1 extends Application {
    
    static class ChartCanvas extends Canvas { 
        
        JFreeChart chart;
        
        private final FXGraphics2D g2;
        
        public ChartCanvas(JFreeChart chart) {
            this.chart = chart;
            this.g2 = new FXGraphics2D(getGraphicsContext2D());
            // Redraw canvas when size changes.
            widthProperty().addListener(e -> draw()); 
            heightProperty().addListener(e -> draw()); 
        }  
        
        private void draw() { 
            double width = getWidth(); 
            double height = getHeight();
            getGraphicsContext2D().clearRect(0, 0, width, height);
            this.chart.draw(this.g2, new Rectangle2D.Double(0, 0, width, 
                    height));
        } 
        
        @Override 
        public boolean isResizable() { 
            return true;
        }  
        
        @Override 
        public double prefWidth(double height) { return getWidth(); }  
        
        @Override 
        public double prefHeight(double width) { return getHeight(); } 
    } 

    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private static JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "International Coffee Organisation : Coffee Prices",    // title
            null,             // x-axis label
            "Tempetur",      // y-axis label
            dataset);

        String fontName = "Palatino";
        chart.getTitle().setFont(new Font(fontName, Font.BOLD, 18));
        chart.addSubtitle(new TextTitle("Source: http://www.ico.org/historical/2010-19/PDF/HIST-PRICES.pdf", new Font(fontName, Font.PLAIN, 14)));
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(false);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setLabelFont(new Font(fontName, Font.BOLD, 14));
        plot.getDomainAxis().setTickLabelFont(new Font(fontName, Font.PLAIN, 12));
        plot.getRangeAxis().setLabelFont(new Font(fontName, Font.BOLD, 14));
        plot.getRangeAxis().setTickLabelFont(new Font(fontName, Font.PLAIN, 12));
        chart.getLegend().setItemFont(new Font(fontName, Font.PLAIN, 14));
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDefaultShapesVisible(false);
            renderer.setDrawSeriesLineAsPath(true);
            // set the default stroke for all series
            renderer.setAutoPopulateSeriesStroke(false);
            renderer.setDefaultStroke(new BasicStroke(3.0f, 
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL), false);
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesPaint(1, new Color(24, 123, 58));
            renderer.setSeriesPaint(2, new Color(149, 201, 136));
            renderer.setSeriesPaint(3, new Color(1, 62, 29));
            renderer.setSeriesPaint(4, new Color(81, 176, 86));
            renderer.setSeriesPaint(5, new Color(0, 55, 122));
            renderer.setSeriesPaint(6, new Color(0, 92, 165));
            renderer.setDefaultLegendTextFont(new Font(fontName, Font.PLAIN, 14));
        }
        return chart;
    }

    /**
     * Creates a dataset, consisting of two series of monthly data.
     *
     * @return the dataset.
     */
    private static XYDataset createDataset() {
        TimeSeries s1 = new TimeSeries("Indicator Price");
        s1.add(new Month(1, 2010), 126.80);
        s1.add(new Month(2, 2010), 123.37);
        s1.add(new Month(3, 2010), 125.30);
        s1.add(new Month(4, 2010), 126.89);
        s1.add(new Month(5, 2010), 128.10);
        s1.add(new Month(6, 2010), 142.20);
        s1.add(new Month(7, 2010), 153.41);
        s1.add(new Month(8, 2010), 157.46);
        s1.add(new Month(9, 2010), 163.61);
        s1.add(new Month(10, 2010), 161.56);
        s1.add(new Month(11, 2010), 173.90);
        s1.add(new Month(12, 2010), 184.26);
        s1.add(new Month(1, 2011), 197.35);
        s1.add(new Month(2, 2011), 216.03);
        s1.add(new Month(3, 2011), 224.33);
        s1.add(new Month(4, 2011), 231.24);
        s1.add(new Month(5, 2011), 227.97);
        s1.add(new Month(6, 2011), 215.58);
        s1.add(new Month(7, 2011), 210.36);
        s1.add(new Month(8, 2011), 212.19);
        s1.add(new Month(9, 2011), 213.04);
        s1.add(new Month(10, 2011), 193.90);
        s1.add(new Month(11, 2011), 193.66);
        s1.add(new Month(12, 2011), 189.02);
        s1.add(new Month(1, 2012), 188.90);
        s1.add(new Month(2, 2012), 182.29);
        s1.add(new Month(3, 2012), 167.77);
        s1.add(new Month(4, 2012), 160.46);
        s1.add(new Month(5, 2012), 157.68);
        s1.add(new Month(6, 2012), 145.31);
        s1.add(new Month(7, 2012), 159.07);
        s1.add(new Month(8, 2012), 148.50);
        s1.add(new Month(9, 2012), 151.28);
        s1.add(new Month(10, 2012), 147.12);
        s1.add(new Month(11, 2012), 136.35);
        s1.add(new Month(12, 2012), 131.31);
        s1.add(new Month(1, 2013), 135.38);
        s1.add(new Month(2, 2013), 131.51);
        s1.add(new Month(3, 2013), 131.38);

        TimeSeries s2 = new TimeSeries("Tempetur");

        Random value = new Random();

        for (int i = 1; i < 12; i++) {
            for (int j = 2010; j <2013 ; j++) {
               s2.add(new Month(i,j),100 + value.nextDouble() * (300-100));
            }
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        return dataset;
    }

    @Override 
    public void start(Stage stage) throws Exception {
        XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset); 
        ChartCanvas canvas = new ChartCanvas(chart);
        StackPane stackPane = new StackPane(); 
        stackPane.getChildren().add(canvas);  
        // Bind canvas size to stack pane size. 
        canvas.widthProperty().bind( stackPane.widthProperty()); 
        canvas.heightProperty().bind( stackPane.heightProperty());  
        stage.setScene(new Scene(stackPane)); 
        stage.setTitle("FXGraphics2DDemo1.java"); 
        stage.setWidth(700);
        stage.setHeight(390);
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
  
}