package cn.richinfo.frame.util;

import cn.richinfo.frame.BuildConfig;

/**
 * author : Pan
 * time   : 2017/4/19
 * desc   : 常量类
 * version: 1.0
 * <p>
 * Copyright: Copyright (c) 2017
 * Company:XXXXXXXXXXXXXXXXXXXX
 */
public class Constant {
    // 基本URL
    public static final  String BASE_URL = BuildConfig.HOST;;
    // 用户对象
    public static final  String USER_INFO = "UserInfo";
    public static final  String VERSION_CODE = "Version";
    //更新信息
    public static final String UPDATE_INFO = "update_info";
    //抽奖信息
    public static final String PRIZE_INFO = "prize_info";

    /*=======webview begin=========*/
    public static final String DATABASES_SUB_FOLDER = "/databases";
    /**
     * 设置操作系统,用于JS辨别操作系统
     */
    public static final String WEBVIEW_UA = "RoodoForAndroid";
    /**
     * JS调用原生的对象,调用方式 window.newWindowInterface.xxx()
     */
    public static final String WEBVIEW_JS_OBJECT = "newWindowInterface";
    /**
     * 用于创建同名文件夹
     */
    public static final String APP_NAME = "Roodo";
    /**
     * 下载目录名
     */
    public static final String DIR_NAME_DOWNLOAD = "Download";
    /**
     * cookie的key,值对应sKey
     */
    public static final String COOKIE_KEY = "kdzpKey=";

    /*=======webview end=========*/

    //极光推送别名设置成功标记
    public static String JPUSH_SET_ALIAS_MARK = "JPUSH_SET_ALIAS_MARK";
    //极光推送注册成功后的唯一标识
    public static String JPUSH_REGISTRATION_ID = "JPUSH_REGISTRATION_ID";


    /**
     * 个人首页H5页面链接
     */
    //被邀请人
    public static final String INVITEE_URL = BASE_URL + "/roodo-web/html/invitee.html";

    //被邀请人
    public static final String INVITE_FRIEND_URL = BASE_URL + "/roodo-web/html/inviteFriends.html";

    //等级中心
    public static final String LEVEL_CENTER_URL = BASE_URL + "/roodo-web/html/gradeCenter.html";

    //话费兑换
    public static final String BILL_URL = BASE_URL + "/roodo-web/html/callExchange.html";

    //流量兑换
    public static final String FLOW_URL = BASE_URL + "/roodo-web/html/flowExchange.html";

    //我的背包
    public static final String MY_PACKEGE_URL = BASE_URL + "/roodo-web/html/myPacket.html";

    //我的喜豆
    public static final String MY_LEBI_URL = BASE_URL + "/roodo-web/html/myLeCoin.html";

    //商品详情
    public static final String GOODS_DETAIL_URL = BASE_URL + "/roodo-web/html/spendGoodsDetail.html?id=";

    //活动规则
    public static final String RULES_DETAIL_URL = BASE_URL + "/roodo-web/html/rule.html";

    //抽奖记录
    public static final String DRAW_URL = BASE_URL + "/roodo-web/html/myDraw.html";

    //视频fenx分享
    public static final String SHARE_URL = BASE_URL + "/roodo-web/html/video.html";


    //下载任务
    public static final String DOWNLOAD_TASK_URL = BASE_URL + "/roodo-web/html/applicationDetail.html?pkgname=";

    //调查任务
    public static final String SURVEY_TASK_URL = BASE_URL + "/roodo-web/AFQ/Q1.html?qid=";


    //赚钱
    public static final String MAKE_MONEY_URL = BASE_URL + "/roodo-web/html/mainMakeMoney.html";

    //花钱
    public static final String SPEND_MONEY_URL = BASE_URL + "/roodo-web/html/spendMoney.html";

    //注册协议条款
    public static final String REGISTER_URL = BASE_URL + "/roodo-web/html/Registration.html";

    //发布任务
    public static final String PUBLISH_TASK = BASE_URL + "/roodo-web/html/releaseTask.html";

}
