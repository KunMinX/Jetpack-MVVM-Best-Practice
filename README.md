![slogan.png](https://upload-images.jianshu.io/upload_images/57036-896e7a8809a9a705.png)

&nbsp;

[《重学安卓》](https://xiaozhuanlan.com/kunminx)付费读者加微信进群：myatejx

&nbsp;

## 公告：

### 2020.7

很高兴有位小伙伴分享了[面试心得](https://juejin.im/pin/5f1a889151882551517ff9b2)。截至目前，专栏已对体系化文章做了 700 余次修订，考虑到维护成本的日益激增，将于 8月初 将价格上调至 379 元，对深度思考感兴趣的小伙伴请抓紧。

### 2020.5

刚刚就本项目 "被卖课" 一事，在掘金发表了一期专访 [《开源项目被人拿去做课程卖了 1000 多万是什么体验》](https://juejin.im/post/5ecb4950518825431a669897)

本项目系我为了方便开发者们 **无痛理解 Google 开源的 Jetpack MVVM 中每个架构组件的 存在缘由、职责边界**，而 **精心设计的一个又一个高频应用场景**，

与此同时，本项目是作为 [《重学安卓》](https://xiaozhuanlan.com/kunminx)专栏 Jetpack MVVM 系列文章的配套项目而存在，**文章内容和项目中的代码设计均涉及本人对 Jetpack MVVM 的独家理解，本人对此享有著作权**。

任何组织或个人，未经与作者本人沟通，不得将本项目的代码设计和本人对 Jetpack MVVM 的独家理解用于出书和卖课的商业用途。

&nbsp;

# 前言

很高兴见到你！

上周我在 各大技术社区 发表了一篇 [《Jetpack MVVM 精讲》](https://juejin.im/post/5dafc49b6fb9a04e17209922)，原以为在 知识网红 唱衰安卓 的 2019 会无人问津，没想到文章一经发布，从 国内知名公司 的架构师、技术经理，到 世界级公司 的 Android 开发 都在看。😉

![reader_say.png](https://upload-images.jianshu.io/upload_images/57036-5445e7b4d66d97c7.png)

并且从读者的反馈来看，近期大部分安卓开发 已跳出舒适圈，开始尝试认识和应用 Jetpack MVVM 到实际的项目开发中。

只可惜，关于 Jetpack MVVM，网上多是 **东拼西凑、人云亦云、通篇贴代码** 的文章，这不仅不能提供完整的视角 来帮助读者 首先明确背景状况，更是给还没入门 Jetpack 的读者 **徒添困扰**、起到 **劝退** 的作用。

好消息是，这一期，我们带着 **精心打磨的 Jetpack MVVM 最佳实践案例** 来了！

&nbsp;
&nbsp;


|                  是让人 爱不释手 的 交互设计！                   |                     是 连贯 的 用户体验                      |                    唯一可信源 的 统一分发                    |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![1231111323.gif](https://upload-images.jianshu.io/upload_images/57036-0a5cdc68f003211a.gif) | ![222.gif](https://upload-images.jianshu.io/upload_images/57036-2b21db531e51ff03.gif) | ![333.gif](https://upload-images.jianshu.io/upload_images/57036-9a541148ce5bed2e.gif) |



|                    横竖屏布局 的 无缝切换                    |
| :----------------------------------------------------------: |
| ![444.gif](https://upload-images.jianshu.io/upload_images/57036-688f3eafc76cfa27.gif) |

&nbsp;
&nbsp;

# 项目简介

本人拥有 3 年的 移动端架构 践行和设计经验，领导团队重构的 中大型项目 多达十数个，对 Jetpack MVVM 架构在 确立规范化、标准化 开发模式 以 **减少不可预期的错误** 所作的努力，有着深入的理解。



在这个案例中，我将为你展示，Jetpack MVVM 是如何 **以简驭繁** 地 将原本十分容易出错、一出错就会耽搁半天时间的开发工作，通过 寥寥的几行代码 轻而易举地完成。😉

> 👆👆👆 划重点！

&nbsp;

在这个项目中，

> 我们为 **横、竖屏** 的情况 分别安排了两套 **截然不同的布局**，并且在 [生命周期](https://xiaozhuanlan.com/topic/0213584967)、[重建机制](https://xiaozhuanlan.com/topic/7692814530)、[状态管理](https://xiaozhuanlan.com/topic/7692814530)、[DataBinding](https://xiaozhuanlan.com/topic/9816742350)、[ViewModel](https://xiaozhuanlan.com/topic/6257931840)、[LiveData](https://xiaozhuanlan.com/topic/0168753249) 、[Navigation](https://xiaozhuanlan.com/topic/5860149732) 等知识点的帮助下，通过寥寥几行代码，轻松做到 **在横竖屏两种布局间 无缝地切换，并且不产生任何 预期外的错误**。


> 我们在多个 Fragment 页面 分别安排了 **播放状态 指示器**（包括 播放暂停按钮状态、播放列表当前索引指示 等），并向你展示了 如何 以及为何 通过 [LiveData](https://xiaozhuanlan.com/topic/0168753249) **配合** 作为唯一可信源 的 [ViewModel](https://xiaozhuanlan.com/topic/6257931840) 或单例，来实现 **全应用范围内 可追溯事件 的统一分发**。


> 我们在 Fragment 和 Activity 之间分别安排了 跨页面通信，从而向你展示 如何基于 **迪米特原则**（也称 最少知道原则）、通过 UnPeekLiveData 和 应用级 SharedViewModel 来实现 **生命周期安全的、事件源可追溯的 页面通信**（事件回调）。


> 我们在 `ui.page ` 、`data.repository`、`domain.request` 等目录下，分别安排了 视图控制器、[ViewModel](https://xiaozhuanlan.com/topic/6257931840) 、DataRepository 等 内容，从而向你展示，**单向依赖** 的架构设计，是如何通过分层的 数据请求和响应，来 **规避 内存泄漏** 等问题。


> 本项目的代码一律采用 经过 ISO 认证的 标准化工业级语言 Java 来编写。并且，在上述目录 所包含的 类中，我们大都 **提供了丰富的注释**，来帮助你理解 骨架代码 为何要如此设计、如此设计能够 **在软件工程的背景下** 避免哪些不可预期的错误。

&nbsp;
&nbsp;

除了 **在 蕴繁于简 的代码中 掌握 MVVM 最佳实践**，你还可以 从这个开源项目中 获得的内容 包括：

1. 整洁的代码风格 和 标准的资源命名规范。
2. 对 视图控制器 知识点的 深入理解 和 正确使用。
3. AndroidX 和 Material Design 2 的全面使用。
4. ConstraintLayout 约束布局的最佳实践。
5. **优秀的 用户体验 和 交互设计**。
6. 绝不使用 Dagger，绝不使用奇技淫巧、编写艰深晦涩的代码。
7. The one more thing is：

即日起，可在 应用商店 下载体验！

[![google-play1.png](https://upload-images.jianshu.io/upload_images/57036-f9dbd7810d38ae95.png)](https://www.coolapk.com/apk/247826) [![coolapk1.png](https://upload-images.jianshu.io/upload_images/57036-6cf24d0c9efe8362.png)](https://www.coolapk.com/apk/247826)


&nbsp;
&nbsp;

# Thanks to

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

[Jetpack MVVM](https://developer.android.google.cn/jetpack/)

[material-components-android](https://github.com/material-components/material-components-android)

[轻听](https://play.google.com/store/apps/details?id=com.tencent.qqmusiclocalplayer)

[AndroidSlidingUpPanel](https://github.com/umano/AndroidSlidingUpPanel)

项目中使用的 图片素材 来自 [UnSplash](https://unsplash.com/) 提供的 **免费授权图片**。

项目中使用的 音频素材 来自 [BenSound](https://www.bensound.com/) 提供的 **免费授权音乐**。

&nbsp;
&nbsp;

# My Pages

Email：[kunminx@gmail.com](mailto:kunminx@gmail.com)

Home：[KunMinX 的个人博客](https://www.kunminx.com/)

Juejin：[KunMinX 在掘金](https://juejin.im/user/58ab0de9ac502e006975d757/posts)

[《重学安卓》 专栏](https://xiaozhuanlan.com/kunminx?rel=kunminx)

付费读者加微信进群：myatejx

[![重学安卓小专栏](https://i.loli.net/2019/06/17/5d067596c2dbf49609.png)](https://xiaozhuanlan.com/kunminx?rel=kunminx)

# License

```
Copyright 2019-present KunMinX

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

