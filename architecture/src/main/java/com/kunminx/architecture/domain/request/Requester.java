package com.kunminx.architecture.domain.request;

import androidx.lifecycle.ViewModel;
/**
 * TODO tip 1：
 * 基于单一职责原则，抽取 Jetpack ViewModel "作用域管理" 的能力作为 "领域层组件"，
 *
 * TODO tip 2：让 UI 和业务分离，让数据总是从生产者流向消费者
 *
 * UI逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
 * "领域层组件" 作为数据的生产者，职责应仅限于 "请求调度 和 结果分发"，
 *
 * 换言之，"领域层组件" 中应当只关注数据的生成，而不关注数据的使用，
 * 改变 UI 状态的逻辑代码，只应在表现层页面中编写、在 Observer 回调中响应数据的变化，
 * 将来升级到 Jetpack Compose 更是如此，
 *
 * Activity {
 *  onCreate(){
 *   vm.livedata.observe { result->
 *     if(result.show)
 *       panel.visible(VISIBLE)
 *     else
 *       panel.visible(GONE)
 *     tvTitle.setText(result.title)
 *     tvContent.setText(result.content)
 *   }
 * }
 *
 * TODO tip 3：Requester 通常按业务划分
 * 一个项目中通常可存在多个 Requester 类，
 * 每个页面可根据业务需要，持有多个不同 Requester 实例，
 * 通过 PublishSubject 回推一次性消息，并在表现层 Observer 中分流，
 * 对于 Event，直接执行，对于 State，使用 BehaviorSubject 通知 View 渲染和兜着状态，
 *
 * Activity {
 *  onCreate(){
 *   request.observe {result ->
 *     is Event ? -> execute one time
 *     is State ? -> BehaviorSubject setValue and notify
 *   }
 * }
 *
 * 如这么说无体会，详见《Jetpack MVVM 分层设计解析》解析
 * https://xiaozhuanlan.com/topic/6741932805
 *
 * Create by KunMinX at 2023/6/5
 */
public class Requester extends ViewModel {

}
