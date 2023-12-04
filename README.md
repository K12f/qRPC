# qRPC

> 使用nettyRPC

## feature
- 1.nacos注册与发现
- 2.loadBalance负载均衡

## 协议设计
- 首位表示数据序列魔法常量 00xCAFEBABE
- 第二位表示数据格式化方法 ,目前只支持1:json
- 1:表示request , 2:表示response
- 数据包长度
- 数据包