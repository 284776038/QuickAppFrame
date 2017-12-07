package cn.richinfo.frame.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import cn.richinfo.frame.download.Request;

/**
 * Description: 管理DownLoadItem,<br/>
 * 用于{@link cn.richinfo.roodo.receiver.DownloadManagerReceiver} {@link cn.richinfo.roodo.webclient.RodoDownLoadListener} <br/>
 *
 * Copyright: Copyright (c) 2017 <br/>
 * Company:XXXXXXXXXXXXXXXXXXXX <br/>
 * Email:284425176@qq.com <br/>
 *
 * @author suma on 2017/5/9
 * @version 1.0
 */

public class DownLoadItemManager {
    private static AtomicReference<List<DownLoadItem>> INSTANCE = new AtomicReference<>(null);

    public static List<DownLoadItem> getInstance(){
        for (;;){
            List<DownLoadItem> curr = INSTANCE.get();
            if (curr != null)
                return curr;
            curr = new ArrayList<>(30);
            if(INSTANCE.compareAndSet(null,curr))
                return curr;
        }
    }

    public static DownLoadItem getDownLoadItemById(long downloadId){
        List<DownLoadItem> list = getInstance();
        if(list.size() == 0){
            return null;
        }
        DownLoadItem temp = null;
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i);
            if (temp.downloadId == downloadId)
                return temp;
        }
        return null;
    }

    public static void removeByDownloadId(long id){
        List<DownLoadItem> list = getInstance();
        Iterator<DownLoadItem> iter = list.iterator();
        DownLoadItem temp = null;
        while(iter.hasNext()){
            temp = iter.next();
            if (temp.downloadId == id) {
                iter.remove();
                return;
            }
        }
    }

    public static DownLoadItem removeByPackageName(String packageName){
        List<DownLoadItem> list = getInstance();
        Iterator<DownLoadItem> iter = list.iterator();
        DownLoadItem temp = null;
        while(iter.hasNext()){
            temp = iter.next();
            if (temp.packageName.equals(packageName)) {
                iter.remove();
                return temp;
            }
        }
        return null;
    }

    public static void add(DownLoadItem item){
        List<DownLoadItem> list = getInstance();
        list.add(item);
    }

    /**
     * 记录通过DownLoadManager下载过的文件信息
     */
    public static class DownLoadItem{
        private long downloadId;
        private String url;
        private String filePath;
        private long contentLength;
        private String mimeType;
        private String packageName;


        public DownLoadItem(long downloadId, String url) {
            this.downloadId = downloadId;
            this.url = url;
        }

        public DownLoadItem(Request request) {
            this.url = request.url;
            this.packageName = request.packageName;
            this.filePath = request.filePath;
        }

        public long getDownloadId() {
            return downloadId;
        }

        public String getUrl() {
            return url;
        }

        public long getContentLength() {
            return contentLength;
        }

        public void setContentLength(long contentLength) {
            this.contentLength = contentLength;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("DownLoadItem = [");
            sb.append("url = ").append(url);
            sb.append("\ndownloadId = ").append(downloadId);
            sb.append("\npackageName = ").append(packageName);
            sb.append("\nfilePath = ").append(filePath);
            sb.append("\ncontentLength = ").append(contentLength);
            sb.append("\nmimeType = ").append(mimeType);
            sb.append("]");
            return sb.toString();
        }
    }
}
