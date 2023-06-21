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
import com.kunminx.architecture.data.response.ResponseStatus;
import com.kunminx.architecture.data.response.ResultSource;
import com.kunminx.architecture.domain.message.MutableResult;
import com.kunminx.architecture.domain.message.Result;
import com.kunminx.architecture.domain.request.Requester;
import com.kunminx.puremusic.data.bean.User;
import com.kunminx.puremusic.data.repository.DataRepository;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户账户 Request
 *
 * TODO tip 1：让 UI 和业务分离，让数据总是从生产者流向消费者
 *
 * UI逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
 * "领域层组件" 作为数据的生产者，职责应仅限于 "请求调度 和 结果分发"，
 *
 * 换言之，"领域层组件" 中应当只关注数据的生成，而不关注数据的使用，
 * 改变 UI 状态的逻辑代码，只应在表现层页面中编写、在 Observer 回调中响应数据的变化，
 * 将来升级到 Jetpack Compose 更是如此，
 *
 * Activity {
 *  onCreate(){
 *   vm.livedata.observe { result->
 *     panel.visible(result.show ? VISIBLE : GONE)
 *     tvTitle.setText(result.title)
 *     tvContent.setText(result.content)
 *   }
 * }
 *
 * 如这么说无体会，详见《Jetpack MVVM 分层设计》解析
 * https://xiaozhuanlan.com/topic/6741932805
 *
 *
 * Create by KunMinX at 20/04/26
 */
public class AccountRequester extends Requester implements DefaultLifecycleObserver {

    //TODO tip 3：👆👆👆 让 accountRequest 可观察页面生命周期，
    // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期问题。

    private final MutableResult<DataResult<String>> tokenResult = new MutableResult<>();

    //TODO tip 4：应顺应 "响应式编程"，做好 "单向数据流" 开发，
    // MutableResult 应仅限 "鉴权中心" 内部使用，且只暴露 immutable Result 给 UI 层，
    // 通过 "读写分离" 实现数据从 "领域层" 到 "表现层" 的单向流动，

    //如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
    //https://xiaozhuanlan.com/topic/6017825943

    public Result<DataResult<String>> getTokenResult() {
        return tokenResult;
    }

    //TODO tip 5：模拟可取消的登录请求：
    //
    // 配合可观察页面生命周期的 accountRequest，
    // 从而在页面即将退出、且登录请求由于网络延迟尚未完成时，
    // 及时通知数据层取消本次请求，以避免资源浪费和一系列不可预期的问题。

    private Disposable mDisposable;

    //TODO tip 6: requester 作为数据的生产者，职责应仅限于 "请求调度 和 结果分发"，
    //
    // 换言之，此处只关注数据的生成和回推，不关注数据的使用，
    // 改变 UI 状态的逻辑代码，只应在表现层页面中编写，例如 Jetpack Compose 的使用，

    public void requestLogin(User user) {
        DataRepository.getInstance().login(user).subscribe(new Observer<DataResult<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }
            @Override
            public void onNext(DataResult<String> dataResult) {
                tokenResult.postValue(dataResult);
            }
            @Override
            public void onError(Throwable e) {
                tokenResult.postValue(new DataResult<>(null,
                    new ResponseStatus(e.getMessage(), false, ResultSource.NETWORK)));
            }
            @Override
            public void onComplete() {
                mDisposable = null;
            }
        });
    }

    public void cancelLogin() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
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
