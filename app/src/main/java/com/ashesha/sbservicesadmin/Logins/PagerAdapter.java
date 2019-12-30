package com.ashesha.sbservicesadmin.Logins;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AdminLoginFragment adminLoginFragment = new AdminLoginFragment();
                return adminLoginFragment;

            case 1:
                EmployeeLoginFragment employeeLoginFragment = new EmployeeLoginFragment();
                return employeeLoginFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}