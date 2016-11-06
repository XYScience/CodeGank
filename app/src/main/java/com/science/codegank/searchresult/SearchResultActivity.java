package com.science.codegank.searchresult;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.science.codegank.R;
import com.science.codegank.base.BaseActivity;
import com.science.codegank.homeday.MainActivity;
import com.science.materialsearch.MaterialSearchView;
import com.science.materialsearch.adapter.SearchAdapter;

import butterknife.BindView;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/9/17
 */
public class SearchResultActivity extends BaseActivity {

    @BindView(R.id.searchView)
    MaterialSearchView mSearchView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarBg));

        setSearchView();
    }

    private void setSearchView() {
        mSearchView = (MaterialSearchView) findViewById(R.id.searchView);
        // 设置搜索样式（默认不显示）：浮于Toolbar上
        mSearchView.setVersion(MaterialSearchView.VERSION_TOOLBAR);
        // 设置搜索输入框文字
        mSearchView.setTextInput(getIntent().getStringExtra("query"));
        // 设置软键盘搜索键监听
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchResultActivity.this, "搜索关键字:" + query, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // 设置搜索历史列表点击监听
        mSearchView.setAdapter(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String queryHistory) {
                Toast.makeText(SearchResultActivity.this, "搜索关键字:" + queryHistory, Toast.LENGTH_SHORT).show();
            }
        });
        // 设置搜索框左边返回箭头监听
        mSearchView.setOnMenuClickListener(new MaterialSearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }
}
