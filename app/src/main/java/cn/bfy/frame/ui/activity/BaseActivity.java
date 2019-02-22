/*
 *
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 * Copyright (C) 2015 GuDong <maoruibin9035@gmail.com>
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.bfy.frame.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author : Pan
 * time   : 2017/4/19
 * desc   : BasicActivity
 * version: 1.0
 * <p>
 * Copyright: Copyright (c) 2017
 * Company:XXXXXXXXXXXXXXXXXXXX
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 记录activity开启情况，用于级联关闭所有应用界面
     */
    protected static Map<String, BaseActivity> mActivityMap
            = new LinkedHashMap<>();

    public static void finishAllActivity() {
        Set<Map.Entry<String, BaseActivity>> set = mActivityMap.entrySet();
        for (Map.Entry<String, BaseActivity> entry : set) {
            if (!entry.getValue().isFinishing()) {
                entry.getValue().finish();
            }
        }
        mActivityMap.clear();
    }

    private Unbinder unbinder;

    /**
     * set layout of this activity
     *
     * @return the id of layout
     */
    protected abstract int getLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        unbinder = ButterKnife.bind(this);
        mActivityMap.put(hashCode() + "", this);
    }

    /**
     * set the id of menu
     *
     * @return if values is less then zero ,and the activity will not show menu
     */
    protected int getMenuRes() {
        return -1;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuRes() < 0) return true;
        getMenuInflater().inflate(getMenuRes(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        mActivityMap.remove(hashCode() + "");
        super.onDestroy();
    }

}
