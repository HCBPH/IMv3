# IMv3
an instance messaging app
im中有三个版本的android app，v1和v2可能用不了了，因为重写了im的后端。
im-server中有java版本的后端以及一个java本地测试用例。

简单介绍一下IMv3，本项目主要是完成后端的内容，前端的聊天界面只是随意做做（但也遇到很多问题，回忆起来，就被android的线程、task栈、保活给恶心到，总之需要很多手段，才能做到像qq那样）
本来前端也可以中websocket，后端也加入了websocket协议，但后来还是删了，无奈队友不会ws，只能重新用cs方案。

后端主要分为两大模块，一个是基于socket协议，还有一个是基于http协议（本质都是socket），在http协议，server主要处理群组的创建、删除、加入、退出等（其实就是用netty写了一个类似tomcat的东西去解析http请求）
如果只是单纯的socket，且包的头部携带IMv3规定的符号，就视为一条有效消息，进行消息的转发存储。

详细的接口文档以及消息报文结构，可以看handler下面的HttpRequestHandler以及FilterHandler。
