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

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.ghostsinthelab.apps.guilelessbopomofo.ChewingUtil
import org.ghostsinthelab.apps.guilelessbopomofo.keys.KeyImageButton
import org.ghostsinthelab.apps.guilelessbopomofo.utils.Vibratable
import kotlin.coroutines.CoroutineContext

class BackspaceKey(context: Context, attrs: AttributeSet) :
    KeyImageButton(context, attrs), CoroutineScope {
    private var backspacePressed: Boolean = false
    private var lastBackspaceClickTime: Long = 0
    private var repeatingBackspace: Job? = null

    companion object {
        private const val BACKSPACE_DEBOUNCE_MS = 100L
        private const val BACKSPACE_REPEAT_INITIAL_DELAY_MS = 50L
        private const val BACKSPACE_REPEAT_INTERVAL_MS = 100L
    }

    // Deleting goes through libchewing, whose context may only be touched from the main
    // thread, so the repeat has to keep to it as well.
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun createGestureListener() = MyGestureListener()

    inner class MyGestureListener : GestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            // avoids too fast repeat clicks
            if (SystemClock.elapsedRealtime() - lastBackspaceClickTime < BACKSPACE_DEBOUNCE_MS) {
                return false
            }
            lastBackspaceClickTime = SystemClock.elapsedRealtime()

            performVibration(context, Vibratable.VibrationStrength.NORMAL)
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            performKeyStroke()
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            startRepeatingBackspace()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> backspacePressed = true
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> backspacePressed = false
            }
        }
        return true
    }

    override fun onDetachedFromWindow() {
        stopRepeatingBackspace()
        super.onDetachedFromWindow()
    }

    /**
     * Keeps deleting for as long as the key is held down.
     */
    private fun startRepeatingBackspace() {
        stopRepeatingBackspace()
        repeatingBackspace = launch {
            delay(BACKSPACE_REPEAT_INITIAL_DELAY_MS)
            while (isActive && backspacePressed) {
                performKeyStroke()
                delay(BACKSPACE_REPEAT_INTERVAL_MS)
            }
        }
    }

    private fun stopRepeatingBackspace() {
        repeatingBackspace?.cancel()
        repeatingBackspace = null
    }

    private fun performKeyStroke() {
        ChewingUtil.handleBackspaceAction()
    }
}
