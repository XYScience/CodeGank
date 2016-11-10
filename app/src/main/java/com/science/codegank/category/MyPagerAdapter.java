package com.science.codegank.category;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.science.codegank.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/10
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> tabNames;

    public MyPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabNames = new ArrayList<>();
        tabNames.add(context.getString(R.string.android));
        tabNames.add(context.getString(R.string.ios));
        tabNames.add(context.getString(R.string.web));
        tabNames.add(context.getString(R.string.other_resource));
        tabNames.add(context.getString(R.string.app));
        tabNames.add(context.getString(R.string.more));
    }

    @Override
    public Fragment getItem(int position) {
        return CategoryFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return tabNames.size();
    }

    /**
     * 这个函数就是给TabLayout的Tab设定Title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames.get(position);
    }
}
