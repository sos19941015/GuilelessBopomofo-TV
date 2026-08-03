package org.ghostsinthelab.apps.guilelessbopomofo

import android.view.inputmethod.EditorInfo
import org.ghostsinthelab.apps.guilelessbopomofo.utils.EnterKeyBehavior
import org.ghostsinthelab.apps.guilelessbopomofo.utils.EnterKeyBehaviorResolver
import org.junit.Assert.assertEquals
import org.junit.Test

class EnterKeyBehaviorResolverTest {

    private fun resolve(
        imeOptions: Int,
        actionId: Int = EditorInfo.IME_ACTION_UNSPECIFIED,
        hasCustomActionLabel: Boolean = false,
    ): EnterKeyBehavior = EnterKeyBehaviorResolver.resolve(imeOptions, actionId, hasCustomActionLabel)

    @Test
    fun noImeOptions_isAPlainEnterKey() {
        assertEquals(EnterKeyBehavior.NewLine, resolve(EditorInfo.IME_ACTION_UNSPECIFIED))
    }

    @Test
    fun actionNone_isAPlainEnterKey() {
        assertEquals(EnterKeyBehavior.NewLine, resolve(EditorInfo.IME_ACTION_NONE))
    }

    @Test
    fun noEnterActionFlag_winsOverAnyDeclaredAction() {
        assertEquals(
            EnterKeyBehavior.NewLine,
            resolve(EditorInfo.IME_ACTION_SEND or EditorInfo.IME_FLAG_NO_ENTER_ACTION)
        )
    }

    @Test
    fun noEnterActionFlag_winsOverACustomActionLabel() {
        assertEquals(
            EnterKeyBehavior.NewLine,
            resolve(
                imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION,
                actionId = 42,
                hasCustomActionLabel = true,
            )
        )
    }

    @Test
    fun declaredActions_areAllPerformed() {
        val actions = listOf(
            EditorInfo.IME_ACTION_GO,
            EditorInfo.IME_ACTION_SEARCH,
            EditorInfo.IME_ACTION_SEND,
            EditorInfo.IME_ACTION_NEXT,
            EditorInfo.IME_ACTION_DONE,
            EditorInfo.IME_ACTION_PREVIOUS,
        )

        actions.forEach { action ->
            assertEquals(
                "action $action should be performed",
                EnterKeyBehavior.EditorAction(action),
                resolve(action)
            )
        }
    }

    @Test
    fun unrelatedImeFlags_doNotHideTheAction() {
        val imeOptions = EditorInfo.IME_ACTION_SEARCH or
                EditorInfo.IME_FLAG_NO_EXTRACT_UI or
                EditorInfo.IME_FLAG_NO_FULLSCREEN or
                EditorInfo.IME_FLAG_NAVIGATE_NEXT

        assertEquals(
            EnterKeyBehavior.EditorAction(EditorInfo.IME_ACTION_SEARCH),
            resolve(imeOptions)
        )
    }

    @Test
    fun customActionLabel_usesItsOwnActionId() {
        assertEquals(
            EnterKeyBehavior.EditorAction(1234),
            resolve(
                imeOptions = EditorInfo.IME_ACTION_UNSPECIFIED,
                actionId = 1234,
                hasCustomActionLabel = true,
            )
        )
    }

    @Test
    fun customActionLabel_winsOverTheImeOptionsAction() {
        assertEquals(
            EnterKeyBehavior.EditorAction(1234),
            resolve(
                imeOptions = EditorInfo.IME_ACTION_DONE,
                actionId = 1234,
                hasCustomActionLabel = true,
            )
        )
    }

    @Test
    fun customActionLabel_withoutAnActionId_fallsBackToTheImeOptionsAction() {
        assertEquals(
            EnterKeyBehavior.EditorAction(EditorInfo.IME_ACTION_SEND),
            resolve(
                imeOptions = EditorInfo.IME_ACTION_SEND,
                actionId = EditorInfo.IME_ACTION_UNSPECIFIED,
                hasCustomActionLabel = true,
            )
        )
    }

    @Test
    fun actionIdWithoutACustomLabel_isIgnored() {
        // actionId is only meaningful together with actionLabel.
        assertEquals(
            EnterKeyBehavior.NewLine,
            resolve(
                imeOptions = EditorInfo.IME_ACTION_UNSPECIFIED,
                actionId = 1234,
                hasCustomActionLabel = false,
            )
        )
    }
}
