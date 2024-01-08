# SilverBug
甲壳虫（SilverBug），简单、灵活、易懂的辅助DDD、OOP等解决复杂业务开发工具

## 快速使用
pom 坐标
```
<dependency>
    <groupId>io.github.wiqer</groupId>
    <artifactId>silver-bug</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## SilverBug是什么
开发小工具，其中承载了多种解决常见棘手业务问题的工具

### 1，能力扩展
通过使用策略模式实现根据入参，选择执行方法的能力。支持spring和普通java代码启动
解决问题：同一段代码段多种场景复用，根据入参灵活选择执行方法等问题。

场景一：商品合规校验，拥有十几种校验，不同场景都需要使用此能力
场景二：发布逻辑基本相同，不同业务元素发布，使用的是基本相同的代码
场景三：数据关联业务中，不同场景、元素关联的代码几乎相同

可以使用 io.github.wiqer.bug.level.BugAbility 实现具体能力，来承载不同场景的能力。

### 2，id生成
生成自增非连续的业务id或者排序号
解决问题：不想使用同步锁搞连续自增id、不想使用uuid（索引效率低）
io.github.wiqer.bug.current.IdGeneratorService


### 3，使用多线程解决业务处理慢的问题

批量处理list数据，分批处理list数据，不相干业务并发执行
io.github.wiqer.bug.current.ConcurrentService

### 4，单线程读数据

io.github.wiqer.bug.current.lock.LockService