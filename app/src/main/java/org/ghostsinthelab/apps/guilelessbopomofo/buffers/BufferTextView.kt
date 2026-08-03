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

package org.ghostsinthelab.apps.guilelessbopomofo.buffers

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import org.ghostsinthelab.apps.guilelessbopomofo.utils.DisplayMetricsComputable
import org.ghostsinthelab.apps.guilelessbopomofo.utils.Vibratable

abstract class BufferTextView(context: Context, attrs: AttributeSet) :
    AppCompatTextView(context, attrs), DisplayMetricsComputable {

    abstract class GestureListener : GestureDetector.SimpleOnGestureListener(), Vibratable

    /** The gestures this buffer answers to. */
    protected abstract fun createGestureListener(): GestureDetector.SimpleOnGestureListener

    /** Whether a quick second tap is a gesture of its own. */
    protected open val detectsDoubleTap: Boolean = false

    private val gestureDetector: GestureDetector by lazy(LazyThreadSafetyMode.NONE) {
        GestureDetector(context, createGestureListener()).also {
            if (!detectsDoubleTap) it.setOnDoubleTapListener(null)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return true
    }

    abstract fun update()
}
