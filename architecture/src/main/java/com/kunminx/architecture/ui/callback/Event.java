/*
 * Copyright 2018-2020 KunMinX
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

package com.kunminx.architecture.ui.callback;

/**
 * TODO: 用于 callback 的情况，配合 MutableLiveData & SharedViewModel 的使用
 * <p>
 * Create by KunMinX at 2020/6/2
 */
public class Event<T> {

    private T content;
    private boolean hasHandled;

    public Event(T content) {
        this.content = content;
    }

    T getContent() {
        if (hasHandled) {
            return null;
        }
        hasHandled = true;
        return content;
    }

    void setContentNull() {
        content = null;
    }
}
