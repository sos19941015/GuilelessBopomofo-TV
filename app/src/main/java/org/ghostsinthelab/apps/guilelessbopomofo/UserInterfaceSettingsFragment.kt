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
import androidx.core.content.edit
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.SAME_HAPTIC_FEEDBACK_TO_FUNCTION_BUTTONS
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.USER_ENABLE_DOUBLE_TOUCH_IME_SWITCH
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.USER_ENABLE_IME_SWITCH
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.USER_FULLSCREEN_WHEN_IN_LANDSCAPE
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.USER_FULLSCREEN_WHEN_IN_PORTRAIT
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.USER_HAPTIC_FEEDBACK_STRENGTH
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.USER_KEY_BUTTON_HEIGHT
import org.ghostsinthelab.apps.guilelessbopomofo.databinding.FragmentUserInterfaceSettingsBinding
import org.ghostsinthelab.apps.guilelessbopomofo.utils.Vibratable
import org.ghostsinthelab.apps.guilelessbopomofo.utils.appSharedPreferences
import org.ghostsinthelab.apps.guilelessbopomofo.utils.bindToPreference

class UserInterfaceSettingsFragment : ViewBindingFragment<FragmentUserInterfaceSettingsBinding>(), Vibratable {
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val DEFAULT_KEY_BUTTON_HEIGHT_DP = 52
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUserInterfaceSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().appSharedPreferences

        binding.sectionUserInterface.apply {
            val hapticFeedbackStrength = sharedPreferences.getInt(
                USER_HAPTIC_FEEDBACK_STRENGTH, GuilelessBopomofoService.defaultHapticFeedbackStrength
            )
            textViewSettingHapticFeedbaclCurrentStrength.text =
                getString(R.string.haptic_feedback_strength_setting, hapticFeedbackStrength)
            seekBarHapticFeedbackStrength.value = hapticFeedbackStrength.toFloat()
            seekBarHapticFeedbackStrength.addOnChangeListener { _, value, _ ->
                val strength = value.toInt()
                // Let the user feel what they are choosing.
                performVibration(requireContext(), strength)
                sharedPreferences.edit { putInt(USER_HAPTIC_FEEDBACK_STRENGTH, strength) }
                textViewSettingHapticFeedbaclCurrentStrength.text =
                    getString(R.string.haptic_feedback_strength_setting, strength)
            }

            val keyButtonHeight = sharedPreferences.getInt(USER_KEY_BUTTON_HEIGHT, DEFAULT_KEY_BUTTON_HEIGHT_DP)
            textViewSettingKeyButtonCurrentHeight.text =
                getString(R.string.key_button_height_setting, keyButtonHeight)
            seekBarKeyButtonHeight.value = keyButtonHeight.toFloat()
            seekBarKeyButtonHeight.addOnChangeListener { _, value, _ ->
                val height = value.toInt()
                sharedPreferences.edit { putInt(USER_KEY_BUTTON_HEIGHT, height) }
                textViewSettingKeyButtonCurrentHeight.text =
                    getString(R.string.key_button_height_setting, height)
            }

            switchSettingApplySameHapticFeedbackStrengthToFunctionButtons.bindToPreference(
                sharedPreferences, SAME_HAPTIC_FEEDBACK_TO_FUNCTION_BUTTONS, false
            )
            switchSettingFullscreenWhenInLandscape.bindToPreference(
                sharedPreferences, USER_FULLSCREEN_WHEN_IN_LANDSCAPE, true
            )
            switchSettingFullscreenWhenInPortrait.bindToPreference(
                sharedPreferences, USER_FULLSCREEN_WHEN_IN_PORTRAIT, false
            )
            switchSettingEnableImeSwitch.bindToPreference(
                sharedPreferences, USER_ENABLE_IME_SWITCH, false
            )
            switchSettingImeSwitch.bindToPreference(
                sharedPreferences, USER_ENABLE_DOUBLE_TOUCH_IME_SWITCH, false
            )
        }
    }
}
