package com.kunminx.puremusic.bridge.request;

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

    public MutableLiveData<DownloadFile> getDownloadFileLiveData() {
        if (downloadFileLiveData == null) {
            downloadFileLiveData = new MutableLiveData<>();
        }
        return downloadFileLiveData;
    }

    public MutableLiveData<DownloadFile> getDownloadFileCanBeStoppedLiveData() {
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
        HttpRequestManager.getInstance().downloadFile(getDownloadFileLiveData());
    }

    public void requestCanBeStoppedDownloadFile() {
        UseCaseHandler.getInstance().execute(getCanBeStoppedUseCase(),
                new CanBeStoppedUseCase.RequestValues(getDownloadFileCanBeStoppedLiveData()),
                new UseCase.UseCaseCallback<CanBeStoppedUseCase.ResponseValue>() {
                    @Override
                    public void onSuccess(CanBeStoppedUseCase.ResponseValue response) {
                        getDownloadFileCanBeStoppedLiveData().setValue(response.getLiveData().getValue());
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
