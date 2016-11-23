package com.science.codegank.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.science.codegank.R;
import com.science.codegank.util.CommonDefine;

import butterknife.ButterKnife;

/**
 * @author SScience
 * @description
 * @email chentushen.science@gmail.com
 * @data 2016/9/16
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getContentLayout();

    private String toolbarTitle;
    private PermissionCallback mPermissionCallback;

    public interface PermissionCallback {
        void hasPermission();

        void noPermission();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());
        ButterKnife.bind(this);
    }

    public Toolbar setToolbar(String title) {
        toolbarTitle = title;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!"".equals(toolbarTitle)) {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hideSoftInput(view);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 权限请求入口
     *
     * @param permissionTip
     * @param permissionCallback
     * @param permissions
     */
    public void performCodeWithPermission(String permissionTip, PermissionCallback permissionCallback, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        } else {
            mPermissionCallback = permissionCallback;
            if (checkPermissionGranted(permissions)) {
                // 有权限
                if (mPermissionCallback != null) {
                    mPermissionCallback.hasPermission();
                    mPermissionCallback = null;
                }
            } else {
                // 没有权限
                requestPermission(permissionTip, permissions);
            }
        }
    }

    private void requestPermission(final String permissionTip, final String[] permissions) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            //如果用户之前拒绝过此权限，再提示一次准备授权相关权限
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.tip))
                    .setMessage(permissionTip)
                    .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseActivity.this, permissions, CommonDefine.REQUEST_PERMISSION_CODE);
                        }
                    }).setNegativeButton(getString(R.string.deny), null).show();
        } else {
            ActivityCompat.requestPermissions(BaseActivity.this, permissions, CommonDefine.REQUEST_PERMISSION_CODE);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        boolean flag = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 检测应用是否已经具有权限
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissionGranted(String[] permissions) {
        boolean flag = true;
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                flag = false; // 没有权限
                break;
            }
        }
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CommonDefine.REQUEST_PERMISSION_CODE) {
            if (verifyPermissions(grantResults)) {
                if (mPermissionCallback != null) {
                    mPermissionCallback.hasPermission();
                    mPermissionCallback = null;
                }
            } else {
                if (mPermissionCallback != null) {
                    mPermissionCallback.noPermission();
                    mPermissionCallback = null;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}

