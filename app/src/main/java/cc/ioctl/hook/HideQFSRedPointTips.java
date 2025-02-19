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

package cc.ioctl.hook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cc.ioctl.util.HookUtils;
import io.github.qauxv.util.SyncUtils;
import io.github.qauxv.base.annotation.FunctionHookEntry;
import io.github.qauxv.base.annotation.UiItemAgentEntry;
import io.github.qauxv.dsl.FunctionEntryRouter;
import io.github.qauxv.hook.CommonSwitchFunctionHook;
import io.github.qauxv.util.HostInfo;
import io.github.qauxv.util.Initiator;
import io.github.qauxv.util.QQVersion;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

@UiItemAgentEntry
@FunctionHookEntry
public class HideQFSRedPointTips extends CommonSwitchFunctionHook {

    public static final HideQFSRedPointTips INSTANCE = new HideQFSRedPointTips();

    private HideQFSRedPointTips() {
        super(SyncUtils.PROC_MAIN);
    }

    @NonNull
    @Override
    public String[] getUiItemLocation() {
        return FunctionEntryRouter.Locations.Simplify.MAIN_UI_TITLE;
    }

    @NonNull
    @Override
    public String getName() {
        return "屏蔽好友推过";
    }

    @Nullable
    @Override
    public String getDescription() {
        return "屏蔽消息列表主页左上角头像旁的好友推荐气泡";
    }

    @Override
    protected boolean initOnce() throws Exception {
        if (HostInfo.requireMinQQVersion(QQVersion.QQ_8_9_3)) {
            // QQ has its QFSRedPointTipsHelper refactor in QQ 8.9.3+
            // TODO: 2022-08-20 implement hook for QQ 8.9.3+
        } else {
            Class<?> kQFSRedPointTipsHelper = Initiator.loadClass("com.tencent.mobileqq.activity.qcircle.utils.QFSRedPointTipsHelper");
            Method showTips = null;
            for (Method method : kQFSRedPointTipsHelper.getDeclaredMethods()) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    Class<?>[] argt = method.getParameterTypes();
                    if (argt.length == 3 || argt.length == 4) {
                        showTips = method;
                        break;
                    }
                }
            }
            Objects.requireNonNull(showTips, "QFSRedPointTipsHelper.showTips not found");
            HookUtils.hookBeforeIfEnabled(this, showTips, p -> p.setResult(null));
        }
        return true;
    }

    @Override
    public boolean isAvailable() {
        return !HostInfo.isTim() && !HostInfo.requireMinQQVersion(QQVersion.QQ_8_9_3);
    }
}
