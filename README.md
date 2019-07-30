

# asyncmd
## 前言
以前有幸看过网商银行的一个异步命令组件源码 感觉功能比较实用 可以在挺多场景上使用 尤其现在领域驱动设计目前比较火 配合这个组件可以对领域内事件更好的支持 因为看目前也没有对应的开源组件 所以自己准备开发一个异步命令组件 核心功能的设计思路主要参考以前看过的异步命令组件
## 使用demo
https://github.com/bojiw/asyncmdDemo
#### 异步命令组件核心功能
就是把一些耗时比较高并且可以异步处理的同步请求转换为异步处理来提高并发,并且把命令内容保存到数据库表中来提高数据可靠性并且通过重试来保证数据的最终一致性
![image](http://qiniu.bojiw.com/20197/2019729203051image.png)
###### 注意：这个目前只是一个组件 只需要引入jar包 并且在自己的应用库中创建异步命令表 指定zookeeper地址就可以使用 并不是集中式单独部署一台应用进行使用 这样可以保证一个应用的异步命令积压或者使用有问题 不会影响到其他应用 也变相的把压力分摊到不同的应用中

#### 使用逻辑
新建一个异步命令对象 一个异步命令执行器 把请求传过来的数据保存到这个异步命令对象中 调用组件到一个服务 就会把对象保存到表中 并且异步的执行创建的异步命令执行器 会把异步命令对象作为入参传进去 使用者只要在异步命令执行器中把后续逻辑写好就可以 重试之类的机制组件会自动完成



### 使用场景一
如果有了解DDD领域驱动设计的人知道其中有个比较重要的概念就是领域驱动事件,当发生某个本地事件需要通知到系统中其他领域模型进行处理时,就可以采用异步命令组件来通知 目前很多是采用spring的事件传播机制 不过通过这种来传播无法保证数据的可靠性

如何有不了解领域驱动事件的同学可以看下下面这篇文章 讲的比较容易懂
[https://blog.csdn.net/weixin_33759269/article/details/91386492]()
#### 下图为异步命令组件在目前主流的三种领域事件发送方式里的应用 目前领域外的事件更多的是采用第二种直接使用mq 异步命令组件主要是第三种情况使用比较多

![image](http://qiniu.bojiw.com/20197/201972920313image.png)

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

## 快速使用
1、引入jar
```
      <dependency>
          <groupId>com.bojiw</groupId>
          <artifactId>asyncmd-core</artifactId>
          <version>1.5</version>
      </dependency>
```
2、在spring的xml文件中 引入xml文件
```
    <import resource="classpath*:/META-INF/asyn/applicationContext.xml"/>
```
3、配置AsynGroupConfig

```
    <bean id="asynGroupConfig" class="com.asyncmd.config.AsynGroupConfig">
        <!--必填项-->
        <!--定时任务名称 重点：需要不同工程不一样 推荐用应用名称来命名 如果多个项目定义的相同 定时任务会有问题-->
        <property name="jobName" value="demo-asyn"/>
        <!--zookeeper地址-->
        <property name="zookeeperUrl" value="127.0.0.1:2181"/>
        <!--数据源 -->
        <property name="dataSource" ref="dataSource"/>
        <!--重试执行频率 5s,10s,1m,1h 频率建议不要小于3秒-->
        <property name="executerFrequencys" value="10s,10s,1m"/>
        <!--分表数量-->
        <property name="tableNum" value="4"/>
      </bean>
```
4、创建自己业务的asynBizObject,asynCmd,asynExecuter
主要是继承AsynBizObject，AsynCmd，AbstractAsynExecuter
例子：
```
/**
 * 短信业务模型
 * @author wangwendi
 * @version $Id: SmsBiz.java, v 0.1 2019年07月23日 下午8:58 wangwendi Exp $
 */
public class SmsBiz extends AsynBizObject {
    /**
     * 手机号
     */
    private String mobiles;
    /**
     * 短信内容
     */
    private String content;

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
```
```
/**
 * 短信异步命令
 * @author wangwendi
 * @version $Id: SmsAsynCmd.java, v 0.1 2019年07月23日 下午8:57 wangwendi Exp $
 */
public class SmsAsynCmd extends AsynCmd<SmsBiz> {

    /**
     * 必须实现 返回对应的AsynBizObject
    **/
    @Override
    protected Class getObject() {
        return SmsBiz.class;
    }
}
```
```
/**
 * 短信异步命令执行器 需要注入到spring容器中 加@Service注解或者xml配置bean
 * @author wangwendi
 * @version $Id: SmsExecuter.java, v 0.1 2019年07月23日 下午9:01 wangwendi Exp $
 */
@Service
public class SmsExecuter extends AbstractAsynExecuter<SmsAsynCmd> {

    /**
      * 具体业务逻辑 调度方式有三种 异步调度(保存异步命令以后，立刻扔到线程池执行)、调度中心调度(保存异步命令以后不做任何操作 由定时任务捞取数据进行执行)、同步调度(保存异步命令以后立刻同步执行executer方法) 默认是异步调度 可以设置其他三种方式调度 设置方法看demo或者详细配置项
    **/
    @Override
    protected void executer(SmsAsynCmd cmd) {
        SmsBiz content = cmd.getContent();
        System.out.println("发送短信");
        System.out.println("短信手机号" + content.getMobiles());
    }
}
```
5、执行源码sql目录下的asyn.sql创建表 
根据前面第三步设置的asynGroupConfig.tableNum分表数量来创建对应的表数量 如上面设置的4 则创建asyn_cmd00,asyn_cmd01,asyn_cmd02,asyn_cmd03 4张表

6、使用异步命令门面服务AsynExecuterFacade 保存异步命令 可以直接用@Autowired注解注入就可以

```
    @Autowired
    private AsynExecuterFacade asynExecuterFacade;
    //创建业务对象 创建异步命令对象 然后调用方法 就会自动执行SmsExecuter.executer方法
      SmsBiz smsBiz = new SmsBiz();
      smsBiz.setContent(content);
      smsBiz.setMobiles(mobiles);
      SmsAsynCmd asynCmd = new SmsAsynCmd();
      asynCmd.setContent(smsBiz);
      asynCmd.setBizId(bizId);
      asynExecuterFacade.saveExecuterAsynCmd(asynCmd);

```
## 注意点
- 为了保证可靠性 当出现极端情况下 比如执行器执行成功了 修改命令状态时系统挂了 会出现重复执行的情况 所以执行器需要保证幂等
- 如果应用系统已经使用了quartz 需要版本为2.1.7以上

## 如何保证高可用
异步命令组件里主要是依靠elasticjob的故障转移和分片来保证高可用
[http://elasticjob.io/docs/elastic-job-lite/00-overview/intro/]()

当设置表的数量为4 elasticjob则会分4片来执行调度 每个分片各自从对应的表捞取数据 如果有两台应用服务器 一般应该两个各自执行两个分片 当其中一台应用服务器挂了 则会让一台执行4个分片 这样就保证了只要有一台应用服务器 异步命令组件肯定会捞取所有表的数据 只是执行快慢的问题 异步命令组件的性能主要依赖与表的数量和应用服务器个数 越多 消费能力越高


![image](http://qiniu.bojiw.com/20197/2019729203113image.png)


## 详细配置项
有两个地方可以配置 一个为全局配置项AsynGroupConfig 一个为设置个性化配置项AbstractAsynExecuter
```
    <bean id="asynGroupConfig" class="com.asyncmd.config.AsynGroupConfig">
        <!--必填项-->
        <!--定时任务名称 重点：需要不同工程不一样 推荐用应用名称来命名 如果多个项目定义的相同 定时任务会有问题-->
        <property name="jobName" value="demo-asyn"/>
        <!--zookeeper地址-->
        <property name="zookeeperUrl" value="127.0.0.1:2181"/>
        <!--数据源 -->
        <property name="dataSource" ref="dataSource"/>
        <!--
             * 重试执行频率 5s,10s,1m,1h 频率建议不要小于3秒
             * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次1分钟以后执行 第四次1小时以后执行 之后都是间隔1小时执行
             * 执行频率 5s,10s,20s
             * 代表第一次重试5秒以后执行 第二次10秒以后执行 第三次20秒以后执行
             * 这个参数如果想不同异步命令频率不一样 可以在异步命令执行器也有参数可以设置
        -->
        <property name="executerFrequencys" value="10s,10s,1m"/>
        <!--分表数量 推荐16 如果改了线程池数据 则需要根据下面优化环节进行调整 正式环境一定要填写 设置需要是2的倍数如 4 8 16 32 64 因为分表位计算是通过biz_id 然后和hashmap相同的计算下标的方式 & hash
            4代表分了4张异步命令表
            也代表有4个线程同时捞取4张异步命令表的数据进行执行 测试环境可以不填写
            默认一张异步命令表 后台只会有一个线程捞取数据
        -->
        <property name="tableNum" value="4"/>
        <!--非必填项-->
        <!--执行线程池配置 根据自己应用来配置 start-->
        <!--最大线程数 默认150-->
        <property name="maxPoolSize" value="200"/>
        <!--缓存队列长度 默认300-->
        <property name="queueCapacity" value="400"/>
        <!--最小线程池 默认10-->
        <property name="corePoolSize" value="20"/>
        <!--执行线程池配置 end-->
        
        <!-- 调度中心调度执行相关配置 start -->
        <!-- 从数据库捞取未执行的异步命令多久捞取一次 默认是1秒捞取一次  0/3 * * * * ?代表3秒捞取一次 如果是为了提高消费速度 推荐通过设置分表数量和线程池大小来配置-->
        <property name="cron" value="0/3 * * * * ?"/>
        <!--异步命令是否要先进后出 默认先进先出 true代表后进先出 注意这个只针对一张表的情况 -->
        <property name="desc" value="true"/>
        <!--一次从一张表中捞取命令数量 默认20条数据 10代表捞取10条数据 如果是为了提高消费速度 推荐通过设置分表数量和线程池大小来配置 -->
        <property name="limit" value="10"/>
        <!--重试次数 默认重试12次 20代表重试20次-->
        <property name="retryNum" value="20"/>

        <!-- 调度中心调度执行相关配置 end -->

        <!-- 异常情况如应用重启导致未成功执行的命令状态重置任务相关配置 start -->
        <!--重置状态任务执行频率 默认每隔60秒执行一次-->
        <property name="restCron" value="0/30 * * * * ?"/>
        <!-- 异常情况如应用重启导致未成功执行的命令状态重置任务相关配置 end -->

        <!-- 备份定时任务相关配置 start-->
        <!--是否开启自动备份异步命令表的功能 开启需要在数据库中创建异步命令历史表 表的数量和异步命令表相同
         默认每天凌晨2点钟开启定时任务把一个月以前的数据从异步命令表拷贝到异步命令历史表中 把异步命令表中备份成功的数据删除
         最多每天同步每张表10w条数据 这样定时清理数据可以保证异步命令表的查询效率高
         可以修改默认配置-->
        <property name="backup" value="true"/>
        <!--配置什么时候执行定时任务 默认每天凌晨2点-->
        <property name="backupCron" value="0/3 * * * * ?"/>
        <!--配置备份多久以前的数据 20代表把20天以前的异步命令表数据都清理 备份到异步命令历史表中-->
        <property name="beforeDate" value="20"/>
        <!--配置备份任务一次最多每张表处理多少条数据 默认是10w条数据 10代表处理1w条数据-->
        <property name="maxNo" value="10"/>
        <!-- 备份定时任务相关配置 end-->
    </bean>
```

对不同异步命令对象进行个性化对配置
```
public class SmsAsynCmd extends AsynCmd<SmsBiz> {

    public static final String name = "sms";

    /**
     * 设置调度模式为调度中心调度 默认异步执行
     * @return
     */
    @Override
    public DispatchMode getDispatchMode() {
        return DispatchMode.DISPATCH;
    }

    /**
     * 设置调度频率
     * @return
     */
    @Override
    public String getExecuterFrequencys() {
        return "4s,4s,4m";
    }
    /**
     * 需要返回业务对象类 组件里要用
     * @return
     */
    @Override
    protected Class getObject() {
        return SmsBiz.class;
    }
}
```

设置异步命令执行器的排序值 数值越大 越早执行 默认100
```
    @Service
public class SmsExecuter extends AbstractAsynExecuter<SmsAsynCmd> {
    /**
     * 只有赠送成功才会进行通知 所以设置push通知处理器最后一个执行
     * @return
     */
    @Override
    protected int getSort() {
        return 90;
    }
```

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
每隔1秒 就会从命令表中捞取30条数据 4张表 20*4=80 代表1秒钟处理80数据 如果想提高消费速度 分成16张表则16*20=320 每隔1秒处理320条数据 表的数量越多 消费速度越快 不过也还是需要依赖线程池 如果捞取的数量大于线程池的数量 则会多余 所以表数量需要和线程池的数量最对应调整
#### 调优
主要是对下面四个参数进行调整
需要 表数量*每次捞取的数量<线程池最大线程数(需要根据自己业务情况来设置 不是越多越好)+缓存队列(根据执行器平均处理时间来设置 可以配合多久扫表执行时间来调整)



