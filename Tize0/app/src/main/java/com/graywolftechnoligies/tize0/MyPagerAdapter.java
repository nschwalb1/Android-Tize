package com.graywolftechnoligies.tize0;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by neil on 11/25/15.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    public MyPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int pos){
        switch(pos){
            case 1: return EventsList.newInstance();
            case 0: return CreateEvent.newInstance();
            case 2: return GuestList.newInstance();
            default:return EventsList.newInstance();
        }
    }

    @Override
    public int getCount(){
        return 3;
    }
}