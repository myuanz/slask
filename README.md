# simple-http-server

# 如何运行?
项目构建于 [sbt](https://www.scala-sbt.org/download.html) + [scala](https://www.scala-lang.org/download/), 你只需要确保已经正确安装 JDK8+, 然后安装 sbt, 之后在本项目根目录运行:
```bash
> sbt run
```
不需要手动下载 scala, 等待 sbt 自动安装各项依赖即可, 然后访问 http://127.0.0.1:20006/

*你也可以参考上述 scala 链接中的其他手动方法.*
# 测试?
略

# 项目结构
我仿照`flask`创建了一个包, 名作`slask`, 内含
- 蓝图
- 配置
- 接受客户端信息的 Client
- 用于响应的 Response
- 状态码相关
- 工具包, 包含 url params 解析和 body form 解析相关的糖

在 example 目录下分为了: 
- 组件, 仿照 React 构造
- 主函数, 格式仿照 Flask
- 主视图, 仿照 Flask 构造
- 工具包

# 性能测试
## 初始单线程版本
```bash
> ./ab -n 10000 -c 10 http://127.0.0.1:20006/
Server Software:        MyuansToyServer
Server Hostname:        127.0.0.1
Server Port:            20006

Document Path:          /
Document Length:        297 bytes

Concurrency Level:      10
Time taken for tests:   6.332 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      4070000 bytes
HTML transferred:       2970000 bytes
Requests per second:    1579.28 [#/sec] (mean)
Time per request:       6.332 [ms] (mean)
Time per request:       0.633 [ms] (mean, across all concurrent requests)
Transfer rate:          627.70 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.7      0      31
Processing:     1    6   2.4      5      55
Waiting:        0    4   2.4      4      55
Total:          1    6   2.6      6      55

Percentage of the requests served within a certain time (ms)
  50%      6
  66%      6
  75%      7
  80%      7
  90%      8
  95%     10
  98%     12
  99%     16
 100%     55 (longest request)
```

## 使用`Future`优化的版本
```bash
> ./ab -n 10000 -c 10 http://127.0.0.1:20006/
Server Software:        MyuansToyServer
Server Hostname:        127.0.0.1
Server Port:            20006

Document Path:          /
Document Length:        294 bytes

Concurrency Level:      10
Time taken for tests:   6.553 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      4060000 bytes
HTML transferred:       2940000 bytes
Requests per second:    1526.06 [#/sec] (mean)
Time per request:       6.553 [ms] (mean)
Time per request:       0.655 [ms] (mean, across all concurrent requests)
Transfer rate:          605.06 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.5      0       4
Processing:     1    6   1.2      6      15
Waiting:        0    4   1.6      4      15
Total:          2    6   1.3      6      15

Percentage of the requests served within a certain time (ms)
  50%      6
  66%      7
  75%      7
  80%      7
  90%      8
  95%      9
  98%     10
  99%     10
 100%     15 (longest request)
```
可见连接时间最大从 55ms 降低到了现在的 15ms.
