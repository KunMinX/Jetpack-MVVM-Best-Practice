/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.architecture.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KunMinX
 * Create at 2018/6/30
 */
public abstract class BaseDataBindingAdapter<M, B extends ViewDataBinding> extends ListAdapter<M, RecyclerView.ViewHolder> {

    protected Context mContext;

    private OnItemClickListener<M> mOnItemClickListener;
    private OnItemLongClickListener<M> mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<M> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public BaseDataBindingAdapter(Context context, @NonNull DiffUtil.ItemCallback<M> diffCallback) {
        super(diffCallback);
        this.mContext = context;
    }

    @Override
    public void submitList(@Nullable List<M> list) {
        super.submitList(list, () -> {
            super.submitList(list == null ? new ArrayList<>() : new ArrayList<>(list));
        });
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(this.mContext), this.getLayoutResId(viewType), parent, false);
        BaseBindingViewHolder holder = new BaseBindingViewHolder(binding.getRoot());
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = holder.getBindingAdapterPosition();
                mOnItemClickListener.onItemClick(getItem(position), position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnItemLongClickListener != null) {
                int position = holder.getBindingAdapterPosition();
                mOnItemLongClickListener.onItemLongClick(getItem(position), position);
                return true;
            }
            return false;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        B binding = DataBindingUtil.getBinding(holder.itemView);
        this.onBindItem(binding, getItem(position), holder);
        if (binding != null) {
            binding.executePendingBindings();
        }
    }

    protected abstract @LayoutRes
    int getLayoutResId(int viewType);

    /**
     * 注意：
     * RecyclerView 中的数据有位置改变（比如删除）时一般不会重新调用 onBindViewHolder() 方法，除非这个元素不可用。
     * 为了实时获取元素的位置，我们通过 ViewHolder.getBindingAdapterPosition() 方法。
     *
     * @param binding .
     * @param item    .
     * @param holder  .
     */
    protected abstract void onBindItem(B binding, M item, RecyclerView.ViewHolder holder);

    public static class BaseBindingViewHolder extends RecyclerView.ViewHolder {
        BaseBindingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<M> {
        void onItemClick(M item, int position);
    }

    public interface OnItemLongClickListener<M> {
        void onItemLongClick(M item, int position);
    }
}