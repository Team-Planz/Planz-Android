@file:OptIn(ExperimentalMaterialApi::class)

package com.yapp.growth.presentation.ui.main.fix

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.growth.base.LoadState
import com.yapp.growth.presentation.R
import com.yapp.growth.presentation.component.*
import com.yapp.growth.presentation.firebase.onDynamicLinkClick
import com.yapp.growth.presentation.ui.main.fix.FixPlanContract.FixPlanEvent
import com.yapp.growth.presentation.ui.main.fix.FixPlanContract.FixPlanSideEffect
import kotlinx.coroutines.launch

@Composable
fun FixPlanScreen(
    viewModel: FixPlanViewModel = hiltViewModel(),
    navigateToPreviousScreen: () -> Unit,
    navigateToNextScreen: (Long) -> Unit,
) {
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.viewState.collectAsState()
    val context = LocalContext.current

    PlanzBottomSheetScaffoldLayout(
        scaffoldState = scaffoldState,
        sheetContent = {
            if (uiState.bottomSheet == FixPlanContract.FixPlanViewState.BottomSheet.RESPONDENT) {
                PlanzRespondentBottomSheetContent(
                    promisingName = uiState.timeTable.promisingName,
                    respondents = uiState.respondents
                )
            } else {
                PlanzParticipantBottomSheetContent(
                    timeTable = uiState.timeTable,
                    currentClickTimeIndex = uiState.currentClickTimeIndex,
                    currentClickUserData = uiState.currentClickUserData,
                    onClickSelectPlan = { date -> viewModel.setEvent(FixPlanEvent.OnClickFixButton(date)) },
                    isLeader = true
                )
            }
        }
    ) {

        Scaffold(
            topBar = {
                PlanzUserAndShareAppBar(
                    title = if (uiState.loadState == LoadState.SUCCESS) uiState.timeTable.promisingName else stringResource(R.string.fix_plan_title),
                    onClickBackIcon = { viewModel.setEvent(FixPlanEvent.OnClickBackButton) },
                    onClickUserIcon = { viewModel.setEvent(FixPlanEvent.OnClickUserIcon) },
                    onClickShareIcon = { onDynamicLinkClick(context = context, id = uiState.planId.toString()) }
                )
            }
        ) { padding ->

            when (uiState.loadState) {
                LoadState.LOADING -> PlanzLoading()
                LoadState.ERROR -> {
                    PlanzError(
                        retryVisible = true,
                        onClickRetry = {
                            viewModel.setEvent(FixPlanEvent.OnClickErrorRetryButton)
                        },
                    )
                }
                LoadState.SUCCESS -> {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        val (column, button) = createRefs()

                        Column(modifier = Modifier.constrainAs(column) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(button.top)
                            height = Dimension.fillToConstraints
                        }) {

                            LocationAndAvailableColorBox(timeTable = uiState.timeTable)

                            PlanzPlanDateIndicator(
                                timeTable = uiState.timeTable,
                                onClickPreviousDayButton = { viewModel.setEvent(FixPlanEvent.OnClickPreviousDayButton) },
                                onClickNextDayButton = { viewModel.setEvent(FixPlanEvent.OnClickNextDayButton) },
                                enablePrev = uiState.enablePrev,
                                enableNext = uiState.enableNext,
                            )

                            FixPlanTimeTable(
                                timeTable = uiState.timeTable,
                                onClickTimeTable = { dateIndex, minuteIndex ->
                                    viewModel.setEvent(
                                        FixPlanEvent.OnClickTimeTable(dateIndex, minuteIndex)
                                    )
                                },
                                currentClickTimeIndex = uiState.currentClickTimeIndex
                            )
                        }
                    }
                }
            }
        }

    }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FixPlanSideEffect.ShowBottomSheet -> {
                    coroutineScope.launch { sheetState.expand() }
                }

                is FixPlanSideEffect.HideBottomSheet -> {
                    coroutineScope.launch { sheetState.collapse() }
                }
                is FixPlanSideEffect.NavigateToNextScreen -> {
                    navigateToNextScreen(effect.planId)
                }
                FixPlanSideEffect.NavigateToPreviousScreen -> navigateToPreviousScreen()
            }

        }
    }

    BackHandler(enabled = sheetState.isExpanded) {
        coroutineScope.launch { sheetState.collapse() }
    }
}
