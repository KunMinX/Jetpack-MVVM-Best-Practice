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

package com.kunminx.puremusic.domain.usecase;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.usecase.UseCase;
import com.kunminx.puremusic.data.bean.DownloadState;


/**
 * UseCase 示例，实现 LifeCycle 接口，单独服务于 有 “叫停” 需求 的业务
 * <p>
 * TODO tip：
 * 同样是“下载”，我不是在数据层分别写两个方法，
 * 而是遵循开闭原则，在 ViewModel 和 数据层之间，插入一个 UseCase，来专门负责可叫停的情况，
 * 除了开闭原则，使用 UseCase 还有个考虑就是避免内存泄漏，
 * 具体缘由可详见 https://xiaozhuanlan.com/topic/6257931840 评论区 15 楼
 * 以及《如何让同事爱上架构模式、少写 bug 多注释》的解析
 * https://xiaozhuanlan.com/topic/8204519736
 * <p>
 *
 * 现已更换为在 MVI-Dispatcher 中处理，具体可参见 DownloadRequest 实现
 *
 *
 * Create by KunMinX at 19/11/25
 */
@Deprecated
public class CanBeStoppedUseCase extends UseCase<CanBeStoppedUseCase.RequestValues,
    CanBeStoppedUseCase.ResponseValue> implements DefaultLifecycleObserver {

    private final DownloadState downloadState = new DownloadState();

    //TODO tip：让 CanBeStoppedUseCase 可观察页面生命周期，
    // 从而在页面即将退出、且下载请求尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期的问题。

    // 关于 Lifecycle 组件的存在意义，可详见《为你还原一个真实的 Jetpack Lifecycle》篇的解析
    // https://xiaozhuanlan.com/topic/3684721950

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        if (getRequestValues() != null) {
            downloadState.isForgive = true;
            downloadState.file = null;
            downloadState.progress = 0;
            getUseCaseCallback().onError();
        }
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        //访问数据层资源，在 UseCase 中处理带叫停性质的业务

//        DataRepository.getInstance().downloadFile(downloadState, dataResult -> {
//            getUseCaseCallback().onSuccess(new ResponseValue(dataResult));
//        });
    }

    public static final class RequestValues implements UseCase.RequestValues {

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final DataResult<DownloadState> mDataResult;

        public ResponseValue(DataResult<DownloadState> dataResult) {
            mDataResult = dataResult;
        }

        public DataResult<DownloadState> getDataResult() {
            return mDataResult;
        }
    }
}
