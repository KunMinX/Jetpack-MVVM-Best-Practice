/*
 * Copyright 2018-2019 KunMinX
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
import androidx.recyclerview.widget.RecyclerView;

import com.kunminx.architecture.ui.adapter.BaseBindingAdapter;

import java.util.List;

/**
 * Create by KunMinX at 20/4/18
 */
public class RecyclerViewBindingAdapter {

    @BindingAdapter(value = {"adapter"})
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        if (recyclerView != null && adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter(value = {"refreshList"})
    public static void refreshList(RecyclerView recyclerView, List list) {
        if (list != null) {
            ((BaseBindingAdapter) recyclerView.getAdapter()).setList(list);
            
            //TODO 此处可通过 diffUtil 进一步优化用户体验
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
