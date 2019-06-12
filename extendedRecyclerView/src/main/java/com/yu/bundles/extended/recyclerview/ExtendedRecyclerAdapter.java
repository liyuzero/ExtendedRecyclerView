package com.yu.bundles.extended.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyu20 on 2018/1/8.
 */

public class ExtendedRecyclerAdapter extends RecyclerView.Adapter<ExtendedHolder> implements ExtendedRecyclerViewHelper {
    private RecyclerView recyclerView;
    private ExtendedHolderFactory extendedHolderFactory;
    private ExtendedDataUtils dataUtils;

    private boolean isEnableExtended = true;

    ExtendedRecyclerAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    final ExtendedRecyclerAdapter init(List<ExtendedNode> dataList, ExtendedHolderFactory extendedHolderFactory) {
        dataUtils = new ExtendedDataUtils(dataList);
        this.extendedHolderFactory = extendedHolderFactory;
        return this;
    }

    public final void updateSrcData(List<ExtendedNode> dataList){
        updateSrcData(dataList, extendedHolderFactory);
    }

    public final void updateSrcData(List<ExtendedNode> dataList, ExtendedHolderFactory extendedHolderFactory){
        this.extendedHolderFactory = extendedHolderFactory;
        dataUtils.updateSrcData(dataList);
    }

    @Override
    public final ExtendedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return extendedHolderFactory.getHolder(this, parent, viewType);
    }

    @Override
    public final void onBindViewHolder(ExtendedHolder holder, int position) {
        holder.setData(dataUtils.getExtendedNode(position));
    }

    @Override
    public final int getItemCount() {
        if(extendedHolderFactory == null){
            return 0;
        }
        return dataUtils.getCurAvailableCount();
    }

    @Override
    public final int getItemViewType(int position) {
        return dataUtils.getExtendedNode(position).layerLevel;
    }

    /*---------------------------------------------点击Item后的数据转换------------------------------------------------------*/

    /*
    * Item点击后的伸缩事件
    *
    * return 当前点击触发的是扩展事件：true， 触发折叠事件：false
    *
    * */
    @Override
    public boolean onExtendedItemClick(final int position){
        ExtendedNode extendNode = dataUtils.getExtendedNode(position);
        int preAvailableCount = dataUtils.getCurAvailableCount();
        int[] notifyPos = dataUtils.onExtendedItemClick(extendNode, position);
        int curAvailableCount = dataUtils.getCurAvailableCount();
        if(notifyPos[0] != -1){
            notifyItemChanged(position);
            if(curAvailableCount > preAvailableCount){
                notifyItemRangeInserted(notifyPos[0], notifyPos[1]);
                if(notifyPos[0] > ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition()){
                    recyclerView.scrollToPosition(notifyPos[0]);
                }
            } else if(curAvailableCount < preAvailableCount){
                notifyItemRangeRemoved(notifyPos[0], notifyPos[1]);
            } else {
                notifyItemRangeChanged(notifyPos[0], notifyPos[1]);
            }
        }
        return extendNode.isExtended;
    }

    /*-------------------------------------------------------删除操作------------------------------------------------------------*/

    /*
    * 递归删除至顶层节点，即第一级节点会被递归删除
    *
    * */
    public void recursionDelete(int position){
        recursionDelete(position, -1);
    }

    /*
    * 递归删除
    *
    * 当被删除节点的父节点不存在其他孩子节点时，删除父节点，以此向上递归删除
    *
    * @param layer: 递归删除至指定层级，start from -1
    *
    * */
    public void recursionDelete(int position, int layer){
        ExtendedNode deleteNode = dataUtils.getRecursionDeleteNode(position, layer);
        int deletePos = dataUtils.getNodeRecyclerPos(deleteNode);
        if(deletePos == -1){
            return;
        }
        if(deleteNode.parent != null){
            notifyItemChanged(dataUtils.getNodeRecyclerPos(deleteNode.parent));
        }
        notifyItemRangeRemoved(deletePos, dataUtils.deleteNode(deleteNode));
    }

    //仅删除节点
    public void usuallyDelete(int position){
        ExtendedNode deleteNode = dataUtils.getExtendedNode(position);
        int deletePos = dataUtils.getNodeRecyclerPos(deleteNode);
        if(deletePos == -1){
            return;
        }
        if(deleteNode.parent != null){
            notifyItemChanged(dataUtils.getNodeRecyclerPos(deleteNode.parent));
        }
        notifyItemRangeRemoved(deletePos, dataUtils.deleteNode(deleteNode));
    }

    /*-----------------------------------------增加节点-------------------------------------------*/

    @Override
    public void insertItems(ExtendedNode parent, int sonInsertIndex, ArrayList<ExtendedNode> items) {
        if(sonInsertIndex < 0 || items == null || items.size() <= 0){
            return ;
        }

        int availableCount = 0;
        for (ExtendedNode node: items){
            availableCount += dataUtils.getAvailableCount(node);
        }

        List<ExtendedNode> tempList = dataUtils.insertItems(parent, sonInsertIndex, items);
        for (ExtendedNode tempNode: tempList){
            if(!tempNode.isExtended){
                onExtendedItemClick(dataUtils.getNodeRecyclerPos(tempNode));
            }
        }

        notifyItemRangeInserted((int) tempList.get(tempList.size() - 1).data, availableCount);
        notifyItemChanged(dataUtils.getTransformOriginDataList().indexOf(parent));
    }

    @Override
    public ExtendedRecyclerAdapter getExtendedRecyclerAdapter() {
        return this;
    }

    @Override
    public <T> ExtendedNode<T> getNode(int recyclerPos) {
        return dataUtils.getExtendedNode(recyclerPos);
    }

    @Override
    public int getCurItemCount(){
        return dataUtils.getCurAvailableCount();
    }

    @Override
    public int getNodePos(ExtendedNode node) {
        return dataUtils.getTransformOriginDataList().indexOf(node);
    }

    void setEnableExtended(boolean enableExtended) {
        isEnableExtended = enableExtended;
    }

    boolean isEnableExtended() {
        return isEnableExtended;
    }
}
