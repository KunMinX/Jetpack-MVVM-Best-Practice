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

public class SongUrl {

    private Req0Bean req_0;
    private int code;

    public Req0Bean getReq_0() {
        return req_0;
    }

    public void setReq_0(Req0Bean req_0) {
        this.req_0 = req_0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public static class Req0Bean {

        private DataBean data;
        private int code;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public static class DataBean {

            private int expiration;
            private String login_key;
            private String msg;
            private int retcode;
            private String servercheck;
            private String testfile2g;
            private String testfilewifi;
            private String uin;
            private int verify_type;
            private List<MidurlinfoBean> midurlinfo;
            private List<String> sip;
            private List<String> thirdip;

            public int getExpiration() {
                return expiration;
            }

            public void setExpiration(int expiration) {
                this.expiration = expiration;
            }

            public String getLogin_key() {
                return login_key;
            }

            public void setLogin_key(String login_key) {
                this.login_key = login_key;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public int getRetcode() {
                return retcode;
            }

            public void setRetcode(int retcode) {
                this.retcode = retcode;
            }

            public String getServercheck() {
                return servercheck;
            }

            public void setServercheck(String servercheck) {
                this.servercheck = servercheck;
            }

            public String getTestfile2g() {
                return testfile2g;
            }

            public void setTestfile2g(String testfile2g) {
                this.testfile2g = testfile2g;
            }

            public String getTestfilewifi() {
                return testfilewifi;
            }

            public void setTestfilewifi(String testfilewifi) {
                this.testfilewifi = testfilewifi;
            }

            public String getUin() {
                return uin;
            }

            public void setUin(String uin) {
                this.uin = uin;
            }

            public int getVerify_type() {
                return verify_type;
            }

            public void setVerify_type(int verify_type) {
                this.verify_type = verify_type;
            }

            public List<MidurlinfoBean> getMidurlinfo() {
                return midurlinfo;
            }

            public void setMidurlinfo(List<MidurlinfoBean> midurlinfo) {
                this.midurlinfo = midurlinfo;
            }

            public List<String> getSip() {
                return sip;
            }

            public void setSip(List<String> sip) {
                this.sip = sip;
            }

            public List<String> getThirdip() {
                return thirdip;
            }

            public void setThirdip(List<String> thirdip) {
                this.thirdip = thirdip;
            }

            public static class MidurlinfoBean {

                private int common_downfromtag;
                private String errtype;
                private String filename;
                private String flowfromtag;
                private String flowurl;
                private int hisbuy;
                private int hisdown;
                private int isbuy;
                private int isonly;
                private int onecan;
                private String opi128kurl;
                private String opi192koggurl;
                private String opi192kurl;
                private String opi30surl;
                private String opi48kurl;
                private String opi96kurl;
                private String opiflackurl;
                private int p2pfromtag;
                private int pdl;
                private int pneed;
                private int pneedbuy;
                private int premain;
                private String purl;
                private int qmdlfromtag;
                private int result;
                private String songmid;
                private String tips;
                private int uiAlert;
                private int vip_downfromtag;
                private String vkey;
                private String wififromtag;
                private String wifiurl;

                public int getCommon_downfromtag() {
                    return common_downfromtag;
                }

                public void setCommon_downfromtag(int common_downfromtag) {
                    this.common_downfromtag = common_downfromtag;
                }

                public String getErrtype() {
                    return errtype;
                }

                public void setErrtype(String errtype) {
                    this.errtype = errtype;
                }

                public String getFilename() {
                    return filename;
                }

                public void setFilename(String filename) {
                    this.filename = filename;
                }

                public String getFlowfromtag() {
                    return flowfromtag;
                }

                public void setFlowfromtag(String flowfromtag) {
                    this.flowfromtag = flowfromtag;
                }

                public String getFlowurl() {
                    return flowurl;
                }

                public void setFlowurl(String flowurl) {
                    this.flowurl = flowurl;
                }

                public int getHisbuy() {
                    return hisbuy;
                }

                public void setHisbuy(int hisbuy) {
                    this.hisbuy = hisbuy;
                }

                public int getHisdown() {
                    return hisdown;
                }

                public void setHisdown(int hisdown) {
                    this.hisdown = hisdown;
                }

                public int getIsbuy() {
                    return isbuy;
                }

                public void setIsbuy(int isbuy) {
                    this.isbuy = isbuy;
                }

                public int getIsonly() {
                    return isonly;
                }

                public void setIsonly(int isonly) {
                    this.isonly = isonly;
                }

                public int getOnecan() {
                    return onecan;
                }

                public void setOnecan(int onecan) {
                    this.onecan = onecan;
                }

                public String getOpi128kurl() {
                    return opi128kurl;
                }

                public void setOpi128kurl(String opi128kurl) {
                    this.opi128kurl = opi128kurl;
                }

                public String getOpi192koggurl() {
                    return opi192koggurl;
                }

                public void setOpi192koggurl(String opi192koggurl) {
                    this.opi192koggurl = opi192koggurl;
                }

                public String getOpi192kurl() {
                    return opi192kurl;
                }

                public void setOpi192kurl(String opi192kurl) {
                    this.opi192kurl = opi192kurl;
                }

                public String getOpi30surl() {
                    return opi30surl;
                }

                public void setOpi30surl(String opi30surl) {
                    this.opi30surl = opi30surl;
                }

                public String getOpi48kurl() {
                    return opi48kurl;
                }

                public void setOpi48kurl(String opi48kurl) {
                    this.opi48kurl = opi48kurl;
                }

                public String getOpi96kurl() {
                    return opi96kurl;
                }

                public void setOpi96kurl(String opi96kurl) {
                    this.opi96kurl = opi96kurl;
                }

                public String getOpiflackurl() {
                    return opiflackurl;
                }

                public void setOpiflackurl(String opiflackurl) {
                    this.opiflackurl = opiflackurl;
                }

                public int getP2pfromtag() {
                    return p2pfromtag;
                }

                public void setP2pfromtag(int p2pfromtag) {
                    this.p2pfromtag = p2pfromtag;
                }

                public int getPdl() {
                    return pdl;
                }

                public void setPdl(int pdl) {
                    this.pdl = pdl;
                }

                public int getPneed() {
                    return pneed;
                }

                public void setPneed(int pneed) {
                    this.pneed = pneed;
                }

                public int getPneedbuy() {
                    return pneedbuy;
                }

                public void setPneedbuy(int pneedbuy) {
                    this.pneedbuy = pneedbuy;
                }

                public int getPremain() {
                    return premain;
                }

                public void setPremain(int premain) {
                    this.premain = premain;
                }

                public String getPurl() {
                    return purl;
                }

                public void setPurl(String purl) {
                    this.purl = purl;
                }

                public int getQmdlfromtag() {
                    return qmdlfromtag;
                }

                public void setQmdlfromtag(int qmdlfromtag) {
                    this.qmdlfromtag = qmdlfromtag;
                }

                public int getResult() {
                    return result;
                }

                public void setResult(int result) {
                    this.result = result;
                }

                public String getSongmid() {
                    return songmid;
                }

                public void setSongmid(String songmid) {
                    this.songmid = songmid;
                }

                public String getTips() {
                    return tips;
                }

                public void setTips(String tips) {
                    this.tips = tips;
                }

                public int getUiAlert() {
                    return uiAlert;
                }

                public void setUiAlert(int uiAlert) {
                    this.uiAlert = uiAlert;
                }

                public int getVip_downfromtag() {
                    return vip_downfromtag;
                }

                public void setVip_downfromtag(int vip_downfromtag) {
                    this.vip_downfromtag = vip_downfromtag;
                }

                public String getVkey() {
                    return vkey;
                }

                public void setVkey(String vkey) {
                    this.vkey = vkey;
                }

                public String getWififromtag() {
                    return wififromtag;
                }

                public void setWififromtag(String wififromtag) {
                    this.wififromtag = wififromtag;
                }

                public String getWifiurl() {
                    return wifiurl;
                }

                public void setWifiurl(String wifiurl) {
                    this.wifiurl = wifiurl;
                }
            }
        }
    }
}
