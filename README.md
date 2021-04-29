# [布局优化深水区，异步测量](https://github.com/genius158/AsyncMeasureTest)

布局优化，如果做过相关的内容、或者看过相关的博客，那么过渡绘制、布局层级、viewStub、布局动态化等关键词，我们已经见过很多次，下面来聊聊一个不太常见的优化方案，异步测量。


> ![snapshot.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d99a2a2235bf4856b30aa2cd10fdb61d~tplv-k3u1fbpfcp-watermark.image)
> 实现测量方法在AsyncFrameLyaout线程执行

### measure
> 借用一张老图

> ![image.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0098259745694436808ecc4138bef829~tplv-k3u1fbpfcp-watermark.image)
> 左边是Choreographer执行UI测量的路径，右边是请求重绘的简单链路    

以上可知
- 执行测量（onMeasure）跟forceLayout（**PFLAG_FORCE_LAYOUT**）标识和specChanged（由**measureCache**判断，新的MeasureSpec是否发生变化）相关
- view调用reqeustLayout会清除调用者的measureCache，设置**PFLAG_FORCE_LAYOUT**标识

### AsyncFrameLayout（异步测量实现）
因为子View要走测量流程会，一层一层调用父类的requestLayout，那么就写一个父view，重写requestLayout
```
/**
     * 为什么可以覆写这个方法，不继续调用 super.requestLayout()
     * 一方面，明确当前这个view 不会因为我们自己的调度而改变宽高的情况，我们可以这么做
     * 另一方面，如果横竖屏切换，等由系统（其实也就是由父View分发的测量事件）调度，执行measure时
     * 当前view的MeasureSpec 缓存匹配不上，会正常执行测量过程
     */
    override fun requestLayout() {
        //父类构造方法内部会调用这个方法，所以measureHandler 需要判空
        measureHandler?.removeCallbacksAndMessages(null)
        AsyncRenderTask.clearTask()

        measureHandler?.post(AsyncRenderTask.provide(Runnable {
            Log.e("AsyncFrameLayout", "requestLayout  " + Thread.currentThread())

            val widthMeasureSpec = widthMeasureSpecCache ?: MeasureSpec.makeMeasureSpec(
                measuredWidth,
                MeasureSpec.EXACTLY
            )
            val heightMeasureSpec = heightMeasureSpecCache ?: MeasureSpec.makeMeasureSpec(
                measuredHeight,
                MeasureSpec.EXACTLY
            )

            try {
                // 使当前view的测量缓存无效化
                // 保证measure的正常执行
                forceLayout()
                // 主动执行测量
                measure(widthMeasureSpec, heightMeasureSpec)
                // 主动执行布局
                layout(left, top, right, bottom)

                invalidate()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }

        }))
    }
```
### 缺陷
- 动画创建开启、fragment实现（需要判断UI线程），链到异步布局测量方法上，就会出错
- 动画去改动某个异步布局子View的宽高，效果不理想，与demo的实现方向有关（当然，实现动画效果，我们应该尽量的使用**invalidate，只改动绘制效果，不要直接改宽高，宽高变动会调用requestLayout，有可能会触发整个界面的测绘，跟布局方式相关**，总之多注意requestLayout的调用）
- ... 

demo项目地址:[https://github.com/genius158/AsyncMeasureTest](https://github.com/genius158/AsyncMeasureTest)