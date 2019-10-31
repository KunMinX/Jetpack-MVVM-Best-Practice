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

package com.kunminx.puremusic.data.bean;

import java.util.List;

/**
 * Create by KunMinX at 19/10/16
 */
public class AlbumResult {

    private int code;
    private String message;
    private DataBean data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private AlbumsResult album;

        public AlbumsResult getAlbum() {
            return album;
        }

        public static class AlbumsResult {

            private int curnum;
            private int curpage;
            private int totalnum;
            private List<AlbumItem> list;

            public int getCurnum() {
                return curnum;
            }

            public void setCurnum(int curnum) {
                this.curnum = curnum;
            }

            public int getCurpage() {
                return curpage;
            }

            public void setCurpage(int curpage) {
                this.curpage = curpage;
            }

            public int getTotalnum() {
                return totalnum;
            }

            public void setTotalnum(int totalnum) {
                this.totalnum = totalnum;
            }

            public List<AlbumItem> getList() {
                return list;
            }

            public void setList(List<AlbumItem> list) {
                this.list = list;
            }

            public static class AlbumItem {

                private String albumName;
                private String singerMID;
                private String singerName_hilight;
                private String docid;
                private String albumMID;
                private int albumID;
                private String albumPic;
                private int type;
                private String singerName;
                private String albumName_hilight;
                private String publicTime;
                private int singerID;
                private int song_count;
                private String catch_song;
                private List<Singer> singer_list;

                public String getAlbumName() {
                    return albumName;
                }

                public void setAlbumName(String albumName) {
                    this.albumName = albumName;
                }

                public String getSingerMID() {
                    return singerMID;
                }

                public void setSingerMID(String singerMID) {
                    this.singerMID = singerMID;
                }

                public String getSingerName_hilight() {
                    return singerName_hilight;
                }

                public void setSingerName_hilight(String singerName_hilight) {
                    this.singerName_hilight = singerName_hilight;
                }

                public String getDocid() {
                    return docid;
                }

                public void setDocid(String docid) {
                    this.docid = docid;
                }

                public String getAlbumMID() {
                    return albumMID;
                }

                public void setAlbumMID(String albumMID) {
                    this.albumMID = albumMID;
                }

                public int getAlbumID() {
                    return albumID;
                }

                public void setAlbumID(int albumID) {
                    this.albumID = albumID;
                }

                public String getAlbumPic() {
                    return albumPic;
                }

                public void setAlbumPic(String albumPic) {
                    this.albumPic = albumPic;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getSingerName() {
                    return singerName;
                }

                public void setSingerName(String singerName) {
                    this.singerName = singerName;
                }

                public String getAlbumName_hilight() {
                    return albumName_hilight;
                }

                public void setAlbumName_hilight(String albumName_hilight) {
                    this.albumName_hilight = albumName_hilight;
                }

                public String getPublicTime() {
                    return publicTime;
                }

                public void setPublicTime(String publicTime) {
                    this.publicTime = publicTime;
                }

                public int getSingerID() {
                    return singerID;
                }

                public void setSingerID(int singerID) {
                    this.singerID = singerID;
                }

                public int getSong_count() {
                    return song_count;
                }

                public void setSong_count(int song_count) {
                    this.song_count = song_count;
                }

                public String getCatch_song() {
                    return catch_song;
                }

                public void setCatch_song(String catch_song) {
                    this.catch_song = catch_song;
                }

                public List<Singer> getSinger_list() {
                    return singer_list;
                }

                public void setSinger_list(List<Singer> singer_list) {
                    this.singer_list = singer_list;
                }

                public static class Singer {

                    private String name;
                    private String name_hilight;
                    private String mid;
                    private int id;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getName_hilight() {
                        return name_hilight;
                    }

                    public void setName_hilight(String name_hilight) {
                        this.name_hilight = name_hilight;
                    }

                    public String getMid() {
                        return mid;
                    }

                    public void setMid(String mid) {
                        this.mid = mid;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }
                }
            }
        }
    }

}
