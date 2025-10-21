package com.ubb.fmi.orar.ui.navigation.model

import com.ubb.fmi.orar.ui.navigation.destination.MainNavDestination
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_explore
import orar_ubb_fmi.ui.catalog.generated.resources.ic_home
import orar_ubb_fmi.ui.catalog.generated.resources.ic_news
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_explore
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_home
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_news
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the items in the bottom navigation bar of the timetable.
 *
 * @property labelRes The string resource for the label of the item.
 * @property icon The drawable resource for the icon of the item.
 * @property destination The navigation destination associated with the item.
 */
enum class BottomBarItem(
    val labelRes: StringResource,
    val icon: DrawableResource,
    val destination: MainNavDestination,
) {
    HOME(
        labelRes = Res.string.lbl_home,
        icon = Res.drawable.ic_home,
        destination = MainNavDestination.UserMain,
    ),
    NEWS(
        labelRes = Res.string.lbl_news,
        icon = Res.drawable.ic_news,
        destination = MainNavDestination.News,
    ),
    EXPLORE(
        labelRes = Res.string.lbl_explore,
        icon = Res.drawable.ic_explore,
        destination = MainNavDestination.Explore,
    )
}
