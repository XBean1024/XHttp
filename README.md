# XHttp  目前支持功能

1、不依赖第三方的网络请求框架;

2、支持get、post、post文件上传;

3、支持重定向;


#### demo展示
##### 登陆

![](https://github.com/Xbean1024/XHttp/blob/master/gif/login.gif)
##### 注册

![](https://github.com/Xbean1024/XHttp/blob/master/gif/register.gif)

##### 引用方式

    compile 'com.bean.libraries:xhttp:1.0.3'

#### 请使用代理进行测试

#### 打开日志开关，在 activity 或者 application 的onCreate()中调用
    JJLogger.debug(true);
### 一、网络请求，默认情况下不启用线程池
#### 1.1、请求json
          XHttp.getInstance()
                  .get("http://3434343434")
                  .setTag("aaa")
                  .setOnXHttpCallback(new OnXHttpCallback() {
                      @Override
                      public void onSuccess(Response response) {
                          Log.i("xxx", "response  " +response.toString());
                          mTextView.setText(response.toString());
                      }

                      @Override
                      public void onFailure(Exception ex, String errorCode) {
                          Log.i("xxx", "onFailure  " +ex.toString());
                          Log.i("xxx", "onFailure  " +errorCode);
                      }
                  });
#### 1.2、请求图片
         XHttp.getInstance()
                   .get("http://sdadadadasd")
                   .setTag("bbb")
                   .setTimeout(5000)
                   .setOnXHttpCallback(new OnXHttpCallback() {
                       @Override
                       public void onSuccess(final Response response) {
                           mImageView.setImageBitmap(response.toBitmap());
                           Log.i("xxx", "onSuccess" );
                       }

                       @Override
                       public void onFailure(Exception ex, String errorCode) {
                           Log.i("xxx", "onFailure  " +ex.toString());
                           Log.i("xxx", "onFailure  " +errorCode);
                       }
                   });
#### 1.3、多文件上传
	String basePtah = Environment.getExternalStorageDirectory().getPath();
	String[] path = new String[]{basePtah+ "/setting.cfg",basePtah+"/Screenshot.png"};
	XHttp.getInstance().post(WebApi.LOGIN_URL)
                        .setParams(USER_NAME, mName.getText().toString())
                        .setParams(USER_PASSWORD, mAge.getText().toString())
                        .setParams("tag", Code.TAG_LOGIN)
                        .setParams("platform", "mobile_phone")
                        .setOnXHttpCallback(new OnXHttpCallback() {
                            @Override
                            public void onSuccess(final Response response) {
                                JJLogger.logInfo(TAG, "MainActivity.onSuccess :" +
                                        response.toString());
                                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onFailure(final Exception ex, final String errorCode) {
                                JJLogger.logInfo(TAG, "MainActivity.onFailure :" + ex.getMessage());
                            }
                        });
#### 1.4、单文件上传
	String basePtah = Environment.getExternalStorageDirectory().getPath();
	String path = basePtah+ "/setting.cfg";
	XHttp.getInstance()
            .post(WebApi.UPLOAD_FILE_URL)//url
            .setHeads("platform","mobile_phone")//设置请求头
            .uploadFile(path)//文件路径（完整路径）
            .setOnXHttpCallback(new OnXHttpCallback() {
                @Override
                public void onSuccess(final Response response) {
                    JJLogger.logInfo(TAG,"MainActivity.onSuccess :"+
                            response.toString());
                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(final Exception ex, final String errorCode) {
                    JJLogger.logInfo(TAG,"MainActivity.onFailure :"+ex.getMessage());
                }
            });
#### 1.5 、启用并行线程池
	final StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < 20; i++) {
        final int finalI = i;
        XHttp.getInstance()
            .post(WebApi.LOGIN_URL)
            .setParams("account", mName.getText().toString())
            .setParams("password", mAge.getText().toString())
            .startConcurrenceThreadPool()
            .setOnXHttpCallback(new OnXHttpCallback() {
                @Override
                public void onSuccess(final Response response) {
                    Log.i("task","MainActivity.onSuccess 任务"+ finalI +"完成:");
                    stringBuilder.append("并行 任务"+ finalI +"完成:").append("\n");
                    mTextView1.setText(stringBuilder.toString());
                }

                @Override
                public void onFailure(final Exception ex, final String errorCode) {
                    JJLogger.logInfo(TAG, "MainActivity.onFailure :" + ex.getMessage());
                }
            });
    }
#### 1.6 、启用串行线程池
	final StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < 20; i++) {
        final int finalI = i;
        XHttp.getInstance()
        .post(WebApi.LOGIN_URL)
        .setParams("account", mName.getText().toString())
        .setParams("password", mAge.getText().toString())
        .startSerialThreadPool()
        .setOnXHttpCallback(new OnXHttpCallback() {
            @Override
            public void onSuccess(final Response response) {
                Log.i("task","MainActivity.onSuccess 任务"+ finalI +"完成:");
                stringBuilder.append("串行 任务"+ finalI +"完成:").append("\n");
                mTextView2.setText(stringBuilder.toString());
            }

            @Override
            public void onFailure(final Exception ex, final String errorCode) {
                JJLogger.logInfo(TAG, "MainActivity.onFailure :" + ex.getMessage());
            }
        });
    }
#### 2、取消请求（XHttpManager 取消请求）
###### 2.1、取消单个请求
      XHttp.getInstance().cancel("bbb");
###### 2.2、取消所有请求
     XHttp.getInstance().cancelAll();
##### QQ：交流群 ：192268854
![](https://github.com/Xbean1024/XHttp/blob/master/gif/QQ.JPG)
