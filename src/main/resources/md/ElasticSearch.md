 Elastic Search 笔记

# Elasticsearch 是一个开源的分布式 RESTful 搜索和分析引擎，
自2010年发布以来，Elasticsearch已迅速成为最受欢迎的搜索引擎，常用于日志分析，全文搜索，安全情报，业务分析和运营情报用例。
## 基本概念: index type

DB | 库名 | 表名 | 列名| 字段名
 -|-|-|-|-
Relational DB | Database  | Tables | Rows  | Columns
Elasticsearch | Indices(Indexes)  | Types | Documents | Fields

> 索引（动词）「索引一个文档」表示把一个文档存储到索引（名词）里，以便它可以被检索或者查询。  
这很像SQL中的 **INSERT** 关键字，**差别是，如果文档已经存在，新的文档将覆盖旧的文档。**

#### ```_all``` 字段
当你索引一个文档(Document)，Elasticsearch把 **所有字符串字段值** 连接起来放在一个大字符串中，  
它被索引为一个特殊的字段_all

### 映射(mapping) 和分析(analysis)
映射(mapping)机制用于进行字段类型确认，将每个字段匹配为一种确定的数据类型(string, number, booleans, date等)。  
分析(analysis)机制用于进行全文文本(Full Text)的分词，以建立供搜索用的反向索引。

首先引入一个问题:

在索引中有12个tweets，只有一个包含日期2014-09-15，但是我们看看下面查询中的total hits。
```
GET /_search?q=2014              # 12 个结果
GET /_search?q=2014-09-15        # 还是 12 个结果 !
GET /_search?q=date:2014-09-15   # 1  一个结果
GET /_search?q=date:2014         # 0  个结果 !
```
为什么全日期的查询返回所有的tweets，而针对date字段进行年度查询却什么都不返回？ 为什么我们的结果因查询_all字段(默认所有字段中进行查询)或date字段而变得不同？
想必是因为我们的数据在_all字段的索引方式和在date字段的索引方式不同而导致。  

查看mapping:

```
GET /gb/_mapping/tweet
返回：

{
   "gb": {
      "mappings": {
         "tweet": {
            "properties": {
               "date": {
                  "type": "date",
                  "format": "dateOptionalTime"
               },
               "name": {
                  "type": "string"
               },
               "tweet": {
                  "type": "string"
               },
               "user_id": {
                  "type": "long"
               }
            }
         }
      }
   }
}
```
Elasticsearch为对字段类型进行猜测，动态生成了字段和类型的映射关系。返回的信息显示了date字段被识别为date类型。```_all```因为是默认字段所以没有在此显示，不过我们知道它是string类型。
date类型的字段和string类型的字段的索引方式是不同的，因此导致查询结果的不同，  

当你索引一个包含新字段的文档——一个之前没有的字段——Elasticsearch将使用动态映射猜测字段类型，这类型来自于JSON的基本数据类型，使用以下规则：

JSON type | Field type
-| -
Boolean: true or false |  "boolean"
Whole number: 123 | "long"
Floating point: 123.45 |  "double"
String, valid date: "2014-09-15" |  "date"
String: "foo bar" | "string"

> 如果你索引一个带引号的数字——"123"，它将被映射为"string"类型，而不是"long"类型。然而，如果字段已经被映射为"long"类型，Elasticsearch将尝试转换字符串为long，并在转换失败时会抛出异常。

#### 自定义字段映射

##### 指定index参数

```
{
    "tag": {
        "type":     "string",
        "index":    "not_analyzed"
    }
}
```
index参数控制字符串以何种方式被索引。它包含以下三个值当中的一个：

值| 解释
-|-
analyzed |  首先分析这个字符串，然后索引。换言之，以全文形式索引此字段。
not_analyzed |  索引这个字段，使之可以被搜索，但是索引内容和指定值一样。不分析此字段。
no |  不索引这个字段。这个字段不能为搜索到。

> 其他简单类型（long、double、date等等）也接受index参数，但相应的值只能是no和not_analyzed，它们的值不能被分析。

##### 指定analyzer参数
```
{
    "tweet": {
        "type":     "string",
        "analyzer": "english"
    }
}
```
Elasticsearch还附带了一些预装的分析器，你可以直接使用它们  
标准分析器是Elasticsearch默认使用的分析器。对于文本分析，如果没啥特殊需求,对于任何一个国家的语言，这个分析器就够用了
此外还有很多可选的分析器  

