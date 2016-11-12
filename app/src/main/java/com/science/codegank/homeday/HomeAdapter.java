package com.science.codegank.homeday;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.science.baserecyclerviewadapter.base.BaseStickyAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.util.CommonUtil;
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
        List<Gank> gankList = ganks.get(section).getGankList();
        if ("福利".equals(ganks.get(section).getHeader())) {
            viewHolder.getView(R.id.iv_day_welfare).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.tv_welfare_desc).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.tv_desc).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_who).setVisibility(View.GONE);
            viewHolder.getView(R.id.view_bottom).setVisibility(View.GONE);
            viewHolder.getView(R.id.view_bg).setVisibility(View.GONE);
            viewHolder.getView(R.id.rl_item).setElevation(0);
            viewHolder.setText(R.id.tv_welfare_desc, CommonUtil.toDate(gankList.get(0).getPublishedAt()));
            ImageLoadUtil.loadImage(mContext, gankList.get(0).getUrl(), 0, (ImageView) viewHolder.getView(R.id.iv_day_welfare));
        } else {
            viewHolder.getView(R.id.iv_day_welfare).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_welfare_desc).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_desc).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.tv_who).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_desc, gankList.get(position).getDesc());
            viewHolder.setText(R.id.tv_who, gankList.get(position).getWho());
            if ("休息视频".equals(ganks.get(section).getHeader())) {
                viewHolder.getView(R.id.view_bottom).setVisibility(View.VISIBLE);
                viewHolder.getView(R.id.view_bg).setVisibility(View.VISIBLE);
                viewHolder.getView(R.id.rl_item).setElevation(4);
            } else {
                viewHolder.getView(R.id.view_bottom).setVisibility(View.GONE);
                viewHolder.getView(R.id.view_bg).setVisibility(View.GONE);
                viewHolder.getView(R.id.rl_item).setElevation(0);
            }
        }
    }

    @Override
    public void convertHeader(ViewHolder viewHolder, List<GankDayResults> ganks, int section) {
        GankDayResults gankDayResults = ganks.get(section);
        if ("福利".equals(gankDayResults.getHeader())) {
            viewHolder.getView(R.id.iv_category).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_gank_day_header).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.tv_gank_day_header).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_gank_day_header, gankDayResults.getHeader(), R.color.textPrimary);
            viewHolder.getView(R.id.iv_category).setVisibility(View.VISIBLE);
            viewHolder.setImageResource(R.id.iv_category, gankDayResults.getHeaderImg());
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
