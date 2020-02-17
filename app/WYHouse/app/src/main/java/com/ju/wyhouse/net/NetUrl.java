package com.ju.wyhouse.net;

import com.ju.wyhouse.entity.Constants;
import com.ju.wyhouse.utils.SaveAndWriteUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Author:gaoju
 * Date:2020/1/18 15:14
 * Path:com.ju.wyhouse.net
 * Desc: 接口拼接
 */
public class NetUrl {


    //头flag
    private static final String HEAD_FLAG = "Wsw-Flag";
    //主字段
    public static final String BASE_URL = "https://www.wywsw.com";
    //登录字段
    private static final String LOGIN_URL = "/user/loginAppByUser";
    //注册字段
    public static final String REGISTER_URL = "/user/registerApp";
    //flag登录
    private static final String FLAG_URL = "/user/loginApp";
    //成绩字段
    private static final String SCORE_URL = "//chengji/getRealTimeChengjiList";
    //总分和绩点字段
    private static final String ALL_SCORE_URL = "//chengji/zongfenPaiming";
    //空教室字段
    private static final String ALL_NULL_CLASSROOM_URL = "/jiaoshi/getKongjiaoshi";
    //班级列表字段
    private static final String ALL_COURSE_CLASS_URL = "//kebiao/getBanjiInfo";
    //全部班级中的班级课表字段
    private static final String ALL_CLASS_COURSE_URL = "//kebiao/getBanjiKebiao";
    //墙字段
    private static final String WALL_DATA_URL = "/wall/list";
    //上传图片文件
    private static final String UPLOAD_IMAGE_URL = "https://wywsw.com:9000/fileUpload";
    //默认背景图片
    private static final String BG_URL = "https://7773-wsw-hello-1300224476.tcb.qcloud.la/bag1.jpg?sign=0a5cc9f2546fc58afb8cca716fcc9478&t=1579676671";
    //发送墙字段
    private static final String SEND_WALL_URL = "/wall/publish";
    //个人课程表查找字段
    private static final String ME_COURSE_URL = "/kebiao/getGerenKebiao";
    //成绩详细信息字段
    private static final String SCORE_MESSAGE_URL = "//chengji/chengjiDetail";
    //墙点赞字段
    private static final String LIKE_WALL_URL = "/wall/like";
    //取消点赞字段
    private static final String UN_LIKE_WALL_URL = "/wall/unLike";
    //获取单个墙数据字段
    private static final String SINGLE_WALL_DATA_URL = "/wall/get";
    //获取墙评论字段
    private static final String WALL_DISCUSS_URL = "/wall/commentList";
    //发送评论字段
    private static final String SEND_WALL_DISCUSS_URL = "/wall/comment";
    //删除评论字段
    private static final String DEL_WALL_DISCUSS_URL = "/wall/delComment";
    //上传头像字段
    private static final String UP_LOAD_CIR_OR_NAME_URL = "/user/inofoUpdate";
    //重置教务处密码字段
    private static final String FORGET_JW_URL = "/user/findJwPwd";
    //更新教务处字段
    public static final String UPDATE_JW_URL = "/user/upJwInfo";
    //找回万事屋ID字段
    private static final String FIND_ID_URL = "https://wywsw.com:9000/findIdByJwUser";


