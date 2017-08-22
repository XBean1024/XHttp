package com.jingjiu.http.core;

import android.annotation.SuppressLint;
import android.content.Context;

import com.jingjiu.http.exception.AdException;

import java.io.IOException;

import static com.jingjiu.http.common.Verification.bWorking;

/**

 */

public final class InitSDK {

    /**
     * mContext 上下文
     */
    @SuppressLint("StaticFieldLeak")

    static Context mContext;

    /**
     * 初始化工具类
     * 必须在application中初始化
     *
     * @param context 上下文
     * @throws IOException 初始化缓存失败
     */
    public static void init(Context context) throws AdException, IOException {
        if (context == null) {
            bWorking = false;
            throw new AdException("请确保 init(Context context) 参数不为空!!!");
        }
        if (mContext == null) {
            mContext = context.getApplicationContext();
        }
    }

    public static Context getContext() {
        return mContext;
    }
}
