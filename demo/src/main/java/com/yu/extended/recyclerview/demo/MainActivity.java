package com.yu.extended.recyclerview.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yu.extended.recyclerview.demo.R;
import com.yu.bundles.extended.recyclerview.ExtendedHolder;
import com.yu.bundles.extended.recyclerview.ExtendedHolderFactory;
import com.yu.bundles.extended.recyclerview.ExtendedNode;
import com.yu.bundles.extended.recyclerview.ExtendedRecyclerViewBuilder;
import com.yu.bundles.extended.recyclerview.ExtendedRecyclerViewHelper;
import com.yu.extended.recyclerview.demo.bean.Type1Bean;
import com.yu.extended.recyclerview.demo.bean.Type2Bean;
import com.yu.extended.recyclerview.demo.bean.Type3Bean;
import com.yu.extended.recyclerview.demo.bean.Type4Bean;
import com.yu.extended.recyclerview.demo.holder.Type1Holder;
import com.yu.extended.recyclerview.demo.holder.Type2Holder;
import com.yu.extended.recyclerview.demo.holder.Type3Holder;
import com.yu.extended.recyclerview.demo.holder.Type4Holder;
import com.yu.extended.recyclerview.demo.holder.Type5Holder;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<ExtendedNode> initList = getInitData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final ExtendedRecyclerViewHelper extendedRecyclerViewHelper = ExtendedRecyclerViewBuilder.build(recyclerView)
                .init(initList, new ExtendedHolderFactory() {
                    @Override
                    public ExtendedHolder getHolder(ExtendedRecyclerViewHelper helper, ViewGroup parent, int viewType) {
                        switch (viewType){
                            case 0:
                                return new Type1Holder(helper, LayoutInflater.from(parent.getContext()).inflate(R.layout.holder1, parent, false));
                            case 1:
                                return new Type2Holder(helper, LayoutInflater.from(parent.getContext()).inflate(R.layout.holder2, parent, false));
                            case 2:
                                return new Type3Holder(helper, LayoutInflater.from(parent.getContext()).inflate(R.layout.holder3, parent, false));
                            case 3:
                                return new Type4Holder(helper, LayoutInflater.from(parent.getContext()).inflate(R.layout.holder4, parent, false));
                            case 4:
                                return new Type5Holder(helper, LayoutInflater.from(parent.getContext()).inflate(R.layout.holder5, parent, false));
                        }
                        return null;
                    }
                })
                .setEnableExtended(true)
                .complete();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<ExtendedNode> list = new ArrayList<>();
                list.add(new ExtendedNode(new Type2Bean(111), false, new ExtendedNode(new Type3Bean(1111))));
                list.add(new ExtendedNode(new Type2Bean(111)));
                extendedRecyclerViewHelper.insertItems(initList.get(0), 0, list);
            }
        }, 3000);
    }

    private ArrayList<ExtendedNode> getInitData(){
        ArrayList<ExtendedNode> roots = new ArrayList<>();
        roots.add(new ExtendedNode<>(new Type1Bean(0), false));
        ArrayList<ExtendedNode> sons = roots.get(0).addSons(getType2Nodes(1)).getSons();
        sons = sons.get(0).addSons(getType3Nodes(1)).getSons();
        sons = sons.get(0).addSons(getType4Nodes(1)).getSons();
        sons.get(0).addSons(getType4Nodes(1));

        roots.add(new ExtendedNode<>(new Type1Bean(1), false));
        sons = roots.get(1).addSons(getType2Nodes(2)).getSons();
        sons.get(0).addSons(getType3Nodes(1));
        sons.get(1).addSons(getType3Nodes(2));

        roots.add(new ExtendedNode<>(new Type1Bean(2), true));
        sons = roots.get(2).addSons(getType2Nodes(3)).getSons();
        sons.get(1).addSons(getType3Nodes(3));

        roots.add(new ExtendedNode<>(new Type1Bean(3), true));
        sons = roots.get(3).addSons(getType2Nodes(4)).getSons();
        sons.get(1).addSons(getType3Nodes(3));
        sons.get(3).addSons(getType3Nodes(3));
        sons = sons.get(0).addSons(getType3Nodes(1)).getSons();
        sons.get(0).addSons(getType4Nodes(2));

        return roots;
    }

    private ArrayList<ExtendedNode> getType2Nodes(int count){
        ArrayList<ExtendedNode> list = new ArrayList<>();
        for (int i=0; i<count; i++){
            list.add(new ExtendedNode<>(new Type2Bean(i), true));
        }
        return list;
    }

    private ArrayList<ExtendedNode> getType3Nodes(int count){
        ArrayList<ExtendedNode> list = new ArrayList<>();
        for (int i=0; i<count; i++){
            list.add(new ExtendedNode<>(new Type3Bean(i), true));
        }
        return list;
    }

    private ArrayList<ExtendedNode> getType4Nodes(int count){
        ArrayList<ExtendedNode> list = new ArrayList<>();
        for (int i=0; i<count; i++){
            list.add(new ExtendedNode<>(new Type4Bean(i), true));
        }
        return list;
    }
}
