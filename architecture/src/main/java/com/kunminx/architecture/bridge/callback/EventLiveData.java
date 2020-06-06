/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.architecture.bridge.callback;

/**
 * TODO: 用于 callback 的情况，配合 MutableLiveData & SharedViewModel 的使用
 * <p>
 * Create by KunMinX at 2020/6/3
 */
@SuppressWarnings("WeakerAccess")
public class EventLiveData<T> extends ELiveData<T> {

    /**
     * Creates a MutableLiveData initialized with the given {@code value}.
     *
     * @param value initial value
     */
    public EventLiveData(Event<T> value) {
        super(value);
    }

    /**
     * Creates a MutableLiveData with no value assigned to it.
     */
    public EventLiveData() {
        super();
    }

    public void postValue(T value) {
        super.postEvent(new Event<>(value));
    }

    public void setValue(T value) {
        super.setEvent(new Event<>(value));
    }
}
