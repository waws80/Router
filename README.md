# <center> Router路由框架

### 前言
网上大片的路由框架实在太多了，实现的方式都大同小异，通过注解实现路由表，但是在多module开发的时候怎么合并路由表，不同的框架有着自己的解决方案。

##### ARouter：通过类查找进行合并路由表。
##### ActivityRouter：通过注解进行路由表合并。

### 开始
对于路由表的合并我采用了ActivityRouter的方案，进行注解合并路由表。
##### 源码：
```java

        if (!hasModules && !hasModule){
        	//普通app
            createrAppRouterHelper();
        }

        if (hasModule){
            //保存每个module中的路由表
            createrModuleHelper(moduleName);
        }
        
        if (hasModules){
            //合并每个module中的路由表
            createrRouterHelper(moduleNames);
        }
```
##### 用法：
app:

```java
@Module("app")
@Modules({"app","module"})
public class App extends Application{
		...
}
```
module:

```java
@Module("module")
@Path("module")
public class ModuleActivity extends AppCompatActivity {
		...
}
```

##### 备注：非多module开发以上直接省略直接初始化路由即可。
### 初始化
```java
		Router.init(this.getApplicationContext());//bixu
        Router.debug(true);//开启debug模式
```

### 页面跳转
1. 程序内部界面之间跳转 通过 path 查找路由表中 对应的Activity 进行页面跳转。
2. 外部跳转程序内部 通过 uri 拦截 Activity 对uri进行拦截处理并最终通过路由表查找 path进行跳转。

##### 用法
基本用法

```java
		Router.getInstance().path("second");
		Router.getInstance().action(MediaStore.ACTION_IMAGE_CAPTURE);
		Router.getInstance().uri("https://www.waws.top/module?id=2&name=haha");
```
okhttp式用法

```java
		//同步
		Request request = new Request.Builder(this).path("second").build();
        Response response = Router.getInstance().newCall(request).execute();
        
        //异步
        request = new Request.Builder(this)
                    .path("second")
                    .responseCode(100)
                    .resultCallBack(new ResultCallBack() {
                        @Override
                        public void next(int resultCode, Intent data) {
                            RouterLog.d("resultCode:"+resultCode+"\ndata:"+data.getStringExtra("tag"));
                        }
                    })
                    .addOption(ActivityOptions.makeSceneTransitionAnimation(this,bt,"share").toBundle())
                    .build();
                    
        Router.getInstance().skipIntecepter().newCall(request)
                .enqueue(new RouterCallBack() {
            @Override
            public void next(Response response) {
                RouterLog.d(response.toString());
            }
        });
```

拦截Activity onActivityResult 使用了代理Fragment进行动态拦截，有兴趣的可以看源码，地址在下边

###  拦截器
支持全局拦截并支持降级操作

```java

		Router.addIntecepter(new RouterIntecepter() {
            @Override
            public Request chain(Request request) {
            	//TODO:
                return request;
            }

            @Override
            public void onLost(String msg) {
            	//TODO:
                Log.d("App", "onLost: "+msg);
            }

            @Override
            public void onSuccess() {
           	 	//TODO:
                Log.d("App", "onSuccess: ");
            }
        });

```

### 外部跳转app内部进行如下配置

```java

		<activity android:name=".DispatcherActivity">
            <intent-filter>
                <action android:name="android.intent.action.VIEW"/>
                <category android:name="android.intent.category.BROWSABLE"/>
                <category android:name="android.intent.category.DEFAULT"/>
   
                <!-- start -->
                <!-- 修改如下data信息即可 -->
                <data android:scheme="http" android:host="www.waws.top"/>
                <data android:scheme="https" android:host="www.waws.top"/>
                <data android:scheme="router" android:host="www.waws.top"/>
                <!-- end -->
                
            </intent-filter>
        </activity>
        
```
