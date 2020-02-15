package com.ju.wyhouse.model;

/**
 * Author:gaoju
 * Date:2020/2/11 15:51
 * Path:com.ju.wyhouse.model
 * Desc:评论结果Model
 */
public class DiscussResultModel {


    /**
     * code : 0
     * message : 待管理员审核后显示
     * data : {"commentId":584,"user":{"id":120,"name":"null","image":"null","jwUser":"20161041313","jwName":"徐庄雄","jwBanji":"数16计科3"},"wallId":198,"content":"111","parentId":0,"targetId":0,"createTime":null,"status":1}
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
         * commentId : 584
         * user : {"id":120,"name":"null","image":"null","jwUser":"20161041313","jwName":"徐庄雄","jwBanji":"数16计科3"}
         * wallId : 198
         * content : 111
         * parentId : 0
         * targetId : 0
         * createTime : null
         * status : 1
         */

        private int commentId;
        private UserBean user;
        private int wallId;
        private String content;
        private int parentId;
        private int targetId;
        private String createTime;
        private int status;

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public int getWallId() {
            return wallId;
        }

        public void setWallId(int wallId) {
            this.wallId = wallId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public int getTargetId() {
            return targetId;
        }

        public void setTargetId(int targetId) {
            this.targetId = targetId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class UserBean {
            /**
             * id : 120
             * name : null
             * image : null
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
}
