package aviapps.cryptosentiment.Screens;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aviapps.cryptosentiment.Custom.MarketPagerAdapter;
import aviapps.cryptosentiment.R;

/*
 * Created by Avijeet on 30-Dec-17.
 */

public class MarketFragment extends Fragment {

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
        ViewPager viewPager = view.findViewById(R.id.mkt_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        MarketPagerAdapter pagerAdapter = new MarketPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(new MktTabCoindelta(), "Coindelta");
        pagerAdapter.addFragment(new MktTabKoinex(), "Koinex");
        pagerAdapter.addFragment(new MktTabBitfinex(), "Bitfinex");
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
