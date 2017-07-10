package cn.richinfo.frame.db;

import cn.richinfo.frame.RichApplication;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　彩讯科技股份有限公司
 * @company    : 彩讯科技股份有限公司
 * @author     : OuyangJinfu
 * @e-mail     : ouyangjinfu@richinfo.cn
 * @createDate : 2017/7/10 0010
 * @modifyDate : 2017/7/10 0010
 * @version    : 1.0
 * @desc       : 数据库管理类
 * </pre>
 */

public class DBManager {

    private static DBManager mInstance;

    private DaoMaster mMaster;
    private DaoSession mSession;

    private DBManager(){
        init();
    }

    private void init(){
        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(RichApplication.mContext, "richinfo.db");
        mMaster = new DaoMaster(helper.getWritableDatabase());
        mSession = mMaster.newSession();
    }

    public static DBManager getInstance(){
        if (mInstance == null) {
            mInstance = new DBManager();
        }
        return mInstance;
    }

    public DaoMaster getmMaster() {
        return mMaster;
    }

    public DaoSession getmSession() {
        return mSession;
    }
}
