package com.science.codegank.homeday;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayEntity;
import com.science.codegank.http.HttpMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Func1;

/**
 * @author 幸运Science
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/10/31
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private View mRootView;
    private HomeContract.Presenter mHomePresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void doCreateView(View view) {
        mRootView = view;
        ButterKnife.bind(this, mRootView);

        HttpMethods.getInstance().getGankDay(2016, 10, 24)
                .map(new Func1<GankDayEntity, GankDayEntity.GankDayResults>() {
                    @Override
                    public GankDayEntity.GankDayResults call(GankDayEntity gankDayEntity) {
                        return gankDayEntity.results;
                    }
                })
                .map(new Func1<GankDayEntity.GankDayResults, List<Gank>>() {
                    @Override
                    public List<Gank> call(GankDayEntity.GankDayResults gankDayResults) {
                        return null;
                    }
                });
    }

    private List<Gank> getAllResults() {
        return null;
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        if (presenter != null) {
            mHomePresenter = presenter;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mHomePresenter.start();
    }


}
