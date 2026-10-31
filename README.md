# an-arts

`an-arts` 是用来展示`生成式艺术和算法创作`的一个 Kotlin 程序。本程序旨在展示一些有意思的生成式艺术程序。
基于 Compose Multiplatform，同一套绘图和参数界面可以运行在桌面和浏览器中。

## 特性

1. 基于 `Compose Multiplatform` 实现，支持 Desktop（JVM）和 Web（Wasm）
1. 不断补充的Arts类型
1. 支持图形展示和💫动画展示
1. 方便调节的Arts参数，不同的参数带给你不一样的感受

## 工作区界面

Desktop 与 Web 共用 shadcn 风格的中性工作区，默认浅色，也可切换深色：

- 作品库支持中文/英文搜索，以及动态、静态筛选。
- 画布与参数面板独立，侧栏可以收起；窄窗口使用抽屉面板。
- 画布支持方形与自适应视图，专注模式可临时隐藏两侧面板。
- 动画可暂停或继续；重置作品会恢复默认参数，同时保留暂停状态。
- 颜色控件使用锚定浮层，支持色域拖选、色相/透明度、HEX/RGBA 输入和预设色；有效修改即时更新画布，Escape 或点击外部关闭。

快捷键：`Ctrl / ⌘ K` 搜索、`Ctrl / ⌘ B` 切换作品库、`Ctrl / ⌘ ,` 切换参数面板、`Escape` 关闭抽屉。
画布获得焦点时，可用空格播放或暂停动画；文本输入时不会触发播放。
外观和侧栏偏好仅在当前会话保留。视觉与交互规范见 [DESIGN.md](DESIGN.md)。

## 开发与运行

使用 JDK 17。项目使用 Kotlin 2.4.21、Compose Multiplatform 1.12.2 和 Gradle 9.4.1，Gradle Wrapper 会自动下载构建工具。
如果本机默认 Java 版本较新，请先将 `JAVA_HOME` 指向 JDK 17。

### Desktop

```shell
./gradlew run
```

启动桌面窗口。生成当前操作系统的安装包：

```shell
./gradlew packageDistributionForCurrentOS
```

### Web

```shell
./gradlew wasmJsBrowserDevelopmentRun
```

启动本地开发服务器，并在浏览器中打开应用。浏览器需要支持 WasmGC（如近期版本的 Chrome、Edge、Firefox 或 Safari）。
首次构建会自动下载 Node.js、Yarn 及 Web 构建依赖。

构建可部署的静态文件：

```shell
./gradlew wasmJsBrowserDistribution
```

产物位于 `build/dist/wasmJs/productionExecutable/`，通过 HTTP 静态服务器部署整个目录，不能直接用 `file://` 打开。

### 验证

```shell
./gradlew jvmTest wasmJsBrowserTest
./gradlew jvmJar wasmJsBrowserDistribution
```

Web 测试使用本机 Chrome 的无头模式；找不到 Chrome 时可设置 `CHROME_BIN`。

### 代码结构

- `src/commonMain`：共享作品、参数表单、界面和工具函数。
- `src/jvmMain`：桌面窗口入口和本地时间实现。
- `src/wasmJsMain`：浏览器入口、HTML 和浏览器本地时间实现。
- `src/commonTest`：两端共用的作品、参数和工具函数测试。

新增作品时实现 `BaseArts`、定义 `ArtsConfig`，并在 `Arts` 中注册构造函数。
参数面板由配置类的 `fields` 显式声明，支持文本、输入框、滑块、单选和颜色列表；两端共用支持透明度的选色浮层。

## 已实现程序

> **说明：**  
> [ 💫 ] 为动画类型Arts

+ CircleLoop
+ Maze
+ 💫 RotatingSquares
+ 💫 CircleAndDotGrid
+ 💫 CircleGridScale
+ Yarn
+ 💫 Hearts
+ Identicon
+ DotsWave
+ CistercianNumerals
+ 💫 CistercianClock
+ 💫 VoidSetup
+ RecursiveTree
+ SilkSky
+ BlackHole
+ 💫 SnowFlakes
+ PointRibbon
+ 💫 BrownianMotion
+ RandomChords

## 特别感谢

感谢以下网站、仓库或链接提供的灵感。

+ https://github.com/jdxyw/generativeart
    + CircleLoop
    + Maze
    + Yarn
    + DotsWave
    + SilkSky
    + BlackHole
    + PointRibbon

+ https://github.com/Akashkamble/CanvasPlayground
    + 💫 RotatingSquares
    + 💫 CircleAndDotGrid
    + 💫 CircleGridScale

+ https://codepen.io/giaco/pen/BbpKZy
    + 💫 Hearts

+ https://github.com/stewartlord/identicon.js
    + Identicon

+ https://github.com/ednl/cistercian-clock/blob/main/sketch.js
    + CistercianNumerals
    + 💫 CistercianClock

+ https://sumygg.com/2018/07/31/p5js-version-for-taiko-drum-master-song-void-setup/
    + 💫 VoidSetup

+ https://p5js.org/examples/simulate-recursive-tree.html
    + RecursiveTree

+ https://p5js.org/examples/simulate-snowflakes.html
    + 💫 SnowFlakes

+ https://p5js.org/examples/simulate-brownian-motion.html
    + 💫 BrownianMotion

+ https://p5js.org/examples/math-random-chords.html
    + RandomChords

## 贡献

一个人的想法、灵感和精力毕竟是有限的。如果有任何想法，欢迎提供PR、Issue。

## 许可

MIT
