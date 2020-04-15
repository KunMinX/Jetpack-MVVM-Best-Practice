package com.kunminx.puremusic.bridge.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kunminx.architecture.data.usecase.UseCase;
import com.kunminx.architecture.data.usecase.UseCaseHandler;
import com.kunminx.puremusic.data.bean.DownloadFile;
import com.kunminx.puremusic.data.repository.HttpRequestManager;
import com.kunminx.puremusic.data.usecase.CanBeStoppedUseCase;

/**
 * Create by KunMinX at 20/03/18
 */
public class DownloadViewModel extends ViewModel {

    private MutableLiveData<DownloadFile> downloadFileLiveData;

    private MutableLiveData<DownloadFile> downloadFileCanBeStoppedLiveData;

    private CanBeStoppedUseCase mCanBeStoppedUseCase;

    //TODO tip 向 ui 层提供的 request LiveData，使用抽象的 LiveData 而不是 MutableLiveData
    // 如此是为了来自数据层的数据，在 ui 层中只读，以避免团队新手不可预期的误用

    public LiveData<DownloadFile> getDownloadFileLiveData() {
        if (downloadFileLiveData == null) {
            downloadFileLiveData = new MutableLiveData<>();
        }
        return downloadFileLiveData;
    }

    public LiveData<DownloadFile> getDownloadFileCanBeStoppedLiveData() {
        if (downloadFileCanBeStoppedLiveData == null) {
            downloadFileCanBeStoppedLiveData = new MutableLiveData<>();
        }
        return downloadFileCanBeStoppedLiveData;
    }

    public CanBeStoppedUseCase getCanBeStoppedUseCase() {
        if (mCanBeStoppedUseCase == null) {
            mCanBeStoppedUseCase = new CanBeStoppedUseCase();
        }
        return mCanBeStoppedUseCase;
    }

    public void requestDownloadFile() {
        HttpRequestManager.getInstance().downloadFile(downloadFileLiveData);
    }

    public void requestCanBeStoppedDownloadFile() {
        UseCaseHandler.getInstance().execute(getCanBeStoppedUseCase(),
                new CanBeStoppedUseCase.RequestValues(downloadFileCanBeStoppedLiveData),
                new UseCase.UseCaseCallback<CanBeStoppedUseCase.ResponseValue>() {
                    @Override
                    public void onSuccess(CanBeStoppedUseCase.ResponseValue response) {
                        downloadFileCanBeStoppedLiveData.setValue(response.getLiveData().getValue());
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
