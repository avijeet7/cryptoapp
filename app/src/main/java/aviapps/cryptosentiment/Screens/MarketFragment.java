package aviapps.cryptosentiment.Screens;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import aviapps.cryptosentiment.Custom.RVCryptoAdapter;
import aviapps.cryptosentiment.R;

/*
 * Created by Avijeet on 30-Dec-17.
 */

public class MarketFragment extends Fragment {

    private RecyclerView recyclerView;

    public MarketFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_fragment, container, false);
        recyclerView = view.findViewById(R.id.rv_crypto);
        init();
        return view;
    }

    private void init() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        List<String> input = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            input.add("Test" + i);
        }
        RecyclerView.Adapter mAdapter = new RVCryptoAdapter(input);
        recyclerView.setAdapter(mAdapter);
    }
}
