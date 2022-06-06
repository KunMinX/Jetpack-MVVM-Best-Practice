package com.kunminx.puremusic.domain.request;

import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.message.Event;
import com.kunminx.architecture.domain.message.MutableEvent;
import com.kunminx.architecture.domain.usecase.UseCaseHandler;
import com.kunminx.puremusic.data.repository.DataRepository;
import com.kunminx.puremusic.domain.usecase.CanBeStoppedUseCase;

/**
 * 数据下载 Request
 * <p>
 * TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 event-ViewModel，
 * event-ViewModel 职责仅限于 "消息分发" 场景承担 "唯一可信源"。
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
 * Create by KunMinX at 20/03/18
 */
public class DownloadRequester extends ViewModel {

    private final MutableEvent<DataResult<CanBeStoppedUseCase.DownloadState>> mDownloadFileEvent = new MutableEvent<>();

    private final MutableEvent<DataResult<CanBeStoppedUseCase.DownloadState>> mDownloadFileCanBeStoppedEvent = new MutableEvent<>();

    private final CanBeStoppedUseCase mCanBeStoppedUseCase = new CanBeStoppedUseCase();

    //TODO tip 3：MutableEvent 应仅限 "唯一可信源" 内部使用，且只暴露 immutable Event 给 UI 层，
    //如此达成 "唯一可信源" 设计，也即通过 "访问控制权限" 实现 "读写分离"，

    //如这么说无体会，详见《吃透 LiveData 本质，享用可靠消息鉴权机制》解析。
    //https://xiaozhuanlan.com/topic/6017825943

    public Event<DataResult<CanBeStoppedUseCase.DownloadState>> getDownloadFileEvent() {
        return mDownloadFileEvent;
    }

    public Event<DataResult<CanBeStoppedUseCase.DownloadState>> getDownloadFileCanBeStoppedEvent() {
        return mDownloadFileCanBeStoppedEvent;
    }

    public CanBeStoppedUseCase getCanBeStoppedUseCase() {
        return mCanBeStoppedUseCase;
    }

    public void requestDownloadFile() {

        //TODO tip 4：为方便语义理解，此处直接将 DataResult 作为 LiveData value 回推给 UI 层，
        //而非 DataResult 泛型实体拆下来单独回推，如此
        //一方面使 UI 层有机会基于 DataResult 的 responseStatus 分别处理 "请求成功或失败" 情况下 UI 表现，
        //另一方面从语义上强调了 该结果是请求得来的只读数据，与 "可变状态" 形成明确区分，
        //从而方便团队开发人员自然而然遵循 "唯一可信源"/"单向数据流" 开发理念，规避消息同步一致性等不可预期错误。

        //如这么说无体会，详见《如何让同事爱上架构模式、少写 bug 多注释》中对 "只读数据" 和 "可变状态" 区别的解析。
        //https://xiaozhuanlan.com/topic/8204519736

        CanBeStoppedUseCase.DownloadState downloadState = new CanBeStoppedUseCase.DownloadState();

        //TODO Tip 5：lambda 语句只有一行时可简写，具体可结合实际情况选择和使用

        /*DataRepository.getInstance().downloadFile(downloadFile, dataResult -> {
            mDownloadFileEvent.postValue(dataResult);
        });*/

        DataRepository.getInstance().downloadFile(downloadState, mDownloadFileEvent::postValue);
    }

    //TODO tip 6：
    // 同是“下载”，我们不是在数据层分别写两个方法，
    // 而是遵循开闭原则，在 vm 和 数据层之间，插入一个 UseCase，来专门负责可叫停情况，
    // 除了开闭原则，使用 UseCase 还有个考虑就是避免内存泄漏，
    // 具体缘由可详见 https://xiaozhuanlan.com/topic/6257931840 评论区 15 楼

    public void requestCanBeStoppedDownloadFile() {
        UseCaseHandler.getInstance().execute(getCanBeStoppedUseCase(),
            new CanBeStoppedUseCase.RequestValues(), response -> {
                mDownloadFileCanBeStoppedEvent.setValue(response.getDataResult());
            });
    }
}
