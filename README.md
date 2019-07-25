

# asyncmd
## 前言
以前有幸看过网商银行的一个异步命令组件源码 感觉功能比较实用 可以在挺多场景上使用 尤其现在领域驱动设计目前比较火 配合这个组件可以对领域内事件更好的支持 因为看目前也没有对应的开源组件 所以自己准备开发一个异步命令组件 核心功能的设计思路主要参考以前看过的异步命令组件
## 使用demo
[https://github.com/bojiw/asyncmdDemo](异步命令组件demo)
#### 异步命令组件核心功能
就是把一些耗时比较高并且可以异步处理的同步请求转换为异步处理来提高并发,并且把命令内容保存到数据库表中来提高数据可靠性并且通过重试来保证数据的最终一致性
![image](https://github.com/bojiw/asyncmdDemo/blob/master/asynimage.png)
###### 注意：这个目前只是一个组件 只需要引入jar包 并且在自己的应用库中创建异步命令表 指定zookeeper地址就可以使用 并不是集中式单独部署一台应用进行使用 这样可以保证一个应用的异步命令积压或者使用有问题 不会影响到其他应用 也变相的把压力分摊到不同的应用中

#### 使用逻辑
新建一个异步命令对象 一个异步命令执行器 把请求传过来的数据保存到这个异步命令对象中 调用组件到一个服务 就会把对象保存到表中 并且异步的执行创建的异步命令执行器 会把异步命令对象作为入参传进去 使用者只要在异步命令执行器中把后续逻辑写好就可以 重试之类的机制组件会自动完成



### 使用场景一
如果有了解DDD领域驱动设计的人知道其中有个比较重要的概念就是领域驱动事件,当发生某个本地事件需要通知到系统中其他领域模型进行处理时,就可以采用异步命令组件来通知 目前很多是采用spring的事件传播机制 不过通过这种来传播无法保证数据的可靠性

如何有不了解领域驱动事件的同学可以看下下面这篇文章 讲的比较容易懂
[https://blog.csdn.net/weixin_33759269/article/details/91386492]()
#### 下图为异步命令组件在目前主流的三种领域事件发送方式里的应用 目前领域外的事件更多的是采用第二种直接使用mq 异步命令组件主要是第三种情况使用比较多

![image](https://github.com/bojiw/asyncmdDemo/blob/master/dominimage.png)

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
          <artifactId>asyncmd</artifactId>
          <version>1.1</version>
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
      * 具体业务逻辑
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


