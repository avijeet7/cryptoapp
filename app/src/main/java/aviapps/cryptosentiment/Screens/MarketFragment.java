package aviapps.cryptosentiment.Screens;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
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

    private RecyclerView recyclerView;
    private RVCryptoAdapter mAdapter;
    private List<String> input;
    private WebSocket ws;

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
        for (int i = 0; i < 5; i++) {
            input.add("Test" + i);
        }
        mAdapter = new RVCryptoAdapter(input);
        recyclerView.setAdapter(mAdapter);

        startSockets();
    }

    private void startSockets() {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url("wss://api.bitfinex.com/ws/2").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
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
        ws.close(1000, "Bye");
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            webSocket.send("{\"event\":\"subscribe\",\"channel\":\"ticker\",\"symbol\":\"tBTCUSD\"}");
//            webSocket.send("{\"event\":\"subscribe\",\"channel\":\"book\",\"pair\":\"BTCUSD\",\"prec\":\"P0\"}");
//            webSocket.send("What's up ?");
//            webSocket.send(ByteString.decodeHex("deadbeef"));
//            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d("TICK: ", text);
            output(text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            output("Error : " + t.getMessage());
        }
    }

    private void output(final String txt) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.update(1, txt);
            }
        });
    }
}
