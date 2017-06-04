# ZiaRecycler
通用RecyclerView管理工具


[直达车](https://github.com/Zzzia/ZiaRecycler/blob/master/app/src/main/java/com/example/zia/ziarecycler/util/RecyclerManager.java)

~~~
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
        manager.setData(list);//这个方法可以放在后面调用，或者在新线程中调用
        manager.Build();
~~~
