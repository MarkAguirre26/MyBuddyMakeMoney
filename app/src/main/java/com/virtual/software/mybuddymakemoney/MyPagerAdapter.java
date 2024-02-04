package com.virtual.software.mybuddymakemoney;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private String[] fragmentTitles = {"FragmentBrains", "FragmentMain", "FragmentSetting"};
    private Fragment[] fragments = { new FragmentBrains(), new FragmentMain(),new FragmentSetting()};

    private String selectedFragmentName;  // Add this variable

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles[position];
    }
    public String getSelectedFragmentName() {
        return selectedFragmentName;
    }

    public void setSelectedFragmentName(String name) {
        selectedFragmentName = name;
    }

}
