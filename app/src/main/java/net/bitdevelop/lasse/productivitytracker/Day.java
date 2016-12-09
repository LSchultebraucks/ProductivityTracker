package net.bitdevelop.lasse.productivitytracker;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.mikephil.charting.data.Entry;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Each Day can save the Score (for the Productivity 1-10) for every time. Later It can be represented in a graph.
 */
public class Day extends SugarRecord implements Parcelable {

    private Date mDate;
    private int mEnergyLevel;
    private int mHour;

    public Day() {
        mDate = new Date();
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getEnergyLevel() {
        return mEnergyLevel;
    }

    public void setEnergyLevel(int energyLevel) {
        mEnergyLevel = energyLevel;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        mHour = hour;
    }

    public Entry getEntry() {
        return new Entry(getHourFromDate(mDate), mEnergyLevel);
    }

    public static int getHourFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static Day checkIfDayExists(Day day, List<Day> dayList) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        for (Day dayOfList : dayList) {
            if (sdf.format(day.mDate).equals(sdf.format(dayOfList.mDate))) {
                return dayOfList;
            }
        }
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mDate != null ? this.mDate.getTime() : -1);
        dest.writeInt(this.mEnergyLevel);
        dest.writeInt(this.mHour);
        dest.writeValue(this.getId());
    }

    protected Day(Parcel in) {
        long tmpMDate = in.readLong();
        this.mDate = tmpMDate == -1 ? null : new Date(tmpMDate);
        this.mEnergyLevel = in.readInt();
        this.mHour = in.readInt();
        this.setId((Long) in.readValue(Long.class.getClassLoader()));
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
