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

package org.ghostsinthelab.apps.guilelessbopomofo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Keeps a fragment's view binding for exactly as long as the fragment has a view, which is
 * the one piece of bookkeeping every screen here would otherwise have to repeat.
 */
abstract class ViewBindingFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null

    /** Only valid between `onCreateView()` and `onDestroyView()`. */
    protected val binding: VB get() = checkNotNull(_binding) { "The fragment has no view right now." }

    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflateBinding(inflater, container).also { _binding = it }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
