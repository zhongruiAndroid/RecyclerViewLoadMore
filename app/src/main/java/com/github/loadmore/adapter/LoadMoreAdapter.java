package com.github.loadmore.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter<LoadMoreViewHolder> {
    Handler handler;
    /*正常view item*/
    private final int normal_view = 2000;
    /*显示加载更多*/
    private final int load_more_view_type = 1000;
    /*暂无更多数据*/
    private final int no_more_view_type = 1001;
    /*加载失败*/
    private final int load_error_view_type = 1002;
    /*回调方法,触发加载更多*/
    private OnLoadMoreListener onLoadMoreListener;
    /***用于判断是否还有更多数据*/
    private int pageSize;
    /***是否还有更多数据,没有更多数据显示"暂无更多"*/
    private boolean hasMoreData = false;
    /*** 加载是否失败,用于点击重新加载*/
    private boolean isLoadError;
    /*** 是否隐藏暂无内容的提示*/
    private boolean isHiddenPromptView = false;
    private View loadView, errorView, noMoreView;
    private String loadViewText = "正在加载更多...";
    private String noMoreViewText = "暂无更多";
    private String errorViewText = "加载失败,点击重试";
    private boolean isEndLoad=true;
    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mInflater;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;

    public LoadMoreAdapter(Context mContext, int pageSize) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.pageSize = pageSize;
    }

    abstract public int getItemLayoutId(int viewType);

    abstract public void bindData(LoadMoreViewHolder holder, int position, T item);

    @Override
    public LoadMoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LoadMoreViewHolder holder;
        if (viewType == normal_view) {//正常item view  viewType == normal_view
            holder = new LoadMoreViewHolder(mContext,
                    mInflater.inflate(getItemLayoutId(viewType), parent, false));
            if (mClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                    }
                });
            }
            if (mLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mLongClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                        return true;
                    }
                });
            }
        } else {
            holder = new LoadMoreViewHolder(mContext, setDefaultView(viewType));
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(final LoadMoreViewHolder holder, int position) {
        if (position <= getItemCount() - 2) {
            bindData(holder, position, mList.get(position));
            if (onLoadMoreListener != null &&isEndLoad&& hasMoreData && !isLoadError && position == getItemCount() - 2) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        isEndLoad=false;
                        onLoadMoreListener.loadMore();
                    }
                });
            }
        } else {
            if (onLoadMoreListener != null) {
                holder.bottomView.setOnClickListener(null);
                switch (holder.getItemViewType()) {
                    /*case load_more_view_type:
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                onLoadMoreListener.loadMore();
                            }
                        });
                        break;*/
                    case load_error_view_type:
                        holder.bottomView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isLoadError = false;
                                hasMoreData = true;
                                holder.bottomView.removeAllViews();
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        isEndLoad=false;
                                        onLoadMoreListener.loadMore();
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null && onLoadMoreListener != null && position == getItemCount() - 1) {
            if (isLoadError) {
                return load_error_view_type;
            } else if (hasMoreData) {
                return load_more_view_type;
            } else {
                return no_more_view_type;
            }
        }
        return normal_view;
    }

    private View setDefaultView(int viewType) {
        LoadMoreViewHolder.BottomView bottomView = new LoadMoreViewHolder.BottomView(mContext);
        bottomView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
        bottomView.setGravity(Gravity.CENTER);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomView.setLayoutParams(layoutParams);

        TextView textView = new TextView(mContext);
        switch (viewType) {
            case load_more_view_type://加载更多view
                if (loadView != null) {
                    bottomView.addView(loadView);
                } else {
                    layoutParams.height = dip2px(mContext, 50);
                    textView.setText(loadViewText);
                    bottomView.addView(textView);
                }
                break;
            case no_more_view_type://暂无更多view
                if (noMoreView != null) {
                    bottomView.addView(noMoreView);
                } else {
                    layoutParams.height = dip2px(mContext, 50);
                    textView.setText(noMoreViewText);
                    bottomView.addView(textView);
                }
                if (isHiddenPromptView) {
                    layoutParams.height = 0;
                }
                break;
            case load_error_view_type://加载失败view
                if (errorView != null) {
                    bottomView.addView(errorView);
                } else {
                    layoutParams.height = dip2px(mContext, 50);
                    textView.setText(errorViewText);
                    bottomView.addView(textView);
                }
                break;
        }
        return bottomView;
    }

    @Override
    public int getItemCount() {
        if (onLoadMoreListener != null) {
            return mList == null ? 0 : mList.size() + 1;
        } else {
            return mList == null ? 0 : mList.size();
        }
    }

    public void setList(List<T> list) {
        setList(list, false);
    }

    public void setList(List<T> list, boolean isNotifyData) {
        if (list == null || list.size() == 0 || list.size() < pageSize) {
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
        this.mList = list;
        isEndLoad=true;
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    public void addList(List<T> list) {
        addList(list, false);
    }

    public void addList(List<T> list, boolean isNotifyData) {
        if (list == null || list.size() == 0) {
            hasMoreData = false;
        } else if (list.size() < pageSize) {
            hasMoreData = false;
            this.mList.addAll(list);
        } else {
            hasMoreData = true;
            this.mList.addAll(list);
        }
        isEndLoad=true;
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    public List<T> getList() {
        return mList;
    }

    public void setClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setLongClickListener(OnItemLongClickListener mLongClickListener) {
        this.mLongClickListener = mLongClickListener;
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int pos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int pos);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    /*是否隐藏底部暂无内容的view*/
    public void setHiddenPromptView(boolean hiddenPromptView) {
        setHiddenPromptView(hiddenPromptView, false);
    }

    public void setHiddenPromptView(boolean hiddenPromptView, boolean isNotifyData) {
        isHiddenPromptView = hiddenPromptView;
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    /*是否加载失败*/
    public void setLoadError(boolean loadError) {
        isLoadError = loadError;
    }

    /*是否还有更多数据*/
    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    /*设置正在加载的view*/
    public void setLoadView(View loadView) {
        this.loadView = loadView;
    }

    /*设置加载失败的view*/
    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    /*设置没有更多数据的view*/
    public void setNoMoreView(View noMoreView) {
        this.noMoreView = noMoreView;
    }

    /*默认BottomView的情况下，设置正在加载的文字*/
    public void setLoadViewText(String loadViewText) {
        this.loadViewText = loadViewText;
    }

    /*默认BottomView的情况下，设置加载失败的文字*/
    public void setErrorViewText(String errorViewText) {
        this.errorViewText = errorViewText;
    }

    /*默认BottomView的情况下，设置没有更多数据的文字*/
    public void setNoMoreViewText(String noMoreViewText) {
        this.noMoreViewText = noMoreViewText;
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void onViewAttachedToWindow(LoadMoreViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        /*int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position))
        {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams)
            {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }*/
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType!= normal_view) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null){
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }
}
