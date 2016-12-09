package net.bitdevelop.lasse.productivitytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticActivity extends AppCompatActivity {

    private TextView mMostProductiveHourTextView;
    private TextView mSecondMostProductiveHourTextView;
    private TextView mLeastProductiveHourTextView;
    private TextView mMostProductiveDayTextView;
    private TextView mMostUnproductiveDayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statisitc);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mMostProductiveHourTextView = (TextView) findViewById(R.id.most_productive_hour_statistics_text_view);
        mSecondMostProductiveHourTextView = (TextView) findViewById(R.id.second_most_productive_hour_statistics_text_view);
        mLeastProductiveHourTextView = (TextView) findViewById(R.id.least_productive_hour_statistics_text_view);
        mMostProductiveDayTextView = (TextView) findViewById(R.id.most_productive_day_statistics_text_view);
        mMostUnproductiveDayTextView = (TextView) findViewById(R.id.most_unproductive_day_statistics_text_view);

        List<Entry> entryList = getEntryList();

        /*
        int most_productive_hour_energy_level = -1;
        int most_productive_hour = -1;
        for (Entry entry : entryList) {
            if (entry.getY() > most_productive_hour_energy_level) {
                most_productive_hour = (int) entry.getX();
                most_productive_hour_energy_level = (int) entry.getY();
            }
        }

        int second_most_productive_hour_energy_level = -1;
        int second_most_productive_hour = -1;
        for (Entry entry : entryList) {
            if (entry.getY() > second_most_productive_hour_energy_level && entry.getY() < most_productive_hour_energy_level) {
                second_most_productive_hour = (int) entry.getX();
                second_most_productive_hour_energy_level = (int) entry.getY();
            }
        }

        int least_productive_hour_energy_level = 11;
        int least_productive_hour = -1;
        for (Entry entry : entryList) {
            if (entry.getY() < least_productive_hour_energy_level && entry.getY() > 0) {
                least_productive_hour = (int) entry.getX();
                least_productive_hour_energy_level = (int) entry.getY();
            }
        }
        */

        List<Day> dayList = Day.listAll(Day.class);
        /*
        float most_productive_day_sum = 0;
        int most_productive_day = -1;
        for (int i = 0; i < 7; i++) {
            int sum = 0;
            int count = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, i);
            for (Day day : dayList) {
                Calendar dayCalendar = Calendar.getInstance();
                dayCalendar.setTime(day.getDate());
                if (dayCalendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK)) {
                    most_productive_day_sum += day.getEnergyLevel();
                    count++;
                }
            }
            if (count != 0) {
                if (most_productive_day_sum > sum / count) {
                    most_productive_day_sum = sum / count;
                    most_productive_day = calendar.get(Calendar.DAY_OF_WEEK);
                }
            }
        }
        */

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
                day_average[i] = (float)sum/count;
            } else {
                day_average[i] = 0f;
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

        /*
        int most_unproductive_day_sum = 2147483647;
        int most_unproductive_day = -1;
        for (int i = 0; i < 7; i++) {
            int sum = 0;
            int count = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, i);
            for (Day day : dayList) {
                Calendar dayCalendar = Calendar.getInstance();
                dayCalendar.setTime(day.getDate());
                if (dayCalendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK)) {
                    most_unproductive_day_sum += day.getEnergyLevel();
                    count++;
                }
            }
            if (count != 0) {
                if (most_unproductive_day_sum < sum / count) {
                    most_unproductive_day_sum = sum / count;
                    most_unproductive_day = calendar.get(Calendar.DAY_OF_WEEK);
                }
            }
        }
        */

        /*
        if (most_productive_hour != -1) {
            mMostProductiveHourTextView.setText(
                    getString(R.string.most_productive_hour) + " " +
                            most_productive_hour + " " + getString(R.string.clock));
        } else {
            mMostProductiveHourTextView.setText(getString(R.string.collect_more_data_to_unlock_this));
        }
        if (second_most_productive_hour != -1) {
            mSecondMostProductiveHourTextView.setText(
                    getString(R.string.second_most_productive_hour)+ " " +
                            second_most_productive_hour + " " + getString(R.string.clock));
        } else {
            mSecondMostProductiveHourTextView.setText(getString(R.string.collect_more_data_to_unlock_this));
        }
        if (least_productive_hour != -1 && least_productive_hour != most_productive_hour) {
            mLeastProductiveHourTextView.setText(
                    getString(R.string.least_productive_hour) + " " +
                            least_productive_hour + " " + getString(R.string.clock));
        } else {
            mLeastProductiveHourTextView.setText(getString(R.string.collect_more_data_to_unlock_this));
        }

        String dayOfWeek = getDayOfWeek(most_productive_day);
        if (most_productive_day > 0) {
            mMostProductiveDayTextView.setText(
                    getString(R.string.most_productive_day) + " " + dayOfWeek

            );
        } else {
            mMostProductiveDayTextView.setText(R.string.collect_more_data_to_unlock_this);
        }

        String unproductiveDayOfWeek = getDayOfWeek(most_unproductive_day);
        if (most_unproductive_day > 0 && most_unproductive_day != most_productive_day) {
            mMostUnproductiveDayTextView.setText(
                    getString(R.string.most_unproductive_day) + " " + unproductiveDayOfWeek
            );
        } else {
            mMostUnproductiveDayTextView.setText(R.string.collect_more_data_to_unlock_this);
        }
        */
    }

    private String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = getString(R.string.sunday);
                break;
            case 2:
                day = getString(R.string.monday);
                break;
            case 3:
                day = getString(R.string.tuesday);
                break;
            case 4:
                day = getString(R.string.wednesday);
                break;
            case 5:
                day = getString(R.string.thursday);
                break;
            case 6:
                day = getString(R.string.friday);
                break;
            case 7:
                day = getString(R.string.saturday);
                break;
        }
        return day;
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
                //entryList.add(new Entry(i, (float)sum/count));
            }
        }
        return entryList;
    }
}
