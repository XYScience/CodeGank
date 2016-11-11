package com.science.codegank.category.welfare;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/3
 */

public class WelfareFragment extends BaseFragment {

    private View mRootView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_welfare;
    }

    @Override
    protected void doCreateView(View view) {
        mRootView = view;

        TextView textView = (TextView) mRootView.findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "test~~~~~", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
