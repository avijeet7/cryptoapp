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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aviapps.cryptosentiment.Common.StatMethod;
import aviapps.cryptosentiment.Custom.CoindeltaRecyclerViewAdapter;
import aviapps.cryptosentiment.GetSet.GetSetStream;
import aviapps.cryptosentiment.R;

public class MktTab2 extends Fragment {

    private RecyclerView recyclerView;
    private CoindeltaRecyclerViewAdapter mAdapter;
    private List<GetSetStream> input;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mkt_tab_2, container, false);
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

        mAdapter = new CoindeltaRecyclerViewAdapter(input, getContext());
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
        String data = StatMethod.getStrPref(getContext(), "ExchData", "coindelta");
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.optString("MarketName").contains("inr")) {
                    GetSetStream row = new GetSetStream();
                    row.setPair(jsonObject.optString("MarketName"));
                    row.setLtp(jsonObject.optDouble("Last"));
                    row.setBid(jsonObject.optDouble("Bid"));
                    row.setAsk(jsonObject.optDouble("Ask"));

                    input.add(row);
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
