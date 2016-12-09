package net.bitdevelop.lasse.productivitytracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.orm.SugarRecord;

public class NewEntryActivity extends AppCompatActivity {

    public static final String EXTRA_ENERGY_LEVEL =
            "net.bitdevelop.lasse.productivitytracker.NewEntryActivity.energyLevel";

    Button mNewEntryButton;
    EditText mEnergyLevelEditText;
    TextView mWarningNewEntryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        AdView mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mNewEntryButton = (Button) findViewById(R.id.add_new_entry);
        mEnergyLevelEditText = (EditText) findViewById(R.id.enery_level_edit_text);
        mWarningNewEntryTextView = (TextView) findViewById(R.id.warning_new_entry_text_view);

        mNewEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int energyLevel;
                try {
                    energyLevel = Integer.parseInt(mEnergyLevelEditText.getText().toString());

                    if (energyLevel >= 1 && energyLevel <= 10) {
                        Day day = new Day();
                        day.setHour(Day.getHourFromDate(day.getDate()));
                        day.setEnergyLevel(energyLevel);
                        SugarRecord.save(day);
                        setEnergyLevel(energyLevel);
                    } else {
                        mWarningNewEntryTextView.setText(R.string.warning_new_entry);
                        mEnergyLevelEditText.setText("");
                    }
                } catch (Exception e) {
                    mWarningNewEntryTextView.setText(R.string.warning_new_entry);
                    mEnergyLevelEditText.setText("");
                }
            }
        });
    }



    private void setEnergyLevel(int energyLevel) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ENERGY_LEVEL, energyLevel);
        setResult(RESULT_OK, data);
        finish();
    }
}
