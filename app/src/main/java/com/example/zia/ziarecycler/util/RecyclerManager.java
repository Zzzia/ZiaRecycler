package com.example.zia.ziarecycler.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zia .
 */

public class RecyclerManager {

    private static final String TAG = "RecyclerManagerTest";

    //回调方法，暴露出去在activity里绑定数据
    public interface OnBindHolder {
        void bind(ViewHolder holder, Object o, int position);
    }
    //侧滑接口
    interface ItemTouchHelperAdapter{
        void onItemMove(int fromPosition,int toPosition);
        void onItemDismiss(int position);
    }

    private Context context;
    private RecyclerView recyclerView;
    private CommonAdapter adapter;
    private ItemTouchHelperCallBack itemTouchHelperCallBack;
    private RecyclerView.LayoutManager layoutManager = null;
    private Wrapper wrapper = null;


    /**
     * 构造方法
     * @param context context
     * @param RecyclerId resourceId
     * @param itemId resourceId
     * @param call 回调接口
     */
    public RecyclerManager(Context context, int RecyclerId, int itemId, OnBindHolder call){
        this.context = context;
        recyclerView = (RecyclerView)((Activity)context).findViewById(RecyclerId);
        adapter = new CommonAdapter(context,itemId,call);
        //itemTouchHelperCallBack = new ItemTouchHelperCallBack(adapter);
        wrapper = new Wrapper(adapter);
        itemTouchHelperCallBack = new ItemTouchHelperCallBack(wrapper);
    }

