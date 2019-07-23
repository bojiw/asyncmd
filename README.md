# asyncmd
#### 异步命令组件核心功能
就是把一些耗时比较高并且可以异步处理的同步请求转换为异步处理来提高并发,并且把命令内容保存到数据库表中来提高数据可靠性并且通过重试来保证数据的最终一致性
![image](https://github.com/bojiw/asyncmd/blob/master/asynimage.png)
###### 注意：这个目前只是一个组件 只需要引入jar包 并且在自己的应用库中创建异步命令表 指定zookeeper地址就可以使用 并不是集中式单独部署一台应用进行使用 这样可以保证一个应用的异步命令积压或者使用有问题 不会影响到其他应用 也变相的把压力分摊到不同的应用中

#### 使用逻辑
新建一个异步命令对象 一个异步命令执行器 把请求传过来的数据保存到这个异步命令对象中 调用组件到一个服务 就会把对象保存到表中 并且异步的执行创建的异步命令执行器 会把异步命令对象作为入参传进去 使用者只要在异步命令执行器中把后续逻辑写好就可以 重试之类的机制组件会自动完成

### 使用场景一
如果有了解DDD领域驱动设计的人知道其中有个比较重要的概念就是领域驱动事件,当发生某个本地事件需要通知到系统中其他领域模型进行处理时,就可以采用异步命令组件来通知 目前很多是采用spring的事件传播机制 不过通过这种来传播无法保证数据的可靠性

如何有不了解领域驱动事件的同学可以看下下面这篇文章 讲的比较容易懂
[https://blog.csdn.net/weixin_33759269/article/details/91386492]()
#### 下图为异步命令组件在目前主流的三种领域事件发送方式里的应用

![image](https://github.com/bojiw/asyncmd/blob/master/dominimage.png)

### 使用场景二
短信或者邮件系统 因为调用第三方运营商接口比较慢 很多都是先接收请求 保存起来 然后立刻返回调用方发送成功的结果 再异步发送给用户 这时候就可以使用异步命令组件来实现 
### 使用场景三
优惠券系统要求可以创建一个任务 到指定时间赠送一批用户指定的优惠券 这时候可以用异步命令组件 设置下一次执行时间为指定的时间 到时间来自动执行对应的异步命令执行器进行赠送用户优惠券
### 使用场景四
营销活动常常需要完成某个条件就会给用户赠送积分、储值卡、金币、app通知等可以异步的操作 如果每次都在某个活动里写 会导致大量重复的赠送代码 可以采用异步命令组件创建一个赠送优惠异步命令 然后对应的异步命令执行器里实现赠送积分、储值卡、金币、app通知等操作 通过入参判断需要赠送哪些优惠 减少重复代码

以上的场景只是我能想到的部分 其实还有很多 核心其实就是可以支持同步转异步 并且组件可以保证可靠性和数据的最终一致性 只要是某些需求场景需要这个功能都可以用异步命令组件来实现
