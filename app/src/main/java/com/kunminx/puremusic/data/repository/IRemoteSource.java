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

package com.kunminx.puremusic.data.repository;

import com.kunminx.architecture.data.repository.DataResult;
import com.kunminx.puremusic.data.bean.DownloadFile;
import com.kunminx.puremusic.data.bean.LibraryInfo;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.data.bean.User;

import java.util.List;

/**
 * Create by KunMinX at 19/10/29
 */
public interface IRemoteSource {

    void getFreeMusic(DataResult<TestAlbum> result);

    void getLibraryInfo(DataResult<List<LibraryInfo>> result);

    void downloadFile(DataResult<DownloadFile> result);

    void login(User user, DataResult<String> result);
}
