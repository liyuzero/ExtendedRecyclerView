package com.yu.bundles.extended.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by liyu20 on 2018/1/8.
 */

public abstract class ExtendedHolder<T> extends RecyclerView.ViewHolder{
    protected ExtendedRecyclerViewHelper helper;

    public ExtendedHolder(final ExtendedRecyclerViewHelper helper, View itemView) {
        super(itemView);
        this.helper = helper;
        if(((ExtendedRecyclerAdapter)helper).isEnableExtended()){
            View view = getExtendedClickView() == null? itemView: getExtendedClickView();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean isExtended = helper.onExtendedItemClick(getLayoutPosition());
                    if(getOnExtendedItemClickListener() != null){
                        if(isExtended){
                            getOnExtendedItemClickListener().onExtendedClick();
                        } else {
                            getOnExtendedItemClickListener().onFoldClick();
                        }
                    }
                }
            });
        }
    }

    public abstract void setData(ExtendedNode<T> node);

    protected abstract View getExtendedClickView();

    public OnExtendedItemClickListener getOnExtendedItemClickListener(){
        return null;
    }

    public interface OnExtendedItemClickListener {
        void onExtendedClick();
        void onFoldClick();
    }
}
