package com.science.codegank.homeday;

import android.content.Context;

import com.science.baserecyclerviewadapter.base.BaseStickyAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;
import com.science.codegank.data.bean.Gank;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/6
 */

public class HomeAdapter extends BaseStickyAdapter<List<List<Gank>>> {

    private List<List<Gank>> list = new ArrayList<>();

    public HomeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_gank_day;
    }

    @Override
    public int getItemHeaderLayoutId() {
        return R.layout.item_gank_day_header;
    }

    @Override
    public void convertCommon(ViewHolder viewHolder, List<List<Gank>> ganks, int section, int position) {
        viewHolder.setText(R.id.tv_desc, ganks.get(section).get(position).getDesc());
        viewHolder.setText(R.id.tv_who, ganks.get(section).get(position).getWho());
    }

    @Override
    public void convertHeader(ViewHolder viewHolder, List<List<Gank>> ganks, int section) {
        viewHolder.setText(R.id.tv_gank_day_header, "Android");
    }

    @Override
    public int getSectionCount() {
        return list.size();
    }

    @Override
    public int getCountOfSection(int i) {
        return list.get(i).size();
    }

    @Override
    public void updateData(boolean isLoadMore, List<List<Gank>> ganks) {
        if (isLoadMore) {
            list.clear();
        }
        list.addAll(ganks);
    }
}
