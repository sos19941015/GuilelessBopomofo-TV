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

package org.ghostsinthelab.apps.guilelessbopomofo.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CompoundButton
import androidx.core.content.edit
import com.google.android.material.textfield.TextInputLayout
import org.ghostsinthelab.apps.guilelessbopomofo.GuilelessBopomofoEnv.APP_SHARED_PREFERENCES

/**
 * The one and only preferences file this app keeps, so that nobody has to spell out its
 * name and mode again.
 */
val Context.appSharedPreferences: SharedPreferences
    get() = getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)

/**
 * Shows [key]'s current value and writes back whatever the user does with the switch.
 */
fun CompoundButton.bindToPreference(
    sharedPreferences: SharedPreferences,
    key: String,
    defaultValue: Boolean,
    onChanged: (Boolean) -> Unit = {},
) {
    isChecked = sharedPreferences.getBoolean(key, defaultValue)
    setOnCheckedChangeListener { _, checked ->
        sharedPreferences.edit { putBoolean(key, checked) }
        onChanged(checked)
    }
}

/**
 * Ties a group of radio buttons to a single preference: the one holding the stored value is
 * checked, and picking another one stores its value.
 *
 * @param options each radio button together with the value it stands for.
 */
fun bindRadioGroupToPreference(
    sharedPreferences: SharedPreferences,
    key: String,
    defaultValue: Int,
    options: Map<out CompoundButton, Int>,
) {
    val storedValue = sharedPreferences.getInt(key, defaultValue)
    options.forEach { (button, value) ->
        button.setOnClickListener { sharedPreferences.edit { putInt(key, value) } }
        button.isChecked = value == storedValue
    }
}

/**
 * As above, for the preferences kept as a string.
 */
fun bindRadioGroupToPreference(
    sharedPreferences: SharedPreferences,
    key: String,
    defaultValue: String,
    options: Map<out CompoundButton, String>,
) {
    val storedValue = sharedPreferences.getString(key, defaultValue) ?: defaultValue
    options.forEach { (button, value) ->
        button.setOnClickListener { sharedPreferences.edit { putString(key, value) } }
        button.isChecked = value == storedValue
    }
}

/**
 * Fills an exposed dropdown menu with [entries] as shown to the user, stores the matching
 * item of [values] under [key], and tells [onSelected] about every pick, the stored one
 * included, so that the surrounding settings can follow along.
 */
fun TextInputLayout.bindToPreference(
    sharedPreferences: SharedPreferences,
    key: String,
    defaultValue: String,
    entries: List<String>,
    values: List<String>,
    onSelected: (value: String) -> Unit = {},
) {
    val autoCompleteTextView = editText as? AutoCompleteTextView ?: return
    // Whatever the two lists disagree on has no name to show or no value to store, so leave it out.
    val choices: List<Pair<String, String>> = entries.zip(values)

    autoCompleteTextView.setAdapter(
        ArrayAdapter(
            context, android.R.layout.simple_dropdown_item_1line, choices.map { (entry, _) -> entry }
        )
    )

    val storedValue = sharedPreferences.getString(key, defaultValue) ?: defaultValue
    autoCompleteTextView.setText(choices.find { (_, value) -> value == storedValue }?.first, false)
    onSelected(storedValue)

    autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
        val (_, value) = choices[position]
        sharedPreferences.edit { putString(key, value) }
        onSelected(value)
    }
}
