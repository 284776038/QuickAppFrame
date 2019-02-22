package cn.bfy.frame.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.bfy.frame.R;
import cn.bfy.frame.presenter.WebviewPresenter;
import cn.bfy.frame.ui.fragment.WebviewFragment;
import cn.bfy.frame.util.ActivityUtils;
import cn.bfy.frame.util.LogUtils;
import cn.bfy.frame.util.schedulers.SchedulerProvider;

/**
 * 公共h5页面，原生标题栏
 */
public class WebviewActivity extends BaseActivity {
    private final static String TITLE = "Title";
    private final static String URL = "Url";
    public final static String HIDE_TITLE_BAR = "HideTitleBar";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.action_bar_title)
    TextView mTitleView;
    @BindView(R.id.app_bar_layout)
    View mBarLayout;

    private WebviewFragment fragment;

    private String url;


    @Override
    protected int getLayout() {
        return R.layout.activity_base_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra(URL);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back_green);
        setTitle("");

        fragment =
                (WebviewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment == null) {
            fragment = WebviewFragment.newInstance();
            new WebviewPresenter(fragment, SchedulerProvider.getInstance());
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.fragment);
        }
        fragment.setUrl(url);

        if (getIntent().getBooleanExtra(HIDE_TITLE_BAR, false)) {
            actionBar.hide();
            mBarLayout.setVisibility(View.GONE);
        } else {
            actionBar.show();
            mBarLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String title = getIntent().getStringExtra(TITLE);
        if (TextUtils.isEmpty(title)) {
            mTitleView.setText(fragment.getmWebview().getTitle());
        } else {
            mTitleView.setText(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (fragment != null){
            if(fragment.onBackPressed()){
                LogUtils.i("WebviewActivity","onBackPressed() " + fragment.getClass() +"=> webviwe can go back");
                return;
            }
        }
        super.onBackPressed();
    }
}
