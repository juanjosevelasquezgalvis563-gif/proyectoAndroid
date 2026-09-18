package com.sena.taskmanager.ui.navHost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sena.taskmanager.domain.model.Task
import com.sena.taskmanager.domain.model.TaskDraft
import com.sena.taskmanager.ui.screen.drafts.DraftFormScreen
import com.sena.taskmanager.ui.screen.drafts.DraftsScreen
import com.sena.taskmanager.ui.screen.login.LoginScreen
import com.sena.taskmanager.ui.screen.register.RegisterScreen
import com.sena.taskmanager.ui.screen.taskform.TaskFormScreen
import com.sena.taskmanager.ui.screen.tasklist.TaskListScreen

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    var selectedTask by remember {
        mutableStateOf<Task?>(null)
    }

    var selectedDraft by remember {
        mutableStateOf<TaskDraft?>(null)
    }

    val startDestination =
        if (authViewModel.isUserLoggedIn()) {
            Screen.TaskList.route
        } else {
            Screen.Login.route
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // LOGIN
        composable(Screen.Login.route) {

            LoginScreen(
                onLoginSuccess = {

                    navController.navigate(
                        Screen.TaskList.route
                    ) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },

                onRegisterClick = {

                    navController.navigate(
                        Screen.Register.route
                    )
                }
            )
        }

        // REGISTER
        composable(Screen.Register.route) {

            RegisterScreen(
                onRegisterSuccess = {

                    navController.navigate(
                        Screen.TaskList.route
                    ) {
                        popUpTo(Screen.Register.route) {
                            inclusive = true
                        }
                    }
                },

                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        // TASK LIST
        composable(Screen.TaskList.route) {

            TaskListScreen(

                onCreateTask = {

                    selectedTask = null

                    navController.navigate(
                        Screen.TaskForm.route
                    )
                },

                onEditTask = { task ->

                    selectedTask = task

                    navController.navigate(
                        Screen.TaskForm.route
                    )
                },

                onDrafts = {

                    navController.navigate(
                        Screen.Drafts.route
                    )
                },

                onLogout = {

                    authViewModel.logout()

                    selectedTask = null
                    selectedDraft = null

                    navController.navigate(
                        Screen.Login.route
                    ) {

                        popUpTo(
                            Screen.TaskList.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // TASK FORM
        composable(Screen.TaskForm.route) {

            TaskFormScreen(
                task = selectedTask,

                onBack = {

                    selectedTask = null

                    navController.popBackStack()
                }
            )
        }

        // DRAFT LIST
        composable(Screen.Drafts.route) {

            DraftsScreen(

                onBack = {
                    navController.popBackStack()
                },

                onEditDraft = { draft ->

                    selectedDraft = draft

                    navController.navigate(
                        Screen.DraftForm.route
                    )
                }
            )
        }

        // DRAFT FORM
        composable(Screen.DraftForm.route) {

            selectedDraft?.let { draft ->

                DraftFormScreen(

                    draft = draft,

                    onBack = {

                        selectedDraft = null

                        navController.popBackStack()
                    }
                )
            }
        }
    }
}