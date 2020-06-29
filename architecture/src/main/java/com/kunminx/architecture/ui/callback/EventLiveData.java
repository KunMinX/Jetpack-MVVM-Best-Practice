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

package com.kunminx.architecture.ui.callback;

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

    /**
     * 有时候只需要简单的发送一个消息，因为没有对监听到的值操作的需求，不想再发送的时候再传一个值，可以调用该方法
     * 所以使用该方法需要注意的是，在liveData监听值变化的时候，不能去使用监听的值，因为它会是个null
     * 之前 xxxViewModel.xxx.postValue(false)
     * 现在 xxxViewModel.xxx.call()
     */
    public void call(){
        super.postEvent(new Event<>(null));
    }
}
