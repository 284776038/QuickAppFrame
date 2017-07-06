package cn.richinfo.frame.download;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by suma on 2017/5/26.
 */

public class Request implements Parcelable {

    /**
     * 用于生成不重复的id
     */
    static final AtomicInteger num = new AtomicInteger(0xff02);

    public String url;
    public String packageName;
    public final int id;
    public String filePath;
//    public String fileName;
//    public final long timeMillis;


    /**
     *
     * @param url
     * @param packageName
     */
    public Request(String url, String packageName) {
        super();
        int curr = num.get();
        num.compareAndSet(curr,curr + 1);
        this.url = url;
        this.packageName = packageName;
        this.id = curr;
//        this.fileName = fileName;
//        timeMillis = SystemClock.currentThreadTimeMillis();
    }

    public Request(Parcel in) {
        this.url = in.readString();
        this.packageName = in.readString();
//        this.fileName = in.readString();
//        this.timeMillis = in.readLong();
        this.id = in.readInt();
        this.filePath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(packageName);
//        parcel.writeString(fileName);
//        parcel.writeLong(timeMillis);
        parcel.writeInt(id);
        parcel.writeString(filePath);
    }

	public static final Creator<Request> CREATOR = new Creator<Request>(){

		@Override
		public Request createFromParcel(Parcel parcel) {
			return new Request(parcel);
		}

		@Override
		public Request[] newArray(int i) {
			return new Request[i];
		}

	};


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Request:");
        sb.append("\nurl = ").append(url);
//        sb.append("fileName = ").append(fileName).append("\n");
        sb.append("\npackageName = ").append(packageName);
        sb.append("\nid = ").append(id);
        sb.append("\nfilePath = ").append(filePath);
        sb.append("]\n");
        return sb.toString();
    }

}
