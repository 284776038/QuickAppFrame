package cn.bfy.frame.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import cn.bfy.frame.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * <pre>
 * @copyright  : Copyright ©2004-2018 版权所有　XXXXXXXXXXXXXXX
 * @company    : XXXXXXXXXXXXXXX
 * @author     : OuyangJinfu
 * @e-mail     : jinfu123.-@163.com
 * @createDate : 2017/6/19 0019
 * @modifyDate : 2017/6/19 0019
 * @version    : 1.0
 * @desc       :
 * </pre>
 */

public class NotificationUtil {

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    public static void showProgressNotification(Context context, String title, int progress, int id){
        /**
         * 几乎Android系统的每一个版本都会对通知这部分功能进行获多或少的修改，API不稳定行问题在通知上面凸显的尤其严重。
         * 解决方案是：用support库中提供的兼容API。support-v4库中提供了一个NotificationCompat类，使用它可以保证我们的
         * 程序在所有的Android系统版本中都能正常工作。
         */
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        //设置通知的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置通知的大图标，当下拉系统状态栏时，就可以看到设置的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));
        //当通知被点击的时候，跳转到MainActivity中
//        builder.setContentIntent(pi);
        //设置通知的标题
        builder.setContentTitle(title);
        if(progress>=0){
            //当progress大于或等于0时，才需要显示下载进度
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }else{
            //当progress小于0时，显示下载失败
            builder.setContentText("下载失败,请重新尝试");
//            builder.setProgress(100,progress,false);
        }
        getNotificationManager(context).notify(id,builder.build());
    }

    public static void showPendingIntentNotification(Context context, String title, Intent intent, int id){
        /**
         * 几乎Android系统的每一个版本都会对通知这部分功能进行获多或少的修改，API不稳定行问题在通知上面凸显的尤其严重。
         * 解决方案是：用support库中提供的兼容API。support-v4库中提供了一个NotificationCompat类，使用它可以保证我们的
         * 程序在所有的Android系统版本中都能正常工作。
         */
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        //设置通知的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //设置通知的大图标，当下拉系统状态栏时，就可以看到设置的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));

        //设置通知的标题
        builder.setContentTitle(title);
        //当通知被点击
        if(intent != null){
            //当progress大于或等于0时，才需要显示下载进度
            PendingIntent pendingIntent = PendingIntent.
                getActivity(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }
        builder.setAutoCancel(true);
        getNotificationManager(context).notify(id,builder.build());
    }

}
