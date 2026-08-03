/*
 * Guileless Bopomofo
 * Copyright (C) 2025.  YOU, Hui-Hong <hiroshi@miyabi-hiroshi.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.ghostsinthelab.apps.guilelessbopomofo.keys.virtual

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import androidx.annotation.AttrRes
import org.ghostsinthelab.apps.guilelessbopomofo.R
import org.ghostsinthelab.apps.guilelessbopomofo.events.Events
import org.ghostsinthelab.apps.guilelessbopomofo.keys.KeyImageButton
import org.ghostsinthelab.apps.guilelessbopomofo.utils.Vibratable
import org.greenrobot.eventbus.EventBus

class ShiftKey(context: Context, attrs: AttributeSet) : KeyImageButton(context, attrs) {
    override val logTag: String = "ShiftKeyImageButton"

    // manage Shift key state
    enum class ShiftKeyState(@param:AttrRes val backgroundColorAttr: Int) {
        RELEASED(R.attr.colorTertiary),
        PRESSED(R.attr.colorSecondary),
        HOLD(R.attr.colorPrimary),
        ;

        /** Tapping the key walks through the states, and around again. */
        fun next(): ShiftKeyState = entries[(ordinal + 1) % entries.size]
    }

    var currentShiftKeyState = ShiftKeyState.RELEASED
    val isActive: Boolean get() = currentShiftKeyState != ShiftKeyState.RELEASED
    val isLocked: Boolean get() = currentShiftKeyState == ShiftKeyState.HOLD

    override fun createGestureListener() = MyGestureListener()

    inner class MyGestureListener : GestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            performVibration(context, Vibratable.VibrationStrength.NORMAL)
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            switchToState(currentShiftKeyState.next())
            return true
        }
    }

    private fun Context.getThemeColor(@AttrRes attrResId: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attrResId, typedValue, true)
        return typedValue.data
    }

    fun switchToState(state: ShiftKeyState) {
        Log.d(logTag, "Switch to state: $state")
        currentShiftKeyState = state
        background.setTint(context.getThemeColor(state.backgroundColorAttr))

        // notify GuilelessBopomofoService of shift key state change
        EventBus.getDefault().post(Events.UpdateShiftKeyState(isActive, isLocked))
    }
}
