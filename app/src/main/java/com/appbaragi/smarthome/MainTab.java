package com.appbaragi.smarthome;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by kim on 2017-02-04.
 */

public class MainTab extends AppCompatActivity {
    int temperature=11;
    String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }


    private void setupViewPager(ViewPager viewPager) {

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.appendFragment(new SpeechFragment(), "음성인식");
        pagerAdapter.appendFragment(new ButtonFragment(this), "버튼방식");
        pagerAdapter.appendFragment(new ChartFragment(), "평균온도");
        viewPager.setAdapter(pagerAdapter);
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mainFragment = new ArrayList<>();

        private final ArrayList<String> tabTitles = new ArrayList<>();


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment, String title) {
            mainFragment.add(fragment);
            tabTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mainFragment.get(position);
        }

        @Override
        public int getCount() {
            return mainFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);

        }
    }


}
