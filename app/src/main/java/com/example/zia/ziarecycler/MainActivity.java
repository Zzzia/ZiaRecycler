package com.example.zia.ziarecycler;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zia.ziarecycler.util.RecyclerManager;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> list;
    private TextView header1,header2,footer;
    private View view1,view2,view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initTextView();
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
        //附加特效方法中所有0值为设置默认值

        manager.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //设置header和footer，view只是一个textView
        manager.addHeaderView(view1);
        manager.addHeaderView(view2);
        manager.addFootView(view3);

        manager.addItemClickListener(new RecyclerManager.OnItemClick() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this, "click "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                Toast.makeText(MainActivity.this, "longClick "+position, Toast.LENGTH_SHORT).show();
            }
        });

        //设置滑动监听
        //manager.setItemViewSwipeAndStop(0,0);//向左滑动并露出额外的view
        manager.setLongPressDragEnabled(0);//长按拖动

        manager.Build();//设置完毕，最后调用该方法

        manager.setData(list);//设置数据集合，这个方法可以在任何时间调用
    }

    private void initData(){
        list = new ArrayList<>();
        for(int i=0;i<20;i++){
            if (i%3 == 0){
                list.add("long\nlong\ndata");
                continue;
            }
            list.add("data:  "+i);
        }
    }

    private void initTextView(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view1 = inflater.inflate(R.layout.header, null,true);
        view2 = inflater.inflate(R.layout.header, null,true);
        view3 = inflater.inflate(R.layout.header, null,true);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click specialView", Toast.LENGTH_SHORT).show();
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click specialView", Toast.LENGTH_SHORT).show();
            }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click specialView", Toast.LENGTH_SHORT).show();
            }
        });
        header1 = new TextView(this);
        header1.setText("header1");
        header1.setWidth(400);
        header1.setGravity(Gravity.CENTER);
        header1.setTextSize(25);
        header2 = new TextView(this);
        header2.setText("header2");
        header2.setWidth(400);
        header2.setTextSize(25);
        header2.setGravity(Gravity.CENTER);
        footer = new TextView(this);
        footer.setText("footer");
        footer.setWidth(400);
        footer.setTextSize(25);
        footer.setGravity(Gravity.CENTER);
    }
}
