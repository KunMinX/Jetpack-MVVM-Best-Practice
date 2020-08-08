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

package com.kunminx.puremusic.player.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.kunminx.architecture.domain.usecase.UseCase;
import com.kunminx.architecture.domain.usecase.UseCaseHandler;
import com.kunminx.architecture.utils.ImageUtils;
import com.kunminx.puremusic.MainActivity;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.data.config.Configs;
import com.kunminx.puremusic.domain.usecase.DownloadUseCase;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.player.helper.PlayerCallHelper;

import java.io.File;

/**
 * Create by KunMinX at 19/7/17
 */
public class PlayerService extends Service {

    public static final String NOTIFY_PREVIOUS = "pure_music.kunminx.previous";
    public static final String NOTIFY_CLOSE = "pure_music.kunminx.close";
    public static final String NOTIFY_PAUSE = "pure_music.kunminx.pause";
    public static final String NOTIFY_PLAY = "pure_music.kunminx.play";
    public static final String NOTIFY_NEXT = "pure_music.kunminx.next";
    private static final String GROUP_ID = "group_001";
    private static final String CHANNEL_ID = "channel_001";
    private PlayerCallHelper mPlayerCallHelper;
    private DownloadUseCase mDownloadUseCase;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mPlayerCallHelper == null) {
            mPlayerCallHelper = new PlayerCallHelper(new PlayerCallHelper.PlayerCallHelperListener() {
                @Override
                public void playAudio() {
                    PlayerManager.getInstance().playAudio();
                }

                @Override
                public boolean isPlaying() {
                    return PlayerManager.getInstance().isPlaying();
                }

                @Override
                public boolean isPaused() {
                    return PlayerManager.getInstance().isPaused();
                }

                @Override
                public void pauseAudio() {
                    PlayerManager.getInstance().pauseAudio();
                }
            });
        }

        TestAlbum.TestMusic results = PlayerManager.getInstance().getCurrentPlayingMusic();
        if (results == null) {
            stopSelf();
            return START_NOT_STICKY;
        }

        mPlayerCallHelper.bindCallListener(getApplicationContext());

        createNotification(results);
        return START_NOT_STICKY;
    }

    private void createNotification(TestAlbum.TestMusic testMusic) {
        try {
            String title = testMusic.getTitle();
            TestAlbum album = PlayerManager.getInstance().getAlbum();
            String summary = album.getSummary();

            RemoteViews simpleContentView = new RemoteViews(
                    getApplicationContext().getPackageName(), R.layout.notify_player_small);

            RemoteViews expandedView;
            expandedView = new RemoteViews(
                    getApplicationContext().getPackageName(), R.layout.notify_player_big);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setAction("showPlayer");
            PendingIntent contentIntent = PendingIntent.getActivity(
                    this, 0, intent, 0);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannelGroup playGroup = new NotificationChannelGroup(GROUP_ID, getString(R.string.play));
                notificationManager.createNotificationChannelGroup(playGroup);

                NotificationChannel playChannel = new NotificationChannel(CHANNEL_ID,
                        getString(R.string.notify_of_play), NotificationManager.IMPORTANCE_DEFAULT);
                playChannel.setGroup(GROUP_ID);
                notificationManager.createNotificationChannel(playChannel);
            }

            Notification notification = new NotificationCompat.Builder(
                    getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_player)
                    .setContentIntent(contentIntent)
                    .setOnlyAlertOnce(true)
                    .setContentTitle(title).build();

            notification.contentView = simpleContentView;
            notification.bigContentView = expandedView;

            setListeners(simpleContentView);
            setListeners(expandedView);

            notification.contentView.setViewVisibility(R.id.player_progress_bar, View.GONE);
            notification.contentView.setViewVisibility(R.id.player_next, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.player_previous, View.VISIBLE);
            notification.bigContentView.setViewVisibility(R.id.player_next, View.VISIBLE);
            notification.bigContentView.setViewVisibility(R.id.player_previous, View.VISIBLE);
            notification.bigContentView.setViewVisibility(R.id.player_progress_bar, View.GONE);

            boolean isPaused = PlayerManager.getInstance().isPaused();
            notification.contentView.setViewVisibility(R.id.player_pause, isPaused ? View.GONE : View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.player_play, isPaused ? View.VISIBLE : View.GONE);
            notification.bigContentView.setViewVisibility(R.id.player_pause, isPaused ? View.GONE : View.VISIBLE);
            notification.bigContentView.setViewVisibility(R.id.player_play, isPaused ? View.VISIBLE : View.GONE);

            notification.contentView.setTextViewText(R.id.player_song_name, title);
            notification.contentView.setTextViewText(R.id.player_author_name, summary);
            notification.bigContentView.setTextViewText(R.id.player_song_name, title);
            notification.bigContentView.setTextViewText(R.id.player_author_name, summary);
            notification.flags |= Notification.FLAG_ONGOING_EVENT;

            String coverPath = Configs.COVER_PATH + File.separator + testMusic.getMusicId() + ".jpg";
            Bitmap bitmap = ImageUtils.getBitmap(coverPath);

            if (bitmap != null) {
                notification.contentView.setImageViewBitmap(R.id.player_album_art, bitmap);
                notification.bigContentView.setImageViewBitmap(R.id.player_album_art, bitmap);
            } else {
                requestAlbumCover(testMusic.getCoverImg(), testMusic.getMusicId());
                notification.contentView.setImageViewResource(R.id.player_album_art, R.drawable.bg_album_default);
                notification.bigContentView.setImageViewResource(R.id.player_album_art, R.drawable.bg_album_default);
            }

            startForeground(5, notification);

            mPlayerCallHelper.bindRemoteController(getApplicationContext());
            mPlayerCallHelper.requestAudioFocus(title, summary);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListeners(RemoteViews view) {
        try {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_PREVIOUS).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_previous, pendingIntent);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_CLOSE).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_close, pendingIntent);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_PAUSE).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_pause, pendingIntent);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_NEXT).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_next, pendingIntent);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, new Intent(NOTIFY_PLAY).setPackage(getPackageName()),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.player_play, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestAlbumCover(String coverUrl, String musicId) {
        if (mDownloadUseCase == null) {
            mDownloadUseCase = new DownloadUseCase();
        }

        UseCaseHandler.getInstance().execute(mDownloadUseCase,
                new DownloadUseCase.RequestValues(coverUrl, musicId + ".jpg"),
                new UseCase.UseCaseCallback<DownloadUseCase.ResponseValue>() {
                    @Override
                    public void onSuccess(DownloadUseCase.ResponseValue response) {
                        startService(new Intent(getApplicationContext(), PlayerService.class));
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayerCallHelper.unbindCallListener(getApplicationContext());
        mPlayerCallHelper.unbindRemoteController();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
