package net.bitdevelop.lasse.productivitytracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_NEW_ENTRY = 0;

    private LineChart mLineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SugarContext.init(getApplicationContext());

        mLineChart = (LineChart) findViewById(R.id.chart);

        configLineChart();
        updateLineChart();

        scheduleNotification();

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateLineChart();
    }

    public void scheduleNotification() {
        Intent intent = new Intent(getApplicationContext(), NotificationAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, NotificationAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (calendar.get(Calendar.HOUR_OF_DAY) != 23) {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
        } else {
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 50);
        }
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), NotificationAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, NotificationAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SugarContext.terminate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_entry:
                // Start NewEntryActivity forResult
                Intent intentNewEntry = new Intent(getApplicationContext(), NewEntryActivity.class);
                startActivityForResult(intentNewEntry, REQUEST_CODE_NEW_ENTRY);
                return true;
            case R.id.statistics:
                // Open Statistics
                Intent intentStatistic = new Intent(getApplicationContext(), StatisticOverView.class);
                startActivity(intentStatistic);
                return true;
            case R.id.settings:
                // Open Settings
                Intent intentSettings = new Intent(getApplicationContext(), PreferenceActivity.class);
                startActivity(intentSettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_NEW_ENTRY) {
            if (data == null) {
                return;
            } else {
                int energyLevel = data.getIntExtra(NewEntryActivity.EXTRA_ENERGY_LEVEL, 0);
                if (energyLevel != 0) {

                    updateLineChart();
                } else {
                    return;
                }
            }
        }
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
