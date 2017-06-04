# ZiaRecycler
通用RecyclerView管理工具

[管理工具直达车](https://github.com/Zzzia/ZiaRecycler/blob/master/app/src/main/java/com/example/zia/ziarecycler/util/RecyclerManager.java)

<img src="https://github.com/Zzzia/Files/blob/master/gifs/ZiaRecycler.gif" width="360" height="640" />

~~~ java
RecyclerManager manager = new RecyclerManager(this,
                R.id.recycler,
                R.layout.item,
                new RecyclerManager.OnBindHolder() {
            @Override
            public void bind(RecyclerManager.ViewHolder holder, Object o, int position) {
                //获取item中的id，绑定数据
                TextView textView = holder.getView(R.id.item_tv);
                textView.setText((String) o);
            }
        });
        //附加特效方法中所有0值为设置默认值
        manager.setItemViewSwipeAndStop(0,0);//向左滑动并露出额外的view
        manager.setLongPressDragEnabled(0);//长按拖动
        manager.Build();//设置完毕，最后调用该方法
        manager.setData(list);//这个方法可以在任何时间调用
~~~

~~~ java
//目前的public方法：

构造方法
	public RecyclerManager(Context context, int RecyclerId, int itemId, OnBindHolder call)

最后调用
	public void Build()

设置LayoutManager
	public void setLayoutManager(RecyclerView.LayoutManager layoutManager) 

长按拖动设置，0为默认，可上可下
参数可选 ItemTouchHelper.UP | ItemTouchHelper.DOWN
	public void setLongPressDragEnabled(int dragFlags)

左右滑动一小段暴露额外view，0为默认，默认只能向左，宽度为itemView的一半
参数可选 ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
	public void setItemViewSwipeAndStop(int swipeFlags,int maxWidth)

设置快速滑动使item消失
参数可选 ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
	public void setSwipeDismiss(int swipeFlags)

~~~

