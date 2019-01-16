package com.prembros.facilis.dialog

import androidx.annotation.IntDef
import com.prembros.facilis.dialog.AnimType.Companion.ANIM_FROM_BOTTOM
import com.prembros.facilis.dialog.AnimType.Companion.ANIM_FROM_LEFT
import com.prembros.facilis.dialog.AnimType.Companion.ANIM_FROM_RIGHT
import com.prembros.facilis.dialog.AnimType.Companion.ANIM_FROM_TOP

@kotlin.annotation.Retention
@IntDef(ANIM_FROM_LEFT, ANIM_FROM_RIGHT, ANIM_FROM_TOP, ANIM_FROM_BOTTOM)
annotation class AnimType {
    companion object {
        const val ANIM_FROM_LEFT = 0
        const val ANIM_FROM_RIGHT = 1
        const val ANIM_FROM_TOP = 2
        const val ANIM_FROM_BOTTOM = 3
    }
}