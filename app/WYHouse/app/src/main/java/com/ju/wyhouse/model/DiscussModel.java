package com.ju.wyhouse.model;

import java.util.List;

/**
 * Author:gaoju
 * Date:2020/2/10 17:43
 * Path:com.ju.wyhouse.model
 * Desc:墙评论Model
 */
public class DiscussModel {


    /**
     * code : 0
     * message : 成功
     * data : [{"commentId":579,"user":{"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"},"wallId":198,"content":"回复","parentId":0,"targetId":0,"createTime":"2020-02-02 21:09:49","status":0},{"commentId":580,"user":{"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"},"wallId":198,"content":"回复啊的施工阿桑的歌阿桑的歌阿桑的歌俺是个大赛夺冠阿桑的歌阿桑的歌阿斯顿噶似的噶围观给娃儿狗娃花地方","parentId":0,"targetId":0,"createTime":"2020-02-03 21:10:33","status":0},{"commentId":581,"user":{"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"},"wallId":198,"content":"回3333","parentId":0,"targetId":0,"createTime":"2020-02-04 21:10:38","status":0},{"commentId":582,"user":{"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"},"wallId":198,"content":"回复444","parentId":0,"targetId":0,"createTime":"2020-02-05 21:10:43","status":0},{"commentId":583,"user":{"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"},"wallId":198,"content":"回555","parentId":0,"targetId":0,"createTime":"2020-02-06 21:11:09","status":0},{"commentId":584,"user":{"id":120},"wallId":198,"content":"111","parentId":0,"targetId":0,"createTime":"2020-02-11 15:47:38","status":0},{"commentId":585,"user":{"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"},"wallId":198,"content":"e而给娃儿给","parentId":0,"targetId":0,"createTime":"2020-02-07 15:58:35","status":0},{"commentId":586,"user":{"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"},"wallId":198,"content":"agwe阿桑的歌按时的各位阿三","parentId":0,"targetId":0,"createTime":"2020-02-11 15:59:54","status":0},{"commentId":587,"user":{"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"},"wallId":198,"content":"agwe阿桑的歌按时的各位阿三","parentId":0,"targetId":0,"createTime":"2020-02-11 15:59:55","status":0},{"commentId":588,"user":{"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"},"wallId":198,"content":"agwe阿桑的歌按时的各位阿三","parentId":0,"targetId":0,"createTime":"2020-02-11 15:59:56","status":0}]
     * cookie : null
     */

    private int code;
    private String message;
    private String cookie;
    private List<DataBean> data;

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

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * commentId : 579
         * user : {"id":1,"name":"zxz","image":"http://image1.wywsw.com/hlw.png"}
         * wallId : 198
         * content : 回复
         * parentId : 0
         * targetId : 0
         * createTime : 2020-02-02 21:09:49
         * status : 0
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
             * id : 1
             * name : zxz
             * image : http://image1.wywsw.com/hlw.png
             */

            private int id;
            private String name;
            private String image;

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
        }
    }
}
