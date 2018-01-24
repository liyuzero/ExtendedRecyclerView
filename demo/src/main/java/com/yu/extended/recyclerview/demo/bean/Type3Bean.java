package com.yu.extended.recyclerview.demo.bean;

/**
 * Created by liyu20 on 2018/1/9.
 */

public class Type3Bean {
    public String name = "三级菜单";

    public Type3Bean(int i) {
        this.name = i + "-" + name;
    }
}
