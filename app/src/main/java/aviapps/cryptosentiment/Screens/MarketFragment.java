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
import aviapps.cryptosentiment.GetSet.GetSetStream;
import aviapps.cryptosentiment.R;

/*
 * Created by Avijeet on 30-Dec-17.
 */

public class MarketFragment extends Fragment {

    HashMap<String, Integer> channelMapper;
    private RecyclerView recyclerView;
    private RVCryptoAdapter mAdapter;
    private List<GetSetStream> input;
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
                            GetSetStream row = new GetSetStream();
                            row.setChanId(jsonObject.optInt("chanId", -1));
                            row.setSymbol(jsonObject.optString("symbol", ""));
                            row.setLtp(-1);
                            row.setPc(-1);

                            input.add(row);
                            mAdapter.notifyDataSetChanged();

                            int position = -1;
                            for (int i=0; i<input.size(); i++) {
                                if (input.get(i).getSymbol().equalsIgnoreCase(jsonObject.optString("symbol")))
                                    position = i;
                            }
                            channelMapper.put(jsonObject.optString("chanId"), position);
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
                            GetSetStream lastUpdate = input.get(position);
                            lastUpdate.setLtp(Double.valueOf(data[7]));
                            lastUpdate.setPc(Double.valueOf(data[6]) * 100);
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
        }
    };
}
