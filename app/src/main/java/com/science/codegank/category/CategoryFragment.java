package com.science.codegank.category;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.science.baserecyclerviewadapter.base.BaseCommonAdapter;
import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/10
 */

public class CategoryFragment extends BaseFragment {

    private static final String TAB_POSITION = "tab_position";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public static CategoryFragment newInstance(int tabPosition) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void doCreateView(View view) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MyAdapter adapter = new MyAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            list.add("item:" + i);
        }
        adapter.setData(false, list);
    }

    class MyAdapter extends BaseCommonAdapter<List<String>> {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public void convertCommon(ViewHolder viewHolder, List<String> list, int i) {
            viewHolder.setText(R.id.tv_gank_day_header, list.get(i));
        }

        @Override
        public int getItemLayoutId() {
            return R.layout.item_gank_day_header;
        }
    }

}
