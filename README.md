# DOLogParser
基于分布式微服务的在线日志事件提取系统
## 项目介绍
本项目的主要功能是进行日志解析和日志事件分析。日志解析是指利用基于文本相似度的在线聚类算法，从日志中提取出日志事件。日志事件分析是指根据提取出的日志事件，利用关联规则的方式挖掘出事件的关系。日志解析算法基于论文：***Length Matters: Clustering System Log Messages using Length of Words***。作者提出的LenMa算法在Github的python实现版本地址：[github.com/keiichishima/templateminer](https://github.com/keiichishima/templateminer)。
由于该解析算法是在线算法，故采用基于分布式微服务的方式部署多个解析服务模块，可以实现分布式解析，提高日志事件提取的效率。
## 项目架构图
## 部署与运行
## 在线体验链接
**请注意日志文件中存在隐私数据则不要上传！！！！**
## 待完善的Features
|功能|预计完成日期|进行状态|
|:-:|:-:|:-:|
|简易前端|2022/04/25|已完成|
|集成MoLFI算法|2022/04/17|已完成|
|日志事件分析模块|2022/05/10|已完成|
|解析模块代码优化|2022/05/05|已完成|
|数据库一主多从部署|未定期|待完成|
|动态配置算法参数|2022/05/03|待完成|
## 联系方式
欢迎交流
邮箱：[reallzt@njust.edu.com](reallzt@njust.edu.com)



