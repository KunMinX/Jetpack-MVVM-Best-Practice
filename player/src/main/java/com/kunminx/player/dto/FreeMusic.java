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
 * 无版权音乐实体
 * <p>
 * Create by KunMinX at 18/9/24
 */
public class FreeMusic {

    private String musicId;
    // 专辑封面，目前是取用 unsplash 提供的无版权图片作为封面
    private String img;
    // 无版权音乐链接，来自 bensound 的提供
    private String url;
    // 音乐标题
    private String title;
    // 音乐来源：bensound.com
    private String source;

    public FreeMusic() {
    }

    public FreeMusic(String musicId, String img, String url, String title, String source) {
        this.musicId = musicId;
        this.img = img;
        this.url = url;
        this.title = title;
        this.source = source;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
