package com.sena.taskmanager.ui.navHost

sealed class Screen(
    val route: String
) {

    data object Login : Screen("login")

    data object Register : Screen("register")

    data object TaskList : Screen("task_list")

    data object TaskForm : Screen("task_form")

    data object Drafts : Screen("drafts")

    data object DraftForm : Screen("draft_form")
}