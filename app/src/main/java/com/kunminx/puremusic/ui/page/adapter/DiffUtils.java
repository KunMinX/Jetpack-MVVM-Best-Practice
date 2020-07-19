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

package com.kunminx.puremusic.ui.page.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.bean.TestAlbum;

/**
 * Create by KunMinX at 2020/7/19
 */
public class DiffUtils {

    private DiffUtil.ItemCallback<LibraryInfo> mLibraryInfoItemCallback;

    private DiffUtil.ItemCallback<TestAlbum.TestMusic> mTestMusicItemCallback;

    private DiffUtils() {
    }

    private static DiffUtils sDiffUtils = new DiffUtils();

    public static DiffUtils getInstance() {
        return sDiffUtils;
    }

    public DiffUtil.ItemCallback<LibraryInfo> getLibraryInfoItemCallback() {
        if (mLibraryInfoItemCallback == null) {
            mLibraryInfoItemCallback = new DiffUtil.ItemCallback<LibraryInfo>() {
                @Override
                public boolean areItemsTheSame(@NonNull LibraryInfo oldItem, @NonNull LibraryInfo newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull LibraryInfo oldItem, @NonNull LibraryInfo newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle());
                }
            };
        }
        return mLibraryInfoItemCallback;
    }

    public DiffUtil.ItemCallback<TestAlbum.TestMusic> getTestMusicItemCallback() {
        if (mTestMusicItemCallback == null) {
            mTestMusicItemCallback = new DiffUtil.ItemCallback<TestAlbum.TestMusic>() {
                @Override
                public boolean areItemsTheSame(@NonNull TestAlbum.TestMusic oldItem, @NonNull TestAlbum.TestMusic newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull TestAlbum.TestMusic oldItem, @NonNull TestAlbum.TestMusic newItem) {
                    return oldItem.getMusicId().equals(newItem.getMusicId());
                }
            };
        }
        return mTestMusicItemCallback;
    }
}
