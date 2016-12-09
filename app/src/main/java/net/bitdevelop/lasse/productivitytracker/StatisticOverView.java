package net.bitdevelop.lasse.productivitytracker;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class StatisticOverView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_overview);

        mRecyclerView = (RecyclerView) findViewById(R.id.statistic_overview_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        updateUI();
    }

    private void updateUI() {
        List<StatisticCardOverView> cardList = new ArrayList<>();
        cardList.add(
                new StatisticCardOverView(getString(R.string.hour_statistics),
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.chart_card_view_one),
                        StatisticHourOverview.class));
        cardList.add(
                new StatisticCardOverView(getString(R.string.day_statistics),
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.chart_card_view_two),
                        StatisticDayOverview.class));

        mCardAdapter = new CardAdapter(cardList);
        mRecyclerView.setAdapter(mCardAdapter);
    }

    private class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

        private List<StatisticCardOverView> mCardList;

        public CardAdapter(List<StatisticCardOverView> cardList) {
            mCardList = cardList;
        }

        @Override
        public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.statistic_overview_row, parent, false);

            return new CardHolder(view);
        }

        @Override
        public void onBindViewHolder(CardHolder holder, int position) {
            StatisticCardOverView cardOverView = mCardList.get(position);
            holder.bindCard(cardOverView);
        }

        @Override
        public int getItemCount() {
            return mCardList.size();
        }

        public class CardHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

            private TextView mNameCard;
            private ImageView mImageCard;

            private StatisticCardOverView mCard;

            public CardHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);

                mNameCard = (TextView) itemView.findViewById(R.id.text_view_statistic_overview);
                mImageCard = (ImageView) itemView.findViewById(R.id.image_statistic_overview);
            }



            public void bindCard(StatisticCardOverView card) {
                mCard = card;

                mNameCard.setText(mCard.getName());
                mImageCard.setImageDrawable(mCard.getDrawable());
            }


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mCard.getActivity());
                startActivity(intent);
            }
        }
    }
}
