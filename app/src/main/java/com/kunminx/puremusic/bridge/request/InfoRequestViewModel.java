/*
 * Copyright 2018-2019 KunMinX
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

package com.kunminx.puremusic.bridge.request;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.repository.HttpRequestManager;
import com.kunminx.puremusic.data.usecase.TestUseCase;
import com.kunminx.puremusic.data.usecase.base.UseCase;
import com.kunminx.puremusic.data.usecase.base.UseCaseHandler;

import java.util.List;

/**
 * Create by KunMinX at 19/11/2
 */
public class InfoRequestViewModel extends ViewModel {

    private MutableLiveData<List<LibraryInfo>> libraryLiveData;

    private TestUseCase mTestUseCase;

    private MutableLiveData<String> testXXX;

    public MutableLiveData<List<LibraryInfo>> getLibraryLiveData() {
        if (libraryLiveData == null) {
            libraryLiveData = new MutableLiveData<>();
        }
        return libraryLiveData;
    }

    public TestUseCase getTestUseCase() {
        if (mTestUseCase == null) {
            mTestUseCase = new TestUseCase();
        }
        return mTestUseCase;
    }

    public MutableLiveData<String> getTestXXX() {
        if (testXXX == null) {
            testXXX = new MutableLiveData<>();
        }
        return testXXX;
    }

    public void requestLibraryInfo() {
        HttpRequestManager.getInstance().getLibraryInfo(getLibraryLiveData());
    }

    public void requestTestXXX() {
        UseCaseHandler.getInstance().execute(getTestUseCase(),
                new TestUseCase.RequestValues(0, 0),
                new UseCase.UseCaseCallback<TestUseCase.ResponseValue>() {
                    @Override
                    public void onSuccess(TestUseCase.ResponseValue response) {
                        getTestXXX().setValue(response.getResult());
                    }

                    @Override
                    public void onError() {
                        //TODO 此处使用相应的 LiveDate 通知 UI 层
                    }
                });
    }
}
