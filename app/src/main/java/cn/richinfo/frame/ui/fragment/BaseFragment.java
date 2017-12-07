package cn.richinfo.frame.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.richinfo.frame.RichApplication;

/**
 * author : Pan
 * time   : 2017/4/19
 * desc   : BasicFragment
 * version: 1.0
 * <p>
 * Copyright: Copyright (c) 2017
 * Company:XXXXXXXXXXXXXXXXXXXX
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(setContentView(), container, false);
        unbinder = ButterKnife.bind(this, root);
        initView(root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public abstract int setContentView();

    public abstract void initView(View root);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RichApplication.mRefWatcher.watch(this);
    }

}
