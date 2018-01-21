package aviapps.cryptosentiment.Screens;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import aviapps.cryptosentiment.Common.StatMethod;
import aviapps.cryptosentiment.R;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // load the store fragment by default
//        toolbar = getSupportActionBar();
//        toolbar.setTitle("Overview");
        loadStatData();
        loadFragment(new OverviewFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_overview:
//                    toolbar.setTitle("Overview");
                    fragment = new OverviewFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_market:
//                    toolbar.setTitle("Market");
                    fragment = new MarketFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_sentiment:
//                    toolbar.setTitle("Sentiment");
                    fragment = new SentimentFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
//                    toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadStatData() {
        queue = Volley.newRequestQueue(this);
        loadBitfinexData();
        loadCoindeltaData();
        loadKoinexData();
    }

    private void loadBitfinexData() {
        String url = "https://api.bitfinex.com/v1/symbols";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StatMethod.savePrefs(getApplicationContext(), "ExchData", "bitfinex", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ASD", "SSS");
            }
        });
        queue.add(stringRequest);
    }

    private void loadCoindeltaData() {
        String url = "https://coindelta.com/api/v1/public/getticker/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StatMethod.savePrefs(getApplicationContext(), "ExchData", "coindelta", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ASD", "SSS");
            }
        });
        queue.add(stringRequest);
    }

    private void loadKoinexData() {
        String url = "https://koinex.in/api/ticker";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        StatMethod.savePrefs(getApplicationContext(), "ExchData", "koinex", response);
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