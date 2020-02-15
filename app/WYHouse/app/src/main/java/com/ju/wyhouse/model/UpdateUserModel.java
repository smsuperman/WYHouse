package com.ju.wyhouse.model;

/**
 * Author:gaoju
 * Date:2020/2/13 15:13
 * Path:com.ju.wyhouse.model
 * Desc:更新用户Model
 */
public class UpdateUserModel {


    /**
     * code : 0
     * message : 成功
     * data : {"id":120,"name":"we","image":"http://image1.wywsw.com/1581602089746.jpg","jwUser":"20161041313","jwName":"徐庄雄","jwBanji":"数16计科3"}
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
         * name : we
         * image : http://image1.wywsw.com/1581602089746.jpg
         * jwUser : 20161041313
         * jwName : 徐庄雄
         * jwBanji : 数16计科3
         */

        private int id;
        private String name;
        private String image;
        private String jwUser;
        private String jwName;
        private String jwBanji;

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
    }
}
