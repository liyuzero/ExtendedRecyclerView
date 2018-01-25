mae-bundles-extended-recyclerview
=================================
支持增删（递归删，普通删除）的折叠RecyclerView

基于viewType实现，因此大量数据下不会有内存问题

## 数据结构：

1、数据存储的基本结构为树

2、在实际实现中树的保存方式分为了两种

（1）、数据输入形式：基于父节点与子节点的树形二维结构存储

（2）、数据与Adapter的联系形式：基于前序遍历的一维数组形式【ArrayList】

## 视图与数据联系

树的每一个层级对应Adapter的一种ViewType，对应一种实际的数据存储结构【Adapter的实际数据源】


## 功能类别

- 折叠显示

- 增：在指定节点增加子树【或单个节点】

- 删：删除指定Item，删除操作分两种：

    1、普通删除，仅删除单个节点

    2、递归删除，当被删除节点的父节点不存在其他孩子节点时，删除父节点，以此向上递归删除

## 使用方法
### 新增依赖
1. 在项目的根目录gradle新增仓库如下：

```java
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

2. 使用module依赖，新增依赖：

```java
    compile 'com.github.liyuzero:extendedRecyclerView:v1.0.1'
```

### 具体调用（详情见demo）
1. 构建原始数据，显示

```java
        ArrayList<ExtendedNode> roots = new ArrayList<>();
        roots.add(new ExtendedNode<>(new Type1Bean(0), false));

        roots.add(new ExtendedNode<>(new Type1Bean(1), false));
        sons = roots.get(1).addSons(getType2Nodes(2)).getSons();
        sons.get(0).addSons(getType3Nodes(1));
        sons.get(1).addSons(getType3Nodes(2));

        roots.add(new ExtendedNode<>(new Type1Bean(2), true));
        sons = roots.get(2).addSons(getType2Nodes(3)).getSons();
        sons.get(1).addSons(getType3Nodes(3));

        roots.add(new ExtendedNode<>(new Type1Bean(3), true));

        final ExtendedRecyclerViewHelper extendedRecyclerViewHelper =  = ExtendedRecyclerViewBuilder.build(recyclerView)
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
                        }).complete();

```

2. 对每个Item进行操作

```java
        //增加节点
        ArrayList<ExtendedNode> list = new ArrayList<>();
                        list.add(new ExtendedNode(new Type2Bean(111), false, new ExtendedNode(new Type3Bean(1111))));
                        list.add(new ExtendedNode(new Type2Bean(111)));
                        extendedRecyclerViewHelper.insertItems(initList.get(0), 0, list);

        //删除节点
        helper.usuallyDelete(getLayoutPosition());
        //递归删除
        helper.recursionDelete(getLayoutPosition(), 1);
```
