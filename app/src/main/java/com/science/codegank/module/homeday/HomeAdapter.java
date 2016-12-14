package com.science.codegank.module.homeday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.science.baserecyclerviewadapter.base.BaseStickyAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayResults;
import com.science.codegank.util.CommonDefine;
import com.science.codegank.util.CommonUtil;
import com.science.codegank.util.ImageLoadUtil;
import com.science.codegank.util.SharedPreferenceUtil;
import com.science.codegank.widget.RatioImageView;

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
    private Boolean isNoPic;

    public HomeAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
        mContext = context;
        setNoPic();
    }

    public void setNoPic() {
        isNoPic = (Boolean) SharedPreferenceUtil.get(mContext, CommonDefine.SP_KEY_SMART_NO_PIC, false);
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
        if (mContext.getResources().getString(R.string.welfare).equals(ganks.get(section).getHeader())) {
            viewHolder.getView(R.id.rl_welfare).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.ll_item).setVisibility(View.GONE);
            viewHolder.setText(R.id.tv_welfare_desc, CommonUtil.toDate(gankList.get(0).getPublishedAt()));
            ImageLoadUtil.loadImage(mContext, isNoPic ? R.drawable.welfare : gankList.get(0).getUrl(), 0, (RatioImageView) viewHolder.getView(R.id.iv_day_welfare));
            viewHolder.getView(R.id.ll_item_root).setElevation(0);
        } else {
            viewHolder.getView(R.id.rl_welfare).setVisibility(View.GONE);
            viewHolder.getView(R.id.ll_item).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_desc, gankList.get(position).getDesc());
            viewHolder.setText(R.id.tv_who, (gankList.get(position).getWho() == null) ? "无" : gankList.get(position).getWho());
            viewHolder.getView(R.id.ll_item_root).setElevation(mContext.getString(R.string.rest_video).equals(ganks.get(section).getHeader()) ? 4 : 0);
        }
    }

    @Override
    public void convertHeader(ViewHolder viewHolder, List<GankDayResults> ganks, int section) {
        GankDayResults gankDayResults = ganks.get(section);
        if (mContext.getResources().getString(R.string.welfare).equals(gankDayResults.getHeader())) {
            viewHolder.getView(R.id.iv_category).setVisibility(View.GONE);
            viewHolder.getView(R.id.tv_gank_day_header).setVisibility(View.GONE);
        } else {
            viewHolder.getView(R.id.iv_category).setVisibility(View.VISIBLE);
            viewHolder.getView(R.id.tv_gank_day_header).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_gank_day_header, gankDayResults.getHeader(), R.color.textPrimary);
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
