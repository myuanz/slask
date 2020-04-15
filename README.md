# simple-http-server
 
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
