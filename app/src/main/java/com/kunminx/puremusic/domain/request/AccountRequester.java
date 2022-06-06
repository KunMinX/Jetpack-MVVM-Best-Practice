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

package com.kunminx.puremusic.domain.request;


import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.message.Event;
import com.kunminx.architecture.domain.message.MutableEvent;
import com.kunminx.puremusic.data.bean.User;
import com.kunminx.puremusic.data.repository.DataRepository;

import org.jetbrains.annotations.NotNull;

/**
 * 用户账户 Request
 * <p>
 * TODO tip 1：Request 通常按业务划分
 * 一个项目中通常存在多个 Request 类，
 * 每个页面配备的 state-ViewModel 实例可根据业务需要持有多个不同的 Request 实例。
 * <p>
 * request 的职责仅限于 "业务逻辑处理" 和 "Event 分发"，不建议在此处理 UI 逻辑，
 * UI 逻辑只适合在 Activity/Fragment 等视图控制器中完成，是 “数据驱动” 的一部分，
 * 将来升级到 Jetpack Compose 更是如此。
 * <p>
 * 如果这样说还不理解的话，详见《如何让同事爱上架构模式、少写 bug 多注释》的解析
 * https://xiaozhuanlan.com/topic/8204519736
 * <p>
 * Create by KunMinX at 20/04/26
 */
public class AccountRequester extends ViewModel
    implements DefaultLifecycleObserver {

    //TODO tip：👆👆👆 让 accountRequest 可观察页面生命周期，
    // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期的问题。

    private final MutableEvent<DataResult<String>> tokenEvent = new MutableEvent<>();

    //TODO tip 2：向 ui 层提供的 request LiveData，使用 "父类的 LiveData" 而不是 "Mutable 的 LiveData"，
    //如此达成了 "唯一可信源" 的设计，也即通过访问控制权限实现 "读写分离"，
    //并且进一步地，使用 ProtectedUnPeekLiveData 类，而不是 LiveData 类，
    //以此来确保消息分发的可靠一致，及 "事件" 场景下的防倒灌特性。

    //如果这样说还不理解的话，详见《关于 LiveData 本质，你看到了第几层》的铺垫和解析。
    //https://xiaozhuanlan.com/topic/6017825943

    public Event<DataResult<String>> getTokenEvent() {

        //TODO tip 3：与此同时，为了方便语义上的理解，故而直接将 DataResult 作为 LiveData value 回推给 UI 层，
        //而不是将 DataResult 的泛型实体拆下来单独回推，如此
        //一方面使 UI 层有机会基于 DataResult 的 responseStatus 来分别处理 请求成功或失败的情况下的 UI 表现，
        //另一方面从语义上强调了 该数据是请求得来的结果，是只读的，与 "可变状态" 形成明确的区分，
        //从而方便团队开发人员自然而然遵循 "唯一可信源"/"单向数据流" 的开发理念，规避消息同步一致性等不可预期的错误。

        //如果这样说还不理解的话，详见《如何让同事爱上架构模式、少写 bug 多注释》中对 "只读数据" 和 "可变状态" 的区分的解析。
        //https://xiaozhuanlan.com/topic/8204519736

        return tokenEvent;
    }

    public void requestLogin(User user) {

        //TODO Tip：lambda 语句只有一行时可简写，具体可结合实际情况选择和使用

        /*DataRepository.getInstance().login(user, dataResult -> {
            tokenLiveData.postValue(dataResult);
        });*/

        DataRepository.getInstance().login(user, tokenEvent::postValue);
    }

    private void cancelLogin() {
        DataRepository.getInstance().cancelLogin();
    }


    //TODO tip：让 accountRequest 可观察页面生命周期，
    // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期的问题。

    // 关于 Lifecycle 组件的存在意义，可详见《为你还原一个真实的 Jetpack Lifecycle》篇的解析
    // https://xiaozhuanlan.com/topic/3684721950

    @Override
    public void onStop(@NonNull @NotNull LifecycleOwner owner) {
        cancelLogin();
    }
}
