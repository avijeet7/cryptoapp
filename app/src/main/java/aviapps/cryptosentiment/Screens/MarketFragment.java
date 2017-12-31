package aviapps.cryptosentiment.Screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.util.HashMap;
import java.util.List;

import aviapps.cryptosentiment.Custom.CustomWebSocket;
import aviapps.cryptosentiment.Custom.RVCryptoAdapter;
import aviapps.cryptosentiment.GetSet.GetSetStream;
import aviapps.cryptosentiment.R;

/*
 * Created by Avijeet on 30-Dec-17.
 */

public class MarketFragment extends Fragment {

    private HashMap<Integer, Integer> channelMapper;
    private RecyclerView recyclerView;
    private RVCryptoAdapter mAdapter;
    private List<GetSetStream> input;
    private CustomWebSocket ws;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public MarketFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_fragment, container, false);
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
        channelMapper = new HashMap<>();

        mAdapter = new RVCryptoAdapter(input, getContext());
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshItems();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getContext().unregisterReceiver(receiver);
            ws.close();
            input.clear();
        } catch (Exception ex) {
            Log.e("Pause", ex.getMessage());
        }
    }

    void refreshItems() {
        input.clear();
        startSockets();
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void startSockets() {
        getActivity().registerReceiver(receiver, new IntentFilter("CustomWebSocket"));
        ws = new CustomWebSocket(getContext(), "wss://api.bitfinex.com/ws/");
        ws.start();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("data");
            Log.d("ASD", msg);
            if (msg.equalsIgnoreCase("{\"event\":\"info\",\"version\":1.1}")) {
                ws.sendMessage("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"pair\":\"BTCUSD\"}");
                ws.sendMessage("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"pair\":\"ETHUSD\"}");
                ws.sendMessage("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"pair\":\"LTCUSD\"}");
                ws.sendMessage("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"pair\":\"XRPUSD\"}");
                ws.sendMessage("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"pair\":\"XMRUSD\"}");
                ws.sendMessage("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"pair\":\"IOTAUSD\"}");
                ws.sendMessage("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"pair\":\"QTMUSD\"}");
            } else {
                if (msg.startsWith("{")) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg);
                        if (jsonObject.optString("event").equalsIgnoreCase("subscribed")) {
                            GetSetStream row = new GetSetStream();
                            row.setChanId(jsonObject.optInt("chanId"));
                            row.setPair(jsonObject.optString("pair"));
                            row.setLtp(-1);
                            row.setPc(-1);

                            input.add(row);
                            mAdapter.notifyDataSetChanged();

                            for (int i = 0; i < input.size(); i++)
                                if (input.get(i).getPair().equalsIgnoreCase(jsonObject.optString("pair")))
                                    channelMapper.put(jsonObject.optInt("chanId"), i);

                        }
                    } catch (Exception ex) {
                        Log.e("OUTPUT", ex.getMessage());
                    }
                } else if (msg.startsWith("[")) try {
                    JSONArray jarray = new JSONArray(msg);
                    if (!jarray.getString(1).equalsIgnoreCase("hb")) {
                        final int position = channelMapper.get(jarray.getInt(0));
                        GetSetStream lastUpdate = input.get(position);
                        lastUpdate.setLtp(jarray.getDouble(7));
                        lastUpdate.setPc(jarray.getDouble(6) * 100);
                        final GetSetStream finalLastUpdate = lastUpdate;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.update(position, finalLastUpdate);
                            }
                        });
                    }
                } catch (Exception ex) {
                    Log.e("OUTPUT", ex.getMessage());
                }
            }
        }
    };
}
