package com.kunminx.puremusic.domain.message;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.puremusic.domain.event.Messages;

/**
 * TODO:Note 2022.07.04
 * 可于领域层通过 MVI-Dispatcher 实现可靠的消息回推，
 * 通过消息队列、引用计数等设计，确保 "消息都能被消费，且只消费一次"，
 * 通过内聚设计，彻底杜绝 mutable 滥用等问题，
 *
 * 鉴于本项目场景难发挥 MVI-Dispatcher 潜能，故目前仅以改造 DownloadRequester 和 SharedViewModel 为例，
 * 通过对比 SharedViewModel 和 PageMessenger 易得，后者可简洁优雅实现可靠一致的消息分发，
 *
 * TODO tip: 于 PageMessenger 中统一鉴权和处理业务逻辑，并通过 sendResult 回推结果至表现层
 *
 * switch (event.eventId) {
 *    case Messages.EVENT_ADD_SLIDE_LISTENER:
 *        //... 业务逻辑处理
 *        //... 末端消息回推:
 *        // event.result.xxx = xxx;
 *        // sendResult(event);
 *        break;
 *    case Messages.EVENT_CLOSE_ACTIVITY_IF_ALLOWED:
 *        break;
 *    case Messages.EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED:
 *        break;
 *    case Messages.EVENT_OPEN_DRAWER:
 *        break;
 *  }
 *
 *
 * 具体可参见专为 MVI-Dispatcher 可信源编写 MVI 使用案例：
 *
 * https://github.com/KunMinX/MVI-Dispatcher
 *
 * Create by KunMinX at 2022/7/4
 */
public class PageMessenger extends MviDispatcher<Messages> {
    @Override
    protected void onHandle(Messages event) {
        sendResult(event);
    }
}
