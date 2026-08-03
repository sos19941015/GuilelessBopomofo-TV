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

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.USER_CANDIDATE_SELECTION_KEYS_OPTION
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.USER_CONVERSION_ENGINE_WHEN_USING_PHYSICAL_KEYBOARD
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.USER_PHYSICAL_KEYBOARD_LAYOUT
import org.ghostsinthelab.apps.guilelessbopomofo.databinding.FragmentPhysicalKeyboardSettingsBinding
import org.ghostsinthelab.apps.guilelessbopomofo.enums.SelectionKeys
import org.ghostsinthelab.apps.guilelessbopomofo.utils.appSharedPreferences
import org.ghostsinthelab.apps.guilelessbopomofo.utils.bindRadioGroupToPreference
import org.ghostsinthelab.apps.guilelessbopomofo.utils.bindToPreference

class PhysicalKeyboardSettingsFragment : ViewBindingFragment<FragmentPhysicalKeyboardSettingsBinding>() {
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        // In the order R.array.physical_bopomofo_keyboard_layouts names them.
        private val PHYSICAL_KEYBOARD_LAYOUTS: List<String> = listOf(
            BopomofoPhysicalKeyboards.KB_DEFAULT.layout,
            BopomofoPhysicalKeyboards.KB_HSU.layout,
            BopomofoPhysicalKeyboards.KB_IBM.layout,
            BopomofoPhysicalKeyboards.KB_GIN_YIEH.layout,
            BopomofoPhysicalKeyboards.KB_ET.layout,
            BopomofoPhysicalKeyboards.KB_ET26.layout,
            BopomofoPhysicalKeyboards.KB_DVORAK.layout,
            BopomofoPhysicalKeyboards.KB_DVORAK_HSU.layout,
            BopomofoPhysicalKeyboards.KB_DACHEN_CP26.layout,
            BopomofoPhysicalKeyboards.KB_HANYU_PINYIN.layout,
            BopomofoPhysicalKeyboards.KB_THL_PINYIN.layout,
            BopomofoPhysicalKeyboards.KB_MPS2_PINYIN.layout,
            BopomofoPhysicalKeyboards.KB_CARPALX.layout,
            BopomofoPhysicalKeyboards.KB_COLEMAK_DH_ANSI.layout,
            BopomofoPhysicalKeyboards.KB_COLEMAK_DH_ORTH.layout,
            BopomofoPhysicalKeyboards.KB_WORKMAN.layout,
            BopomofoPhysicalKeyboards.KB_COLEMAK.layout,
        )
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPhysicalKeyboardSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().appSharedPreferences

        binding.sectionPhysicalKeyboard.apply {
            physicalBopomofoKeyboardLayoutDropdownMenu.bindToPreference(
                sharedPreferences,
                USER_PHYSICAL_KEYBOARD_LAYOUT,
                BopomofoPhysicalKeyboards.KB_DEFAULT.layout,
                entries = resources.getStringArray(R.array.physical_bopomofo_keyboard_layouts).toList(),
                values = PHYSICAL_KEYBOARD_LAYOUTS,
            )

            bindRadioGroupToPreference(
                sharedPreferences,
                USER_CANDIDATE_SELECTION_KEYS_OPTION,
                SelectionKeys.NUMBER_ROW.set,
                mapOf(
                    radioButtonNumberRow to SelectionKeys.NUMBER_ROW.set,
                    radioButtonTabRow to SelectionKeys.TAB_ROW.set,
                    radioButtonHomeRow to SelectionKeys.HOME_ROW.set,
                    radioButtonHomeTabMixedMode1 to SelectionKeys.HOME_TAB_MIXED_MODE1.set,
                    radioButtonHomeTabMixedMode2 to SelectionKeys.HOME_TAB_MIXED_MODE2.set,
                    radioButtonDvorakHomeRow to SelectionKeys.DVORAK_HOME_ROW.set,
                )
            )

            bindRadioGroupToPreference(
                sharedPreferences,
                USER_CONVERSION_ENGINE_WHEN_USING_PHYSICAL_KEYBOARD,
                ConversionEngines.CHEWING_CONVERSION_ENGINE.mode,
                mapOf(
                    radioButtonSimpleConversionEnginePhysical to ConversionEngines.SIMPLE_CONVERSION_ENGINE.mode,
                    radioButtonChewingConversionEnginePhysical to ConversionEngines.CHEWING_CONVERSION_ENGINE.mode,
                    radioButtonFuzzyChewingConversionEnginePhysical to ConversionEngines.FUZZY_CHEWING_CONVERSION_ENGINE.mode,
                )
            )
        }
    }
}
