package com.kunminx.puremusic.domain.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.puremusic.data.repository.DataRepository;
import com.kunminx.puremusic.domain.event.DownloadEvent;

/**
 * 数据下载 Request
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
 * TODO:Note 2022.07.04
 * 可于领域层通过 MVI-Dispatcher 实现成熟形态 "唯一可信源"，
 * 使支持 LiveData 连续发送多种类事件 + 彻底消除 mutable 样板代码 + 彻底杜绝团队新手 LiveData.setValue 误用滥用，
 * 鉴于本项目场景难发挥 MVI-Dispatcher 潜能，故目前仅以改造 SharedViewModel 为例，
 * 通过对比 SharedViewModel 和 PageMessenger 易得，后者可简洁优雅实现可靠一致消息分发，
 * <p>
 * 具体可参见专为 MVI-Dispatcher 唯一可信源编写 MVI 使用案例：
 * <p>
 * https://github.com/KunMinX/MVI-Dispatcher
 * <p>
 * <p>
 * Create by KunMinX at 20/03/18
 */
public class DownloadRequester extends MviDispatcher<DownloadEvent> {

    private boolean pageStopped;

    @Override
    protected void onHandle(DownloadEvent event) {
        switch (event.eventId) {
            case DownloadEvent.EVENT_DOWNLOAD:
                DataRepository.getInstance().downloadFile(event.result.downloadState, dataResult -> {
                    if (pageStopped) {
                        event.result.downloadState.isForgive = true;
                        event.result.downloadState.file = null;
                        event.result.downloadState.progress = 0;
                    }
                    sendResult(event);
                });
                break;
            case DownloadEvent.EVENT_DOWNLOAD_GLOBAL:
                DataRepository.getInstance().downloadFile(event.result.downloadState, dataResult -> {
                    sendResult(event);
                });
                break;
        }
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        super.onCreate(owner);
        pageStopped = false;
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        super.onStop(owner);
        pageStopped = true;
    }
}
