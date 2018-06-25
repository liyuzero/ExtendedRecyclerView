package com.yu.bundles.extended.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by liyu20 on 2018/1/9.
 */

public class ExtendedRecyclerViewBuilder {
    private RecyclerView recyclerView;
    private List<ExtendedNode> dataList;
    private ExtendedHolderFactory extendedHolderFactory;
    private boolean isEnableExtended = true;

    private boolean isInit;

    private ExtendedRecyclerViewBuilder(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView.setLayoutManager(new ExtendLayoutManager(recyclerView.getContext()));
        isInit = false;
    }

    public static ExtendedRecyclerViewBuilder build(RecyclerView recyclerView){
        return new ExtendedRecyclerViewBuilder(recyclerView);
    }

    public ExtendedRecyclerViewBuilder init(List<ExtendedNode> dataList, ExtendedHolderFactory extendedHolderFactory){
        this.dataList = dataList;
        this.extendedHolderFactory = extendedHolderFactory;
        isInit = true;
        return this;
    }

    public ExtendedRecyclerViewBuilder setEnableExtended(boolean enableExtended) {
        isEnableExtended = enableExtended;
        return this;
    }

    public ExtendedRecyclerViewHelper complete(){
        if(!isInit){
            throw new IllegalArgumentException("must call the method init()");
        }
        ExtendedRecyclerAdapter adapter = new ExtendedRecyclerAdapter(recyclerView).init(dataList, extendedHolderFactory);
        adapter.setEnableExtended(isEnableExtended);
        recyclerView.setAdapter(adapter);
        return adapter;
    }

    private class ExtendLayoutManager extends LinearLayoutManager {
        ExtendLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e){
                //兼容异常
            }
        }
    }
}
