package cn.richinfo.frame.network;

/**
 * author : Pan
 * time   : 2017/4/20
 * desc   : 网络接口请求路径及参数
 * version: 1.0
 * <p>
 * Copyright: Copyright (c) 2017
 * Company:深圳彩讯科技有限公司
 */

public interface RichApi {

    /**
     * 获取验证码
     *
     * @param mobile 手机号码
     * @param type   0 注册， 1 忘记密码
     * @return
     */
//    @GET("/roodo-mgr/sendSmsValidateCode.do")
//    Observable<VerifyCodeModel> getVerifyCode(@Query("mobile") String mobile, @Query("sendtype") int type);


    /**
     * 注册
     *
     * @param registerBody 注册请求参数对象
     * @return
     */
//    @POST("/roodo-mgr/register.do")
//    Observable<RegisterModel> register(@Body RegisterBody registerBody);


}
