package net.bitdevelop.lasse.productivitytracker;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.github.mikephil.charting.components.Legend.LegendPosition.BELOW_CHART_CENTER;


public class StatisticDayOverview extends AppCompatActivity {

    LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_day_overview);

        mLineChart = (LineChart) findViewById(R.id.day_line_chart);
        configChart();
        updateData();
    }

    private void updateData() {
        ArrayList<LineData> dataSets = new ArrayList<>();

        //Getting Data from DayList in Array
        List<Day> dayList = Day.listAll(Day.class);
        Float day_average[] = new Float[7];
        for (int i = 0; i < 7; i++) {
            int sum = 0;
            int count = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, i);
            for (Day day : dayList) {
                Calendar dayCalendar = Calendar.getInstance();
                dayCalendar.setTime(day.getDate());
                if (dayCalendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK)) {
                    sum += day.getEnergyLevel();
                    count++;
                }
            }
            if (count != 0) {
                if (i == 0) {
                    day_average[5] =(float)sum/count;
                } else if (i == 1) {
                    day_average[6] = (float)sum/count;
                }else if (i == 2) {
                    day_average[0] = (float)sum/count;
                }else if (i == 3) {
                    day_average[1] = (float)sum/count;
                }else if (i == 4) {
                    day_average[2] = (float)sum/count;
                }else if (i == 5) {
                    day_average[3] = (float)sum/count;
                }else if (i == 6) {
                    day_average[4] = (float)sum/count;
                }
            } else {
                if (i == 0) {
                    day_average[5] = 0f;
                } else if (i == 1) {
                    day_average[6] = 0f;
                }else if (i == 2) {
                    day_average[0] = 0f;
                }else if (i == 3) {
                    day_average[1] = 0f;
                }else if (i == 4) {
                    day_average[2] = 0f;
                }else if (i == 5) {
                    day_average[3] = 0f;
                }else if (i == 6) {
                    day_average[4] = 0f;
                }
            }
        }
        int most_productive_day = 0;
        for (int i = 0; i < 7; i++) {
            if (day_average[i] > day_average[most_productive_day]) {
                most_productive_day = i;
            }
        }

        int most_unproductive_day = most_productive_day;
        for (int i = 0; i < 7; i++) {
            if (day_average[i] < day_average[most_unproductive_day] && day_average[i] != 0) {
                most_unproductive_day = i;
            }
        }

        ArrayList<Entry> valueSet = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            BarEntry entry;
            if (day_average[i] != 0) {
                entry = new BarEntry(i, day_average[i]);
                valueSet.add(entry);
            }
        }

        LineDataSet dataSet = new LineDataSet(valueSet, "");;

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

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add(getString(R.string.sunday));
        xAxis.add(getString(R.string.monday));
        xAxis.add(getString(R.string.tuesday));
        xAxis.add(getString(R.string.wednesday));
        xAxis.add(getString(R.string.thursday));
        xAxis.add(getString(R.string.friday));
        xAxis.add(getString(R.string.saturday));
        return xAxis;
    }

    private void configChart() {
        //mBarChart.setFitBars(true);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.animateY(2000);
        mLineChart.setTouchEnabled(false);
        mLineChart.setDragEnabled(false);
        mLineChart.setScaleEnabled(true);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.setDescription(getString(R.string.statistics_day_description));
        mLineChart.setDescriptionTextSize(12f);
        mLineChart.setDescriptionColor(Color.BLACK);
        //mLineChart.setDescriptionPosition(0, 0);

        YAxis yAxis = mLineChart.getAxisLeft();
        yAxis.setAxisMinValue(0);
        yAxis.setAxisMaxValue(10);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setAxisMinValue(0);
        xAxis.setAxisMaxValue(6);
        Legend legend = mLineChart.getLegend();
        legend.setEnabled(false);
        mLineChart.invalidate();
    }
}
