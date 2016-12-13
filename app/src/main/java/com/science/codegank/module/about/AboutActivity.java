package com.science.codegank.module.about;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.science.baserecyclerviewadapter.base.ViewHolder;
import com.science.baserecyclerviewadapter.interfaces.OnItemClickListener;
import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.science.codegank.R.id.recyclerView;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/11/29
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        super.onCreate(savedInstanceState);
        setToolbar("");

        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange() - (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                ViewCompat.setAlpha(mToolbarTitle, percentage);
            }
        });

        mTvVersion.setText(getString(R.string.version, CommonUtil.getAppVersion(this)));

        initList();
    }

    private void initList() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AboutAdapter aboutAdapter = new AboutAdapter(this, mRecyclerView);
        aboutAdapter.setOnItemClickListener(new OnItemClickListener<AboutSection>() {
            @Override
            public void onItemClick(ViewHolder viewHolder, AboutSection data, int position) {
                if (!TextUtils.isEmpty(data.data.getUrl())) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.data.getUrl())));
                }
            }
        });
        mRecyclerView.setAdapter(aboutAdapter);

        List<AboutSection> list = new ArrayList<>();
        Resources resources = getResources();
        list.add(new AboutSection(new About(resources.getString(R.string.codegank_detail), "")));
        list.add(new AboutSection(true, false, new About(resources.getString(R.string.project_address), "")));
        list.add(new AboutSection(new About(resources.getString(R.string.xyscience_codegank), resources.getString(R.string.xyscience_codegank_address))));
        list.add(new AboutSection(true, false, new About(resources.getString(R.string.issues), "")));
        list.add(new AboutSection(new About(resources.getString(R.string.xyscience_codegank_issues), resources.getString(R.string.xyscience_codegank_issues_address))));
        list.add(new AboutSection(true, false, new About(resources.getString(R.string.gank_source), "")));
        list.add(new AboutSection(new About(resources.getString(R.string.gank_io), resources.getString(R.string.gank_io_address))));
        list.add(new AboutSection(true, false, new About(resources.getString(R.string.libraries_used), "")));
        list.add(new AboutSection(new About(resources.getString(R.string.reactivex_rxjava), resources.getString(R.string.reactivex_rxjava_address))));
        list.add(new AboutSection(new About(resources.getString(R.string.reactivex_rxandroid), resources.getString(R.string.reactivex_rxandroid_address))));
        list.add(new AboutSection(new About(resources.getString(R.string.square_retrofit), resources.getString(R.string.square_retrofit_address))));
        list.add(new AboutSection(new About(resources.getString(R.string.glide), resources.getString(R.string.glide_address))));
        list.add(new AboutSection(new About(resources.getString(R.string.picasso), resources.getString(R.string.picasso_address))));
        list.add(new AboutSection(new About(resources.getString(R.string.butterknife), resources.getString(R.string.butterknife_address))));
        list.add(new AboutSection(false, true, new About(resources.getString(R.string.open_source_awesome), "")));

        aboutAdapter.setData(false, list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
