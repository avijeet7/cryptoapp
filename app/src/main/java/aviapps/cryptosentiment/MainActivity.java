package aviapps.cryptosentiment;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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
import java.util.List;

import aviapps.cryptosentiment.GetSet.Crypto;
import aviapps.cryptosentiment.common.CryptoArrayAdapter;
import aviapps.cryptosentiment.custom.CustomPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TextView tv_m1;
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
//        tv_m1 = (TextView) findViewById(R.id.tv_m1);

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    private void initVariables() {
        dataArray = new ArrayList<>();
    }

    private void setFlow() {
        adapter = new CryptoArrayAdapter(this, dataArray);
        ListView listView = (ListView) findViewById(R.id.elements_list);
        listView.setAdapter(adapter);
        getDataVolleyCall();
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
                tv_m1.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
