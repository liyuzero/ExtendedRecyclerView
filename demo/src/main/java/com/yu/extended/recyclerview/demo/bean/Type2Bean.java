package com.yu.extended.recyclerview.demo.bean;

/**
 * Created by liyu20 on 2018/1/9.
 */

public class Type2Bean {
    public String name = "二级菜单";

    public Type2Bean(int i) {
        this.name = i + "-" + name;
    }
}