    /**
     * 最后调用的方法，依据之前的设置构建recyclerView
     */
    public void Build(){
        recyclerView.setAdapter(wrapper);
        if(layoutManager != null) {
            recyclerView.setLayoutManager(layoutManager);
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
        //设置拖动效果
        ItemTouchHelper helper = new ItemTouchHelper(itemTouchHelperCallBack);
        helper.attachToRecyclerView(recyclerView);
    }


    /**
     * 添加headerView
     * @param view view
     */
    public void addHeaderView(View view){
        wrapper.addHeaderView(view);
    }

    /**
     * 添加footerView
     * @param view view
     */
    public void addFootView(View view){
        wrapper.addFootView(view);
    }

    /**
     * 设置recycler布局方式
     * @param layoutManager layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }

    /**
     * 长按拖动设置，0为默认，可上可下
     * @param dragFlags 上下拖动方向设置   ItemTouchHelper.UP | ItemTouchHelper.DOWN
     */
    public void setLongPressDragEnabled(int dragFlags){
        itemTouchHelperCallBack.setLongPressDragEnabled(true);
        if(dragFlags != 0){
            itemTouchHelperCallBack.setDragFlags(dragFlags);
        }
    }

    /**
     * 左右滑动一小段暴露额外view，0为默认，默认只能向左，宽度为itemView的一半
     * @param swipeFlags 左右滑动方向设置，默认只能左滑 ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
     * @param maxWidth 滑动的最大距离
     */
    public void setItemViewSwipeAndStop(int swipeFlags,int maxWidth){
        itemTouchHelperCallBack.setItemViewSwipeEnabled(true);
        if(swipeFlags != 0){
            itemTouchHelperCallBack.setSwipeFlags(swipeFlags);
        }
        if(maxWidth != 0){
            itemTouchHelperCallBack.setMaxWidth(maxWidth);
        }
    }

    /**
     * 设置快速滑动使item消失
     * @param swipeFlags ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
     */
    public void setSwipeDismiss(int swipeFlags){
        if(swipeFlags != 0){
            itemTouchHelperCallBack.setSwipeFlags(swipeFlags);
        }
        else {
        itemTouchHelperCallBack.setSwipeFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }
        itemTouchHelperCallBack.setItemViewSwipeEnabled(true);
        itemTouchHelperCallBack.setIsSwipeDismiss(true);
    }


    /**
     * 刷新或者初始化数据
     * @param list 数据集合
     */
    public void setData(List list){
        adapter.setDataList(list);
    }

    /**
     * 获取recycler
     * @return recycler
     */
    public RecyclerView getRecycler(){
        return recyclerView;
    }


    /**
     * 获取adapter
     * @return adapter
     */
    public CommonAdapter getAdapter(){
        return adapter;
    }



    /**
     * 通用适配器
     * @param <T>
     */
    class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements ItemTouchHelperAdapter{

        private Context context;
        private int layoutId;
        private List<T> dataList;
        private RecyclerManager.OnBindHolder call;

        CommonAdapter(Context context, int layoutId,RecyclerManager.OnBindHolder call){
            this.context = context;
            this.layoutId = layoutId;
            this.call = call;
            dataList = new ArrayList<>();
        }

        void setDataList(List<T> dataList){
            this.dataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
            return new ViewHolder(context,itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            call.bind(viewHolder, dataList.get(i),i);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            Collections.swap(dataList,fromPosition,toPosition);
            notifyItemMoved(fromPosition,toPosition);
        }

        @Override
        public void onItemDismiss(int position) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }


    /**
     * item的视图类
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        private ViewHolder(Context context, View itemView)
        {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<View>();
        }

        /**
         * 通过viewId获取控件
         *
         * @param viewId
         * @return
         */
        public <T extends View> T getView(int viewId)
        {
            View view = mViews.get(viewId);
            if (view == null)
            {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public View getConvertView()
        {
            return mConvertView;
        }
    }


    /**
     * 装饰，header,footer等
     * @param <T>
     */
    class Wrapper<T> extends RecyclerView.Adapter<ViewHolder> implements ItemTouchHelperAdapter{

        private static final int BASE_HEADER = 10000;
        private static final int BASE_FOOTER = 20000;

        private SparseArrayCompat<View> mHeaderViews;
        private SparseArrayCompat<View> mFootViews;
        private CommonAdapter innerAdapter;

        Wrapper(CommonAdapter innerAdapter){
            this.innerAdapter = innerAdapter;
            mFootViews = new SparseArrayCompat<>();
            mHeaderViews = new SparseArrayCompat<>();
        }

        void addHeaderView(View view){
            mHeaderViews.put(mHeaderViews.size()+BASE_HEADER,view);
        }

        void addFootView(View view){
            mFootViews.put(mFootViews.size()+BASE_FOOTER,view);
        }

        int getHeaderCount(){
            return mHeaderViews.size();
        }

        int getFootCount(){
            return mFootViews.size();
        }

        private boolean isHeaderViewPos(int position)
        {
            return position < getHeaderCount();
        }

        private boolean isFooterViewPos(int position)
        {
            return position >= getHeaderCount() + getRealItemCount();
        }

        private int getRealItemCount()
        {
            return innerAdapter.getItemCount();
        }


        @Override
        public int getItemViewType(int position) {
            if(isHeaderViewPos(position)) return mHeaderViews.keyAt(position);
            else if(isFooterViewPos(position)) return mFootViews.keyAt(position-getHeaderCount()-getRealItemCount());
            return innerAdapter.getItemViewType(position-getHeaderCount());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
            if(mHeaderViews.get(type) != null){
                return new ViewHolder(context,mHeaderViews.get(type));
            }
            else if(mFootViews.get(type) != null) return new ViewHolder(context,mFootViews.get(type));
            return innerAdapter.onCreateViewHolder(viewGroup,type);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            if(isHeaderViewPos(position)) return;
            if(isFooterViewPos(position)) return;
            innerAdapter.onBindViewHolder(viewHolder,position-getHeaderCount());
        }

        @Override
        public int getItemCount() {
            return getHeaderCount()+getFootCount()+getRealItemCount();
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            //禁止header和footer改变顺序
            if(isFooterViewPos(fromPosition) || isHeaderViewPos(fromPosition)) return;
            Collections.swap(innerAdapter.dataList,fromPosition-getHeaderCount(),toPosition-getHeaderCount());
            notifyItemMoved(fromPosition,toPosition);
        }

        @Override
        public void onItemDismiss(int position) {
            //禁止header和footer改变顺序
            if(isFooterViewPos(position) || isHeaderViewPos(position)) return;
            innerAdapter.dataList.remove(position-getHeaderCount());
            notifyItemRemoved(position);
        }
    }


    /**
     * item的手势监听类
     */
    class ItemTouchHelperCallBack extends android.support.v7.widget.helper.ItemTouchHelper.Callback {

        private ItemTouchHelperAdapter adapter;//回调控制接口
        private int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//上下拖动，默认可以上下活动
        private int swipeFlags = ItemTouchHelper.LEFT;//左右滑动，默认只能向左
        private boolean isLongPressDragEnabled = false;//上下拖动开关，默认不能上下滑动
        private boolean isItemViewSwipeEnabled = false;//左右滑动开关，默认不能左右滑动
        private int maxWidth = 0;//itemView左右滑动距离，默认item的一半
        private boolean isSwipeDismiss = false;//快速滑动消失开关，默认不能

        ItemTouchHelperCallBack(ItemTouchHelperAdapter helper){
            this.adapter = helper;
        }

        void setDragFlags(int i){
            dragFlags = i;
        }
        void setSwipeFlags(int i){
            swipeFlags = i;
        }
        void setLongPressDragEnabled(boolean isLongPressDragEnabled){
            this.isLongPressDragEnabled = isLongPressDragEnabled;
        }
        void setItemViewSwipeEnabled(boolean isItemViewSwipeEnabled){
            this.isItemViewSwipeEnabled = isItemViewSwipeEnabled;
        }
        void setMaxWidth(int maxWidth){
            this.maxWidth = maxWidth;
        }
        void setIsSwipeDismiss(boolean isSwipeDismiss){
            this.isSwipeDismiss = isSwipeDismiss;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(dragFlags,swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
            adapter.onItemMove(viewHolder.getAdapterPosition(),viewHolder1.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            if(isSwipeDismiss)
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setScrollX(0);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX <= 0){
                if(maxWidth == 0) {
                    if (-dX <= viewHolder.itemView.getWidth() / 2) {
                        viewHolder.itemView.scrollTo(-(int) dX, 0);
                    } else {
                        viewHolder.itemView.setScrollX(viewHolder.itemView.getWidth() / 2);
                    }
                }
                else{
                    if (-dX <= maxWidth) {
                        viewHolder.itemView.scrollTo(-(int) dX, 0);
                    } else {
                        viewHolder.itemView.setScrollX(maxWidth);
                    }
                }
            }
            else if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return isLongPressDragEnabled;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return isItemViewSwipeEnabled;
        }

    }
}






