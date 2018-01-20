package aviapps.cryptosentiment.Screens;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import aviapps.cryptosentiment.Custom.BitfinexRecyclerViewAdapter;
import aviapps.cryptosentiment.Custom.CustomWebSocket;
import aviapps.cryptosentiment.Custom.MarketPagerAdapter;
import aviapps.cryptosentiment.GetSet.GetSetStream;
import aviapps.cryptosentiment.R;

/*
 * Created by Avijeet on 30-Dec-17.
 */

public class MarketFragment extends Fragment {

    private HashMap<Integer, Integer> channelMapper;
    private RecyclerView recyclerView;
    private BitfinexRecyclerViewAdapter mAdapter;
    private List<GetSetStream> input;
    private CustomWebSocket ws;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public MarketFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.mkt_pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        MarketPagerAdapter pagerAdapter = new MarketPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(new MktTab1(), "Bitfinex");
        pagerAdapter.addFragment(new MktTab2(), "Coindelta");
        pagerAdapter.addFragment(new MktTab3(), "Koinex");
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
