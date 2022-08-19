/*
 *
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
 *
 */

package com.kunminx.architecture.data.response;

/**
 * TODO: 专用于数据层返回结果至 domain 层或 ViewModel，原因如下：
 * <p>
 * liveData 专用于页面开发、解决生命周期安全问题，
 * 有时数据并非通过 liveData 分发给页面，也可是通过其他方式通知非页面组件，
 * 这时 repo 方法中内定通过 liveData 分发便不合适，不如一开始就规定不在数据层通过 liveData 返回结果。
 * <p>
 * 如这么说无体会，详见《如何让同事爱上架构模式、少写 bug 多注释》解析
 * https://xiaozhuanlan.com/topic/8204519736
 * <p>
 * Create by KunMinX at 2020/7/20
 */
public class DataResult<T> {

    private final T mEntity;
    private final ResponseStatus mResponseStatus;

    public DataResult(T entity, ResponseStatus responseStatus) {
        mEntity = entity;
        mResponseStatus = responseStatus;
    }

    public DataResult(T entity) {
        mEntity = entity;
        mResponseStatus = new ResponseStatus();
    }

    public T getResult() {
        return mEntity;
    }

    public ResponseStatus getResponseStatus() {
        return mResponseStatus;
    }

    public interface Result<T> {
        void onResult(DataResult<T> dataResult);
    }
}

