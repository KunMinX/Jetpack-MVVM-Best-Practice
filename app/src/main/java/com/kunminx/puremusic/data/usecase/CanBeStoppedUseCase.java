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

package com.kunminx.puremusic.data.usecase;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.data.usecase.UseCase;
import com.kunminx.puremusic.data.bean.DownloadFile;
import com.kunminx.puremusic.data.repository.HttpRequestManager;

import static com.kunminx.puremusic.data.usecase.CanBeStoppedUseCase.RequestValues;
import static com.kunminx.puremusic.data.usecase.CanBeStoppedUseCase.ResponseValue;


/**
 * UseCase 示例，实现 LifeCycle 接口，单独服务于 有 “叫停” 需求 的业务
 * <p>
 * Create by KunMinX at 19/11/25
 */
public class CanBeStoppedUseCase extends UseCase<RequestValues, ResponseValue> implements DefaultLifecycleObserver {

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        if (getRequestValues() != null && getRequestValues().liveData != null) {
            DownloadFile downloadFile = getRequestValues().liveData.getValue();
            downloadFile.setForgive(true);
            getRequestValues().liveData.setValue(downloadFile);
            getUseCaseCallback().onSuccess(new ResponseValue(getRequestValues().liveData));
        }
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        //访问数据层资源，在 UseCase 中处理带叫停性质的业务

        HttpRequestManager.getInstance().downloadFile(requestValues.liveData);

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private MutableLiveData<DownloadFile> liveData;

        public RequestValues(MutableLiveData<DownloadFile> liveData) {
            this.liveData = liveData;
        }

        public MutableLiveData<DownloadFile> getLiveData() {
            return liveData;
        }

        public void setLiveData(MutableLiveData<DownloadFile> liveData) {
            this.liveData = liveData;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private MutableLiveData<DownloadFile> liveData;

        public ResponseValue(MutableLiveData<DownloadFile> liveData) {
            this.liveData = liveData;
        }

        public MutableLiveData<DownloadFile> getLiveData() {
            return liveData;
        }

        public void setLiveData(MutableLiveData<DownloadFile> liveData) {
            this.liveData = liveData;
        }
    }
}
