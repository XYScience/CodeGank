package com.science.codegank.homeday;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.science.codegank.R;
import com.science.codegank.base.BaseFragment;
import com.science.codegank.data.bean.Gank;
import com.science.codegank.data.bean.GankDayEntity;
import com.science.codegank.http.HttpMethods;
import com.science.codegank.util.MyLogger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
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
                .map(new Func1<GankDayEntity.GankDayResults, List<Gank>>() {
                    @Override
                    public List<Gank> call(GankDayEntity.GankDayResults gankDayResults) {
                        return getAllResults(gankDayResults);
                    }
                })
                .subscribe(new Subscriber<List<Gank>>() {
                    @Override
                    public void onCompleted() {
                        MyLogger.e("onCompleted:");
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLogger.e("onError:" + e.toString());
                    }

                    @Override
                    public void onNext(List<Gank> ganks) {
                        MyLogger.e(ganks.toString());
                    }
                });
    }

    private List<Gank> getAllResults(GankDayEntity.GankDayResults gankDayResults) {
        List<Gank> gankList = new ArrayList<>();
        if (gankDayResults.Android != null) {
            gankList.addAll(gankDayResults.Android);
        }
        if (gankDayResults.iOS != null) {
            gankList.addAll(gankDayResults.iOS);
        }
        if (gankDayResults.前端 != null) {
            gankList.addAll(gankDayResults.前端);
        }
        if (gankDayResults.拓展资源 != null) {
            gankList.addAll(gankDayResults.拓展资源);
        }
        if (gankDayResults.瞎推荐 != null) {
            gankList.addAll(gankDayResults.瞎推荐);
        }
        if (gankDayResults.App != null) {
            gankList.addAll(gankDayResults.App);
        }
        if (gankDayResults.休息视频 != null) {
            gankList.addAll(gankDayResults.休息视频);
        }
        if (gankDayResults.福利 != null) {
            gankList.addAll(0, gankDayResults.福利);
        }
        return gankList;
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
