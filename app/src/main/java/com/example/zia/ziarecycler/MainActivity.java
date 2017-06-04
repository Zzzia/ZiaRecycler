package com.example.zia.ziarecycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;

import com.example.zia.ziarecycler.util.RecyclerManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        setRecycler();
    }

    private void setRecycler(){
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
        manager.setLayoutManager(new GridLayoutManager(this,1));//默认为LinearLayoutManager
        //附加特效方法中所有0值为设置默认值
        manager.setItemViewSwipeAndStop(0,0);//向左滑动并露出额外的view
        manager.setLongPressDragEnabled(0);//长按拖动
        manager.Build();//设置完毕，最后调用该方法
        manager.setData(list);//这个方法可以在任何时间调用
    }

    private void initData(){
        list = new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add("data:  "+i);
        }
    }
}
