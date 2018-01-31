package com.bean.xhttp;


import android.text.TextUtils;
import android.util.Log;

import com.bean.xhttp.callback.OnXHttpCallback;
import com.bean.xhttp.core.IHttp;
import com.bean.xhttp.core.pool.IThreadPool;
import com.bean.xhttp.core.pool.ThreadPool;
import com.bean.xhttp.core.task.HttpRunnable;
import com.bean.logger.JJLogger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

import static com.bean.util.ErrorCode.CODE_CANCLE;


/**
 * author xander on  2017/5/31.
 * function  处理具体的业务逻辑 ，管理线程池
 *
 */
@SuppressWarnings("unchecked")
public class XHttp implements IHttp<XHttp>, IXHttp<XHttp> {

    private final String TAG = "xander";

    /**
     * 异步任务
     */
    private HttpRunnable mHttpRunnable;


    /**
     * 线程池
     */
    private static IThreadPool sCurrentThreadPool;
    private static IThreadPool sSerialThreadPool;

    /**
     * 开启线程池
     */
    private boolean mStartCurrentThreadPool = true;
    /**
     * 开启线程池
     */
    private boolean mStartSerailThreadPool = true;
    /**
     * cpu 数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 线程池核心线程数
     */

    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    /**
     * 线程池最大线程数
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 存活时间
     */
    private static final long KEEP_ALIVE = 1L;
    /**
     * 定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，
     * 避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
     */
    private static volatile XHttp mInstance;
    /**
     * 异步任务管理器
     */
    private static Map<String, HttpRunnable> mTaskMap;

    // 定义一个私有构造方法
    private XHttp() {

    }

    private static class SingletonHolder {
        private static final XHttp TASK_MANAGER = new XHttp();
    }

    //定义一个共有的静态方法，返回该类型实例
    public static XHttp getInstance() {
        mInstance =SingletonHolder.TASK_MANAGER;
        return mInstance;
    }

