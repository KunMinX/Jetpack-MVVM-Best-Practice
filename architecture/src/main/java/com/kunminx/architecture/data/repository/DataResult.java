/*
 *
 *  * Copyright 2018-2020 KunMinX
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.kunminx.architecture.data.repository;

import com.kunminx.architecture.domain.manager.NetState;

/**
 * TODO: 专用于数据层返回结果给 domain 层或 ViewModel 用，原因如下：
 * <p>
 * livedata 是专用于页面开发的、用于解决生命周期安全问题的组件，
 * 有时候数据并非一定是通过 livedata 来分发给页面，也可能是通过别的组件去通知给非页面的东西，
 * 这时候 repo 方法中内定通过 livedata 分发就不太合适，不如一开始就规定不在数据层通过 livedata 返回结果。
 * <p>
 * Create by KunMinX at 2020/7/20
 */
public class DataResult<T> {

    private T mT;
    private Result<T> mResult;
    private NetState mNetState;

    public DataResult() {
    }

    public DataResult(Result<T> result) {
        mResult = result;
    }

    public T getResult() {
        return mT;
    }

    public NetState getNetState() {
        return mNetState;
    }

    public void setResult(T t, NetState netState) {
        if (mResult == null) {
            throw new NullPointerException("Need to instantiate the Result<T> first ...");
        }
        if (t == null) {
            throw new NullPointerException("Need to instantiate the T first ...");
        }
        if (netState == null) {
            throw new NullPointerException("Need to instantiate the NetState first ...");
        }

        mT = t;
        mNetState = netState;
        mResult.onResult(t, netState);
    }

    public void onObserve(Result<T> result) {
        if (result == null) {
            throw new NullPointerException("Need to instantiate the Result<T> first ...");
        }
        if (mResult == null) {
            mResult = result;
        }
    }

    public interface Result<T> {
        void onResult(T t, NetState netState);
    }
}
