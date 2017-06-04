package com.example.zia.ziarecycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        manager.setData(list);//这个方法可以放在后面调用，或者在新线程中调用
        manager.Build();
    }

    private void initData(){
        list = new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add("data:  "+i);
        }
    }
}
