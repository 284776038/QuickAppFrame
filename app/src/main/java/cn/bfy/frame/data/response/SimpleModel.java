package cn.bfy.frame.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * author: shell
 * date 2017/3/13 下午8:52
 **/
public class SimpleModel {

    @SerializedName("retcode")
    @Expose
    private int retcode;

    @SerializedName("message")
    @Expose
    private String message;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "SimpleModel{" +
                "retcode=" + retcode +
                ", message='" + message + '\'' +
                '}';
    }
}
