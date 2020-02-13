# optimistic-lock-db-demo

Java+MySQL实现乐观锁

在高并发下，经常需要处理SELECT之后，在业务层处理逻辑，再执行UPDATE的情况。

若两个连接并发查询同一条数据，然后在执行一些逻辑判断或业务操作后，执行UPDATE，可能出现与预期不相符的结果。

在不使用悲观锁与复杂SQL的前提下，可以使用乐观锁处理该问题，同时兼顾性能。


---


## 场景模拟
假设一张表，三个字段：

|字段名|说明|
|-----|----|
|id|商品ID|
|goodsName|商品名称|
|goodsStock|库存|

里面有一条数据：
id = 1, goodsName = 并发商品, goodsStock = 5

用户每购买一件商品，商品的库存减1。

现在有若干个用户在深夜12:00抢购“并发商品”，每个人限购1件。

在高并发情况下，会遇到一种问题： 假设数据表中有一条记录为：id = 1, goodsName = 并发商品, goodsStock = 5

A用户与B用户两个连接并发查询id=1商品的库存，都执行下列SQL:

```SQL
SELECT goodsStock FROM table WHERE id=1; -- 查询库存
```

A先执行，得到id=1的goodsStock是5，之后在程序里做了一些逻辑判断或业务操作后执行SQL：

```java
goodsStock = goodsStock - 1
if (goodsStock > 0) ↓
```
```SQL
UPDATE table SET goodsStock = 4 WHERE id=1; -- 扣减库存
```


在A做判断且没有update之前，B也执行了查询SQL，发现goodsStock是5，之后它也会执行SQL：

```java
goodsStock = goodsStock - 1
if (goodsStock > 0) ↓
```
```SQL
UPDATE table SET goodsStock = 4 WHERE id=1; -- 扣减库存
```

这样A、B两个用户都买到了“并发商品”，然而库存却从5变成了4，这样库存计算错误。

如果有成百上千个用户并发操作，会造成“虚卖”，即实际库存已没，但是系统持续计算错误，仍然持续卖出。

---

处理步骤如下：
1. 添加第3个字段version，int类型，default值为0。version值每次update时作加1处理。

|字段名|说明|
|-----|----|
|id|商品ID|
|goodsName|商品名称|
|goodsStock|库存|
|version|数据版本号|

2. SELECT时同时获取version值（例如version为1、goodsStock为5）。

```SQL
SELECT goodsStock, version FROM table WHERE id=1;
```

3. UPDATE时检查version值是否为第2步获取到的值。

```SQL
UPDATE table SET version=2, goodsStock=4 WHERE id=1 AND version=1;
```

- 如果UPDATE的记录数为1，则表示成功。
- 如果UPDATE的记录数为0，则表示已经被其他连接UPDATE过了，需作异常处理。

例如本例中使用JDBC的executeUpdate()方法获取UPDATE的记录数。
```java
int affectRows = sql1.executeUpdate();
```

---

## 快速开始

1. git clone
2. 使用eclipse导入项目
3. 将db目录下的表导入到自己的数据库中
4. 配置`DbUtil.java`，修改数据库连接参数
5. `Demo1.java`文件中的
6. 找到main方法，调节`preson`参数的值(代表有多少线程并发)，切换buy、newBuy两个方法进行测试

|方法名|说明|
|-----|----|
|buy(currentIndex)|传统开发方法|
|newBuy(currentIndex)|版本号控制并发|

---

## 测试结果

- 未使用并发控制方法时，计算错误
![](https://www.webpro.ltd/blog1/wp-content/uploads/2020/02/oold_1_1.png)
![](https://www.webpro.ltd/blog1/wp-content/uploads/2020/02/oold_1_2.png)

- 使用并发控制方法时，问题解决
![](https://www.webpro.ltd/blog1/wp-content/uploads/2020/02/oold_2_1.png)
![](https://www.webpro.ltd/blog1/wp-content/uploads/2020/02/oold_2_2.png)

