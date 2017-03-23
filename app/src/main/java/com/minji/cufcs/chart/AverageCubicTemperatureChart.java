/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.minji.cufcs.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.minji.cufcs.R;
import com.minji.cufcs.uitls.ViewsUitls;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

/**
 * Average temperature demo chart.
 */
public class AverageCubicTemperatureChart extends AbstractDemoChart {
	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Average temperature";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "The average temperature in 4 Greek islands (cubic line chart)";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public XYMultipleSeriesRenderer execute() {

		// 此为曲线的颜色
		int[] colors = new int[] { Color.BLUE, Color.CYAN };

		// 此为曲线上节点的样式
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.DIAMOND };

		// 此处方法内还有一系列设置
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);

		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);// 是否填充节点样式
		}

		// 曲线图标的标题 X轴名称 Y轴名称 X起点 X终点 Y起点 Y终点 XY轴颜色 与轴标颜色
		setChartSettings(renderer, "内/外河水位折线图", "小时/h", "水位/m", 0, 25, 0, 10,
				Color.BLACK, Color.BLACK);

		renderer.setApplyBackgroundColor(true);// 必须设置为true，颜色值才生效
		renderer.setBackgroundColor(Color.WHITE);// 设置表格背景色
		renderer.setMarginsColor(Color.WHITE);// 设置周边背景色

		renderer.setXLabels(24);// 设置X轴分隔数
		renderer.setYLabels(10);// 设置Y轴分隔数

		renderer.setShowGrid(true);// 设置是否显示网格
		renderer.setGridColor(Color.parseColor("#88999999"));// 设置网格线的颜色

		// 设置X轴刻度对应的文本的位置
		renderer.setXLabelsAlign(Align.CENTER);// 设置X轴下的标签相对于X轴上刻度的位置
		// 设置Y轴刻度对应的文本的位置
		renderer.setYLabelsVerticalPadding(ViewsUitls.dptopx(-5));
		renderer.setYLabelsPadding(ViewsUitls.dptopx(16));

		// 设置拉动的范围 前两个为X轴的拉动范围 后两个为Y轴的
		renderer.setPanLimits(new double[] { 0, 24, 0, 100 });

		// 设置是否可以缩放
		renderer.setZoomButtonsVisible(false);
		renderer.setZoomEnabled(false, false);

		// cubeLineChartView.repaint();

		return renderer;
	}

}
