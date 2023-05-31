package com.kunminx.puremusic.domain.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.puremusic.data.bean.DownloadState;
import com.kunminx.puremusic.data.repository.DataRepository;
import com.kunminx.puremusic.domain.event.DownloadEvent;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 数据下载 Request
 *
 * TODO tip 1：基于 "单一职责原则"，应将 Jetpack ViewModel 框架划分为 state-ViewModel 和 result-ViewModel，
 * result-ViewModel 作为领域层组件，提取和继承 Jetpack ViewModel 框架中 "作用域管理" 的能力，
 * 使业务实例能根据需要，被单个页面独享，或多个页面共享，
 *
 * mDownloadRequester = getFragmentScopeViewModel(DownloadRequester.class);
 * mGlobalDownloadRequester = getActivityScopeViewModel(DownloadRequester.class);
 *
 * result-ViewModel 作为领域层组件，职责仅限于 "业务逻辑处理 和 消息分发"，
 * UI 逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
 * 数据总是来自领域层业务逻辑的处理，并单向回推至 UI 层，在 UI 层中响应数据的变化，
 *
 * 故此应根据上述介绍明确区分 UI 逻辑和业务逻辑，勿将 UI 逻辑混为业务逻辑的一部分，
 * result-ViewModel 中应当只关注数据的生成，而不关注数据的使用，
 *
 * 如这么说无体会，详见《再回首 页面搭档 Jetpack ViewModel》解析。
 * https://xiaozhuanlan.com/topic/6018295743
 *
 * TODO tip 2：应顺应 "响应式编程"，做好 "单向数据流" 开发，
 *
 * 常见消息分发场景包括：数据请求，页面间通信等，
 * 数据请求由 Requester 负责，页面通信由 PageMessenger 负责，
 *
 * 所有事件都可交由 "鉴权中心" 在内部决策和处理，并统一分发结果给所有订阅者页面。
 *
 * 如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
 * https://xiaozhuanlan.com/topic/6017825943
 *
 *
 * TODO tip 3：Requester 通常按业务划分
 * 一个项目中通常存在多个 Requester 类，
 * 每个页面可根据业务需要持有多个不同 Requester 实例。
 *
 * requester 职责仅限于 "业务逻辑处理" 和 "消息分发"，不建议在此处理 UI 逻辑，
 * UI 逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
 * 数据总是来自领域层业务逻辑的处理，并单向回推至 UI 层，在 UI 层中响应数据的变化（也即处理 UI 逻辑），
 * 换言之，UI 逻辑只适合在 Activity/Fragment 等视图控制器中编写，将来升级到 Jetpack Compose 更是如此。
 *
 *
 * 如这么说无体会，详见《Jetpack MVVM 分层设计解析》解析
 * https://xiaozhuanlan.com/topic/6741932805
 *
 *
 * TODO:Note 2022.07.04
 * 可于领域层通过 MVI-Dispatcher 实现可靠的消息回推，
 * 通过消息队列、引用计数等设计，确保 "消息都能被消费，且只消费一次"，
 * 通过内聚设计，彻底杜绝 mutable 滥用等问题，
 * 鉴于本项目场景难发挥 MVI-Dispatcher 潜能，故目前仅以改造 DownloadRequester 和 SharedViewModel 为例，
 * 通过对比 SharedViewModel 和 PageMessenger 易得，后者可简洁优雅实现可靠一致的消息分发，
 *
 * 具体可参见专为 MVI-Dispatcher 编写 MVI 使用案例：
 *
 * https://github.com/KunMinX/MVI-Dispatcher
 *
 *
 * Create by KunMinX at 20/03/18
 */
public class DownloadRequester extends MviDispatcher<DownloadEvent> {

    private Disposable mDisposable;

    //TODO Tip: result-ViewModel 作为领域层组件，提取和继承 Jetpack ViewModel 框架中 "作用域管理" 的能力，
    // 使业务实例能根据需要，被单个页面独享，或多个页面共享，例如：
    //
    // mDownloadRequester = getFragmentScopeViewModel(DownloadRequester.class);
    // mGlobalDownloadRequester = getActivityScopeViewModel(DownloadRequester.class);
    //
    // 在本案例中，fragment 级作用域的 mDownloadRequester 只走 DownloadEvent.EVENT_DOWNLOAD 业务，
    // Activity 级作用域的 mGlobalDownloadRequester 只走 DownloadEvent.EVENT_DOWNLOAD_GLOBAL 业务，
    // 二者都为 SearchFragment 所持有，用于对比不同作用域的效果，

    @Override
    protected void onHandle(DownloadEvent event) {
        switch (event.eventId) {
            case DownloadEvent.EVENT_DOWNLOAD:
                Observable.create(DataRepository.getInstance().downloadFile())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDisposable = d;
                        }
                        @Override
                        public void onNext(Integer integer) {
                            sendResult(event.copy(new DownloadState(true, integer)));
                        }
                        @Override
                        public void onError(Throwable e) {

                        }
                        @Override
                        public void onComplete() {

                        }
                    });
                break;
            case DownloadEvent.EVENT_DOWNLOAD_GLOBAL:
                Observable.create(DataRepository.getInstance().downloadFile())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        sendResult(event.copy(new DownloadState(true, integer)));
                    });
                break;
        }
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        super.onStop(owner);
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }
}
