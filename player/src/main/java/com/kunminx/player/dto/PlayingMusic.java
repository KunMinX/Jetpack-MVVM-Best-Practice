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

package com.kunminx.player.dto;


/**
 * Create by KunMinX at 18/9/24
 */
public class PlayingMusic extends ChangeMusic {

    private String nowTime;
    private String allTime;
    private int duration;
    private int playerPosition;

    public PlayingMusic(String nowTime, String allTime) {
        this.nowTime = nowTime;
        this.allTime = allTime;
    }

    public PlayingMusic(String title, String content, String bookId, String chapterId, String nowTime, String allTime, String img) {
        super(title, content, bookId, chapterId, img);
        this.nowTime = nowTime;
        this.allTime = allTime;
    }

    public PlayingMusic(MusicAlbum musicAlbum, int playIndex, String nowTime, String allTime) {
        super(musicAlbum, playIndex);
        this.nowTime = nowTime;
        this.allTime = allTime;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getAllTime() {
        return allTime;
    }

    public void setAllTime(String allTime) {
        this.allTime = allTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }
}
