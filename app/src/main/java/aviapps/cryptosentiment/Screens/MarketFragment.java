package aviapps.cryptosentiment.Screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aviapps.cryptosentiment.Custom.CustomWebSocket;
import aviapps.cryptosentiment.Custom.RVCryptoAdapter;
import aviapps.cryptosentiment.R;

/*
 * Created by Avijeet on 30-Dec-17.
 */

public class MarketFragment extends Fragment {

    HashMap<String, Integer> channelMapper;
    private RecyclerView recyclerView;
    private RVCryptoAdapter mAdapter;
    private List<JSONObject> input;
    private CustomWebSocket ws;

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
        input = new ArrayList<>();
        channelMapper = new HashMap<>();

        JSONObject main_json = new JSONObject();
        try {
            main_json.put("chanId", "");
            main_json.put("symbol", "");
            main_json.put("ltp", "");
            main_json.put("pc", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        input.add(main_json);
        input.add(main_json);
        mAdapter = new RVCryptoAdapter(input);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        startSockets();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getContext().unregisterReceiver(receiver);
            ws.close();
        } catch (Exception ex) {
            Log.e("Pause", ex.getMessage());
        }
    }

    private void startSockets() {
        getActivity().registerReceiver(receiver, new IntentFilter("CustomWebSocket"));
        ws = new CustomWebSocket(getContext(), "wss://api.bitfinex.com/ws/2");
        ws.start();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("data");
            Log.d("ASD", msg);
            if (msg.equalsIgnoreCase("{\"event\":\"info\",\"version\":2}")) {
                ws.sendMessage("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"symbol\":\"tBTCUSD\"}");
                ws.sendMessage("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"symbol\":\"tETHUSD\"}");
            } else {
                if (msg.startsWith("{")) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg);
                        if (jsonObject.optString("event").equalsIgnoreCase("subscribed")) {
                            int id = 0;
                            switch (jsonObject.optString("symbol")) {
                                case "tBTCUSD":
                                    id = 0;
                                    break;
                                case "tETHUSD":
                                    id = 1;
                                    break;
                            }
                            input.get(id).put("symbol", jsonObject.optString("symbol"));
                            input.get(id).put("chanId", jsonObject.optString("chanId"));
                            channelMapper.put(jsonObject.optString("chanId"), id);
                        }
                    } catch (Exception ex) {
                        Log.e("OUTPUT", ex.getMessage());
                    }
                } else {
                    try {
                        String tick = msg.replaceAll("[\\[\\]]", "");
                        String[] data = tick.split(",");
                        if (!data[1].equalsIgnoreCase("\"hb\"")) {
                            final int position = channelMapper.get(data[0]);
                            input.get(position).put("ltp", data[7]);
                            input.get(position).put("pc", data[6]);

                            final JSONObject finalLastPrice = input.get(position);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.update(position, finalLastPrice);
                                }
                            });
                        }
                    } catch (Exception ex) {
                        Log.e("OUTPUT", ex.getMessage());
                    }
                }
            }
        }
    };
}
