package com.yu.extended.recyclerview.demo.bean;

/**
 * Created by liyu20 on 2018/1/9.
 */

public class Type4Bean {
    public String name = "四级菜单";

    public Type4Bean(int i) {
        this.name = i + "-" + name;
    }
}
