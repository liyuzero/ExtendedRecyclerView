package com.yu.extended.recyclerview.demo.bean;

/**
 * Created by liyu20 on 2018/1/9.
 */

public class Type1Bean {
    public String name = "一级菜单";

    public Type1Bean(int i) {
        this.name = i + "-" + name;
    }
}
