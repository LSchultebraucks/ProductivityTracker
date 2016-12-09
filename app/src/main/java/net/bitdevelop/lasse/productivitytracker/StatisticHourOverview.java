package net.bitdevelop.lasse.productivitytracker;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class StatisticHourOverview extends AppCompatActivity {

    private LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_hour_overview);

        mLineChart = (LineChart) findViewById(R.id.hour_chart);

        configLineChart();
        updateLineChart();
    }

    private void configLineChart() {
        mLineChart.setDescription("");
        mLineChart.setDrawGridBackground(false);
        mLineChart.setBackgroundColor(Color.BLUE);
        mLineChart.setTouchEnabled(false);
        mLineChart.setDragEnabled(false);
        mLineChart.setScaleEnabled(true);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.setBackgroundColor(Color.TRANSPARENT);
        mLineChart.setNoDataTextDescription(getString(R.string.no_data_graph_description));

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinValue(0);
        xAxis.setAxisMaxValue(24);
        YAxis yAxis = mLineChart.getAxisLeft();
        yAxis.setAxisMaxValue(10);
        yAxis.setAxisMinValue(0);
        YAxis leftAxis = mLineChart.getAxisRight();
        leftAxis.setEnabled(false);

        mLineChart.getXAxis().setDrawLabels(true);


        mLineChart.invalidate();
    }

    private void updateLineChart() {
        List<Entry> entryList = getEntryList();
        LineDataSet dataSet = new LineDataSet(entryList, "DataSet 1");
        dataSet.setColor(Color.BLACK);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setHighlightLineWidth(4f);
        dataSet.setLabel("");
        dataSet.setValueTextSize(12f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setColor(Color.parseColor("#336699"));
        dataSet.setValueTypeface(Typeface.MONOSPACE);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.parseColor("#336699"));
        dataSet.setCircleHoleRadius(0.5f);
        dataSet.setCircleColorHole(Color.parseColor("#336699"));

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean showText = sharedPref.getBoolean("key_enable_text_data", false);
        if (showText) {
            dataSet.setValueTextColor(Color.parseColor("#336699"));
        } else {
            dataSet.setValueTextColor(Color.TRANSPARENT);
        }


        LineData data = new LineData(dataSet);

        mLineChart.setData(data);
        mLineChart.invalidate();
    }

    private List<Entry> getEntryList() {
        List<Entry> entryList = new ArrayList<>();
        List<Day> dayList = Day.listAll(Day.class);
        for (int i = 0; i < 24; i++) {
            int count = 0;
            int sum = 0;
            for (Day day : dayList) {
                if (day.getHour() == i) {
                    sum += day.getEnergyLevel();
                    count++;
                }
            }

            if (count > 0){
                entryList.add(new Entry(i, sum/count));
            }
        }
        return entryList;
    }
}