名称 | 简介
-|-
标准分析器 | 根据Unicode Consortium的定义的单词边界(word boundaries)来切分文本，然后去掉大部分标点符号。最后，把所有词转为小写。
简单分析器 | 简单分析器将非单个字母的文本切分，然后把每个词转为小写。
空格分析器 | 空格分析器依据空格切分文本。它不转换小写。
语言分析器 | 特定语言分析器适用于很多语言。它们能够考虑到特定语言的特性。例如，english分析器自带一套英语停用词库——像and或the这些与语义无关的通用词。

> 自定义字段映射主要针对 类型 为String 的字段, 因为String类型的字段, 考虑到包含全文本, 所以它们的值在 **索引前要经过分析器分析** ，并且在 **全文搜索此字段前要把查询语句做分析** 处理。

#### 确切值(Exact values) vs. 全文文本(Full text)
**确切值** 是确定的，正如它的名字一样。比如一个date或用户ID，也可以包含更多的字符串比如username或email地址。
确切值"Foo"和"foo"就并不相同。确切值2014和2014-09-15也不相同。  
确切值 常见于sql 查询语句, ```select user.name from  user where user.id = '123';```  
**全文文本**，从另一个角度来说是文本化的数据(常常以人类的语言书写)，比如一篇推文(Twitter的文章)或邮件正文。    
ES 的全文搜索 有别于sql查询：关心匹配程度(相关性)
- Foo -- foo
- 2014 -- 2014-09-15
- UK -- United Kingdom
- jump -- jumped, jumps, jumping, leap

### 倒排索引(inverted index)

Elasticsearch使用一种叫做倒排索引(inverted index)的结构来做快速的全文搜索。倒排索引由在 **文档中出现的唯一的单词列表，以及对于每个单词在文档中的位置** 组成。

为了创建倒排索引，ES会切分每个文档的content字段为单独的单词（我们把它们叫做词(**terms**)或者表征(tokens)）, 然后将所有唯一词放入列表并排序. (标记化)
然后我们还需要将词统一成标准格式, 这样我们就可以找到不是确切匹配.但是足够相似可以关联的文档.(标准化)  
标记化和标准化的过程叫做分词(analysis)，

假设我们有两个文档:

1. ElasticSearch is powerful;
2. ElasticSearch is open_source;

经过分词后得到的倒排索引可能类似这样

Token | DocIDs
- | -
open_source | 2
search | 1,2
...| ...


> 你只可以找到确实存在于索引中的词，所以索引文本和查询字符串都要标准化为相同的形式。

### 分析器 (analysis)
- 字符过滤器
字符过滤器是让字符串在被分词前变得更加“整洁”。例如，如果我们的文本是 HTML 格式，它可能会包含一些我们不想被索引的 HTML 标签，诸如 ```<p>``` 或 ```<div>```。
我们可以使用 html_strip 字符过滤器 来删除所有的 HTML 标签，并且将 HTML 实体转换成对应的 Unicode 字符，比如将 ```&Aacute; ```转成 Á。
一个分析器可能包含零到多个字符过滤器。  

- 分词器
一个分析器 必须 包含一个分词器。分词器将字符串分割成单独的词（terms）或标记（tokens）。standard 分析器使用 standard 分词器将字符串分割成单独的字词，删除大部分标点符号，但是现存的其他分词器会有不同的行为特征。
例如，keyword 分词器输出和它接收到的相同的字符串，不做任何分词处理。[whitespace 分词器]只通过空格来分割文本。[pattern 分词器]可以通过正则表达式来分割文本。  
- 标记过滤器
分词结果的 标记流 会根据各自的情况，传递给特定的标记过滤器。
标记过滤器可能修改，添加或删除标记。我们已经提过 lowercase 和 stop 标记过滤器，但是 Elasticsearch 中有更多的选择。stemmer 标记过滤器将单词转化为他们的根形态（root form）。ascii_folding 标记过滤器会删除变音符号，比如从 très 转为 tres。 ngram 和 edge_ngram 可以让标记更适合特殊匹配情况或自动完成。  

### 查询与过滤

#### 结构化查询 Query DSL
> 结构化查询是一种灵活的，多表现形式的查询语言。 Elasticsearch在一个简单的JSON接口中用结构化查询来展现Lucene绝大多数能力。 你应当在你的产品中采用这种方式进行查询。它使得你的查询更加灵活，精准，易于阅读并且易于debug。

