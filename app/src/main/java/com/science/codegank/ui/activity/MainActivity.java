package com.science.codegank.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.science.codegank.R;
import com.science.materialsearch.MaterialSearchView;
import com.science.materialsearch.adapter.SearchAdapter;

import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.searchView)
    MaterialSearchView mSearchView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
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
        setSearchView();

        getData();
    }

    private void getData() {
        String url = "http://gank.io/api/";
    }

    private void initDrawerLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView menuView = (NavigationMenuView) navigationView.getChildAt(0);
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
                intent.putExtra("query", query);
                startActivityForResult(intent, 1);
                return true;
            }
        });
        // 设置搜索历史列表点击监听
        mSearchView.setAdapter(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String queryHistory) {
                strQuery = queryHistory;
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                intent.putExtra("query", queryHistory);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 在搜索结果界面清除历史记录后，需要调用onTextChanged，以更新历史界面
        mSearchView.onTextChanged(strQuery);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_collections) {

        } else if (id == R.id.nav_android) {

        } else if (id == R.id.nav_ios) {

        } else if (id == R.id.nav_web) {

        } else if (id == R.id.nav_other_resource) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
