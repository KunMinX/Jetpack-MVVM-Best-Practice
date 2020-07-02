/*
 * Copyright 2020-present KunMinX
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

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Create by KunMinX at 2020/6/2
 */
public class Event<T> {

    private T content;
    private boolean hasHandled;
    private boolean isDelaying;

    public Event(T content) {
        this.content = content;
    }

    public @Nullable
    T getContent() {
        if (!hasHandled) {
            hasHandled = true;
            isDelaying = true;
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    content = null;
                    isDelaying = false;
                }
            };
            timer.schedule(task, 1000);
            return content;
        } else if (isDelaying) {
            return content;
        } else {
            return null;
        }
    }
}
