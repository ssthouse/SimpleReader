# SimpleReader
简易的分级阅读程序

##要求
附件说明（请到百度云提取，链接: http://pan.baidu.com/s/1nttEIFZ 密码: ca44）
1.  新概念 4 的文本，包括所有课文
2.  单词列表 nce4_words.txt （每行一个单词，以及对应的等级）

App 基本要求
这是一个简易的分级阅读程序， 基本功能是：
1. 打开后看到文章列表
2. 点击列表某项，打开文章
3. 文章界面有一个按钮，点击则在文章中高亮在单词列表中出现的单词
4. 文章内容实现两边对齐；
5. 单词实现点击高亮；


##示例
譬如 nce4_words 的内容如下：
单词              级别
compare         3
backward        2
technology      2
alien                1

文本内容是
Compared with the alien, our technology is backward。
如果 slide-bar 为 2， 那么只高亮级别在 2 及以下的词，包括 backward, technology,
alien；如果 slide-bar 为 3，那么高亮级别小于等于 3 的词。


提示： 可以预先把数据整理成程序便于操作的格式；
