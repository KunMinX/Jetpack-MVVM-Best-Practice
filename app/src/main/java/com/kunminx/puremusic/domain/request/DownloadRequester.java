package com.kunminx.puremusic.domain.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.puremusic.data.bean.DownloadState;
import com.kunminx.puremusic.data.repository.DataRepository;
import com.kunminx.puremusic.domain.event.DownloadEvent;
import com.kunminx.puremusic.ui.page.helper.DefaultInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 数据下载 Request
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
 *     if(result.show)
 *       panel.visible(VISIBLE)
 *     else
 *       panel.visible(GONE)
 *     tvTitle.setText(result.title)
 *     tvContent.setText(result.content)
 *   }
 * }
 *
 * 如这么说无体会，详见《Jetpack MVVM 分层设计》解析
 * https://xiaozhuanlan.com/topic/6741932805
 *
 *
 * Create by KunMinX at 20/03/18
 */
public class DownloadRequester extends MviDispatcher<DownloadEvent> {

    private Disposable mDisposable;

    //TODO Tip 2：基于 "单一职责原则"，宜将 Jetpack ViewModel 框架划分为 state-ViewModel 和 result-ViewModel，
    // result-ViewModel 作为领域层组件，仅提取和继承 Jetpack ViewModel 框架中 "作用域管理" 的能力，
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
                DataRepository.getInstance().downloadFile()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultInterface.Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mDisposable = d;
                        }
                        @Override
                        public void onNext(Integer integer) {
                            sendResult(event.copy(new DownloadState(true, integer)));
                        }
                    });
                break;
            case DownloadEvent.EVENT_DOWNLOAD_GLOBAL:
                DataRepository.getInstance().downloadFile()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((DefaultInterface.Observer<Integer>) integer -> {
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
