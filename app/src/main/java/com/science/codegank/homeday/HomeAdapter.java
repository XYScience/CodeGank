package com.science.codegank.homeday;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.science.baserecyclerviewadapter.base.BaseStickyAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.util.ImageLoadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/6
 */

public class HomeAdapter extends BaseStickyAdapter<List<GankDayResults>> {

    private List<GankDayResults> list = new ArrayList<>();
    private Context mContext;

    public HomeAdapter(Context context) {
        super(context);
        mContext = context;
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
    public void convertCommon(ViewHolder viewHolder, List<GankDayResults> ganks, int section, int position) {
        if ("福利".equals(ganks.get(section).getHeader())) {
            viewHolder.getView(R.id.iv_day_welfare).setVisibility(View.VISIBLE);
            ImageLoadUtil.loadImage(mContext, ganks.get(section).getWelfareUrl(), (ImageView) viewHolder.getView(R.id.iv_day_welfare));
        } else {
            viewHolder.getView(R.id.iv_day_welfare).setVisibility(View.GONE);
            viewHolder.setText(R.id.tv_desc, ganks.get(section).getGankList().get(position).getDesc());
            viewHolder.setText(R.id.tv_who, ganks.get(section).getGankList().get(position).getWho());
        }
    }

    @Override
    public void convertHeader(ViewHolder viewHolder, List<GankDayResults> ganks, int section) {
        String header = ganks.get(section).getHeader();
        if ("福利".equals(header)) {
            viewHolder.getView(R.id.tv_gank_day_header).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.tv_gank_day_header).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_gank_day_header, ganks.get(section).getHeader());
        }
    }

    @Override
    public int getSectionCount() {
        return list.size();
    }

    @Override
    public int getCountOfSection(int section) {
        return list.get(section).getGankList().size();
    }

    @Override
    public void updateData(boolean isLoadMore, List<GankDayResults> ganks) {
        if (!isLoadMore) {
            list.clear();
        }
        list.addAll(ganks);
    }
}
