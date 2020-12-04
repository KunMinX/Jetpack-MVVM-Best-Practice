package com.kunminx.puremusic.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kunminx.architecture.data.response.DataResult;
import com.kunminx.architecture.domain.request.BaseRequest;
import com.kunminx.architecture.domain.usecase.UseCaseHandler;
import com.kunminx.puremusic.data.bean.DownloadFile;
import com.kunminx.puremusic.data.repository.DataRepository;
import com.kunminx.puremusic.domain.usecase.CanBeStoppedUseCase;

/**
 * 数据下载 Request
 * <p>
 * TODO tip 1：Request 通常按业务划分
 * 一个项目中通常存在多个 Request 类，
 * 每个页面配备的 state-ViewModel 实例可根据业务需要持有多个不同的 Request 实例。
 * <p>
 * request 的职责仅限于 对数据请求的转发，不建议在此处理 UI 逻辑，
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

    private final MutableLiveData<DataResult<DownloadFile>> mDownloadFileLiveData = new MutableLiveData<>();

    private final MutableLiveData<DataResult<DownloadFile>> mDownloadFileCanBeStoppedLiveData = new MutableLiveData<>();

    private final CanBeStoppedUseCase mCanBeStoppedUseCase = new CanBeStoppedUseCase();

    //TODO tip 2：向 ui 层提供的 request LiveData，使用父类 LiveData 而不是 MutableLiveData，
    //如此达成了 "唯一可信源" 的设计，也即通过访问控制权限实现 "读写分离"（国外称 "单向数据流"），
    //从而确保了消息分发的一致性和可靠性。

    //如果这样说还不理解的话，详见《LiveData 鲜为人知的 身世背景 和 独特使命》中结合实际场合 对"唯一可信源"本质的解析。
    //https://xiaozhuanlan.com/topic/0168753249

    public LiveData<DataResult<DownloadFile>> getDownloadFileLiveData() {

        //TODO tip 3：与此同时，为了方便语义上的理解，故而直接将 DataResult 作为 LiveData value 回推给 UI 层，
        //而不是将 DataResult 的泛型实体拆下来单独回推，如此
        //一方面使 UI 层有机会基于 DataResult 的 responseStatus 来分别处理 请求成功或失败的情况下的 UI 表现，
        //另一方面从语义上强调了 该数据是请求得来的结果，是只读的，与 "可变状态" 形成明确的区分，
        //从而方便团队开发人员自然而然遵循 "唯一可信源"/"单向数据流" 的开发理念，规避消息同步一致性等不可预期的错误。

        //如果这样说还不理解的话，详见《如何让同事爱上架构模式、少写 bug 多注释》中对 "只读数据" 和 "可变状态" 的区分的解析。
        //https://xiaozhuanlan.com/topic/8204519736

        return mDownloadFileLiveData;
    }

    public LiveData<DataResult<DownloadFile>> getDownloadFileCanBeStoppedLiveData() {
        return mDownloadFileCanBeStoppedLiveData;
    }

    public CanBeStoppedUseCase getCanBeStoppedUseCase() {
        return mCanBeStoppedUseCase;
    }

    public void requestDownloadFile() {

        DownloadFile downloadFile = new DownloadFile();

        //TODO Tip：lambda 语句只有一行时可简写，具体可结合实际情况选择和使用

        /*DataRepository.getInstance().downloadFile(downloadFile, dataResult -> {
            mDownloadFileLiveData.postValue(dataResult);
        });*/

        DataRepository.getInstance().downloadFile(downloadFile, mDownloadFileLiveData::postValue);
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
