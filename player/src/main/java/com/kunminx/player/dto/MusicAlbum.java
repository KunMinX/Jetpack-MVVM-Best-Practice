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

import java.util.List;

/**
 * 无版权音乐的专辑实体
 * <p>
 * Create by KunMinX at 18/9/24
 */
public class MusicAlbum<F extends FreeMusic> {

    private String albumId;
    private String title;
    private String summary;
    private String artist;
    private String coverImg;
    private List<F> freeMusics;

    public MusicAlbum() {
    }

    public MusicAlbum(String albumId, String title, String summary, String artist, String coverImg, List<F> freeMusics) {
        this.albumId = albumId;
        this.title = title;
        this.summary = summary;
        this.artist = artist;
        this.coverImg = coverImg;
        this.freeMusics = freeMusics;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public List<F> getFreeMusics() {
        return freeMusics;
    }

    public void setFreeMusics(List<F> freeMusics) {
        this.freeMusics = freeMusics;
    }

}
