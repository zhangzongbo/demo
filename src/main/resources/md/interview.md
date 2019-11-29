interview

ArrayList与LinkedList的实现和区别:

    ArrayList 是数组, LinkedList 是链表
    区别就是数组与链表的区别,
    1. 数组内存连续, 内存利用率不高, 链表内存地址不连续, 内存利用高
    2. 数组支持随机存取,查询复杂度是O(1), 链表不支持随机存取, 查询复杂度是O(n)
    3. 数组插入删除时需要整理内存, 插入慢查询快,  链表插入时只需修改头尾, 插入快查询慢
    4. 数组创建之后内存大小确定, 扩容时需要创建新数组,然后整体映射, 链表扩容灵活

String.hashcode = s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
选用31 是因为
1. 31 是一个数值大小合适的质数,如果过小hash值分布区间较小 ,容易碰撞, 过大会导致int 溢出
2. 31 = 32 -1 = 2^5 - 1 ,这样计算31 * i 时可以把乘法优化成位运算 + 减法 31 * i =  (i<<5) - i

HashMap:
    数据结构:
        结合数组和链表两种数据结构的优点, 实质是元素为链表的数组
        Entry[] table
        lIst<Entry<key, value>> entryList;
    hash冲突如何解决:
        1. 开放定址法（线性探测再散列，二次探测再散列，伪随机探测再散列）
        2. 再哈希法
        3. 链地址法   （Java中HashMap使用的算法）
        4. 建立一个公共溢出区
    HashMap如何优化hash冲突?
        hashMap 使用链地址法, 把冲突的key 使用头插法插入单链表, 极端情况下, 所有key 落入同一个桶, 此时hashMap 退化成单链表 性能从O(1) 变为O(n),
        java 8中优化方式是 当hashMap中 一个桶(单链表) size 大于8(TREEIFY_THRESHOLD: less than 1 in ten million when > 8) 时, 会把单链表转为 红黑树, 此时极端情况下的性能由O(n) 变为O(log(n))
    扩容时机: THRESHOLD = capacity * load factor  , DEFAULT_LOAD_FACTOR = 0.75f)
       当前Entry 数量(m.size()) > THRESHOLD 时 ,capacity2倍扩容, 即达到最大容量的0.75时扩容,
    为什么是 0.75f?  平衡时间与空间, 过小时扩容频繁,内存占用大, 过大时hash 碰撞多, 影响存取效率,时间复杂度增加
    为什么indexFor() 不用取余运算(%)而是用位运算(&)? h % len 或者 h & (len - 1):
        第一, 位运算效率高, 只有加减与移位, 没有乘除, 第二, 避免resize()时 ,rehash
    扩容时避免rehash的优化:
        扩容时 (len << 1) 即容量扩大2倍, 计算indexFor(h & (len - 1))时,  原有元素的key 的位置要不不变 , 要不等于 index + oldCap, 这样重新映射时不需要rehash

LinkedHashMap:
    基本原理:
        继承hashMap, 在hashMap 基础上添加双向链表记录迭代顺序和插入顺序
    两种有序:
        插入顺序, 访问顺序, boolean accessOrder 标识
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>(16, 0.75f, true);
        这样实例化时,指定accessOrder=true, get put 都会产生structural modification,
        所以多线程中get操作也会触发 ConcurrentModificationException
    如何用它实现LRU:
        linkedHashMap 实现了Hashmap 中的三个方法,afterNodeAccess,afterNodeInsertion,afterNodeRemoval
        如上, 指定accessOrder=true 时, 保证读写都会更新顺序(被操作的Entry 放在链尾,实际是先删后插)
        由此实现LRU, 即eldest 最前,  youngest最后,

TreeMap:
    数据结构:  红黑树(二叉树)
    key对象为什么必须要实现Comparable接口:
        在使用TreeMap时，key必须实现Comparable接口或者在构造TreeMap传入自定义的Comparator，否则会在运行时抛出java.lang.ClassCastException类型的异常。
    如何用它实现一致性哈希:
        非一致性hash是由 key.hashcode 与集群节点个数 size 取余确定插入节点, key.hashcode % size , 此时增删节点,size变化, 导致找不到对应节点上key
        思路是先根据key.hash 直接找对应node 节点, 找不到时往上找比hash值稍大的Node, 利用了treeMap key 有序的特征, treeMap.tailMap(key.hashcode).firstKey

总结: treeMap 由于需要维护红黑树, 导致其存取效率没有 hashMap 和 linkedHashMap 效率高,
但是其优势是有序,且可导航(快速获取first, last , 根据 值切割Map获取tailMap的first), 红黑树保证其极限性能不会太差(O(logN)),
故如涉及频繁修改的map 最好不要使用TreeMap, 不要求顺序 直接用hashMap, 只要求顺序不要求导航用linkedHashMap,

HashTable:
    线程安全, 性能比ConcurrentHashMap差
    不要求线程安全时,推荐hashMap, 要求线程安全推荐ConcurrentHashMap, 1.8后已经不推荐使用hashTable
Set:
  基于hashMap 实现, 用hashSet 的key 存值,  keySet

Collections.synchronized:
    实现Collections的内部类, get put 方法都加上synchronized 修饰词, 保证线程安全

CopyOnWriteArrayList:
    线程安全的 List: get(), contains()操作不用加锁, 复制快照, 但是 add()操作还是要加可重入锁(reentrantLock)
    为什么没有ConcurrentArrayList? 因为没法像ConcurrentHashMap那样用分段锁和弱一致性的Map迭代器去规避并发压力, 例如 contains() 时不能避免完整加锁
ConcurrentHashMap:
    实现原理: 分段锁, 弱一致性Map( 即乐观锁),
    扩容优化:
