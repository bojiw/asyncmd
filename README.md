

# asyncmd
## 前言
以前有幸看过网商银行的一个异步命令组件源码 感觉功能比较实用 可以在挺多场景上使用 尤其现在领域驱动设计目前比较火 配合这个组件可以对领域内事件更好的支持 因为看目前也没有对应的开源组件 所以准备自己开发一个异步命令组件
**QQ交流群 709378280**
## 快速使用参考demo
https://github.com/bojiw/asyncmdDemo
#### 异步命令组件功能
- 把一些耗时比较高并且可以异步处理的同步请求转换为异步处理来提高并发,并且把命令内容保存到数据库表中来提高数据可靠性并且通过重试来保证数据的最终一致性
- 领域驱动事件的通知封装 也可以只用组件保证事件的可靠性 用EventBus来进行事件通知
- 使MQ消息有顺序执行 如只有订单创建的消息处理完 才能处理订单修改的消息

![image](http://qiniu.bojiw.com/20197/201972920313image.png)

**注意：这个目前只是一个组件 只需要引入jar包 并且在自己的应用库中创建异步命令表 指定zookeeper地址就可以使用 并不是集中式单独部署一台应用进行使用 这样可以保证一个应用的异步命令积压或者使用有问题 不会影响到其他应用 也变相的把压力分摊到不同的应用中**

#### 使用逻辑
新建一个异步命令对象 一个异步命令执行器 把请求传过来的数据保存到这个异步命令对象中 调用组件到一个服务 就会把对象保存到表中 并且异步的执行创建的异步命令执行器 会把异步命令对象作为入参传进去 使用者只要在异步命令执行器中把后续逻辑写好就可以 重试之类的机制组件会自动完成



### 使用场景一
如果有了解DDD领域驱动设计的人知道其中有个比较重要的概念就是领域驱动事件,当发生某个本地事件需要通知到系统中其他领域模型进行处理时,就可以采用异步命令组件来通知 目前很多是采用spring的事件传播机制 不过通过这种来传播无法保证数据的可靠性

如何有不了解领域驱动事件的同学可以看下下面这篇文章 讲的比较容易懂
[https://blog.csdn.net/weixin_33759269/article/details/91386492]()
#### 下图为异步命令组件在目前主流的三种领域事件发送方式里的应用 目前领域外的事件更多的是采用第二种直接使用mq 异步命令组件主要是第三种情况使用比较多

![image](http://qiniu.bojiw.com/20197/2019729203051image.png)


### 使用场景二
一般很多大公司的开放平台有些接口因为内部逻辑非常复杂 处理比较耗时 都是采用接收请求 保存起来 然后立刻返回调用方发送成功的结果 再异步慢慢处理 处理完以后再会回调调用方 也可能是默认肯定成功 这种就可以使用异步命令组件来实现 以前我们和一家公司合作 互相提供接口进行调用 因为另一家公司有一些老系统逻辑处理很慢 常常因为处理不过来导致连接超时 如果使用这个组件就可以先接收 然后再处理
### 使用场景三
优惠券系统要求可以创建一个任务 到指定时间赠送一批用户指定的优惠券 这时候可以用异步命令组件 设置下一次执行时间为指定的时间 到时间来自动执行对应的异步命令执行器进行赠送用户优惠券
### 使用场景四
营销活动常常需要完成某个条件就会给用户赠送积分、储值卡、金币、app通知等可以异步的操作 如果每次都在某个活动里写 会导致大量重复的赠送代码 可以采用异步命令组件创建一个赠送优惠异步命令 然后对应的异步命令执行器里实现赠送积分、储值卡、金币、app通知等操作 通过入参判断需要赠送哪些优惠 减少重复代码
### 使用场景五
一般接收mq的消息都是立刻处理业务逻辑 如果消费方的逻辑非常复杂消费需要很久 很容易导致消息处理不过来出现mq消息挤压 而影响整个公司的生产者和消费者 这时候可以采用异步命令组件 收到消息以后先保存到异步命令表 然后立刻通知mq消息已经收到了 提高消费速度 因为异步命令表是各个应用自己的 所以消费慢也只是会影响到应用自己的库 而不会影响全公司的消费方
### 使用场景。。。欢迎补充

以上的场景只是我能想到的部分 其实还有很多 核心其实就是可以支持同步转异步 并且组件可以保证可靠性和数据的最终一致性 只要是某些需求场景需要这个功能都可以用异步命令组件来实现

## 如何保证高可用
异步命令组件里主要是依靠elasticjob的故障转移和分片来保证高可用
[http://elasticjob.io/docs/elastic-job-lite/00-overview/intro/]()

当设置表的数量为4 elasticjob则会分4片来执行调度 每个分片各自从对应的表捞取数据 如果有两台应用服务器 一般应该两个各自执行两个分片 当其中一台应用服务器挂了 则会让一台执行4个分片 这样就保证了只要有一台应用服务器 异步命令组件肯定会捞取所有表的数据 只是执行快慢的问题 异步命令组件的性能主要依赖与表的数量和应用服务器个数 越多 消费能力越高


![image](http://qiniu.bojiw.com/20197/2019729203113image.png)



## 模型
asynCmdDB(异步命令对象)

|  字段名 |  描述 |
| :------------ | :------------ |
| cmdId  |  单表唯一id由表自增 |
| cmdType  | 异步命令类型 异步命令对象类的名称  |
| bizId  | 业务id 全局唯一 根据这个ID做hash然后取余获取分表位 计算逻辑和hashmap计算下标为相同  |
| content  | 业务上下文 业务对象AsynBizObject的json数据  |
| executeNum  | 异步命令对象执行次数 重试一次加1  |
| nextTime  | 下一次执行时间 根据设置的重试频率来计算  |
| status  | 状态 分为"INIT","初始化" "EXECUTE","执行中" "SUCCESS","成功" "ERROR","失败"  |
| createHostName  | 执行插入异步命令服务器的主机名 方便排查问  |
| createIp  | 执行插入异步命令服务器的ip地址 方便排查问题  |
| updateHostName  | 更新异步命令服务器的主机名 方便排查问题   |
| updateIp  | 更新异步命令服务器的ip地址 方便排查问题  |
| createName  | 创建异步命令的人 默认system   |
| successExecuters  | 执行成功的异步命令执行器类名 如果一个异步命令对象对应多个异步命令执行器 通过这个可以看有成功执行的异步命令对象   |
| env  | 所在环境  |
| exception  | 异步命令执行时的异常信息  |
| rely_biz_id  | 依赖的异步命令业务id  |
| gmtCreate  | 创建时间  |
| gmtModify  | 修改时间  |

AsynCmdHistoryDO异步命令历史模型 字段和内容和异步命令对象模型一样 只有创建时间和更新时间不同

## 三种调度方式
DispatchMode
异步调度
![image](http://qiniu.bojiw.com/20197/2019730202546image.png)
调度中心调度
![image](http://qiniu.bojiw.com/20197/2019730203025image.png)
同步调度
![image](http://qiniu.bojiw.com/20197/2019730203249image.png)


## 性能和调优
#### 默认配置性能测试
这个测试结果只是作为一个参考 实际使用会有一定的差别
目前在本地测试情况
使用默认线程池配置 最大线程数150 等待队列300
一个tomcat 分4个表
每秒钟qps300 持续2分钟 执行器业务逻辑随机处理50毫米到1秒 总生产3万6千条
异步命令全部处理完成为2分15秒 基本不会出现命令挤压的情况

当qps400 持续2分钟 执行器业务逻辑随机处理50毫米到1秒 总生产4万8千条
压测结束以后 每张表大概3千条命令 4张表 总共挤压大概1万2千条命令
总共花费4分钟消费完所有命令

消费挤压命令逻辑为
每隔1秒 就会从命令表中捞取30条数据 4张表 20 * 4=80 代表1秒钟处理80数据 如果想提高消费速度 分成16张表则16 * 20=320 每隔1秒处理320条数据 表的数量越多 消费速度越快 不过也还是需要依赖线程池 如果捞取的数量大于线程池的数量 则会多余 所以表数量需要和线程池的数量最对应调整
#### 调优
主要是对下面四个参数进行调整需要 
###### 表数量*每次捞取的数量<线程池最大线程数(需要根据自己业务情况来设置 不是越多越好)+缓存队列(根据执行器平均处理时间来设置 可以配合多久扫表执行时间来调整)


