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

package com.kunminx.architecture.data.response;

/**
 * TODO：本类仅用作示例参考，请根据 "实际项目需求" 配置自定义的 "响应状态元信息"
 * <p>
 * Create by KunMinX at 19/10/11
 */
public class ResponseStatus {

    private String responseCode = "";
    private boolean success = true;
    private Enum<ResultSource> source = ResultSource.NETWORK;

    public ResponseStatus() {
    }

    public ResponseStatus(String responseCode, boolean success) {
        this.responseCode = responseCode;
        this.success = success;
    }

    public ResponseStatus(String responseCode, boolean success, Enum<ResultSource> source) {
        this(responseCode, success);
        this.source = source;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public Enum<ResultSource> getSource() {
        return source;
    }
}