使用结构化查询，你需要传递query参数：

```
GET /_search
{
    "query": YOUR_QUERY_HERE
}
```
空查询 - {} - 在功能上等同于使用match_all查询子句，正如其名字一样，匹配所有的文档：
```
GET /_search
{
    "query": {
        "match_all": {}
    }
}
```

#### 查询字句
一个查询子句一般使用这种结构：
```
{
    QUERY_NAME: {
        ARGUMENT: VALUE,
        ARGUMENT: VALUE,...
    }
}
```
或指向一个指定的字段：

```
{
    QUERY_NAME: {
        FIELD_NAME: {
            ARGUMENT: VALUE,
            ARGUMENT: VALUE,...
        }
    }
}
```

一个简单的例子:
```
{
    "query": {
        "match": {
            "tweet": "elasticsearch"
        }
    }
}
```
#### 合并多子句
查询子句就像是搭积木一样，可以合并简单的子句为一个复杂的查询语句，比如：
- 叶子子句(leaf clauses)(比如match子句)用以在将查询字符串与一个字段(或多字段)进行比较
- 复合子句(compound)用以合并其他的子句。例如，**bool子句** 允许你合并其他的合法子句，比如: must，must_not或者should

一个复杂点的例子:
```
{
    "bool": {
        "must": { "match":      { "email": "business opportunity" }},
        "should": [
             { "match":         { "starred": true }},
             { "bool": {
                   "must":      { "folder": "inbox" }},
                   "must_not":  { "spam": true }}
             }}
        ],
        "minimum_should_match": 1
    }
}
```
#### 结构化过滤 filter DSL
> 事实上 , 除了结构化查询（Query DSL）我们还可以使用结构化过滤（Filter DSL）。 查询与过滤语句非常相似，但是它们由于使用目的不同而稍有差异。

区别:
- 查询结果的区别: 过滤和查询都能得到一系列满足查询条件的结果, 但是查询比过滤多的一点是,查询会关注结果的匹配程度, 查询语句会计算每个文档与查询语句的相关性，并给出一个相关性评分 ```_score```，然后按照相关性对匹配到的文档进行排序.
- 性能方面的差异: 使用过滤语句得到的结果集——一个简单的文档列表,能够存入内存进行缓存, 每个文档仅需要一个字节, 查询语句由于要计算每个文档的相关性, 所以一般来说比过滤语句更加耗时, 而且查询结果 **不可缓存**

> 幸亏有了倒排索引，一个只匹配少量文档的简单查询语句在百万级文档中的查询效率会与一条经过缓存 的过滤语句旗鼓相当，甚至略占上风。 但是一般情况下，一条经过缓存的过滤查询要远胜一条查询语句的执行效率。

什么情况下使用?  
原则上来说，除了使用查询语句做全文本搜索或其他需要进行相关性评分的时候，剩下的全部用过滤语句

#### 重要的过滤子句
- term / terms 过滤  
  用于精确匹配某个值或者某些值
- range 过滤  
  range过滤允许我们按照指定的范围查找一批数据  
  gt :: 大于  
  gte:: 大于等于  
  lt :: 小于  
  lte:: 小于等于  
- exists / missing 过滤  
  用于查找文档中是否包含指定字段或没有某个字段
- bool 过滤  
  bool过滤可以用来合并多个过滤条件查询结果的布尔逻辑  
  must :: 多个查询条件的完全匹配,相当于 and。  
  must_not :: 多个查询条件的相反匹配，相当于 not。  
  should :: 至少有一个查询条件匹配, 相当于 or。  

#### 重要的查询子句
- match_all 查询  
查询所有文档, 是没有查询条件下的默认语句, 常用于合并过滤条件,
- natch 查询   
 match查询是一个标准查询, 全文本查询和精确查询 都需要用到
- mutil_match  
同时搜索多个字段  
- bool 查询  
bool 查询与 bool 过滤相似，用于合并多个查询子句。不同的是，bool 过滤可以直接给出是否匹配成功，  
而bool 查询要计算每一个查询子句的 ```_score``` （相关性分值）。  
must:: 查询指定文档一定要被包含。  
must_not:: 查询指定文档一定不要被包含。  
should:: 查询指定文档，有则可以为文档相关性加分。  

### 查询与过滤条件的合并

