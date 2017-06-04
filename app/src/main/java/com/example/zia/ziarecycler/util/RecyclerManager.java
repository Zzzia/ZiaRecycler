package com.example.zia.ziarecycler.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 2017/6/4.
 */

public class RecyclerManager {
    //回调方法，暴露出去在activity里绑定数据
    public interface OnBindHolder {
        void bind(ViewHolder holder, Object o, int position);
    }
    private Context context;
    private RecyclerView recyclerView;
    private CommonAdapter adapter;


    public RecyclerManager(Context context, int RecyclerId, int itemId, OnBindHolder call){
        this.context = context;
        recyclerView = (RecyclerView)((Activity)context).findViewById(RecyclerId);
        adapter = new CommonAdapter(context,itemId,call);
    }

    /**
     * 刷新或者初始化数据
     * @param list 数据集合
     */
    public void setData(List list){
        adapter.setDataList(list);
    }

    /**
     * 最后调用的方法，依据之前的设置构建recyclerView
     */
    public void Build(){
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public RecyclerView getRecycler(){
        return recyclerView;
    }

    public CommonAdapter getAdapter(){
        return adapter;
    }



    /**
     * 通用适配器
     * @param <T>
     */
    class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>{

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
}






