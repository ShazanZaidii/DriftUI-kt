package com.example.driftui.core

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * A type-erased controller that allows pushing new tabs without
 * requiring the developer to pass generic types into the hook.
 */
class TabController(private val onTabChanged: (Any) -> Unit) {
    fun push(tab: Any) {
        onTabChanged(tab)
    }
}

/**
 * The hidden CompositionLocal that acts as our global radio station.
 */
@PublishedApi
internal val LocalTabController = staticCompositionLocalOf<TabController?> { null }

/**
 * The smart wrapper that manages state, handles the Android back button,
 * and injects the routing controller into the Compose tree.
 * * @param initialTab The default tab to show when the dashboard first loads.
 * @param content The scaffold/UI that receives the current tab and a manual setter.
 */
@Composable
inline fun <reified T : Enum<T>> DriftTabNavigator(
    initialTab: T,
    crossinline content: @Composable (currentTab: T, setTab: (T) -> Unit) -> Unit
) {
    // Automatically saves tab state even if a DriftUI full-screen route covers it
    var currentTab by rememberSaveable { mutableStateOf(initialTab) }

    // The controller accepts 'Any', but we safely cast it back to 'T' for the state
    val controller = remember { TabController { currentTab = it as T } }

    // Smart BackStack: If the user presses the hardware back button and they
    // aren't on the initial tab, return them to the initial tab natively.
    BackHandler(enabled = currentTab != initialTab) {
        currentTab = initialTab
    }

    // Inject the controller globally without the dev having to write boilerplate
    CompositionLocalProvider(
        LocalTabController provides controller
    ) {
        content(currentTab) { currentTab = it }
    }
}

/**
 * The silky-smooth hook for nested tab routing.
 * Use this anywhere inside a DriftTabNavigator to switch tabs.
 * * Example:
 * val tabNav = useTabNav()
 * tabNav.push(EmployerTab.POSTINGS)
 */
@Composable
fun useTabNav(): TabController {
    return LocalTabController.current
        ?: error("useTabNav() must be called inside a DriftTabNavigator!")
}