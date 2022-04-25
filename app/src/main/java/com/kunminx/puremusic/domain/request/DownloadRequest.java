package com.kunminx.puremusic.domain.request;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.kunminx.architecture.domain.usecase.UseCaseHandler;
import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData;
import com.kunminx.architecture.ui.callback.UnPeekLiveData;
import com.kunminx.puremusic.data.repository.DataRepository;
import com.kunminx.puremusic.domain.usecase.CanBeStoppedUseCase;

/**
 * 数据下载 Request
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
 * <p>
 * Create by KunMinX at 20/03/18
 */
public class DownloadRequest extends BaseRequest {

    private final UnPeekLiveData<DataResult<CanBeStoppedUseCase.DownloadState>> mDownloadFileLiveData = new UnPeekLiveData<>();

    private final UnPeekLiveData<DataResult<CanBeStoppedUseCase.DownloadState>> mDownloadFileCanBeStoppedLiveData = new UnPeekLiveData<>();

    private final CanBeStoppedUseCase mCanBeStoppedUseCase = new CanBeStoppedUseCase();

    //TODO tip 2：向 ui 层提供的 request LiveData，使用 "父类的 LiveData" 而不是 "Mutable 的 LiveData"，
    //如此达成了 "唯一可信源" 的设计，也即通过访问控制权限实现 "读写分离"，
    //并且进一步地，使用 ProtectedUnPeekLiveData 类，而不是 LiveData 类，
    //以此来确保消息分发的可靠一致，及 "事件" 场景下的防倒灌特性。

    //如果这样说还不理解的话，详见《关于 LiveData 本质，你看到了第几层》的铺垫和解析。
    //https://xiaozhuanlan.com/topic/6017825943

    public ProtectedUnPeekLiveData<DataResult<CanBeStoppedUseCase.DownloadState>> getDownloadFileLiveData() {

        //TODO tip 3：与此同时，为了方便语义上的理解，故而直接将 DataResult 作为 LiveData value 回推给 UI 层，
        //而不是将 DataResult 的泛型实体拆下来单独回推，如此
        //一方面使 UI 层有机会基于 DataResult 的 responseStatus 来分别处理 请求成功或失败的情况下的 UI 表现，
        //另一方面从语义上强调了 该数据是请求得来的结果，是只读的，与 "可变状态" 形成明确的区分，
        //从而方便团队开发人员自然而然遵循 "唯一可信源"/"单向数据流" 的开发理念，规避消息同步一致性等不可预期的错误。

        //如果这样说还不理解的话，详见《如何让同事爱上架构模式、少写 bug 多注释》中对 "只读数据" 和 "可变状态" 的区分的解析。
        //https://xiaozhuanlan.com/topic/8204519736

        return mDownloadFileLiveData;
    }

    public ProtectedUnPeekLiveData<DataResult<CanBeStoppedUseCase.DownloadState>> getDownloadFileCanBeStoppedLiveData() {
        return mDownloadFileCanBeStoppedLiveData;
    }

    public CanBeStoppedUseCase getCanBeStoppedUseCase() {
        return mCanBeStoppedUseCase;
    }

    public void requestDownloadFile() {

        CanBeStoppedUseCase.DownloadState downloadState = new CanBeStoppedUseCase.DownloadState();

        //TODO Tip：lambda 语句只有一行时可简写，具体可结合实际情况选择和使用

        /*DataRepository.getInstance().downloadFile(downloadFile, dataResult -> {
            mDownloadFileLiveData.postValue(dataResult);
        });*/

        DataRepository.getInstance().downloadFile(downloadState, mDownloadFileLiveData::postValue);
    }

    //TODO tip2：
    // 同样是“下载”，我不是在数据层分别写两个方法，
    // 而是遵循开闭原则，在 vm 和 数据层之间，插入一个 UseCase，来专门负责可叫停的情况，
    // 除了开闭原则，使用 UseCase 还有个考虑就是避免内存泄漏，
    // 具体缘由可详见 https://xiaozhuanlan.com/topic/6257931840 评论区 15 楼

    public void requestCanBeStoppedDownloadFile() {
        UseCaseHandler.getInstance().execute(getCanBeStoppedUseCase(),
            new CanBeStoppedUseCase.RequestValues(), response -> {
                mDownloadFileCanBeStoppedLiveData.setValue(response.getDataResult());
            });
    }
}
