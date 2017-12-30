package aviapps.cryptosentiment;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import aviapps.cryptosentiment.GetSet.Crypto;
import aviapps.cryptosentiment.common.CryptoArrayAdapter;
import aviapps.cryptosentiment.custom.CustomPagerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private TextView tv_ticks;
    ArrayList<Crypto> dataArray;
    CryptoArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        loadComponents();
        initVariables();
        setFlow();
    }

    private void loadComponents() {
        tv_ticks = findViewById(R.id.tv_ticks);

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this);

        ViewPager mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    private void initVariables() {
        dataArray = new ArrayList<>();
    }

    private void setFlow() {
        adapter = new CryptoArrayAdapter(this, dataArray);
        ListView listView = findViewById(R.id.elements_list);
        listView.setAdapter(adapter);
        getDataVolleyCall();
        start();
    }

    private void getDataVolleyCall() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.reddit.com/r/ethtrader/hot.json?sort=new";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("children");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Crypto element = new Crypto();
                                element.setTitle(jsonArray.getJSONObject(i).getJSONObject("data").getString("title"));
                                element.setDescription(jsonArray.getJSONObject(i).getJSONObject("data").getString("url"));
                                dataArray.add(element);
                            }
                            adapter.notifyDataSetChanged();
//                            tv_m1.setText(jsonObject.getJSONObject("data").getJSONArray("children").getJSONObject(1).getJSONObject("data").getString("title"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv_ticks.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void start() {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url("wss://api.bitfinex.com/ws/2").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_ticks.setText(txt);
            }
        });
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
            output("Receiving : " + text);
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
}
