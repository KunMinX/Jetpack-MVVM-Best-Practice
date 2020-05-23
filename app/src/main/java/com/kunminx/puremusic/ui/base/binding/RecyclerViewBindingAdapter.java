/*
 * Copyright 2018-2020 KunMinX
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

package com.kunminx.puremusic.ui.base.binding;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kunminx.architecture.ui.adapter.BaseBindingAdapter;

import java.util.List;

/**
 * Create by KunMinX at 20/4/18
 */
public class RecyclerViewBindingAdapter {

    @BindingAdapter(value = {"setSpanCount"})
    public static void setSpanCount(RecyclerView recyclerView, int spanCount) {
        if (recyclerView != null) {
            if (recyclerView.getLayoutManager() == null || !(recyclerView.getLayoutManager() instanceof GridLayoutManager)) {
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount));
            } else {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(spanCount);
                }
            }
        }
    }

    @BindingAdapter(value = {"adapter", "refreshList"}, requireAll = false)
    public static void bindList(RecyclerView recyclerView, RecyclerView.Adapter adapter, List list) {
        if (recyclerView != null && list != null) {
            if (recyclerView.getAdapter() == null) {
                recyclerView.setAdapter(adapter);
            }

            ((BaseBindingAdapter) recyclerView.getAdapter()).setList(list);

            //TODO 此处可通过 diffUtil 进一步优化用户体验
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
