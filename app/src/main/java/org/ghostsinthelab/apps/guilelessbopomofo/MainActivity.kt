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

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import org.ghostsinthelab.apps.guilelessbopomofo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val logTag: String = "MainActivity"

    // ViewBinding
    private lateinit var viewBinding: ActivityMainBinding

    companion object {
        // How many taps on the app icon it takes to reveal the engineering mode.
        private const val ENGINEERING_MODE_ENTER_CLICKS = 5
    }

    private var engineeringModeEnterCount: Int = 0
    private var engineeringModeEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(logTag, "onCreate()")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        viewBinding.apply {
            textViewAppVersion.text = getString(
                R.string.app_version, BuildConfig.VERSION_NAME, ChewingBridge.chewing.version()
            )

            imageViewAppIcon.setOnClickListener {
                if (engineeringModeEnterCount >= ENGINEERING_MODE_ENTER_CLICKS || engineeringModeEnabled) {
                    engineeringModeEnabled = true
                    startActivity(Intent(this@MainActivity, EngineeringModeActivity::class.java))
                } else {
                    engineeringModeEnterCount += 1
                }
            }

            bottomNavigation.setOnItemSelectedListener { item ->
                val fragment: Fragment? = when (item.itemId) {
                    R.id.nav_general -> GeneralSettingsFragment()
                    R.id.nav_user_interface -> UserInterfaceSettingsFragment()
                    R.id.nav_physical_keyboard -> PhysicalKeyboardSettingsFragment()
                    R.id.nav_user_phrases -> UserPhraseManagerFragment()
                    else -> null
                }
                if (fragment == null) {
                    false
                } else {
                    switchFragment(fragment)
                    true
                }
            }
        }

        setContentView(viewBinding.root)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewBinding.headerLayout.isVisible = false
            viewBinding.divider.isVisible = false
        }

        // Apply system-bar and cutout insets as internal padding on the header and
        // bottom navigation so the window itself can still draw edge-to-edge while
        // the contents stay clear of the status bar, navigation bar, and cutouts.
        // In landscape the header is hidden, so the top inset is routed to the
        // fragment container instead.
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            if (!viewBinding.headerLayout.isVisible) {
                viewBinding.fragmentContainer.updatePadding(
                    left = insets.left, top = insets.top, right = insets.right
                )
            } else {
                viewBinding.headerLayout.updatePadding(
                    left = insets.left, top = insets.top, right = insets.right
                )
            }
            viewBinding.bottomNavigation.updatePadding(
                left = insets.left, right = insets.right, bottom = insets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        // Show general settings tab by default
        if (savedInstanceState == null) {
            switchFragment(GeneralSettingsFragment())
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
