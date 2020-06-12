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
public class ChangeMusic<M extends MusicAlbum, F extends FreeMusic> {

    private String title;
    private String content;
    private String albumId;
    private String musicId;
    private String img;

    public ChangeMusic() {
    }

    public ChangeMusic(String title, String content, String albumId, String musicId, String img) {
        this.title = title;
        this.content = content;
        this.albumId = albumId;
        this.musicId = musicId;
        this.img = img;
    }

    public ChangeMusic(M musicAlbum, int playIndex) {
        this.title = musicAlbum.getTitle();
        this.content = musicAlbum.getSummary();
        this.albumId = musicAlbum.getAlbumId();
        this.musicId = ((F) musicAlbum.getFreeMusics().get(playIndex)).getMusicId();
        this.img = musicAlbum.getCoverImg();
    }

    public void setBaseInfo(M musicAlbum, F freeMusic) {
        //要用当前实际播放的列表，因为不同模式存在不同的播放列表
        this.title = freeMusic.getTitle();
        this.content = musicAlbum.getSummary();
        this.albumId = musicAlbum.getAlbumId();
        this.musicId = freeMusic.getMusicId();
        //TODO 事实上，这里应该 img = musicAlbum.getCoverImg, 此处示例写作 getImg
        this.img = freeMusic.getImg();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
