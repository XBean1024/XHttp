package com.dragon.app.helper;

import android.content.Context;
import android.view.View;

import com.dragon.R;
import com.dragon.app.callback.ViewHolderItemClickedCallback;
import com.dragon.app.bean.BeanMainActivity;
import com.dragon.app.holder.ViewHolderMain;
import com.smart.holder.iinterface.IViewHolder;
import com.smart.holder.iinterface.IViewHolderHelper;

import java.util.List;

import static com.dragon.util.UtilWidget.setViewAlphaAnimation;
import static com.bean.util.Common.getView;


/**
 * function
 */

public class ViewHolderHelperMain implements IViewHolderHelper<ViewHolderMain, BeanMainActivity> {
    private ViewHolderItemClickedCallback mViewHolderItemClickedCallback;

    public ViewHolderHelperMain(ViewHolderItemClickedCallback viewHolderItemClickedCallback) {
        mViewHolderItemClickedCallback = viewHolderItemClickedCallback;
    }

    @Override
    public IViewHolder initItemViewHolder(ViewHolderMain viewHolder, View convertView) {
        viewHolder = new ViewHolderMain();
        viewHolder.mTextView = getView(convertView, R.id.grid_item_text_view);
        return viewHolder;
    }

    @Override
    public void bindListDataToView(final Context context, List<BeanMainActivity> iBaseBeanList, final ViewHolderMain viewHolder, int position) {
        viewHolder.mTextView.setText(iBaseBeanList.get(position).getTextViewName());
        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewAlphaAnimation(viewHolder.mTextView);
                String name = (String) viewHolder.mTextView.getText();
                mViewHolderItemClickedCallback.onItemClickedInList(name);
            }
        });
    }
}
