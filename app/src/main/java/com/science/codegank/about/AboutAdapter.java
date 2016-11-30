package com.science.codegank.about;

import android.content.Context;
import android.view.View;

import com.science.baserecyclerviewadapter.base.BaseSectionAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/30
 */

public class AboutAdapter extends BaseSectionAdapter<AboutSection> {

    public AboutAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_about_common;
    }

    @Override
    public int getItemHeaderLayoutId() {
        return R.layout.item_about_header;
    }

    @Override
    public int getItemFooterLayoutId() {
        return R.layout.item_about_common;
    }

    @Override
    public void convert(ViewHolder viewHolder, AboutSection data) {
        About about = data.data;
        viewHolder.setText(R.id.tv_item_common, about.getCommon());
    }

    @Override
    public void convertHeader(ViewHolder viewHolder, AboutSection data) {
        About about = data.data;
        viewHolder.setText(R.id.tv_item_header, about.getCommon());
    }

    @Override
    public void convertFooter(ViewHolder viewHolder, AboutSection data) {
        About about = data.data;
        viewHolder.getView(R.id.tv_item_footer).setVisibility(View.VISIBLE);
        viewHolder.getView(R.id.tv_item_common).setVisibility(View.GONE);
        viewHolder.setText(R.id.tv_item_footer, about.getCommon());
    }
}
