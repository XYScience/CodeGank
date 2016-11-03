package com.science.codegank.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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

import com.science.codegank.R;
import com.science.codegank.ui.fragment.HomeFragment;
import com.science.codegank.ui.fragment.WelfareFragment;
import com.science.materialsearch.MaterialSearchView;
import com.science.materialsearch.adapter.SearchAdapter;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.searchView)
    MaterialSearchView mSearchView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private HomeFragment mHomeFragment;
    private WelfareFragment mWelfareFragment;
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
        showFragment(R.id.nav_home);
        setSearchView();

        getData();
    }

    private void getData() {
        String url = "http://gank.io/api/";
    }

    private void initDrawerLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        setCollapsingToolbar(getString(R.string.home));

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
            setCollapsingToolbar(getString(R.string.home));
        } else if (id == R.id.nav_collections) {
            setCommonToolbar(getString(R.string.collections));
        } else if (id == R.id.nav_android) {
            setCommonToolbar(getString(R.string.android));
        } else if (id == R.id.nav_ios) {
            setCommonToolbar(getString(R.string.ios));
        } else if (id == R.id.nav_web) {
            setCommonToolbar(getString(R.string.web));
        } else if (id == R.id.nav_other_resource) {
            setCommonToolbar(getString(R.string.other_resource));
        } else if (id == R.id.nav_more) {
            setCommonToolbar(getString(R.string.more));
        } else if (id == R.id.nav_welfare) {
            setCommonToolbar(getString(R.string.welfare));
        } else if (id == R.id.nav_break_video) {
            setCommonToolbar(getString(R.string.break_video));
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        showFragment(id);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setCommonToolbar(String title) {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        appBarLayout.setFitsSystemWindows(false);
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        imageView.setVisibility(View.GONE);
        View tvTime = findViewById(R.id.tv_time);
        tvTime.setVisibility(View.GONE);
        TextView tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

    private void setCollapsingToolbar(String title) {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        appBarLayout.setFitsSystemWindows(true);
        View imageView = findViewById(R.id.backdrop);
        imageView.setVisibility(View.VISIBLE);
        final View tvTime = findViewById(R.id.tv_time);
        tvTime.setVisibility(View.VISIBLE);
        final TextView tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                ViewCompat.setAlpha(tvTime, 1 - percentage);
                ViewCompat.setAlpha(tvTitle, percentage);
            }
        });
    }

    private void showFragment(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(fm);
        switch (index) {
            case R.id.nav_home:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.content_main, mHomeFragment);
                } else {
                    ft.show(mHomeFragment);
                }
                break;
            case R.id.nav_collections:
                if (mWelfareFragment == null) {
                    mWelfareFragment = new WelfareFragment();
                    ft.add(R.id.content_main, mWelfareFragment);
                } else {
                    ft.show(mWelfareFragment);
                }
                break;
            case R.id.nav_android:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.content_main, mHomeFragment);
                } else {
                    ft.show(mHomeFragment);
                }
                break;
            case R.id.nav_ios:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.content_main, mHomeFragment);
                } else {
                    ft.show(mHomeFragment);
                }
                break;
            case R.id.nav_web:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.content_main, mHomeFragment);
                } else {
                    ft.show(mHomeFragment);
                }
                break;
            case R.id.nav_other_resource:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.content_main, mHomeFragment);
                } else {
                    ft.show(mHomeFragment);
                }
                break;
            case R.id.nav_more:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.content_main, mHomeFragment);
                } else {
                    ft.show(mHomeFragment);
                }
                break;
            case R.id.nav_welfare:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.content_main, mHomeFragment);
                } else {
                    ft.show(mHomeFragment);
                }
                break;
            case R.id.nav_break_video:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.content_main, mHomeFragment);
                } else {
                    ft.show(mHomeFragment);
                }
                break;
        }

        ft.commit();
    }

    private void hideFragment(FragmentManager fm) {
        List<Fragment> fragmentList = fm.getFragments();
        if (fragmentList != null && !fragmentList.isEmpty()) {
            for (Fragment fragment : fragmentList) {
                fm.beginTransaction().hide(fragment).commit();
            }
        }
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
