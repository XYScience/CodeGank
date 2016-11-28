package com.science.codegank.category.welfare;

import android.content.Context;

import com.science.baserecyclerviewadapter.base.BaseCommonAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.util.CommonDefine;
import com.science.codegank.util.CommonUtil;
import com.science.codegank.util.ImageLoadUtil;
import com.science.codegank.util.SharedPreferenceUtil;
import com.science.codegank.view.RatioImageView;

import java.util.List;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/11/12
 */

public class WelfareListAdapter extends BaseCommonAdapter<List<Gank>> {

    private Context mContext;
    private Boolean isNoPic;

    public WelfareListAdapter(Context context) {
        super(context);
        mContext = context;
        isNoPic = (Boolean) SharedPreferenceUtil.get(mContext, CommonDefine.SP_KEY_SMART_NO_PIC, false);
    }

    @Override
    public void convertCommon(ViewHolder viewHolder, List<Gank> ganks, int i) {
        Gank gank = ganks.get(i);
        viewHolder.setText(R.id.tv_date, CommonUtil.toDate(gank.getPublishedAt()));
        RatioImageView imgWelfare = viewHolder.getView(R.id.iv_welfare);
        if (isNoPic) {
            ImageLoadUtil.loadImage(mContext, R.drawable.welfare, 0, imgWelfare);
        } else {
            ImageLoadUtil.loadImage(mContext, gank.getUrl(), 0, imgWelfare);
        }
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_welfare_list;
    }
}