    //注册Request
    public static Request getRegisterRequest(String account, String password, String WYpassword) {
        Map<String, String> map = new HashMap<>();
        map.put("jwUser", account);
        map.put("jwPassword", password);
        map.put("password", WYpassword);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + REGISTER_URL, Constants.REQUEST_GET, null, map);

    }


    //wswFlag登录 查询用户信息也可以用此接口
    public static Request getFlagIdLoginRequest() {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + FLAG_URL, Constants.REQUEST_GET, null, map);
    }


    //账号和密码登录Request
    public static Request getLoginRequest(String account, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("key", account);
        map.put("password", password);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + LOGIN_URL, Constants.REQUEST_GET, null, map);
    }

    //成绩查找Request xnxq可以加学期时间比如2019-2020
    public static Request getScoreRequest(String week) {
        Map<String, String> map = new HashMap<>();
        map.put("cookie", SaveAndWriteUtil.getCookie());
        map.put("xnxq", week);
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + SCORE_URL, Constants.REQUEST_GET, null, map);
    }


    //获取总分和绩点
    public static Request getAllScoreRequest(String week) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("xnxq", week);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + ALL_SCORE_URL, Constants.REQUEST_GET, null, map);

    }


    //课程表查找,周次
    public static Request getCourseRequest(String term) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("xnxq", term);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + ME_COURSE_URL, Constants.REQUEST_GET, null, map);
    }

    //全部课程获取班级列表
    public static Request getAllClassListRequest() {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + ALL_COURSE_CLASS_URL, Constants.REQUEST_GET, null, map);

    }


    //获取班级课程表
    public static Request getAllClassCourseRequest(String week, String classRoom) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("xnxq", week);
        map.put("banji", classRoom);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + ALL_CLASS_COURSE_URL, Constants.REQUEST_GET, null, map);
    }

    //获取全部空教室
    public static Request getAllClassRoomRequest(String mainBuild, String build, String date) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("xiaoqu", mainBuild);
        map.put("jiaoxuelou", build);
        map.put("date", date);
        map.put("cookie", SaveAndWriteUtil.getCookie());
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + ALL_NULL_CLASSROOM_URL, Constants.REQUEST_GET, null, map);
    }


    //获取成绩课程详细信息Request
    public static Request getCourseScoreMessageRequest(String url) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("cookie", SaveAndWriteUtil.getCookie());
        map.put("url", url);
        return HttpRequest.getInstance().
                createRequestParams(BASE_URL + SCORE_MESSAGE_URL, Constants.REQUEST_GET, null, map);

    }


    //获取墙数据请求
    public static Request getWallDataRequest(String lastId) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("lastId", lastId);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + WALL_DATA_URL, Constants.REQUEST_GET, null, map);
    }

    //上传图片
    public static Request getUpLoadImageFile(File file) {
        //设置文件上传封装Request
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/"), file);
        //设置表单
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)
                .build();
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());

        return HttpRequest.getInstance()
                .createRequestParams(UPLOAD_IMAGE_URL, Constants.REQUEST_POST, formBody, map);
    }

    //获取背景图片
    public static String getBgUrl() {
        return BG_URL;
    }

    //发送墙
    public static Request getSendWallRequest(String content, int anonymous, int backgroundColor, int avatarColor, String imageUrl) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("content", content);
        map.put("anonymous", String.valueOf(anonymous));
        map.put("backgroundColor", String.valueOf(backgroundColor));
        map.put("avatarColor", String.valueOf(avatarColor));
        map.put("backgroundImage", imageUrl);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + SEND_WALL_URL, Constants.REQUEST_GET, null, map);
    }

    //墙点赞
    public static Request getLikeWallRequest(int wallId) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("wallId", String.valueOf(wallId));
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + LIKE_WALL_URL, Constants.REQUEST_GET, null, map);

    }

    //获取单个墙数据
    public static Request getSingleWallDataRequest(int wallId) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("wallId", String.valueOf(wallId));
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + SINGLE_WALL_DATA_URL, Constants.REQUEST_GET, null, map);

    }

    //取消墙点赞字段
    public static Request getUnLikeWallRequest(int wallId) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("wallId", String.valueOf(wallId));
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + UN_LIKE_WALL_URL, Constants.REQUEST_GET, null, map);

    }

    //获取墙评论Request
    public static Request getWallDiscussRequest(int wallId, String lastId) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("wallId", String.valueOf(wallId));
        map.put("lastId", lastId);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + WALL_DISCUSS_URL, Constants.REQUEST_GET, null, map);

    }

    //发送评论
    public static Request getWallDiscussSendRequest(int wallId, String content) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("wallId", String.valueOf(wallId));
        map.put("content", content);
        return HttpRequest.getInstance().createRequestParams(BASE_URL + SEND_WALL_DISCUSS_URL, Constants.REQUEST_GET
                , null, map);
    }

    //删除评论
    public static Request getDelWallDiscussRequest(int wallId, int commentId) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("wallId", String.valueOf(wallId));
        map.put("commentId", String.valueOf(commentId));
        return HttpRequest.getInstance().createRequestParams(BASE_URL + DEL_WALL_DISCUSS_URL,
                Constants.REQUEST_GET, null, map);
    }

    //上传头像和昵称，更新个人信息
    public static Request getUpDateMeInfoRequest(String imageUrl, String name) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("image", imageUrl);
        map.put("name", name);
        return HttpRequest.getInstance().createRequestParams(BASE_URL + UP_LOAD_CIR_OR_NAME_URL,
                Constants.REQUEST_GET, null, map);
    }

    //重置教务处
    public static Request getForgetJwAccountRequest(String account, String bodyNumber) {
        Map<String, String> map = new HashMap<>();
        map.put("username", account);
        map.put("sfzhm", bodyNumber);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + FORGET_JW_URL, Constants.REQUEST_GET, null, map);
    }


    /**
     * 绑定教务处密码
     *
     * @param jwUser
     * @param jwPassword
     * @return
     */
    public static Request getUpdateAccountRequest(String jwUser, String jwPassword) {
        Map<String, String> map = new HashMap<>();
        map.put(HEAD_FLAG, SaveAndWriteUtil.getFlagId());
        map.put("jwUser", jwUser);
        map.put("jwPassword", jwPassword);
        return HttpRequest.getInstance()
                .createRequestParams(BASE_URL + UPDATE_JW_URL, Constants.REQUEST_GET, null, map);
    }


    public static Request getFindIdRequest(String account) {
        Map<String, String> map = new HashMap<>();
        map.put("jwUser", account);
        return HttpRequest.getInstance()
                .createRequestParams(FIND_ID_URL, Constants.REQUEST_GET, null, map);
    }
}
