package com.proxy.androidcloud.detail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.helper.AbstractListHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/13  10:40 PM
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.DetailViewHolder> {

    private List<AbstractListHelper.HelperItemInfo> mItemInfos = new ArrayList<>();
    private AbstractListHelper mHelper;

    public void setData(AbstractListHelper helper) {
        this.mHelper = helper;
        this.mItemInfos.addAll(mHelper.createItems());
        helper.setRefreshListener(() -> {
            mItemInfos.clear();
            mItemInfos.addAll(mHelper.createItems());
            ListAdapter.this.notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        AbstractListHelper.HelperItemInfo itemInfo = mItemInfos.get(position);

        holder.reset();

        holder.setListener((context, button) -> mHelper.onItemClick(context, itemInfo, button));
        holder.setTitle(itemInfo.title);
        holder.setLeftButton(itemInfo.leftButton);
        holder.setCenterButton(itemInfo.centerButton);
        holder.setRightButton(itemInfo.rightButton);
    }

    @Override
    public int getItemCount() {
        return mItemInfos.size();
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {

        private onItemClickListener listener;

        private AppCompatButton mContent;
        private AppCompatTextView mContent2;
        private RelativeLayout mLayout;
        private AppCompatButton mLeft;
        private AppCompatButton mCenter;
        private AppCompatButton mRight;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.content);
            mContent2 = itemView.findViewById(R.id.content2);
            mLayout = itemView.findViewById(R.id.layout);
            mLeft = itemView.findViewById(R.id.left);
            mCenter = itemView.findViewById(R.id.center);
            mRight = itemView.findViewById(R.id.right);

            mContent.setOnClickListener(v -> click(AbstractListHelper.HelperItemInfo.BUTTON_TITLE));
            mLeft.setOnClickListener(v -> click(AbstractListHelper.HelperItemInfo.BUTTON_LEFT));
            mCenter.setOnClickListener(v -> click(AbstractListHelper.HelperItemInfo.BUTTON_CENTER));
            mRight.setOnClickListener(v -> click(AbstractListHelper.HelperItemInfo.BUTTON_RIGHT));
        }

        private void click(int button) {
            listener.onItemClick(itemView.getContext(), button);
        }

        public void reset() {
            mLayout.setVisibility(View.GONE);
            mLeft.setVisibility(View.GONE);
            mCenter.setVisibility(View.GONE);
            mRight.setVisibility(View.GONE);
        }

        public void setTitle(String text) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            mContent.setText(text);
            mContent2.setText(text);

            mContent.setVisibility(View.VISIBLE);
            mContent2.setVisibility(View.GONE);
        }

        public void setLeftButton(String text) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            mLayout.setVisibility(View.VISIBLE);
            mLeft.setText(text);
            mLeft.setVisibility(View.VISIBLE);

            mContent.setVisibility(View.GONE);
            mContent2.setVisibility(View.VISIBLE);
        }

        public void setCenterButton(String text) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            mLayout.setVisibility(View.VISIBLE);
            mCenter.setText(text);
            mCenter.setVisibility(View.VISIBLE);

            mContent.setVisibility(View.GONE);
            mContent2.setVisibility(View.VISIBLE);
        }

        public void setRightButton(String text) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            mLayout.setVisibility(View.VISIBLE);
            mRight.setText(text);
            mRight.setVisibility(View.VISIBLE);

            mContent.setVisibility(View.GONE);
            mContent2.setVisibility(View.VISIBLE);
        }

        public void setListener(onItemClickListener listener) {
            this.listener = listener;
        }

    }

    private interface onItemClickListener {
        void onItemClick(Context context, int button);
    }
}
