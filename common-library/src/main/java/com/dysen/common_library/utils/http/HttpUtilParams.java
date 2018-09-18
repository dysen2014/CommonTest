package com.dysen.common_library.utils.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dysen.common_library.utils.LogUtils;
import com.dysen.common_library.utils.SharedPreUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by integrated on 2018/4/20.
 * * 服务器请求操作类
 * 所有服务器的链接请求都封装在这个类中
 */
public class HttpUtilParams {
    // 服务器地址
    public static String SERVER_ADDRESS = "http://edu-pad-dev.integrated.cn";
    public static String RESOURCE_SERVER_ADDRESS = "http://edu-res-dev.integrated.cn";
    // 公共接口
    private static final String GET_RECIPSE = "/recipes/dau_get"; // 绑定设备
    private static final String BIND_KPAD = "/pub/bind_kpad"; // 绑定设备
    private static final String HEART_BEAT_KPAD = "/pub/heartbeat_kapd"; // 心跳
    private static final String GET_TIME = "/pub/time"; // 获取服务器时间，格式：yyyy-MM-dd HH:mm:ss
    private static final String GET_TIME_MILLIS = "/pub/time_millis"; // 获取服务器
    // 时间戳
    private static final String ERROR_CODE = "/pub/error_codes";    // 获取错误代码
    private static final String GET_RESOURCE = "/pub/host_res";  //获取资源下载地址
    // 获取信息接口
    private static final String GET_SCHOOL_INFO = "/sv-data/data/sch/sch_get_rds";  // 获取学校信息
    private static final String GET_CLASS_INFO = "/sv-data/data/sch/cla_get_rds";    // 获取班级信息
    private static final String GET_TEACHER_LIST = "/sv-data/data/tch/list";    // 获取教师列表
    private static final String GET_CLASS_TEACHER_LIST = "/sv-data/data/sch/cla_tch_get";        // 获取班级-教师关系列表
    private static final String GET_STUDENT_LIST = "/sv-data/data/stu/stu_list";            // 获取学生信息
    private static final String GET_FAMILY_LIST = "/sv-data/data/stu/fam_list";    // 获取家长信息
    // 课程表接口
    private static final String GET_CUR_LIST = "/sv-data/data/cur/cur_list";    // 获取课程表列表
    // 通知接口
    private static final String GET_NOTICE = "/sv-xy/notice/get";         // 查询详细的公告内容
    private static final String GET_NOTICE_BY_CHANGE = "/sv-xy/notice/list_byut";   // 查询指定变更时间段内的通知
    private static final String GET_NOTICE_BY_PUBLISH = "/sv-xy/notice/list_byrt";   // 查询指定发布时间段内的通知
    // 德育接口
    private static final String GET_MORALITY_SCORE = "/sv-xy/moral/mc_get";   // 查询班级德育评分
    private static final String GET_MORALITY_RATER = "/sv-xy/moral/rater_get";   // 查询德育评分员列表
    private static final String SAVE_MORALITY_SCORE = "/sv-xy/moral/mc_rate";   // 查询班级德育评分
    private static final String GET_CLASS_HONOR = "/sv-xy/honor/hc_list";     // 查询班级点评
    private static final String GET_STUDENT_HONOR = "/sv-xy/honor/hs_list";    // 查询学生荣誉
    // 风采接口
    private static final String GET_CLASS_STYLE = "/sv-xy/mien/mien_get";   // 查询班级风采
    private static final String GET_ALL_PHOTO_ALBUM = "/sv-xy/mien/album_list";   // 查询学校/班级所有相册
    // 校园公播
    private static final String GET_AFFICHE_LIST = "/sv-xy/play/affiche/list";    // 查询一天内待显示的紧急公告
    private static final String GET_EXAMINATION = "/sv-xy/play/exam/get";    // 查询一天内待显示的紧急公告
    private static final String GET_PRV_VIDEO = "/sv-xy/play/video/get_prv";     // 查询本设备轮播视频
    private static final String GET_PUB_VIDEO = "/sv-xy/play/video/get_pub";   // 查询公共轮播视频
    // 考勤接口
    private static final String GET_ATTENDANCE_CONFIG = "/sv-kq/kq/group_list_by_cid"; // 获取考勤配置
    private static final String GET_ATTENDANCE_STATISTICS = "/sv-kq/kq/cla_statis_list";    // 获取学生考勤信息
    private static final String GET_ATTENDANCE_DETAIL = "/sv-kq/kq/stu_detail_paging";    // 获取学生考勤信息
    private static final String UPLOAD_ATTENDANCE = "/kq/upload_data";  // 上传考勤数据
    private static final String UPLOAD_PHOTO = "/kq/upload_pic";        // 上传考勤图片
    // 设备接口
    private static final String GET_WEATHER = "/sv-xy/comm/weather";    // 获取天气信息
    private static final String GET_DEV_MANAGEMENT = "/sv-data/dev/dev_mana_get";     // 获取管理员卡号
    private static final String GET_LAST_VERSION = "/sv-data/dev/app/get_latest_ver";     // 获取最新版本APP
    private static final String GET_API_LIST = "/sv-data/dev/api_list";     // 获取设备允许请求的服务器地址

