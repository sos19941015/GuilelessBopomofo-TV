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

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.ghostsinthelab.apps.guilelessbopomofo.databinding.DialogAddUserPhraseBinding
import org.ghostsinthelab.apps.guilelessbopomofo.databinding.FragmentUserPhraseManagerBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class UserPhraseManagerFragment : ViewBindingFragment<FragmentUserPhraseManagerBinding>() {
    private val logTag = "UserPhraseManager"

    private lateinit var adapter: UserPhraseAdapter

    private val backupLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let { exportUserPhrases(it) }
    }

    private val restoreLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { importUserPhrases(it) }
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUserPhraseManagerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ChewingUtil.ensureChewingConnected(requireContext())

        adapter = UserPhraseAdapter(mutableListOf()) { userPhrase ->
            confirmDeletePhrase(userPhrase)
        }
        binding.recyclerViewUserPhrases.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUserPhrases.adapter = adapter

        binding.fabAddPhrase.setOnClickListener { showAddPhraseDialog() }
        binding.buttonBackup.setOnClickListener { backupLauncher.launch("guileless_bopomofo_user_phrases.csv") }
        binding.buttonRestore.setOnClickListener { restoreLauncher.launch(arrayOf("text/*")) }

        binding.buttonResetUserPhraseData.setOnClickListener { confirmResetUserPhraseData() }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""
                adapter.filter(query)
                updateEmptyState(query)
            }
        })

        loadUserPhrases()
    }

    override fun onResume() {
        super.onResume()
        if (this::adapter.isInitialized) {
            loadUserPhrases()
        }
    }

    private fun loadUserPhrases() {
        adapter.setData(ChewingUtil.enumerateUserPhrases())
        val query = binding.editTextSearch.text?.toString()?.trim() ?: ""
        if (query.isNotEmpty()) {
            adapter.filter(query)
        }
        updateEmptyState(query)
    }

    private fun updateEmptyState(query: String) {
        val isEmpty = adapter.itemCount == 0
        binding.textViewEmptyState.isVisible = isEmpty
        binding.recyclerViewUserPhrases.isVisible = !isEmpty
        binding.textViewEmptyState.text = if (query.isNotEmpty()) {
            getString(R.string.search_user_phrases_no_results)
        } else {
            getString(R.string.no_user_phrases)
        }
    }

    private fun toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    private fun toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    private fun confirmDeletePhrase(userPhrase: UserPhrase) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_user_phrase_title)
            .setMessage(getString(R.string.delete_user_phrase_message, userPhrase.phrase))
            .setPositiveButton(R.string.delete_user_phrase) { _, _ ->
                ChewingBridge.chewing.userphraseRemove(userPhrase.phrase, userPhrase.bopomofo)
                ChewingUtil.flushContext(requireContext())
                loadUserPhrases()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun exportUserPhrases(uri: Uri) {
        val phrases = ChewingUtil.enumerateUserPhrases()
        if (phrases.isEmpty()) {
            toast(R.string.no_user_phrases)
            return
        }

        try {
            requireContext().contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream, Charsets.UTF_8).use { writer ->
                    writer.write(UserPhraseCsv.format(phrases))
                }
            }
            toast(getString(R.string.backup_success, phrases.size))
        } catch (e: Exception) {
            Log.e(logTag, "Failed to export user phrases", e)
            toast(R.string.backup_failed)
        }
    }

    private fun importUserPhrases(uri: Uri) {
        try {
            val lines = requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { it.readLines() }
            } ?: emptyList()

            when (val result = UserPhraseCsv.parse(lines)) {
                is UserPhraseCsv.ParseResult.Success -> confirmRestore(result.phrases)
                else -> {
                    val message = describeParseFailure(result)
                    Log.w(logTag, "CSV validation failed: $message")
                    toast(message, Toast.LENGTH_LONG)
                }
            }
        } catch (e: Exception) {
            Log.e(logTag, "Failed to import user phrases", e)
            toast(R.string.restore_failed)
        }
    }

    private fun describeParseFailure(result: UserPhraseCsv.ParseResult): String = when (result) {
        is UserPhraseCsv.ParseResult.UnreadableFile -> getString(R.string.restore_invalid_file)
        is UserPhraseCsv.ParseResult.UnexpectedHeader -> getString(R.string.restore_invalid_header)
        is UserPhraseCsv.ParseResult.MalformedRow ->
            getString(R.string.restore_malformed_row, result.lineNumber)

        is UserPhraseCsv.ParseResult.InvalidBopomofo ->
            getString(R.string.restore_invalid_bopomofo, result.lineNumber)

        is UserPhraseCsv.ParseResult.Success -> ""
    }

    private fun confirmRestore(phrases: List<UserPhrase>) {
        if (phrases.isEmpty()) {
            toast(R.string.restore_no_data)
            return
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.restore_user_phrases)
            .setMessage(getString(R.string.restore_confirm_message, phrases.size))
            .setPositiveButton(R.string.restore_user_phrases) { _, _ ->
                val added = phrases.count {
                    ChewingBridge.chewing.userphraseAdd(it.phrase, it.bopomofo) > 0
                }
                ChewingUtil.flushContext(requireContext())
                loadUserPhrases()
                toast(getString(R.string.restore_success, added))
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun confirmResetUserPhraseData() {
        // Asked twice on purpose: there is no undo for this one.
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.reset_user_phrase_data)
            .setMessage(R.string.reset_user_phrase_data_confirm_first)
            .setPositiveButton(R.string.reset_user_phrase_data) { _, _ ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.reset_user_phrase_data)
                    .setMessage(R.string.reset_user_phrase_data_confirm_second)
                    .setPositiveButton(R.string.reset_user_phrase_data) { _, _ ->
                        ChewingUtil.resetUserPhraseData(requireContext())
                        loadUserPhrases()
                        toast(R.string.reset_user_phrase_data_done)
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showAddPhraseDialog() {
        BopomofoLookup.init(requireContext())
        val dialogBinding = DialogAddUserPhraseBinding.inflate(layoutInflater)

        dialogBinding.buttonAnalyzeBopomofo.setOnClickListener {
            val phrase = dialogBinding.editTextPhrase.text?.toString()?.trim() ?: ""
            if (phrase.isEmpty()) return@setOnClickListener

            val combinations = BopomofoLookup.generateCombinations(phrase)
            if (combinations.isEmpty()) {
                toast(R.string.analyze_bopomofo_no_results)
                return@setOnClickListener
            }

            dialogBinding.autoCompleteBopomofo.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, combinations)
            )
            dialogBinding.autoCompleteBopomofo.setText(combinations[0], false)
            dialogBinding.textInputLayoutBopomofoDropdown.isVisible = true
            dialogBinding.textInputLayoutBopomofoManual.isVisible = false
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.add_user_phrase)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.add_user_phrase) { _, _ ->
                val phrase = dialogBinding.editTextPhrase.text?.toString()?.trim() ?: ""
                val bopomofo = if (dialogBinding.textInputLayoutBopomofoDropdown.isVisible) {
                    dialogBinding.autoCompleteBopomofo.text?.toString()?.trim() ?: ""
                } else {
                    dialogBinding.editTextBopomofo.text?.toString()?.trim() ?: ""
                }

                if (phrase.isEmpty() || bopomofo.isEmpty()) {
                    toast(R.string.user_phrase_input_empty)
                    return@setPositiveButton
                }

                if (ChewingBridge.chewing.userphraseAdd(phrase, bopomofo) > 0) {
                    ChewingUtil.flushContext(requireContext())
                    loadUserPhrases()
                } else {
                    toast(R.string.user_phrase_add_failed)
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
