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

package com.kunminx.architecture.data.manager;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.kunminx.architecture.bridge.callback.UnPeekLiveData;

/**
 * Create by KunMinX at 19/10/11
 */
public class NetworkStateManager implements DefaultLifecycleObserver {

    private static NetworkStateManager sManager = new NetworkStateManager();

    private NetworkStateManager() {
    }

    public static NetworkStateManager getInstance() {
        return sManager;
    }

    public final UnPeekLiveData<NetState> mNetworkStateCallback = new UnPeekLiveData<>();
    private NetworkStateReceive mNetworkStateReceive;


    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        /*mNetworkStateReceive = new NetworkStateReceive();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if (owner instanceof AppCompatActivity) {
            ((AppCompatActivity) owner).registerReceiver(mNetworkStateReceive, filter);
        } else if (owner instanceof Fragment) {
            ((AppCompatActivity) Objects.requireNonNull(((Fragment) owner).getActivity()))
                    .registerReceiver(mNetworkStateReceive, filter);
        }*/
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        /*if (owner instanceof AppCompatActivity) {
            ((AppCompatActivity) owner).unregisterReceiver(mNetworkStateReceive);
        } else if (owner instanceof Fragment) {
            ((AppCompatActivity) Objects.requireNonNull(((Fragment) owner).getActivity()))
                    .unregisterReceiver(mNetworkStateReceive);
        }*/
    }
}
