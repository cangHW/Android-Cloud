package com.proxy.androidcloud.detail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proxy.androidcloud.R;
import com.proxy.androidcloud.helper.AbstractHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/08/13  10:40 PM
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    private List<AbstractHelper.HelperItemInfo> mItemInfos = new ArrayList<>();
    private AbstractHelper mHelper;


    public void setData(AbstractHelper helper) {
        this.mHelper = helper;
        this.mItemInfos.addAll(mHelper.createItems());
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        AbstractHelper.HelperItemInfo itemInfo = mItemInfos.get(position);

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

        private TextView mContent;
        private RelativeLayout mLayout;
        private Button mLeft;
        private Button mCenter;
        private Button mRight;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.content);
            mLayout = itemView.findViewById(R.id.layout);
            mLeft = itemView.findViewById(R.id.left);
            mCenter = itemView.findViewById(R.id.center);
            mRight = itemView.findViewById(R.id.right);

            mContent.setOnClickListener(v -> click(AbstractHelper.HelperItemInfo.BUTTON_TITLE));
            mLeft.setOnClickListener(v -> click(AbstractHelper.HelperItemInfo.BUTTON_LEFT));
            mCenter.setOnClickListener(v -> click(AbstractHelper.HelperItemInfo.BUTTON_CENTER));
            mRight.setOnClickListener(v -> click(AbstractHelper.HelperItemInfo.BUTTON_RIGHT));
        }

        private void click(int button) {
            listener.onItemClick(itemView.getContext(), button);
        }

        public void reset() {
            mContent.setClickable(true);
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
        }

        public void setLeftButton(String text) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            mContent.setClickable(false);
            mLayout.setVisibility(View.VISIBLE);
            mLeft.setText(text);
            mLeft.setVisibility(View.VISIBLE);
        }

        public void setCenterButton(String text) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            mContent.setClickable(false);
            mLayout.setVisibility(View.VISIBLE);
            mCenter.setText(text);
            mCenter.setVisibility(View.VISIBLE);
        }

        public void setRightButton(String text) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            mContent.setClickable(false);
            mLayout.setVisibility(View.VISIBLE);
            mRight.setText(text);
            mRight.setVisibility(View.VISIBLE);
        }

        public void setListener(onItemClickListener listener) {
            this.listener = listener;
        }

    }

    private interface onItemClickListener {
        void onItemClick(Context context, int button);
    }
}
