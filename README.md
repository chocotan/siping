私评
======
JAVA轻量级评论系统
------


### 为啥要搞这个
没有好用的评论系统啊！！！

多说、云跟帖早就挂了，畅评要备案，来必力我这加载速度很慢，disqus是外国的，gitment更加卡......


### 使用方法
1. 添加maven依赖
2. 启动类上添加@EnableSiping注解
3. 配置文件
```yaml
siping:
  // 将评论格式化为html的转换器，目前只有一个SIMPLE
  render-type: SIMPLE
  // 你的私评的域名
  url: http://localhost:21001
  // 站点ID
  site-id: 1
  // 站点secret
  secret: testsecret
  // 要使用私评的网站域名 
  site-url: https://www.yoursite.com
  // 要使用私评的url
  filter-patterns: /article/*
```
4. 在html里这样使用
```html
<div th:utext="${#request.getAttribute('sipingComments')}"></div>
```

### 截图


### 功能
啥都没



TODO
* 接入各种社会化登录
* 管理界面（增删改查审核）
* 插件（邮件提醒、垃圾评论判断）

等自己有需求了再搞
