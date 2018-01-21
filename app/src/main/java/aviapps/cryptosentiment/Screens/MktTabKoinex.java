package aviapps.cryptosentiment.Screens;

/*
 * Created by Avijeet on 20-Jan-18.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import aviapps.cryptosentiment.Common.StatMethod;
import aviapps.cryptosentiment.Custom.KoinexRecyclerViewAdapter;
import aviapps.cryptosentiment.GetSet.GetSetStream;
import aviapps.cryptosentiment.R;

public class MktTabKoinex extends Fragment {

    private RecyclerView recyclerView;
    private KoinexRecyclerViewAdapter mAdapter;
    private List<GetSetStream> input;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mkt_tab_3, container, false);
        recyclerView = view.findViewById(R.id.rv_crypto);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        init();
        return view;
    }

    private void init() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        input = new ArrayList<>();

        mAdapter = new KoinexRecyclerViewAdapter(input, getContext());
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataVolleyCall();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataVolleyCall();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            input.clear();
        } catch (Exception ex) {
            Log.e("Pause", ex.getMessage());
        }
    }

    private void getDataVolleyCall() {
        if (input != null)
            input.clear();
        String data = StatMethod.getStrPref(getContext(), "ExchData", "koinex");
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject newJsonObject = jsonObject.getJSONObject("stats");
            Iterator<?> keys = newJsonObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (newJsonObject.get(key) instanceof JSONObject) {
                    GetSetStream row = new GetSetStream();
                    row.setPair(key);
                    row.setLtp(newJsonObject.getJSONObject(key).optDouble("last_traded_price"));
                    row.setBid(newJsonObject.getJSONObject(key).optDouble("highest_bid"));
                    row.setAsk(newJsonObject.getJSONObject(key).optDouble("lowest_ask"));
                    input.add(row);
                }
            }
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

