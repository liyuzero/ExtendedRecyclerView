package com.yu.bundles.extended.recyclerview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyu20 on 2018/1/9.
 */

public interface ExtendedRecyclerViewHelper {

    void updateSrcData(List<ExtendedNode> dataList);

    void updateSrcData(List<ExtendedNode> dataList, ExtendedHolderFactory extendedHolderFactory);

    void recursionDelete(int position);

    void recursionDelete(int position, int layer);

    void usuallyDelete(int position);

    boolean onExtendedItemClick(int position);

    void insertItems(ExtendedNode parent, int index, ArrayList<ExtendedNode> items);

    int getCurItemCount();

    <T>ExtendedNode<T> getNode(int recyclerPos);

    ExtendedRecyclerAdapter getExtendedRecyclerAdapter();
}
