package cn.bfy.frame.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bfy.frame.R;
import cn.bfy.frame.ui.widget.recycleView.ProgressWheel;

/**
 * Created by lq on 16/6/29.
 */
public abstract class BaseAdapter<M, IVH extends RecyclerView.ViewHolder, HVH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    /**
     * M  item  数据类型
     * HVH  header  viewHolder
     * IVH  Item    viewHolder
     * BVH  bottom  viewHolder
     */


    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOTER = Integer.MAX_VALUE;
    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected boolean hasMoreData = false;
    protected boolean hasFooter = false;
    protected List<M> dataList = new ArrayList<>();
    protected String hint;

    public BaseAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        hint = context.getString(R.string.user_center_no_data);
    }

    //内容长度
    public int getContentItemCount() {
        if(dataList != null) {
            return dataList.size();
        }else{
            return 0;
        }
    }

    //判断是否是Footer
    public boolean isFooter(int position) {
        return hasFooter && position == getContentItemCount();
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            //Footer
            return ITEM_TYPE_FOOTER;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    if (isFullSpanType(adapter.getItemViewType(position))) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        int type = getItemViewType(position);
        if (isFullSpanType(type)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
    }

    //根据类型判断是否单独占一行
    private boolean isFullSpanType(int type) {
        return type == ITEM_TYPE_FOOTER || type == ITEM_TYPE_HEADER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            return onCreateItemViewHolder(parent, viewType);
        } else if (viewType == ITEM_TYPE_FOOTER) {
            return new FooterViewHolder(mLayoutInflater.inflate(R.layout.item_view_load_more, parent, false));
        } else if (viewType == ITEM_TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_TYPE_FOOTER) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            //没有更多数据
            if (!hasMoreData) {
                footerViewHolder.mProgressView.setVisibility(View.GONE);
                footerViewHolder.loadMore.setText(hint);
            }
        } else if (holder.getItemViewType() == ITEM_TYPE_HEADER) {
                onBindHeaderViewHolder((HVH) holder, position);
        } else {
            onBindItemViewHolder((IVH) holder, position);
        }

    }

    @Override
    public int getItemCount() {
        return getContentItemCount() + (hasFooter ? 1 : 0);
    }


    public void setHasMoreData(boolean isMoreData) {
        if (this.hasMoreData != isMoreData) {
            this.hasMoreData = isMoreData;
            notifyDataSetChanged();
        }
    }

    public void setDataList(List<M> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void addDataList(List<M> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public M getItemData(int positon) {
        return dataList.get(positon);
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setHint(int resId) {
        hint = mContext.getResources().getString(resId);
    }

    public void setHasFooter(boolean hasFooter) {
        if (this.hasFooter != hasFooter) {
            this.hasFooter = hasFooter;
            notifyDataSetChanged();
        }
    }

    public void setHasMoreDataAndFooter(boolean hasMoreData, boolean hasFooter) {
        if (this.hasMoreData != hasMoreData || this.hasFooter != hasFooter) {
            this.hasMoreData = hasMoreData;
            this.hasFooter = hasFooter;
            notifyDataSetChanged();
        }
    }

    //item   ViewHolder 实现
    public abstract IVH onCreateItemViewHolder(ViewGroup parent, int viewType);

    public abstract HVH onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    //item   ViewHolder data实现
    public abstract void onBindItemViewHolder(final IVH holder, int position);

    public abstract void onBindHeaderViewHolder(HVH holder, int position);



    //Footer ViewHolder
    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public final ProgressWheel mProgressView;
        private final TextView loadMore;

        public FooterViewHolder(View view) {
            super(view);
            mProgressView = (ProgressWheel) itemView.findViewById(R.id.progress_view);
            loadMore = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }


}
