## 库的总结：
- 这是一个Android用于网络请求的库，这个库是从 [Live](https://github.com/anlonglong/Live) 项目上面独立出来的，
底层用的是 [okHttp](https://github.com/square/okhttp)，我只是在其上面进行了一个简单的封装，由于是第二次封装，可能里面有一些
地方设计的不是你很好，所以里面提供很多的接口，方便以后进行优化和扩展，实现里面的一些接口来重写重要的方法；
- 上传的项目已经是进行了二次优化和解耦的代码，由于第一版实在是太说说不过去了，里面分层很杂，网络的请求和响应全都混在一起，
没有进行有效的两者分离，如果你想看第一次的封装的逻辑的话，可以去[Live](https://github.com/anlonglong/Live)项目下面的net包
下面去查看。
- 库的的请求和结果的响应是分开的，分层的逻辑用的MVP的设计思想。
## 使用方式
- 在appdemo中已经展示了如何去使用库。
## okHTTP使用中的一些注意事项
 1.  网路请求的回调是在子线程中，所以不能直接在回调中更新UI，可以通过自定义的handler发送到主线程中。<br/>
 2.  网络请求得的响应数据只能读取一次。因为okHttp的底层有一个boolean的开关标志，第一次读完数据以后就置true，
然后第二次读数据的话就会抛出异常，提示通道已经关闭。所以在添加拦截器的时候要考虑好是否读取body().string()中的数据。

