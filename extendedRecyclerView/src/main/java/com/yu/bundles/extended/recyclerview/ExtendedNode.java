package com.yu.bundles.extended.recyclerview;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by liyu20 on 2018/1/8.
 */

public class ExtendedNode<T> {
    public T data;
    ExtendedNode parent;
    int layerLevel;
    private ArrayList<ExtendedNode> sons = new ArrayList<>();
    public boolean isExtended;

    public ExtendedNode(T data, boolean isExtended, ExtendedNode... sons) {
        this.data = data;
        this.isExtended = isExtended;
        if(sons != null && sons.length > 0){
            Collections.addAll(this.sons, sons);
            for (ExtendedNode son: sons){
                son.parent = this;
            }
        }
    }

    public ArrayList<ExtendedNode> getSons() {
        return sons;
    }

    public ExtendedNode addSons(ArrayList<ExtendedNode> sons) {
        return addSons(this.sons.size(), sons);
    }

    public ExtendedNode addSons(int index, ArrayList<ExtendedNode> sons) {
        if(sons != null && sons.size() > 0){
            this.sons.addAll(index, sons);
            for (ExtendedNode son: sons){
                son.parent = this;
            }
        }
        return this;
    }

    public boolean hasSons(){
        return !(sons == null || sons.size() <= 0);
    }

    public ExtendedNode(T data) {
        this.data = data;
        this.isExtended = true;
    }
}
