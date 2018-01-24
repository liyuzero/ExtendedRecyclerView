package com.yu.extended.recyclerview.demo.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yu.extended.recyclerview.demo.R;
import com.yu.bundles.extended.recyclerview.ExtendedHolder;
import com.yu.bundles.extended.recyclerview.ExtendedNode;
import com.yu.bundles.extended.recyclerview.ExtendedRecyclerViewHelper;
import com.yu.extended.recyclerview.demo.bean.Type3Bean;

/**
 * Created by liyu20 on 2018/1/9.
 */

public class Type3Holder extends ExtendedHolder<Type3Bean> implements View.OnLongClickListener{
    private TextView textView;
    private ImageView imageView;

    public Type3Holder(ExtendedRecyclerViewHelper helper, View itemView) {
        super(helper, itemView);
        textView = itemView.findViewById(R.id.textView);
        imageView = itemView.findViewById(R.id.image);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void setData(ExtendedNode<Type3Bean> node) {
        textView.setText(node.data.name + "：" + node.getSons().size() + "：长按递归删除至一级菜单");
        imageView.setImageResource(node.isExtended? R.drawable.down: R.drawable.up);
        imageView.setVisibility(!node.hasSons()? View.GONE: View.VISIBLE);
    }

    @Override
    public boolean onLongClick(View v) {
        helper.recursionDelete(getLayoutPosition(), 0);
        return true;
    }

    @Override
    protected View getExtendedClickView() {
        return null;
    }
}
