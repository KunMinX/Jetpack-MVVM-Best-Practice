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

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by KunMinX at 19/10/16
 */

public class SongInfo {

    private int code;
    private DataBean data;
    private String message;
    private int subcode;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSubcode() {
        return subcode;
    }

    public void setSubcode(int subcode) {
        this.subcode = subcode;
    }

    public static class DataBean {

        private String aDate;
        private String albumTips;
        private int color;
        private String company;
        private CompanyNewBean company_new;
        private int cur_song_num;
        private String desc;
        private String genre;
        private int id;
        private String lan;
        private String mid;
        private String name;
        private int radio_anchor;
        private int singerid;
        private String singermblog;
        private String singermid;
        private String singername;
        private int song_begin;
        private int total;
        private int total_song_num;
        private List<SongItem> list;

        public String getADate() {
            return aDate;
        }

        public void setADate(String aDate) {
            this.aDate = aDate;
        }

        public String getAlbumTips() {
            return albumTips;
        }

        public void setAlbumTips(String albumTips) {
            this.albumTips = albumTips;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public CompanyNewBean getCompany_new() {
            return company_new;
        }

        public void setCompany_new(CompanyNewBean company_new) {
            this.company_new = company_new;
        }

        public int getCur_song_num() {
            return cur_song_num;
        }

        public void setCur_song_num(int cur_song_num) {
            this.cur_song_num = cur_song_num;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLan() {
            return lan;
        }

        public void setLan(String lan) {
            this.lan = lan;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRadio_anchor() {
            return radio_anchor;
        }

        public void setRadio_anchor(int radio_anchor) {
            this.radio_anchor = radio_anchor;
        }

        public int getSingerid() {
            return singerid;
        }

        public void setSingerid(int singerid) {
            this.singerid = singerid;
        }

        public String getSingermblog() {
            return singermblog;
        }

        public void setSingermblog(String singermblog) {
            this.singermblog = singermblog;
        }

        public String getSingermid() {
            return singermid;
        }

        public void setSingermid(String singermid) {
            this.singermid = singermid;
        }

        public String getSingername() {
            return singername;
        }

        public void setSingername(String singername) {
            this.singername = singername;
        }

        public int getSong_begin() {
            return song_begin;
        }

        public void setSong_begin(int song_begin) {
            this.song_begin = song_begin;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotal_song_num() {
            return total_song_num;
        }

        public void setTotal_song_num(int total_song_num) {
            this.total_song_num = total_song_num;
        }

        public List<SongItem> getList() {
            return list;
        }

        public void setList(List<SongItem> list) {
            this.list = list;
        }

        public static class CompanyNewBean {

            private String brief;
            private String headPic;
            private int id;
            private int is_show;
            private String name;

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIs_show() {
                return is_show;
            }

            public void setIs_show(int is_show) {
                this.is_show = is_show;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class SongItem {

            private String albumdesc;
            private int albumid;
            private String albummid;
            private String albumname;
            private int alertid;
            private int belongCD;
            private int cdIdx;
            private int interval;
            private int isonly;
            private String label;
            private int msgid;
            private PayBean pay;
            private PreviewBean preview;
            private int rate;
            private int size128;
            private int size320;
            private int size5_1;
            private int sizeape;
            private int sizeflac;
            private int sizeogg;
            private int songid;
            private String songmid;
            private String songname;
            private String songorig;
            private int songtype;
            private String strMediaMid;
            private int stream;
            @SerializedName("switch")
            private int switchX;
            private int type;
            private String vid;
            private List<Singer> singer;

            public String getAlbumdesc() {
                return albumdesc;
            }

            public void setAlbumdesc(String albumdesc) {
                this.albumdesc = albumdesc;
            }

            public int getAlbumid() {
                return albumid;
            }

            public void setAlbumid(int albumid) {
                this.albumid = albumid;
            }

            public String getAlbummid() {
                return albummid;
            }

            public void setAlbummid(String albummid) {
                this.albummid = albummid;
            }

            public String getAlbumname() {
                return albumname;
            }

            public void setAlbumname(String albumname) {
                this.albumname = albumname;
            }

            public int getAlertid() {
                return alertid;
            }

            public void setAlertid(int alertid) {
                this.alertid = alertid;
            }

            public int getBelongCD() {
                return belongCD;
            }

            public void setBelongCD(int belongCD) {
                this.belongCD = belongCD;
            }

            public int getCdIdx() {
                return cdIdx;
            }

            public void setCdIdx(int cdIdx) {
                this.cdIdx = cdIdx;
            }

            public int getInterval() {
                return interval;
            }

            public void setInterval(int interval) {
                this.interval = interval;
            }

            public int getIsonly() {
                return isonly;
            }

            public void setIsonly(int isonly) {
                this.isonly = isonly;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public int getMsgid() {
                return msgid;
            }

            public void setMsgid(int msgid) {
                this.msgid = msgid;
            }

            public PayBean getPay() {
                return pay;
            }

            public void setPay(PayBean pay) {
                this.pay = pay;
            }

            public PreviewBean getPreview() {
                return preview;
            }

            public void setPreview(PreviewBean preview) {
                this.preview = preview;
            }

            public int getRate() {
                return rate;
            }

            public void setRate(int rate) {
                this.rate = rate;
            }

            public int getSize128() {
                return size128;
            }

            public void setSize128(int size128) {
                this.size128 = size128;
            }

            public int getSize320() {
                return size320;
            }

            public void setSize320(int size320) {
                this.size320 = size320;
            }

            public int getSize5_1() {
                return size5_1;
            }

            public void setSize5_1(int size5_1) {
                this.size5_1 = size5_1;
            }

            public int getSizeape() {
                return sizeape;
            }

            public void setSizeape(int sizeape) {
                this.sizeape = sizeape;
            }

            public int getSizeflac() {
                return sizeflac;
            }

            public void setSizeflac(int sizeflac) {
                this.sizeflac = sizeflac;
            }

            public int getSizeogg() {
                return sizeogg;
            }

            public void setSizeogg(int sizeogg) {
                this.sizeogg = sizeogg;
            }

            public int getSongid() {
                return songid;
            }

            public void setSongid(int songid) {
                this.songid = songid;
            }

            public String getSongmid() {
                return songmid;
            }

            public void setSongmid(String songmid) {
                this.songmid = songmid;
            }

            public String getSongname() {
                return songname;
            }

            public void setSongname(String songname) {
                this.songname = songname;
            }

            public String getSongorig() {
                return songorig;
            }

            public void setSongorig(String songorig) {
                this.songorig = songorig;
            }

            public int getSongtype() {
                return songtype;
            }

            public void setSongtype(int songtype) {
                this.songtype = songtype;
            }

            public String getStrMediaMid() {
                return strMediaMid;
            }

            public void setStrMediaMid(String strMediaMid) {
                this.strMediaMid = strMediaMid;
            }

            public int getStream() {
                return stream;
            }

            public void setStream(int stream) {
                this.stream = stream;
            }

            public int getSwitchX() {
                return switchX;
            }

            public void setSwitchX(int switchX) {
                this.switchX = switchX;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public List<Singer> getSinger() {
                return singer;
            }

            public void setSinger(List<Singer> singer) {
                this.singer = singer;
            }

            public static class PayBean {

                private int payalbum;
                private int payalbumprice;
                private int paydownload;
                private int payinfo;
                private int payplay;
                private int paytrackmouth;
                private int paytrackprice;
                private int timefree;

                public int getPayalbum() {
                    return payalbum;
                }

                public void setPayalbum(int payalbum) {
                    this.payalbum = payalbum;
                }

                public int getPayalbumprice() {
                    return payalbumprice;
                }

                public void setPayalbumprice(int payalbumprice) {
                    this.payalbumprice = payalbumprice;
                }

                public int getPaydownload() {
                    return paydownload;
                }

                public void setPaydownload(int paydownload) {
                    this.paydownload = paydownload;
                }

                public int getPayinfo() {
                    return payinfo;
                }

                public void setPayinfo(int payinfo) {
                    this.payinfo = payinfo;
                }

                public int getPayplay() {
                    return payplay;
                }

                public void setPayplay(int payplay) {
                    this.payplay = payplay;
                }

                public int getPaytrackmouth() {
                    return paytrackmouth;
                }

                public void setPaytrackmouth(int paytrackmouth) {
                    this.paytrackmouth = paytrackmouth;
                }

                public int getPaytrackprice() {
                    return paytrackprice;
                }

                public void setPaytrackprice(int paytrackprice) {
                    this.paytrackprice = paytrackprice;
                }

                public int getTimefree() {
                    return timefree;
                }

                public void setTimefree(int timefree) {
                    this.timefree = timefree;
                }
            }

            public static class PreviewBean {

                private int trybegin;
                private int tryend;
                private int trysize;

                public int getTrybegin() {
                    return trybegin;
                }

                public void setTrybegin(int trybegin) {
                    this.trybegin = trybegin;
                }

                public int getTryend() {
                    return tryend;
                }

                public void setTryend(int tryend) {
                    this.tryend = tryend;
                }

                public int getTrysize() {
                    return trysize;
                }

                public void setTrysize(int trysize) {
                    this.trysize = trysize;
                }
            }

            public static class Singer {

                private int id;
                private String mid;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getMid() {
                    return mid;
                }

                public void setMid(String mid) {
                    this.mid = mid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
