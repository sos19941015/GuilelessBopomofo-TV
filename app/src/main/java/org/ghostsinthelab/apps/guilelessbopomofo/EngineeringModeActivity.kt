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

import android.content.res.Configuration
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.ghostsinthelab.apps.guilelessbopomofo.databinding.ActivityEngineeringModeBinding
import org.ghostsinthelab.apps.guilelessbopomofo.utils.EdgeToEdge

class EngineeringModeActivity : AppCompatActivity(), EdgeToEdge {
    // ViewBinding
    private lateinit var viewBinding: ActivityEngineeringModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityEngineeringModeBinding.inflate(this.layoutInflater)

        // Chewing data files status
        val chewingDataFilesStatusText: String = if (
            ChewingUtil.chewingDataFilesInstalled(applicationInfo.dataDir)
        ) {
            getString(R.string.chewing_data_files_status_ok)
        } else {
            getString(R.string.chewing_data_files_status_error)
        }
        viewBinding.chewingDataFilesStatus.text = chewingDataFilesStatusText

        // Hardware keyboard type
        val hardwareKeyboardTypeText: String = when (resources.configuration.keyboard) {
            Configuration.KEYBOARD_QWERTY -> getString(R.string.hardware_keyboard_type_qwerty)
            Configuration.KEYBOARD_12KEY -> getString(R.string.hardware_keyboard_type_12key)
            else -> getString(R.string.hardware_keyboard_type_unknown_or_missing)
        }
        viewBinding.hardwareKeyboardType.text = hardwareKeyboardTypeText

        // Hardware keyboard hidden status
        val hardwareKeyboardHiddenStatusText: String = when (resources.configuration.hardKeyboardHidden) {
            Configuration.HARDKEYBOARDHIDDEN_NO -> getString(R.string.hardware_keyboard_hidden_status_no)
            Configuration.HARDKEYBOARDHIDDEN_YES -> getString(R.string.hardware_keyboard_hidden_status_yes)
            Configuration.HARDKEYBOARDHIDDEN_UNDEFINED -> getString(R.string.hardware_keyboard_hidden_status_undefined)
            else -> getString(R.string.hardware_keyboard_hidden_status_unknown)
        }
        viewBinding.hardwareKeyboardHiddenStatus.text = hardwareKeyboardHiddenStatusText

        // Long pressing either test field is a shortcut to the IME picker, so that one can
        // switch keyboards without leaving this screen.
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        listOf(viewBinding.editTextTestTextInput, viewBinding.editTextTestNumberInput).forEach {
            it.setOnLongClickListener {
                inputMethodManager.showInputMethodPicker()
                true
            }
        }

        setContentView(viewBinding.root)
        applyInsetsAsPadding(viewBinding.root)
    }
}