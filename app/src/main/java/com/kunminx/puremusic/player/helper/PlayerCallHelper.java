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

package com.kunminx.puremusic.player.helper;


import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.kunminx.puremusic.player.notification.PlayerReceiver;


/**
 * 在来电时自动协调和暂停音乐播放
 * Create by KunMinX at 19/7/18
 */
public class PlayerCallHelper implements AudioManager.OnAudioFocusChangeListener {

    private final PlayerCallHelperListener mPlayerCallHelperListener;
    private PhoneStateListener phoneStateListener;
    private RemoteControlClient remoteControlClient;
    private AudioManager mAudioManager;
    private boolean ignoreAudioFocus;
    private boolean mIsTempPauseByPhone;
    private boolean tempPause;

    public PlayerCallHelper(PlayerCallHelperListener playerCallHelperListener) {
        mPlayerCallHelperListener = playerCallHelperListener;
    }

    public void bindCallListener(Context context) {
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (mIsTempPauseByPhone) {
                        if (mPlayerCallHelperListener != null) {
                            mPlayerCallHelperListener.playAudio();
                        }
                        mIsTempPauseByPhone = false;
                    }
                } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                    if (mPlayerCallHelperListener != null) {
                        if (mPlayerCallHelperListener.isPlaying() &&
                                !mPlayerCallHelperListener.isPaused()) {
                            mPlayerCallHelperListener.pauseAudio();
                            mIsTempPauseByPhone = true;
                        }
                    }

                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {

                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    public void bindRemoteController(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        ComponentName remoteComponentName = new ComponentName(context, PlayerReceiver.class.getName());
        try {
            if (remoteControlClient == null) {
                mAudioManager.registerMediaButtonEventReceiver(remoteComponentName);
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.setComponent(remoteComponentName);
                PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(
                        context, 0, mediaButtonIntent, 0);
                remoteControlClient = new RemoteControlClient(mediaPendingIntent);
                mAudioManager.registerRemoteControlClient(remoteControlClient);
            }
            remoteControlClient.setTransportControlFlags(RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                    | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
                    | RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE
                    | RemoteControlClient.FLAG_KEY_MEDIA_STOP
                    | RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
                    | RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
        } catch (Exception e) {
            Log.e("tmessages", e.toString());
        }
    }

    public void unbindCallListener(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mgr != null) {
                mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            }
        } catch (Exception e) {
            Log.e("tmessages", e.toString());
        }
    }

    public void unbindRemoteController() {
        if (remoteControlClient != null) {
            RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
            metadataEditor.clear();
            metadataEditor.apply();
            mAudioManager.unregisterRemoteControlClient(remoteControlClient);
            mAudioManager.abandonAudioFocus(this);
        }
    }

    public void requestAudioFocus(String title, String summary) {
        if (remoteControlClient != null) {
            RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, summary);
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, title);
            metadataEditor.apply();
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    public void setIgnoreAudioFocus() {
        ignoreAudioFocus = true;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (ignoreAudioFocus) {
            ignoreAudioFocus = false;
            return;
        }
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            if (mPlayerCallHelperListener != null) {
                if (mPlayerCallHelperListener.isPlaying() &&
                        !mPlayerCallHelperListener.isPaused()) {
                    mPlayerCallHelperListener.pauseAudio();
                    tempPause = true;
                }
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            if (tempPause) {
                if (mPlayerCallHelperListener != null) {
                    mPlayerCallHelperListener.playAudio();
                }
                tempPause = false;
            }
        }
    }

    public interface PlayerCallHelperListener {
        void playAudio();

        boolean isPlaying();

        boolean isPaused();

        void pauseAudio();
    }
}