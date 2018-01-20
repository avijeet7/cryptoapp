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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import aviapps.cryptosentiment.Custom.KoinexRecyclerViewAdapter;
import aviapps.cryptosentiment.GetSet.GetSetStream;
import aviapps.cryptosentiment.R;

public class MktTab3 extends Fragment {

    private RecyclerView recyclerView;
    private KoinexRecyclerViewAdapter mAdapter;
    private List<GetSetStream> input;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RequestQueue queue;

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

        queue = Volley.newRequestQueue(getActivity());
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
        String url = "https://koinex.in/api/ticker";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ASD", "SSS");
            }
        });
        queue.add(stringRequest);
    }
}