#### 带过滤的查询
```
{
    "query": {
        "filtered": {
            "query":  { "match": { "email": "business opportunity" }},
            "filter": { "term": { "folder": "inbox" }}
        }
    }
}
```
> search API中只能包含 query 语句，所以我们需要用 filtered 来同时包含 "query" 和 "filter" 子句

#### 单条过滤语句
在 query 上下文中，如果你只需要一条过滤语句，，你可以省略 query 子句：
```
{
    "query": {
        "filtered": {
            "filter":   { "term": { "folder": "inbox" }}
        }
    }
}
```

如果一条查询语句没有指定查询范围，那么它默认使用 match_all 查询，所以上面语句 的完整形式如下：
```
{
    "query": {
        "filtered": {
            "query":    { "match_all": {}},
            "filter":   { "term": { "folder": "inbox" }}
        }
    }
}
```

#### 带查询的过滤语句
有时候，你需要在 filter 的上下文中使用一个 query 子句。
```
{
    "query": {
        "filtered": {
            "filter":   {
                "bool": {
                    "must":     { "term":  { "folder": "inbox" }},
                    "must_not": {
                        "query": {
                            "match": { "email": "urgent business proposal" }
                        }
                    }
                }
            }
        }
    }
}
```
> 提示： 我们很少用到的过滤语句中包含查询，保留这种用法只是为了语法的完整性。 只有在过滤中用到全文本匹配的时候才会使用这种结构。

## 排序 sort
```
"sort": [
        { "date":   { "order": "desc" }},
        { "_score": { "order": "desc" }}
    ]
```
一个真实的示例 :
```
{
  "query": {
    "bool": {
      "must": [
        {
          "query_string": {
            "default_field": "code",
            "query": "R0000989"
          }
        }
      ],
      "must_not": [],
      "should": []
    }
  },
  "from": 0,
  "size": 10,
  "sort": [
    {
      "updateTime": {
      "order": "desc"
      }
    }
  ],
  "aggs": {}
}
```

### 相关性
ElasticSearch的相似度算法被定义为 TF/IDF，即检索词频率/反向文档频率  
- 检索词频率::
检索词在该字段出现的频率？出现频率越高，相关性也越高。 字段中出现过5次要比只出现过1次的相关性高。
- 反向文档频率::
每个检索词在索引中出现的频率？频率越高，相关性越低。 检索词出现在多数文档中会比出现在少数文档中的权重更低，即检验一个检索词在文档中的普遍重要性。
- 字段长度准则::
字段的长度是多少？长度越长，相关性越低。 检索词出现在一个短的 title 要比同样的词出现在一个长的 content 字段相关性更高。

使用 ```explain```  参数可以了解查询的详细信息, **输出 explain 结果代价是十分昂贵的**，它只能用作调试工具 ——千万不要用于生产环境。

## 分页  
from 30 size 10 --> limit 30,10  

### 查询过程
在初始化查询阶段（query phase），查询被向索引中的每个分片副本（原本或副本）广播。每个分片在本地执行搜索并且建立了匹配document的优先队列（priority queue）。
> 优先队列
一个优先队列（priority queue is）只是一个存有前n个（top-n）匹配document的有序列表。这个优先队列的大小由分页参数from和size决定。例如，下面这个例子中的搜索请求要求优先队列要能够容纳100个document:

```
GET /_search
{
    "from": 90,
    "size": 10
}
```   

