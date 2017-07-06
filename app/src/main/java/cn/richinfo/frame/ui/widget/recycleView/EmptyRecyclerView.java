package cn.richinfo.frame.ui.widget.recycleView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　彩讯科技股份有限公司
 * @company    : 彩讯科技股份有限公司
 * @author     : OuyangJinfu
 * @e-mail     : ouyangjinfu@richinfo.cn
 * @createDate : 2017/5/17 0004
 * @modifyDate : 2017/5/17 0004
 * @version    : 1.0
 * @desc       : 带EmptyView的recycleView
 * </pre>
 */
public class EmptyRecyclerView extends RecyclerView {

    private View emptyView;
    private static final String TAG = "hiwhitley";

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            Log.i(TAG, "onItemRangeInserted" + itemCount);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
        init();
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
       mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    private float downX;
    private float downY;
    protected int mTouchSlop = 5;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            downX = ev.getRawX();
            downY = ev.getRawY();
        }else if(ev.getAction() == MotionEvent.ACTION_MOVE) {
            LayoutManager lm = getLayoutManager();
            int orientation = OrientationHelper.VERTICAL;
            if(lm instanceof LinearLayoutManager){
                orientation = ((LinearLayoutManager)lm).getOrientation();
            }else if(lm instanceof StaggeredGridLayoutManager){
                orientation = ((StaggeredGridLayoutManager)lm).getOrientation();
            }
            float dy = Math.abs(ev.getRawY() - downY);
            float dx = Math.abs(ev.getRawX() - downX);
            if(orientation == OrientationHelper.VERTICAL) {
                if (dy < dx && dx > mTouchSlop) {
                    return false;
                }
            }else{
                if(dy > dx && dy > mTouchSlop){
                    return false;
                }
            }

        }
        return super.onInterceptTouchEvent(ev);
    }


}