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

package com.kunminx.puremusic.bridge.request;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.data.repository.HttpRequestManager;

/**
 * 音乐资源  RequestViewModel
 *
 * TODO tip：RequestViewModel 通常按业务划分
 * 一个项目中通常存在多个 RequestViewModel
 *
 * requestViewModel 的职责仅限于 数据请求，不建议在此处理 UI 逻辑，
 * UI 逻辑只适合在 Activity/Fragment 等视图控制器中完成，是 “数据驱动” 的一部分，
 * 将来升级到 Jetpack Compose 更是如此。
 *
 * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/6257931840
 *
 * Create by KunMinX at 19/10/29
 */
public class MusicRequestViewModel extends ViewModel {

    private MutableLiveData<TestAlbum> freeMusicsLiveData;

    public MutableLiveData<TestAlbum> getFreeMusicsLiveData() {
        if (freeMusicsLiveData == null) {
            freeMusicsLiveData = new MutableLiveData<>();
        }
        return freeMusicsLiveData;
    }

    public void requestFreeMusics() {
        HttpRequestManager.getInstance().getFreeMusic(getFreeMusicsLiveData());
    }
}
