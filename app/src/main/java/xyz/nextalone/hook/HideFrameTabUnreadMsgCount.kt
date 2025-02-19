/*
 * QAuxiliary - An Xposed module for QQ/TIM
 * Copyright (C) 2019-2022 qwq233@qwq2333.top
 * https://github.com/cinit/QAuxiliary
 *
 * This software is non-free but opensource software: you can redistribute it
 * and/or modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or any later version and our eula as published
 * by QAuxiliary contributors.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and eula along with this software.  If not, see
 * <https://www.gnu.org/licenses/>
 * <https://github.com/cinit/QAuxiliary/blob/master/LICENSE.md>.
 */

package xyz.nextalone.hook

import android.widget.TextView
import cc.ioctl.util.Reflex
import io.github.qauxv.base.annotation.FunctionHookEntry
import io.github.qauxv.base.annotation.UiItemAgentEntry
import io.github.qauxv.dsl.FunctionEntryRouter
import io.github.qauxv.hook.CommonSwitchFunctionHook
import xyz.nextalone.util.clazz
import xyz.nextalone.util.method
import xyz.nextalone.util.replace

@FunctionHookEntry
@UiItemAgentEntry
object HideFrameTabUnreadMsgCount : CommonSwitchFunctionHook() {

    override val name = "隐藏消息列表底栏未读消息数"

    override val extraSearchKeywords: Array<String> = arrayOf("隐藏小红点")

    override val uiItemLocation = FunctionEntryRouter.Locations.Simplify.UI_MISC

    override fun initOnce(): Boolean {
        // bottom red point
        "com.tencent.mobileqq.activity.home.impl.TabFrameControllerImpl".clazz?.method("updateRedTouch")
            ?.replace(this, null)
        "com.tencent.mobileqq.activity.framebusiness.controllerinject.FrameControllerInjectImpl".clazz?.let {
            Reflex.findSingleMethod(
                it, Void.TYPE, false,
                TextView::class.java,
                Int::class.java,
                String::class.java,
                Int::class.java,
                Int::class.java,
                Int::class.java
            ).replace(this, null)
        }
        return true
    }
}
