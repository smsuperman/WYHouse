package com.ju.wyhouse.model;

/**
 * Author:gaoju
 * Date:2020/1/18 15:48
 * Path:com.ju.wyhouse.model
 * Desc:个人用户Model
 */
public class UserModel {


    /**
     * code : 0
     * message : 成功
     * data : {"id":120,"name":"null","image":"http://image1.wywsw.com/1581430931146.png","jwUser":"20161041313","jwName":"徐庄雄","jwBanji":"数16计科3","wswFlag":"app:120:70a96e3f-56d4-4462-a67f-797a5c85b04f"}
     * cookie : null
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
         * id : 120
         * name : null
         * image : http://image1.wywsw.com/1581430931146.png
         * jwUser : 20161041313
         * jwName : 徐庄雄
         * jwBanji : 数16计科3
         * wswFlag : app:120:70a96e3f-56d4-4462-a67f-797a5c85b04f
         */

        private int id;
        private String name;
        private String image;
        private String jwUser;
        private String jwName;
        private String jwBanji;
        private String wswFlag;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getJwUser() {
            return jwUser;
        }

        public void setJwUser(String jwUser) {
            this.jwUser = jwUser;
        }

        public String getJwName() {
            return jwName;
        }

        public void setJwName(String jwName) {
            this.jwName = jwName;
        }

        public String getJwBanji() {
            return jwBanji;
        }

        public void setJwBanji(String jwBanji) {
            this.jwBanji = jwBanji;
        }

        public String getWswFlag() {
            return wswFlag;
        }

        public void setWswFlag(String wswFlag) {
            this.wswFlag = wswFlag;
        }
    }
}
