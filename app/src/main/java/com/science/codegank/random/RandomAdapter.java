package com.science.codegank.random;

import android.content.Context;

import com.science.baserecyclerviewadapter.base.BaseCommonAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.util.CommonUtil;

import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/11
 */

public class RandomAdapter extends BaseCommonAdapter<List<Gank>> {

    private Context mContext;
    private String mCategory;

    public RandomAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    @Override
    public void convertCommon(ViewHolder viewHolder, List<Gank> ganks, int i) {
        Gank gank = ganks.get(i);
        viewHolder.setText(R.id.tv_desc, gank.getDesc());
        if (mContext.getString(R.string.all).equals(mCategory)) {
            viewHolder.setText(R.id.tv_category, gank.getType());
        }
        viewHolder.setText(R.id.tv_who, gank.getWho());
        viewHolder.setText(R.id.tv_date, CommonUtil.toDate(gank.getPublishedAt()));
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_gank_random;
    }
}
