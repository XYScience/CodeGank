package com.science.codegank;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.science.codegank.base.BaseActivity;
import com.science.codegank.category.CategoryActivity;
import com.science.codegank.category.restvideo.RestVideoActivity;
import com.science.codegank.category.welfare.WelfareListActivity;
import com.science.codegank.homeday.HomeFragment;
import com.science.codegank.homeday.HomePresenter;
import com.science.codegank.random.RandomActivity;
import com.science.codegank.searchresult.SearchResultActivity;
import com.science.codegank.setting.SettingActivity;
import com.science.materialsearch.MaterialSearchView;
import com.science.materialsearch.adapter.SearchAdapter;

import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SEARCH = 1;
    private static final int CATEGORY = 2;
    public static final String SEARCH_QUERY = "query";
    @BindView(R.id.searchView)
    MaterialSearchView mSearchView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private String strQuery;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        initDrawerLayout();
        showHomeFragment();
        setSearchView();
        new HomePresenter(mHomeFragment);
    }

    private void initDrawerLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (mSearchView != null && mSearchView.isSearchOpen()) {
                    mSearchView.close();
                }
            }
        };
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView menuView = (NavigationMenuView) mNavigationView.getChildAt(0);
        if (menuView != null) {
            menuView.setVerticalScrollBarEnabled(false);
        }
    }

    private void setSearchView() {
        // 设置软键盘搜索键监听
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            // 点击软件盘搜索键
            @Override
            public boolean onQueryTextSubmit(String query) {
                strQuery = query;
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                intent.putExtra(SEARCH_QUERY, query);
                startActivityForResult(intent, SEARCH);
                return true;
            }
        });
        // 设置搜索历史列表点击监听
        mSearchView.setAdapter(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String queryHistory) {
                strQuery = queryHistory;
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                intent.putExtra(SEARCH_QUERY, queryHistory);
                startActivityForResult(intent, SEARCH);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH) {
            // 在搜索结果界面清除历史记录后，需要调用onTextChanged，以更新历史界面
            mSearchView.onTextChanged(strQuery);
        } else if (requestCode == CATEGORY) {
            mNavigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            showHomeFragment();
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_category) {
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivityForResult(intent, CATEGORY);
        } else if (id == R.id.nav_random) {
            Intent intent = new Intent(this, RandomActivity.class);
            startActivityForResult(intent, CATEGORY);
        } else if (id == R.id.nav_collections) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent, CATEGORY);
        } else if (id == R.id.nav_welfare) {
            Intent intent = new Intent(this, WelfareListActivity.class);
            startActivityForResult(intent, CATEGORY);
        } else if (id == R.id.nav_rest_video) {
            Intent intent = new Intent(this, RestVideoActivity.class);
            startActivityForResult(intent, CATEGORY);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void setCommonToolbar(String title) {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        appBarLayout.setFitsSystemWindows(false);
        ImageView imageView = (ImageView) findViewById(R.id.iv_welfare_today);
        imageView.setVisibility(View.GONE);
        View tvTime = findViewById(R.id.tv_time_today);
        tvTime.setVisibility(View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

    private void showHomeFragment() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        final View tvTime = findViewById(R.id.tv_time_today);
        final TextView tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitle.setText(getString(R.string.home));
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange() - (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                ViewCompat.setAlpha(tvTime, 1 - percentage);
                ViewCompat.setAlpha(tvTitle, percentage);
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            ft.add(R.id.content_main, mHomeFragment);
        } else {
            ft.show(mHomeFragment);
        }
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                mSearchView.open(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 3000) {
                Toast.makeText(this, getString(R.string.quit_again), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

}
