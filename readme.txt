android应用快速开发框架

主要集成的框架有：
    1、网络请求+响应式编程组合：retrofit2 + Rxjava;
    2、图片缓存：picasso;
    3、序列化：Gson;
    4、控件依赖注入：Butterknife;
    5、内存泄露检测：leakcanary;
    6、圆角图片：roundedimageview;
    7、两个兼容包：appcompat-v7和support-v4;
    8、其他：图片选择器(PhotoHelper)、文件下载器(download包中)、数据库框架(GreenDao)等.

业务实现方式：
    1、在view包下创建一个继承BaseView的接口，该接口主要定义具体业务回调的抽象方法，写法参见BaseWebView;
    2、在presenter包下创建一个实现BasePresenter的类，该类中主要定义具体业务接口，写法参见WebViewPresenter类;
    3、Util包下的Constant类中定义好BASE_URL常量，这个是配置服务器地址的;
    4、在data/request包中自定义自己的请求实体类(JavaBean格式的)，可参见LoginBody类的写法;
    5、在data/response包中自定义自己的响应实体类(JavaBean格式的)，可参见Simple类的写法;
    6、在network包下的RichApi中定义网络请求的方法，可以参见RichApi中的例子，也可以参考retrofit2+Rxjava网络
    请求使用详解;
    7、然后在之前第二步定义好的presenter类中定义具体业务方法，通过调用RichApiService.getRichApiInstance()
    .xxx(...)获取Observable对象，之后就是Rxjava的操作方式，具体例子可参见WebPresenter类;
    8、在Rxjava的回调中完成请求后，可以调用第一步定义的实现BaseView接口的对象进行回调，一般BaseView接口都由一个
    android组件或者fragment来继承实现，具体例子可参见WebviewFragment类;
    9、最后一步，在自己的activity类中实例化Presenter类，具体可参见WebviewActivity类.

UI界面实现：
    要求严格按照android编程规范来实现UI界面的设计。


GreenDao框架的简单使用详见：http://www.jianshu.com/p/dac3bd9bad72
                        https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650821932&idx=1&sn=d26c09af7cbbfb1b0a95517bd78cc784&chksm=80b781b2b7c008a4a8dab45756e4d433b1c56e1c61762f98ee3b8a2b89a00756f82d6bb4d6b6&scene=0#rd
                        https://github.com/greenrobot/greenDAO。
                        http://blog.csdn.net/Hi_AndG/article/details/54695725

retrofit+Rxjava框架的使用详见：http://www.jianshu.com/p/5bc866b9cbb9
                            http://blog.csdn.net/byxyrq/article/details/52672202


