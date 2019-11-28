用Netty实现的Android IM。
参考原文：[掘金](https://juejin.im/post/5c97ae12e51d45580b681b0b)

##### 测试步骤

1. TestServer模块: run NettyServerDemo.main()方法启动一个本地服务器
2. 运行app到两台android手机上，启动后在app界面输入服务器的ip地址及设置一个端口，两台设备相互指定发送人的id和接收人的id, 点登录连接上服务器后可以两台设备间发消息。

##### 运行示例图

<img src="https://github.com/chenyan-github/NettyChat/blob/master/image/test1.png" width = "360" height = "640"/>
<img src="https://github.com/chenyan-github/NettyChat/blob/master/image/test2.jpeg" width = "360" height = "640"/>
