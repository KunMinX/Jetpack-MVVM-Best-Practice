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

import android.annotation.SuppressLint;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.message.MutableResult;
import com.kunminx.architecture.domain.message.Result;
import com.kunminx.architecture.domain.request.Requester;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.repository.DataRepository;

import java.util.List;

/**
 * 信息列表 Request
 * <p>
 * TODO tip 1：让 UI 和业务分离，让数据总是从生产者流向消费者
 * <p>
 * UI逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
 * "领域层组件" 作为数据的生产者，职责应仅限于 "请求调度 和 结果分发"，
 * <p>
 * 换言之，"领域层组件" 中应当只关注数据的生成，而不关注数据的使用，
 * 改变 UI 状态的逻辑代码，只应在表现层页面中编写、在 Observer 回调中响应数据的变化，
 * 将来升级到 Jetpack Compose 更是如此，
 * <p>
 * Activity {
 * onCreate(){
 * vm.livedata.observe { result->
 * panel.visible(result.show ? VISIBLE : GONE)
 * tvTitle.setText(result.title)
 * tvContent.setText(result.content)
 * }
 * }
 * <p>
 * TODO tip 2：Requester 通常按业务划分
 * 一个项目中通常可存在多个 Requester 类，
 * 每个页面可根据业务需要，持有多个不同 Requester 实例，
 * 通过 PublishSubject 回推一次性消息，并在表现层 Observer 中分流，
 * 对于 Event，直接执行，对于 State，使用 BehaviorSubject 通知 View 渲染和兜着状态，
 * <p>
 * Activity {
 * onCreate(){
 * request.observe {result ->
 * is Event ? -> execute one time
 * is State ? -> BehaviorSubject setValue and notify
 * }
 * }
 * <p>
 * 如这么说无体会，详见《Jetpack MVVM 分层设计解析》解析
 * https://xiaozhuanlan.com/topic/6741932805
 * <p>
 * <p>
 * Create by KunMinX at 19/11/2
 */
public class InfoRequester extends Requester {

    private final MutableResult<DataResult<List<LibraryInfo>>> mLibraryResult = new MutableResult<>();

    //TODO tip 4：应顺应 "响应式编程"，做好 "单向数据流" 开发，
    // MutableResult 应仅限 "鉴权中心" 内部使用，且只暴露 immutable Result 给 UI 层，
    // 通过 "读写分离" 实现数据从 "领域层" 到 "表现层" 的单向流动，

    //如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
    //https://xiaozhuanlan.com/topic/6017825943

    public Result<DataResult<List<LibraryInfo>>> getLibraryResult() {
        return mLibraryResult;
    }

    //TODO tip 5: requester 作为数据的生产者，职责应仅限于 "请求调度 和 结果分发"，
    //
    // 换言之，此处只关注数据的生成和回推，不关注数据的使用，
    // 改变 UI 状态的逻辑代码，只应在表现层页面中编写，例如 Jetpack Compose 的使用，

    @SuppressLint("CheckResult")
    public void requestLibraryInfo() {
        if (mLibraryResult.getValue() == null)
            DataRepository.getInstance().getLibraryInfo().subscribe(mLibraryResult::setValue);
    }
}
