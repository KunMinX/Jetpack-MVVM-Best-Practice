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
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.puremusic.data.usecase.base.UseCase;

import static com.kunminx.puremusic.data.usecase.TestUseCase.RequestValues;
import static com.kunminx.puremusic.data.usecase.TestUseCase.ResponseValue;


/**
 * UseCase 示例，单独服务于有 “叫停” 需求的业务
 * <p>
 * Create by KunMinX at 19/11/25
 */
public class TestUseCase extends UseCase<RequestValues, ResponseValue> implements DefaultLifecycleObserver {

    private boolean isActive;

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        isActive = owner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        isActive = owner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        //TODO do some work

        //访问数据层资源，在 UseCase 中处理带叫停性质的业务
        // HttpRequestManager.getInstance().getFreeMusic(null);

        if (!isActive) {
            //TODO 叫停
            return;
        }

        getUseCaseCallback().onSuccess(new ResponseValue("xxx"));

    }

    public static final class RequestValues implements UseCase.RequestValues {

        private int page;
        private int size;

        public RequestValues(int page, int size) {
            this.page = page;
            this.size = size;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private String result;

        public ResponseValue(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