分布式搜索查询 图示:
 ![](https://es.xiaoleilu.com/images/elas_0901.png)

查询阶段主要包含三步:

1. 客户端发送一个search（搜索）请求给Node 3, Node 3创建了一个长度为from+size的空优先级队列.
2. Node 3 转发这个搜索请求到索引中每个分片的原本或副本。每个分片在本地执行这个查询并且将结果合并到一个大小为from+size的有序本地优先队列里去。
3. 每个分片返回document的ID和它优先队列里的所有document的排序值给协调节点Node 3。Node 3把这些值合并到自己的优先队列里产生全局排序结果。  

当一个搜索请求被发送到一个节点Node，这个节点就变成了协调节点。这个节点的工作是向所有相关的分片广播搜索请求并且把它们的响应整合成一个全局的有序结果集。这个结果集会被返回给客户端。
第一步是向索引里的每个节点的分片副本广播请求。就像document的GET请求一样，搜索请求可以被每个分片的原本或任意副本处理。**这就是更多的副本（当结合更多的硬件时）如何提高搜索的吞吐量的方法。对于后续请求，协调节点会轮询所有的分片副本以分摊负载。**
每一个分片在本地执行查询和建立一个长度为from+size的有序优先队列——这个长度意味着它自己的结果数量就足够满足全局的请求要求。分片返回一个轻量级的结果列表给协调节点。只包含documentID值和排序需要用到的值，例如_score。
协调节点将这些分片级的结果合并到自己的有序优先队列里。这个就代表了最终的全局有序结果集。到这里，查询阶段结束。

整个过程类似于归并排序算法，先分组排序再归并到一起，对于这种分布式场景非常适用。

### 取回阶段
从前一步可以看出,查询的第一阶段并没有直接获取到完整的数据, 协调节点只获得了一个包含documentId和_score的轻量级的结果集.
所以还需要一个取回操作,来获取完整的数据

分布式搜索取回阶段
![](https://es.xiaoleilu.com/images/elas_0902.png)

取回操作大致也分为三步:

1. 协调节点辨别出哪个document需要取回，并且向相关分片发出GET请求。
2. 每个分片加载document并且根据需要丰富（enrich）它们，然后再将document返回协调节点。
3. 一旦所有的document都被取回，协调节点会将结果返回给客户端。

### 深度分页问题

仔细观察 分布式搜索的查询和取回操作,我们会发现一个问题:
假设我们有三个分片, 当我们用 ```from=90, size=10``` 分页获取第十页即(90 到100)这十条数据时, 每个分片节点返给协调节点
90 + 10 = 100条, 所以协调节点会对 3 * 100 = 300 条结果进行排序然后得到前十条, 然后执行取回操作从各个分片节点取数据并返给客户端,,这貌似没有问题, 但是但我们往下翻页, 翻到1000页之后, 即获取```from=10000,size=10```, 这时三个分片节点各返给协调节点10000 + 10 = 10010 条数据, 协调节点对10010 * 3 = 30030条数据排序, 然后取回十条数据, 然后丢弃余下的30020条, 类推一下, 100W页呢?

> 根据document的数量，分片的数量以及所使用的硬件，对10,000到50,000条结果（1,000到5,000页）深分页是可行的。但是对于足够大的from值，排序过程将会变得非常繁重，会使用巨大量的CPU，内存和带宽。因此，强烈不建议使用深分页。  
在实际中，“深分页者”也是很少的一部人。一般人会在翻了两三页后就停止翻页，并会更改搜索标准。  
如果你确实需要从集群里获取大量documents，你可以通过设置搜索类型scan禁用排序，来高效地做这件事。

### 搜索选项

#### preference（偏爱）
preference参数允许你控制使用哪个分片或节点来处理搜索请求。她接受如下一些参数 ```_primary```， ```_primary_first```， ```_local```，``` _only_node:xyz```， ```_prefer_node:xyz```和```_shards:2,3```。

应用实例:
##### 结果震荡（Bouncing Results）
想像一下，你正在按照timestamp字段来对你的结果排序，并且有两个document有相同的timestamp。由于搜索请求是在所有有效的分片副本间轮询的，这两个document可能在原始分片里是一种顺序，在副本分片里是另一种顺序。
这就是被称为结果震荡（bouncing results）的问题：用户每次刷新页面，结果顺序会发生变化。避免这个问题方法是对于同一个用户总是使用同一个分片。方法就是使用一个随机字符串例如用户的会话ID（session ID）来设置preference参数。
#### timeout（超时）
通常，协调节点会等待接收所有分片的回答。如果有一个节点遇到问题，它会拖慢整个搜索请求。
timeout参数告诉协调节点最多等待多久，就可以放弃等待而将已有结果返回。返回部分结果总比什么都没有好。
```
...
"timed_out":     true,  (1)
"_shards": {
   "total":      5,
   "successful": 4,
   "failed":     1     (2)
},
...
```
(1) 搜索请求超时。
(2) 五个分片中有一个没在超时时间内答复。
如果一个分片的所有副本都因为其他原因失败了——也许是因为硬件故障——这个也同样会反映在该答复的_shards部分里。
#### routing（路由选择）
在建立索引时提供一个自定义的routing参数来保证所有 **相关** 的document（如属于单个用户的document）被存放在一个单独的分片中。在搜索时，你可以指定一个或多个routing 值来限制只搜索那些分片而不是搜索index里的全部分片：
```
GET /_search?routing=user_1,user2
```
这个技术在设计非常大的搜索系统时就会派上用场了。
#### search_type（搜索类型）
默认的搜索类型是query_then_fetch，我们可以根据特定目的指定其它的搜索类型，例如：
```
GET /_search?search_type=count
```
- count（计数）  
count（计数）搜索类型只有一个query（查询）的阶段。当不需要搜索结果只需要知道满足查询的document的数量时，可以使用这个查询类型。
- query_and_fetch（查询并且取回）  
query_and_fetch（查询并且取回）搜索类型 **将查询和取回阶段合并成一个步骤** 。这是一个内部优化选项，当搜索请求的目标只是一个分片时可以使用，例如指定了routing（路由选择）值时。虽然你可以手动选择使用这个搜索类型，但是这么做基本上不会有什么效果。
- dfs_query_then_fetch 和 dfs_query_and_fetch  
dfs搜索类型有一个预查询的阶段，它会从全部相关的分片里取回项目频数来计算全局的项目频数。
- scan（扫描）  
scan（扫描）搜索类型是和scroll（滚屏）API连在一起使用的，可以高效地取回巨大数量的结果。它是通过禁用排序来实现的。

### 扫描与滚屏(scan and scroll)
> scan（扫描）搜索类型是和scroll（滚屏）API一起使用来从Elasticsearch里高效地取回巨大数量的结果而不需要付出深分页的代价。

#### scroll（滚屏）
一个滚屏搜索允许我们做一个初始阶段搜索并且持续批量从Elasticsearch里拉取结果直到没有结果剩下。这有点像传统数据库里的cursors（游标）。
#### scan（扫描）
深度分页代价最高的部分是对结果的全局排序，但如果禁用排序，就能以很低的代价获得全部返回结果。为达成这个目的，可以采用scan（扫描）搜索模式。扫描模式让Elasticsearch不排序，只要分片里还有结果可以返回，就返回一批结果。

为了使用scan-and-scroll（扫描和滚屏），需要执行一个搜索请求，将search_type 设置成scan，并且传递一个scroll参数来告诉Elasticsearch滚屏应该持续多长时间。
```
GET /old_index/_search?search_type=scan&scroll=1m (1)
{
    "query": { "match_all": {}},
    "size":  1000
}
```
（1）保持滚屏开启1分钟。

### 关于搜索的细节

#### prefix 前缀查询
prefix 查询是一个词级别的底层的查询，它不会在搜索之前分析查询字符串，它假定传入前缀就正是要查找的前缀。
```
{
    "query": {
        "prefix": {
            "postcode": "W1"
        }
    }
}
```

#### wildcard 通配符查询

使用标准的 shell 通配符查询： ? 匹配任意字符， * 匹配 0 或多个字符。

```
{
  "query": {
    "bool": {
      "must": [
        {
          "wildcard": {
            "code": "*9*"
          }
        },
        {
          "term": {
            "name": "dot"
          }
        }
      ],
      "must_not": [

      ],
      "should": [
        {
          "range": {
            "id": {
              "gt": "1500"
            }
          }
        }
      ]
    }
  },
  "from": 0,
  "size": 10,
  "sort": [

  ],
  "aggs": {

  }
}
```

#### regexp 正则表达式查询

和 ```wildcard``` 类似, 但是支持更复杂的匹配模式
```
{
    "query": {
        "regexp": {
            "postcode": "W[0-9].+"
        }
    }
}
```

#### boost 提升权重
```
{
  "query": {
    "bool": {
      "should": [
        {
          "match": {
            "title": {
              "query": "quick brown fox",
              "boost": 2
            }
          }
        },
        {
          "match": {
            "content": "quick brown fox"
          }
        }
      ]
    }
  }
}
```
title 查询语句的重要性是 content 查询的 2 倍，因为它的权重提升值为 2 。

没有设置 boost 的查询语句的值为 1 。


#### 即时搜索（instant search） 或 输入即搜索（search-as-you-type）
例如，如果用户输入 johnnie walker bl ，我们希望在它们完成输入搜索条件前就能得到：Johnnie Walker Black Label 和 Johnnie Walker Blue Label 。
需要在索引文档时进行处理, 然后在查询时配合短语匹配查询(match_phrase)的一种特殊形式 match_phrase_prefix 查询:
```
{
    "match_phrase_prefix" : {
        "brand" : "johnnie walker bl"
    }
}
```