    @Override
    public XHttp setTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            if (mTaskMap == null) {
                mTaskMap = new HashMap<>();
            }
            mTaskMap.put(tag, mHttpRunnable);//用于管理任务
        }
        return mInstance;
    }

    @Override
    public XHttp startSerialThreadPool() {
        mStartSerailThreadPool = true;
        if (sSerialThreadPool == null) {
            sSerialThreadPool = new ThreadPool(1, 1, 0L);
        }
        return mInstance;
    }

    @Override
    public XHttp startConcurrenceThreadPool() {
        mStartCurrentThreadPool = true;
        if (sCurrentThreadPool == null) {
            sCurrentThreadPool = new ThreadPool(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE);
        }
        return mInstance;
    }

    @Override
    public XHttp customThreadPool(final IThreadPool poolExecutor) {
        sCurrentThreadPool = poolExecutor;
        mStartCurrentThreadPool = true;
        return mInstance;
    }

    @Override
    public XHttp closeThreadPool() {
        if (sSerialThreadPool != null) {
            sSerialThreadPool.closeThreadPool();
        }
        if (sCurrentThreadPool != null) {
            sCurrentThreadPool.closeThreadPool();
        }

        return mInstance;
    }

    /**
     * @param url 请求url
     * @return 该类的实例
     */
    @Override
    public XHttp get(String url) {
        mHttpRunnable = new HttpRunnable();
        mHttpRunnable.get(url);
        return mInstance;
    }

    /**
     * @param url 请求url
     * @return 该类的实例
     */
    @Override
    public XHttp post(String url) {
        mHttpRunnable = new HttpRunnable();
        mHttpRunnable.post(url);
        return mInstance;
    }

    /**
     * @param params 请求参数
     * @return 该类的实例
     */
    @Override
    public XHttp setParams(Map<String, String> params) {
        Map<String, String> stringMap = new HashMap<>();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringMap.put(entry.getKey(), URLEncoder.encode(entry.getValue(), "utf-8"));
            }
            mHttpRunnable.setParams(stringMap);
        } catch (UnsupportedEncodingException e) {
            mHttpRunnable.setParams(params);
        }
        return mInstance;
    }

    /**
     * @param key   字段
     * @param value 值
     * @return 该类的实例
     */
    @Override
    public XHttp setParams(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            return mInstance;
        }
        try {
            mHttpRunnable.setParams(key, URLEncoder.encode(value, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            mHttpRunnable.setParams(key, value);
        }
        JJLogger.logInfo(TAG, "XHttp.setParams :" + key + "= " + value);
        return mInstance;
    }

    /**
     * @param key   字段
     * @param value 值
     * @return 该类的实例
     */
    @Override
    public XHttp setHeads(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            return mInstance;
        }
        mHttpRunnable.setHeads(key, value);
        return mInstance;
    }

    /**
     * @param heads 请求头
     * @return 该类的实例
     */
    @Override
    public XHttp setHeads(Map<String, String> heads) {
        mHttpRunnable.setHeads(heads);
        return mInstance;
    }

    /**
     * @param timeout 超时时间
     * @return 该类的实例
     */
    @Override
    public XHttp setTimeout(int timeout) {
        mHttpRunnable.setTimeout(timeout);
        return mInstance;
    }

    /**
     * @param charset 字符集
     * @return 该类的实例
     */
    @Override
    public XHttp setCharset(String charset) {
        mHttpRunnable.setCharset(charset);
        return mInstance;
    }

    @Override
    public XHttp uploadFiles(final String[] uploadFilePaths) {
        mHttpRunnable.uploadFiles(uploadFilePaths);
        return mInstance;
    }

    @Override
    public XHttp uploadFile(final String uploadFilePath) {
        mHttpRunnable.uploadFile(uploadFilePath);
        return mInstance;
    }

    @Override
    public XHttp setOnXHttpCallback(final OnXHttpCallback taskCallback) {
        mHttpRunnable.setOnXHttpCallback(taskCallback);
        if (mStartSerailThreadPool && sSerialThreadPool != null) {
            //启用线程池
            mStartSerailThreadPool = false;
            if (sSerialThreadPool.isShutdownPool() || sSerialThreadPool.isShutdownPool()) {
                JJLogger.logInfo(TAG, "XHttp.start : 线程池已关闭 错误码：" + CODE_CANCLE);
                return mInstance;
            }
            try {
                sSerialThreadPool.start(mHttpRunnable);
            } catch (RejectedExecutionException e) {
                JJLogger.logInfo(TAG, "XHttp.start :" + sCurrentThreadPool.getCount());
            }
        } else if (mStartCurrentThreadPool && sCurrentThreadPool != null) {
            //启用线程池
            mStartCurrentThreadPool = false;
            if (sCurrentThreadPool.isShutdownPool() || sCurrentThreadPool.isShutdownPool()) {
                JJLogger.logInfo(TAG, "XHttp.start : 线程池已关闭 错误码：" + CODE_CANCLE);
                return mInstance;
            }
            try {
                sCurrentThreadPool.start(mHttpRunnable);
            } catch (RejectedExecutionException e) {
                JJLogger.logInfo(TAG, "XHttp.start :" + sCurrentThreadPool.getCount());
            }
        } else {
            new Thread(mHttpRunnable).start();
        }
        return mInstance;
    }


    /**
     * @param tag 线程标志
     */
    @Override
    public void cancel(String tag) {
        if (mTaskMap == null) {
            return;
        }
        int taskSize = mTaskMap.size();
        if (taskSize > 0) {
            for (final Map.Entry<String, HttpRunnable> entry : mTaskMap.entrySet()) {
                if (entry.getKey().equals(tag)) {
                    Log.i(TAG, "XHttp.cancel :" + entry.getKey());
                    entry.getValue().cancle();
                }
            }
        }
    }

    @Override
    public void cancelAll() {
        if (mTaskMap == null) {
            return;
        }
        int taskSize = mTaskMap.size();
        if (taskSize > 0) {
            for (final Map.Entry<String, HttpRunnable> entry : mTaskMap.entrySet()) {
                entry.getValue().cancle();
            }
        }
        mTaskMap.clear();
    }

}
