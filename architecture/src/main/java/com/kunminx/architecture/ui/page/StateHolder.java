package com.kunminx.architecture.ui.page;

import androidx.lifecycle.ViewModel;

/**
 * Create by KunMinX at 2022/8/11
 */
public class StateHolder extends ViewModel {

    //TODO tip 6：每个页面都需单独准备一个 state-ViewModel，托管与 "控件属性" 发生绑定的 State，
    // 此外，state-ViewModel 职责仅限于状态托管和保存恢复，不建议在此处理 UI 逻辑，

    // UI 逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
    // 数据总是来自领域层业务逻辑的处理，并单向回推至 UI 层，在 UI 层中响应数据的变化（也即处理 UI 逻辑），
    // 换言之，UI 逻辑只适合在 Activity/Fragment 等视图控制器中编写，将来升级到 Jetpack Compose 更是如此。

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805

}
