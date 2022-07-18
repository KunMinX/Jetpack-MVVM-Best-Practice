package com.kunminx.puremusic.domain.message;

import com.kunminx.architecture.domain.dispatch.MviDispatcher;
import com.kunminx.puremusic.domain.event.Messages;

/**
 * TODO:Note 2022.07.04
 * 可于领域层通过 MVI-Dispatcher 实现成熟形态 "唯一可信源"，
 * 使支持 LiveData 连续发送多种类事件 + 彻底消除 mutable 样板代码 + 彻底杜绝团队新手 LiveData.setValue 误用滥用，
 * 鉴于本项目场景难发挥 MVI-Dispatcher 潜能，故目前仅以改造 SharedViewModel 为例，
 * 通过对比 SharedViewModel 和 PageMessenger 易得，后者可简洁优雅实现可靠一致消息分发，
 *
 * 具体可参见专为 MVI-Dispatcher 唯一可信源编写 MVI 使用案例：
 *
 * https://github.com/KunMinX/MVI-Dispatcher
 *
 * Create by KunMinX at 2022/7/4
 */
public class PageMessenger extends MviDispatcher<Messages> {
    @Override
    protected void onHandle(Messages event) {

      /* TODO 于唯一可信源中统一鉴权处理业务逻辑，并通过 sendResult 回推结果至表现层

         switch (event.eventId) {
            case Messages.EVENT_ADD_SLIDE_LISTENER:
                //... 业务逻辑处理
                //... 末端消息回推:
                // event.result.xxx = xxx;
                // sendResult(event);
                break;
            case Messages.EVENT_CLOSE_ACTIVITY_IF_ALLOWED:
                break;
            case Messages.EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED:
                break;
            case Messages.EVENT_OPEN_DRAWER:
                break;
          }
       */

        sendResult(event);
    }
}
