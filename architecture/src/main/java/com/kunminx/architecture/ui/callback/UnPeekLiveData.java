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

package com.kunminx.architecture.ui.callback;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO：UnPeekLiveData 的存在是为了在 "重回二级页面" 的场景下，解决 "数据倒灌" 的问题。
 * 对 "数据倒灌" 的状况不理解的小伙伴，可参考《jetpack MVVM 精讲》的解析
 * <p>
 * https://juejin.im/post/5dafc49b6fb9a04e17209922
 * <p>
 * 本类参考了官方 SingleEventLive 的非入侵设计，
 * TODO：并创新性地引入了 "延迟清空消息" 的设计，
 * 如此可确保：
 * 1.一条消息能被多个观察者消费
 * 2.延迟期结束，不再能够收到旧消息的推送
 * 3.并且旧消息在延迟期结束时能从内存中释放，避免内存溢出等问题
 *
 *
 * <p>
 * Create by KunMinX at 19/9/23
 */
public class UnPeekLiveData<T> extends MutableLiveData<T> {

    private boolean isCleaning;
    private boolean hasHandled = true;
    private boolean isDelaying;
    private int DELAY_TO_CLEAR_EVENT = 1000;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {

        super.observe(owner, t -> {

            if (isCleaning) {
                hasHandled = true;
                isDelaying = false;
                isCleaning = false;
                return;
            }

            if (!hasHandled) {
                hasHandled = true;
                isDelaying = true;
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        clear();
                    }
                };
                timer.schedule(task, DELAY_TO_CLEAR_EVENT);
                observer.onChanged(t);
            } else if (isDelaying) {
                observer.onChanged(t);
            }
        });
    }

    @Override
    public void setValue(T value) {
        hasHandled = false;
        isDelaying = false;
        super.setValue(value);
    }

    private void clear() {
        isCleaning = true;
        super.postValue(null);
    }

    public static class Builder<T> {

        /**
         * time of event's life
         */
        private int eventLiveTime = 1000;

        public Builder<T> setEventLiveTime(int eventLiveTime) {
            this.eventLiveTime = eventLiveTime;
            return this;
        }

        public UnPeekLiveData<T> create() {
            UnPeekLiveData<T> liveData = new UnPeekLiveData<>();
            liveData.DELAY_TO_CLEAR_EVENT = this.eventLiveTime;
            return liveData;
        }
    }
}
