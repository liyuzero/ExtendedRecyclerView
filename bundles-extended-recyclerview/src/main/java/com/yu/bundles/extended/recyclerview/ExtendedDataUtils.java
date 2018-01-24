package com.yu.bundles.extended.recyclerview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liyu20 on 2018/1/10.
 */

@SuppressWarnings({"unchecked", "unused"})
class ExtendedDataUtils {
    //源数据
    private List<ExtendedNode> srcDataList;
    //转换后的数据
    private List<ExtendedNode> transformOriginDataList = new ArrayList<>();
    private int[] posMapArray;
    private int curAvailableCount;

    ExtendedDataUtils(List<ExtendedNode> srcDataList) {
        this.srcDataList = srcDataList;
        updateSrcData(srcDataList);
    }

    final void updateSrcData(List<ExtendedNode> dataList){
        this.srcDataList = dataList;
        //更新originDataList
        transform(dataList, 0, transformOriginDataList);
        posMapArray = new int[transformOriginDataList.size()];
        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray);
    }

    /*-----------------------------树形结构转换为一维数组形式，方便关联Adapter视图-----------------------------------*/

    /*
    * 转换数据
    *
    * @param arrays, 数据数组
    * @param layerLevel, 该数组所处的树内层次
    *
    * */
    private void transform(List<ExtendedNode> dataList, int layerLevel, List<ExtendedNode> transformOriginDataList){
        if(dataList == null){
            return ;
        }
        for (ExtendedNode baseBean: dataList){
            baseBean.layerLevel = layerLevel;
            transformOriginDataList.add(baseBean);
            transform(baseBean.getSons(), layerLevel + 1, transformOriginDataList);
        }
    }

    /*
    * 遍历转换后的数据集合，得到折叠后的数据位置和原始数据集合位置的映射数组
    *
    * return 映射数组的实际大小，也是视图上所需展示的未被折叠的item的个数
    *
    * */
    private int obtainPosMapArray(List<ExtendedNode> transformOriginDataList, int[] posMapArray){
        int i = 0, j = 0;
        while(j < transformOriginDataList.size()){
            posMapArray[i++] = j;
            ExtendedNode node = transformOriginDataList.get(j);
            int curLayer = node.layerLevel;
            //如果折叠
            if(!node.isExtended){
                j++;
                while(j < transformOriginDataList.size() && transformOriginDataList.get(j).layerLevel > curLayer){
                    j++;
                }
            } else {
                j++;
            }
        }
        return i;
    }

    /*---------------------------------------------点击Item后的数据转换------------------------------------------------------*/

    /*
    * Item点击后的伸缩事件
    *
    * return 当前点击触发的是扩展事件：true， 触发折叠事件：false
    *
    * */
    int[] onExtendedItemClick(ExtendedNode extendNode, final int position){
        extendNode.isExtended = !extendNode.isExtended;
        int[] notifyPos = getItemRange(position);
        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray);
        return notifyPos;
    }

    /*
    * 获取折叠或展开所需要改变的item的位置范围, startPos, count
    * 改变动作包括：插入，删除，改变
    * */
    private int[] getItemRange(int position){
        int curLevel = transformOriginDataList.get(posMapArray[position]).layerLevel;
        int[] res = new int[]{-1, -1};
        int count = 0;
        int i = posMapArray[position] + 1;
        while (i < transformOriginDataList.size() && transformOriginDataList.get(i).layerLevel > curLevel){
            if(res[0] == -1){
                res[0] = position + 1;
            }
            if(!transformOriginDataList.get(i).isExtended){
                int itemLevel = transformOriginDataList.get(i).layerLevel;
                int j;
                for (j = i + 1; j < transformOriginDataList.size() && transformOriginDataList.get(j).layerLevel > itemLevel; j++){}
                i = j;
            } else {
                i++;
            }
            count++;
        }
        res[1] = count;
        return res;
    }

    /*-------------------------------------------------------删除操作------------------------------------------------------------*/

    ExtendedNode getRecursionDeleteNode(int position, int layer){
        ExtendedNode deleteNode = transformOriginDataList.get(posMapArray[position]);
        ExtendedNode parent = deleteNode.parent;
        while (parent != null && parent.getSons().size() <= 1 && parent.layerLevel > layer){
            deleteNode = parent;
            parent = parent.parent;
        }
        return deleteNode;
    }

    /*
    * return 删除节点子树包含的展开的节点数量
    * */
    int deleteNode(ExtendedNode deleteNode){
        int deleteCount = getAvailableCount(deleteNode);

        //删除源数据
        ExtendedNode parent = deleteNode.parent;
        deleteNode.parent = null;
        if(parent != null){
            parent.getSons().remove(deleteNode);
        } else {
            srcDataList.remove(deleteNode);
        }

        //删除转换集合内的数据
        int deleteNodeLayerLevel = deleteNode.layerLevel;
        int deletePos = transformOriginDataList.indexOf(deleteNode);
        transformOriginDataList.remove(deletePos);
        while(deletePos < transformOriginDataList.size() && transformOriginDataList.get(deletePos).layerLevel > deleteNodeLayerLevel){
            transformOriginDataList.remove(deletePos);
        }

        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray);

        return deleteCount;
    }

    //找出Node在RecyclerView中的位置【注：onBindViewHolder中的position并不总是对应list数据中的位置，所以不可采用，{“Do not treat position as fixed”}】
    int getNodeRecyclerPos(ExtendedNode deleteNode){
        int tempIndex = transformOriginDataList.indexOf(deleteNode);
        int deleteNodeIndex = -1;
        for (int i = 0; i < curAvailableCount; i++){
            if(posMapArray[i] == tempIndex){
                deleteNodeIndex = i;
                break;
            }
        }
        return deleteNodeIndex;
    }

    /*-----------------------------------------增加节点-------------------------------------------*/

    //返回需要扩展开的父类节点位置集合
    List<ExtendedNode> insertItems(ExtendedNode parent, int sonInsertIndex, ArrayList<ExtendedNode> items) {
        if(sonInsertIndex < 0 || items == null || items.size() <= 0){
            return null;
        }

        //设置层级，转换数据
        ArrayList<ExtendedNode> transformItems = new ArrayList<>();
        transform(items, parent.layerLevel + 1, transformItems);

        //设置转换数据集合
        ArrayList<ExtendedNode> sons = parent.getSons();
        if(sonInsertIndex > sons.size()){
            sonInsertIndex = sons.size();
        }

        int index;
        if(sonInsertIndex < sons.size()){
            ExtendedNode node = sons.get(sonInsertIndex);
            index = transformOriginDataList.indexOf(node);
            transformOriginDataList.addAll(index, transformItems);
        } else {
            ExtendedNode node = getLastSonNode(parent);
            index = transformOriginDataList.indexOf(node) + 1;
            transformOriginDataList.addAll(index, transformItems);
        }
        posMapArray = new int[transformOriginDataList.size()];
        curAvailableCount = obtainPosMapArray(transformOriginDataList, posMapArray);

        List<ExtendedNode> tempList = new ArrayList<>();
        ExtendedNode node = parent;
        while(node != null && !node.isExtended){
            tempList.add(node);
            node = parent.parent;
        }
        Collections.reverse(tempList);

        tempList.add(new ExtendedNode(index, true));

        //设置原始数据集合
        parent.addSons(sonInsertIndex, items);

        return tempList;
    }

    /*---------------------------------------------数据通用操作-----------------------------------------------------*/

    private ExtendedNode getLastSonNode(ExtendedNode node){
        if(!node.hasSons()){
            return node;
        }
        return getLastSonNode((ExtendedNode) node.getSons().get(node.getSons().size() - 1));
    }

    int getAvailableCount(ExtendedNode deleteNode){
        if(deleteNode == null){
            return 0;
        }
        if(!deleteNode.isExtended || deleteNode.getSons() == null || deleteNode.getSons().size() == 0){
            return 1;
        }
        int count = 1;
        for (Object son: deleteNode.getSons()){
            count += getAvailableCount((ExtendedNode) son);
        }
        return count;
    }

    ExtendedNode getExtendedNode(int recyclerPosition){
        return transformOriginDataList.get(posMapArray[recyclerPosition]);
    }

    int getCurAvailableCount() {
        return curAvailableCount;
    }

    List<ExtendedNode> getTransformOriginDataList() {
        return transformOriginDataList;
    }
}