    //返回的数据解析
    public static final String RESPONSE_STATUS = "s"; // 状态码
    public static final String RESPONSE_MESSAGE = "m"; // 提示信息
    public static final String RESPONSE_DATA = "d"; // 数据
    //状态码
    public static final int STATUS_SUCCESS = 1; // 成功
    public static final int STATUS_FAILURE = 2; // 失败
    public static final int STATUS_SERVER_ERROR = 3; // 服务器调用失败
    public static final int STATUS_SERVER_TIME_OUT = 4; //服务器调用超时
    public static final int STATUS_NO_DATA_PERMISSION = 13; // 无数据权限
    public static final int STATUS_PARAMS_ERROR = 400; // 参数错误
    public static final int STATUS_USER_NO_ACCESS = 401; // 客户未授权
    public static final int STATUS_USER_NO_PERMISSION = 403; // 客户无访问权限
    public static final int STATUS_NO_API = 404; // API不存在
    public static final int STATUS_API_TYPE_ERROR = 405; // API请求类型错误
    public static final int STATUS_API_ERROR = 500; // API内部错误
    // 参数
    public static final String DAY_BEGIN = "dayb";  // 开始日期
    public static final String DAY_END = "daye";  // 结束日期
    public static final String PARAMS_ID = "id";  // ID
    public static final String PARAMS_DEVICE_ID = "dcode";  // 设备ID
    public static final String PARAMS_DEVICE_TYPE = "dtype";  // 设备ID
    public static final String PARAMS_SCHOOL_ID = "hid";    //  学校ID
    public static final String PARAMS_CLASS_ID = "cid";     // 班级ID
    public static final String PARAMS_CLASS_NAME = "cname"; // 班级名称
    public static final String PARAMS_ROOM_ID = "rid";      // 教室ID
    public static final String PARAMS_ROOM_NAME = "rname";  // 教室名称
    public static final String PARAMS_TIME_STAMP = "ts";    // 时间戳
    public static final String PARAMS_PSW_TIME_STAMP = "_ts";    // 时间戳
    public static final String PARAMS_DEVICE_KEY = "_dkey";    // 设备标识
    public static final String PARAMS_PSS = "_pss";    // 时间戳
    public static final String PARAMS_TYPE = "type";   //数据变化标识
    public static final String PARAMS_TAGS = "tags";   //数据变化标识
    public static final String PARAMS_NUM = "num";   //错误代码
    public static final String PARAMS_VAL = "val";   //错误内容
    public static final String PARAMS_BEGIN = "begin";   //开始时间
    public static final String PARAMS_END = "end";   //结束时间
    public static final String VALUE_DEVICE_TYPE = "KPAD";   //结束时间

    // 单例
    private static HttpUtilParams SERVER_CLIENT;

    /**
     * 安全随机数
     */
    private static SecureRandom mRandom;
    /**
     * 加密算法
     */
    private static Cipher mEncryptCipher;
    /**
     * 解密算法
     */
    private static Cipher mDecryptCipher;
    /**
     * Base64 编码
     */
    private static Base64 B64;
    /**
     * 编码
     */
    private Charset UTF8;

    private static String mPassword; // 设备密码
    private static String mDeviceID; // 设备ID
    private static String mSchoolID; // 学校ID
    private static String mClassID; // 班级ID
    private static String mDeviceKey;  // 设备标识
    private static boolean mIsInitial; // 初始化标志

    /**
     * 获取服务器请求客户端
     *
     * @return 服务器请求客户端
     */
    public static HttpUtilParams getInstance() {
        syncInit();
        return SERVER_CLIENT;
    }

    private static synchronized void syncInit() {
        if (SERVER_CLIENT == null)
            SERVER_CLIENT = new HttpUtilParams();
    }

    public HttpUtilParams() {
        mRandom = new SecureRandom();
        UTF8 = Charset.forName("UTF-8");
    }


}
