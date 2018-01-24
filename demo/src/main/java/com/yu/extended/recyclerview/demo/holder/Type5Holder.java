package com.yu.extended.recyclerview.demo.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yu.extended.recyclerview.demo.R;
import com.yu.bundles.extended.recyclerview.ExtendedHolder;
import com.yu.bundles.extended.recyclerview.ExtendedNode;
import com.yu.bundles.extended.recyclerview.ExtendedRecyclerViewHelper;
import com.yu.extended.recyclerview.demo.bean.Type4Bean;

/**
 * Created by liyu20 on 2018/1/9.
 */

public class Type5Holder extends ExtendedHolder<Type4Bean> implements View.OnLongClickListener{
    private TextView textView;
    private ImageView imageView;

    public Type5Holder(ExtendedRecyclerViewHelper helper, View itemView) {
        super(helper, itemView);
        textView = itemView.findViewById(R.id.textView);
        imageView = itemView.findViewById(R.id.image);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void setData(ExtendedNode<Type4Bean> node) {
        textView.setText("五级菜单："+ "长按递归删除至二级菜单");
        imageView.setVisibility(View.GONE);
    }

    @Override
    public boolean onLongClick(View v) {
        helper.recursionDelete(getLayoutPosition(), 1);
        return true;
    }

    @Override
    protected View getExtendedClickView() {
        return null;
    }
}
