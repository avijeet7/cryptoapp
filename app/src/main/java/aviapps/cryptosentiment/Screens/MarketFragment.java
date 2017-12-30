package aviapps.cryptosentiment.Screens;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import aviapps.cryptosentiment.Custom.RVCryptoAdapter;
import aviapps.cryptosentiment.R;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/*
 * Created by Avijeet on 30-Dec-17.
 */

public class MarketFragment extends Fragment {

    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private RecyclerView recyclerView;
    private RVCryptoAdapter mAdapter;
    private List<JSONObject> input;
    private WebSocket ws;
    private EchoWebSocketListener listener;
    private long tick_count = 0;

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

        startSockets();
    }

    private void startSockets() {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url("wss://api.bitfinex.com/ws/2").build();
        listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        ws.close(NORMAL_CLOSURE_STATUS, "Bye");
    }

    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            webSocket.send("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"symbol\":\"tBTCUSD\"}");
            webSocket.send("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"symbol\":\"tETHUSD\"}");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d("TICK: ", text);
            tick_count++;
            output(text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
            tick_count++;
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
            Log.d("TICK: ", reason);
            tick_count = 0;
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            output("Error : " + t.getMessage());
            tick_count = 0;
        }
    }

    private void output(final String txt) {
        if (tick_count <= 2) {
            try {
                JSONObject jsonObject = new JSONObject(txt);
                if (jsonObject.optString("event").equalsIgnoreCase("subscribed")) {
                    input.get(0).putOpt("symbol", jsonObject.optString("symbol"));
                    input.get(0).putOpt("chanId", jsonObject.optString("chanId"));
                }
            } catch (Exception ex) {
                Log.e("OUTPUT", ex.getMessage());
            }
        } else {
            try {
                String tick = txt.replaceAll("[\\[\\]]", "");
                String[] data = tick.split(",");
                if (!data[1].equalsIgnoreCase("\"hb\"")) {
                    input.get(0).putOpt("ltp", data[7]);
                    input.get(0).putOpt("pc", data[6]);

                    final JSONObject finalLastPrice = input.get(0);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.update(0, finalLastPrice);
                        }
                    });
                }
            } catch (Exception ex) {
                Log.e("OUTPUT", ex.getMessage());
            }
        }
    }
}
