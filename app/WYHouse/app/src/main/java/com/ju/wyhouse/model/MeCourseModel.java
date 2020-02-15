package com.ju.wyhouse.model;

import java.util.List;
import java.util.Map;

/**
 * Author:gaoju
 * Date:2020/2/9 14:37
 * Path:com.ju.wyhouse.model
 * Desc:个人课程表Model
 */
public class MeCourseModel {


    /**
     * code : 0
     * message : 成功
     * data : {"tab":{"0":[-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"1":[-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"3":[-1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"5":[-1,-1,-1,-1,-1,-1,3,3,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"8":[-1,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"9":[-1,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"11":[-1,6,-1,6,-1,6,-1,6,-1,6,-1,6,-1,6,-1,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]},"kecheng":[{"id":0,"jwUser":null,"name":"软件测试（软件方向）","jiaoshi":"瑞樟6-406[数计]","laoshi":"马阿曼讲师（高校）","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,","xnxq":null,"zhouciStr":"1-15(周)","jieci":0},{"id":0,"jwUser":null,"name":"编译原理","jiaoshi":"瑞樟10-405","laoshi":"严礽麒副教授","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,","xnxq":null,"zhouciStr":"1-16(周)","jieci":1},{"id":0,"jwUser":null,"name":"Windows应用系统开发（软件方向）","jiaoshi":"瑞樟6-409","laoshi":"亓文娟副教授","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,","xnxq":null,"zhouciStr":"1-15(周)","jieci":3},{"id":0,"jwUser":null,"name":"移动产品UI设计与实验","jiaoshi":"同文1-304","laoshi":"王峰工程师,韩存鸽讲师（高校）","zhouci":",6,7,","xnxq":null,"zhouciStr":"6-7(周)","jieci":5},{"id":0,"jwUser":null,"name":"Windows应用系统开发（软件方向）","jiaoshi":"瑞樟6-409","laoshi":"亓文娟副教授","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,","xnxq":null,"zhouciStr":"1-15(周)","jieci":8},{"id":0,"jwUser":null,"name":"软件测试（软件方向）","jiaoshi":"瑞樟6-402[数计]","laoshi":"马阿曼讲师（高校）","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,","xnxq":null,"zhouciStr":"1-15(周)","jieci":9},{"id":0,"jwUser":null,"name":"编译原理","jiaoshi":"瑞樟10-405","laoshi":"严礽麒副教授","zhouci":",1,3,5,7,9,11,13,15,","xnxq":null,"zhouciStr":"1-16(单周)","jieci":11}],"zhouci":1}
     * cookie : JSESSIONID=3E1C7FD6FFD49255B2436F48CEF2F268; Path=/
     */

    private int code;
    private String message;
    private DataBean data;
    private String cookie;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public static class DataBean {
        /**
         * tab : {"0":[-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"1":[-1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"3":[-1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"5":[-1,-1,-1,-1,-1,-1,3,3,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"8":[-1,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"9":[-1,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],"11":[-1,6,-1,6,-1,6,-1,6,-1,6,-1,6,-1,6,-1,6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]}
         * kecheng : [{"id":0,"jwUser":null,"name":"软件测试（软件方向）","jiaoshi":"瑞樟6-406[数计]","laoshi":"马阿曼讲师（高校）","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,","xnxq":null,"zhouciStr":"1-15(周)","jieci":0},{"id":0,"jwUser":null,"name":"编译原理","jiaoshi":"瑞樟10-405","laoshi":"严礽麒副教授","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,","xnxq":null,"zhouciStr":"1-16(周)","jieci":1},{"id":0,"jwUser":null,"name":"Windows应用系统开发（软件方向）","jiaoshi":"瑞樟6-409","laoshi":"亓文娟副教授","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,","xnxq":null,"zhouciStr":"1-15(周)","jieci":3},{"id":0,"jwUser":null,"name":"移动产品UI设计与实验","jiaoshi":"同文1-304","laoshi":"王峰工程师,韩存鸽讲师（高校）","zhouci":",6,7,","xnxq":null,"zhouciStr":"6-7(周)","jieci":5},{"id":0,"jwUser":null,"name":"Windows应用系统开发（软件方向）","jiaoshi":"瑞樟6-409","laoshi":"亓文娟副教授","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,","xnxq":null,"zhouciStr":"1-15(周)","jieci":8},{"id":0,"jwUser":null,"name":"软件测试（软件方向）","jiaoshi":"瑞樟6-402[数计]","laoshi":"马阿曼讲师（高校）","zhouci":",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,","xnxq":null,"zhouciStr":"1-15(周)","jieci":9},{"id":0,"jwUser":null,"name":"编译原理","jiaoshi":"瑞樟10-405","laoshi":"严礽麒副教授","zhouci":",1,3,5,7,9,11,13,15,","xnxq":null,"zhouciStr":"1-16(单周)","jieci":11}]
         * zhouci : 1
         */

        private Map<String, List<Integer>> tab;
        private int zhouci;
        private List<KechengBean> kecheng;

        public Map<String, List<Integer>> getTab() {
            return tab;
        }

        public void setTab(Map<String, List<Integer>> tab) {
            this.tab = tab;
        }

        public int getZhouci() {
            return zhouci;
        }

        public void setZhouci(int zhouci) {
            this.zhouci = zhouci;
        }

        public List<KechengBean> getKecheng() {
            return kecheng;
        }

        public void setKecheng(List<KechengBean> kecheng) {
            this.kecheng = kecheng;
        }


        public static class KechengBean {
            /**
             * id : 0
             * jwUser : null
             * name : 软件测试（软件方向）
             * jiaoshi : 瑞樟6-406[数计]
             * laoshi : 马阿曼讲师（高校）
             * zhouci : ,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,
             * xnxq : null
             * zhouciStr : 1-15(周)
             * jieci : 0
             */

            private int id;
            private String jwUser;
            private String name;
            private String jiaoshi;
            private String laoshi;
            private String zhouci;
            private String xnxq;
            private String zhouciStr;
            private int jieci;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getJwUser() {
                return jwUser;
            }

            public void setJwUser(String jwUser) {
                this.jwUser = jwUser;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getJiaoshi() {
                return jiaoshi;
            }

            public void setJiaoshi(String jiaoshi) {
                this.jiaoshi = jiaoshi;
            }

            public String getLaoshi() {
                return laoshi;
            }

            public void setLaoshi(String laoshi) {
                this.laoshi = laoshi;
            }

            public String getZhouci() {
                return zhouci;
            }

            public void setZhouci(String zhouci) {
                this.zhouci = zhouci;
            }

            public String getXnxq() {
                return xnxq;
            }

            public void setXnxq(String xnxq) {
                this.xnxq = xnxq;
            }

            public String getZhouciStr() {
                return zhouciStr;
            }

            public void setZhouciStr(String zhouciStr) {
                this.zhouciStr = zhouciStr;
            }

            public int getJieci() {
                return jieci;
            }

            public void setJieci(int jieci) {
                this.jieci = jieci;
            }
        }
    }
}
