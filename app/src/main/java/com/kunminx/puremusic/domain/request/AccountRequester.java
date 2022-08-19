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
import com.kunminx.architecture.domain.message.MutableResult;
import com.kunminx.architecture.domain.message.Result;
import com.kunminx.puremusic.data.bean.User;
import com.kunminx.puremusic.data.repository.DataRepository;

import org.jetbrains.annotations.NotNull;

/**
 * 用户账户 Request
 * <p>
 * TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 Result-ViewModel，
 * Result-ViewModel 职责仅限于 "消息分发" 场景承担 "唯一可信源"。
 * <p>
 * 常见消息分发场景包括：数据请求，页面间通信等，
 * 数据请求 Requester 负责，页面通信 Messenger 负责，
 * <p>
 * 所有事件都可交由 "唯一可信源" 在内部决策和处理，并统一分发结果给所有订阅者页面。
 * <p>
 * 如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
 * https://xiaozhuanlan.com/topic/6017825943
 * <p>
 * <p>
 * TODO tip 2：Requester 通常按业务划分
 * 一个项目中通常存在多个 Requester 类，
 * 每个页面可根据业务需要持有多个不同 Requester 实例。
 * <p>
 * requester 职责仅限于 "业务逻辑处理" 和 "消息分发"，不建议在此处理 UI 逻辑，
 * UI 逻辑只适合在 Activity/Fragment 等视图控制器中完成，是 “数据驱动” 一部分，
 * 将来升级到 Jetpack Compose 更是如此。
 * <p>
 * 如这么说无体会，详见《如何让同事爱上架构模式、少写 bug 多注释》解析
 * https://xiaozhuanlan.com/topic/8204519736
 * <p>
 * <p>
 * Create by KunMinX at 20/04/26
 */
public class AccountRequester extends ViewModel implements DefaultLifecycleObserver {

    //TODO tip 3：👆👆👆 让 accountRequest 可观察页面生命周期，
    // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期问题。

    private final MutableResult<DataResult<String>> tokenResult = new MutableResult<>();

    //TODO tip 4：MutableResult 应仅限 "唯一可信源" 内部使用，且只暴露 immutable Result 给 UI 层，
    //如此达成 "唯一可信源" 设计，也即通过 "访问控制权限" 实现 "读写分离"，

    //如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
    //https://xiaozhuanlan.com/topic/6017825943

    public Result<DataResult<String>> getTokenResult() {
        return tokenResult;
    }

    public void requestLogin(User user) {

        //TODO tip 5：为方便语义理解，此处直接将 DataResult 作为 LiveData value 回推给 UI 层，
        //而非 DataResult 泛型实体拆下来单独回推，如此
        //一方面使 UI 层有机会基于 DataResult 的 responseStatus 分别处理 "请求成功或失败" 情况下 UI 表现，
        //另一方面从语义上强调了 该结果是请求得来的只读数据，与 "可变状态" 形成明确区分，
        //从而方便团队开发人员自然而然遵循 "唯一可信源"/"单向数据流" 开发理念，规避消息同步一致性等不可预期错误。

        //如这么说无体会，详见《如何让同事爱上架构模式、少写 bug 多注释》中对 "只读数据" 和 "可变状态" 区别的解析。
        //https://xiaozhuanlan.com/topic/8204519736

        //TODO Tip 6：lambda 语句只有一行时可简写，具体可结合实际情况选择和使用

        /*DataRepository.getInstance().login(user, dataResult -> {
            tokenResult.postValue(dataResult);
        });*/

        DataRepository.getInstance().login(user, tokenResult::postValue);
    }

    private void cancelLogin() {
        DataRepository.getInstance().cancelLogin();
    }


    //TODO tip 7：让 accountRequest 可观察页面生命周期，
    // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期问题。

    // 关于 Lifecycle 组件的存在意义，详见《为你还原一个真实的 Jetpack Lifecycle》解析
    // https://xiaozhuanlan.com/topic/3684721950

    @Override
    public void onStop(@NonNull @NotNull LifecycleOwner owner) {
        cancelLogin();
    }
}
