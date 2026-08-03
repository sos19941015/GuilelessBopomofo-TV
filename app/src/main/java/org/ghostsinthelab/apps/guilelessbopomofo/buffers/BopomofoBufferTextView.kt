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
import org.ghostsinthelab.apps.guilelessbopomofo.ChewingBridge

class BopomofoBufferTextView(context: Context, attrs: AttributeSet) :
    BufferTextView(context, attrs) {

    override fun createGestureListener() = MyGestureListener()

    override val detectsDoubleTap: Boolean = true

    init {
        // Purposes to this padding setting:
        //   1. Acts as, and looks like a cursor
        //   2. Set its height early
        val px = convertDpToPx(2F).toInt()
        this.setPadding(px, 0, px, 0)
    }

    override fun update() {
        this@BopomofoBufferTextView.text = ChewingBridge.chewing.bopomofoStringStatic()
    }

    inner class MyGestureListener : GestureListener()
}