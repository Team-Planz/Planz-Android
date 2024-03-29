package com.yapp.growth.presentation.ui.createPlan.timerange

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.growth.base.LoadState
import com.yapp.growth.presentation.R
import com.yapp.growth.presentation.component.*
import com.yapp.growth.presentation.model.TimeType
import com.yapp.growth.presentation.theme.*
import com.yapp.growth.presentation.ui.createPlan.CreatePlanContract
import com.yapp.growth.presentation.ui.createPlan.CreatePlanContract.CreatePlanEvent.DecideTimeRange
import com.yapp.growth.presentation.ui.createPlan.CreatePlanViewModel
import com.yapp.growth.presentation.ui.createPlan.timerange.TimeRangeContract.Companion.DEFAULT_END_HOUR
import com.yapp.growth.presentation.ui.createPlan.timerange.TimeRangeContract.Companion.DEFAULT_START_HOUR
import com.yapp.growth.presentation.ui.createPlan.timerange.TimeRangeContract.TimeRangeEvent
import com.yapp.growth.presentation.ui.createPlan.timerange.TimeRangeContract.TimeRangeSideEffect
import com.yapp.growth.presentation.ui.createPlan.timerange.TimeRangeContract.TimeRangeViewState.DialogState
import com.yapp.growth.presentation.util.composableActivityViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeRangeScreen(
    sharedViewModel: CreatePlanViewModel = composableActivityViewModel(),
    viewModel: TimeRangeViewModel = hiltViewModel(),
    exitCreateScreen: () -> Unit,
    navigateToNextScreen: (String) -> Unit,
    navigateToPreviousScreen: () -> Unit,
) {
    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()
    val sharedViewState by sharedViewModel.viewState.collectAsState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scaffoldState = rememberScaffoldState()
    val startHourListState = rememberLazyListState()
    val endHourListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // TODO: 드래그로 시트가 닫히지 않도록 수정
    PlanzModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            TimeRangeTimeBottomSheetContent(
                timeOptions = TimeType.values(),
                listState = if (viewState.timeSelectDialog == DialogState.START_HOUR) startHourListState else endHourListState,
                onItemClick = {
                    viewModel.setEvent(TimeRangeEvent.OnSelectedHourChanged(it.ordinal))
                },
            )
        }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                PlanzCreateStepTitle(
                    currentStep = 4,
                    title = stringResource(id = R.string.create_plan_time_range_title_text),
                    onExitClick = { viewModel.setEvent(TimeRangeEvent.OnClickExitButton) }
                )
            },
            snackbarHost = { snackbarHostState ->
                SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                    PlanzErrorSnackBar(
                        message = snackbarData.message,
                        bottomPadding = 100
                    )
                }
            }
        ) { padding ->
            when(sharedViewState.createTempPlanLoadState) {
                LoadState.SUCCESS -> {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)) {
                        TimeRangeText(
                            modifier = Modifier.padding(top = 140.dp),
                            isError = viewState.isSelectedErrorRange,
                            startHour = viewState.startHour,
                            endHour = viewState.endHour,
                            onStartHourClick = { viewModel.setEvent(TimeRangeEvent.OnStartHourClicked) },
                            onEndHourClick = { viewModel.setEvent(TimeRangeEvent.OnEndHourClicked) }
                        )

                        PlanzButtonWithBack(
                            text = stringResource(id = R.string.create_plan_next_button_text),
                            enabled = !viewState.isSelectedErrorRange,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 32.dp),
                            onClick = { viewModel.setEvent(TimeRangeEvent.OnClickNextButton) },
                            onBackClick = { viewModel.setEvent(TimeRangeEvent.OnClickBackButton) }
                        )
                    }
                }
                LoadState.LOADING -> PlanzLoading()
                LoadState.ERROR -> PlanzError(retryVisible = true)
            }

            PlanzAlertDialog(
                visible = viewState.isAlertDialogVisible,
                title = stringResource(R.string.planz_component_alert_dialog_title),
                content = stringResource(R.string.create_plan_time_range_alert_dialog_content),
                positiveButtonText = stringResource(R.string.planz_component_alert_dialog_positive_button_text),
                negativeButtonText = stringResource(R.string.planz_component_alert_dialog_negative_button_text),
                onClickNegativeButton = {
                    viewModel.setEvent(TimeRangeEvent.OnClickAlertDialogNegativeButton)
                },
                onClickPositiveButton = {
                    viewModel.setEvent(TimeRangeEvent.OnClickAlertDialogPositiveButton)
                },
            )
        }
    }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TimeRangeSideEffect.ExitCreateScreen -> {
                    exitCreateScreen()
                }
                is TimeRangeSideEffect.NavigateToPreviousScreen -> {
                    navigateToPreviousScreen()
                }
                is TimeRangeSideEffect.ShowBottomSheet -> {
                    coroutineScope.launch {
                        sheetState.show()
                    }

                }
                is TimeRangeSideEffect.HideBottomSheet -> {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                }
                is TimeRangeSideEffect.ShowSnackBar -> {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            context.getString(R.string.create_plan_time_range_error_message)
                        )
                    }
                }
                is TimeRangeSideEffect.CreateTemporaryPlan -> {
                    sharedViewModel.setEvent(
                        DecideTimeRange(
                            startHour = viewState.startHour ?: DEFAULT_START_HOUR,
                            endHour = viewState.endHour ?: DEFAULT_END_HOUR
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = sharedViewModel.effect) {
        sharedViewModel.effect.collect { effect ->
            when (effect) {
                is CreatePlanContract.CreatePlanSideEffect.NavigateToNextScreen -> {
                    navigateToNextScreen(sharedViewModel.viewState.value.tempPlanUuid)
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        sharedViewModel.setEvent(CreatePlanContract.CreatePlanEvent.EnterTimeRangeScreen)
    }
}

@Composable
fun TimeRangeTimeBottomSheetContent(
    timeOptions: Array<TimeType>,
    listState: LazyListState,
    onItemClick: (TimeType) -> Unit,
) {
    // TODO: Number Picker 형태로 수정
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        state = listState
    ) {
        items(timeOptions) { timeOption ->
            TimeRangeTimeBottomSheetItem(
                timeOption = timeOption,
                onItemClick = { onItemClick(timeOption) }
            )
        }
    }
}

@Composable
fun TimeRangeTimeBottomSheetItem(
    timeOption: TimeType,
    onItemClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(BackgroundColor3)
            .clickable { onItemClick() }
            .padding(vertical = 14.dp),
        text = stringResource(id = timeOption.timeStringResId),
        style = PlanzTypography.subtitle1,
        color = Gray900,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun TimeRangeText(
    modifier: Modifier = Modifier,
    isError: Boolean,
    startHour: Int?,
    endHour: Int?,
    onStartHourClick: () -> Unit,
    onEndHourClick: () -> Unit,
) {
    /*
        TODO: 밑줄 박스와 시간 텍스트 하나의 컴포넌트로 묶고 분리
        현재 텍스트들이 baseline 기준으로 정렬되어 있어서 분리가 어려움...
     */
    ConstraintLayout(
        modifier = modifier
            .padding(horizontal = 20.dp),
    ) {
        val (startHourText, fromText, endHourText, toText, startHourUnderline, endHourUnderline) = createRefs()

        Box(
            modifier = Modifier
                .width(50.dp)
                .height(12.dp)
                .constrainAs(startHourUnderline) {
                    start.linkTo(parent.absoluteLeft)
                    bottom.linkTo(parent.bottom, 4.dp)
                }
                .background(
                    when {
                        startHour == null -> Gray100
                        endHour != null && isError -> SubCoral.copy(alpha = 0.4f)
                        else -> MainPurple200
                    }
                )
        )

        Box(
            modifier = Modifier
                .width(50.dp)
                .height(12.dp)
                .constrainAs(endHourUnderline) {
                    start.linkTo(fromText.end, 12.dp)
                    bottom.linkTo(parent.bottom, 4.dp)
                }
                .background(
                    when {
                        endHour == null -> Gray100
                        startHour != null && isError -> SubCoral.copy(alpha = 0.4f)
                        else -> MainPurple200
                    }
                )
        )

        Text(
            modifier = Modifier
                .constrainAs(startHourText) {
                    start.linkTo(startHourUnderline.absoluteLeft)
                    end.linkTo(startHourUnderline.absoluteRight)
                    baseline.linkTo(parent.baseline)
                }
                .clickable { onStartHourClick() },
            text = String.format("%02d", (startHour ?: DEFAULT_START_HOUR)),
            style = PlanzTypography.h1.copy(
                fontSize = 36.sp
            ),
            color = when {
                startHour == null -> Gray300
                endHour != null && isError -> SubCoral
                else -> MainPurple900
            },
        )

        Text(
            modifier = Modifier.constrainAs(fromText) {
                start.linkTo(startHourUnderline.end, 8.dp)
                baseline.linkTo(parent.baseline)
            },
            text = stringResource(id = R.string.create_plan_time_range_from_text),
            style = PlanzTypography.subtitle1,
            color = Gray900
        )

        Text(
            modifier = Modifier
                .constrainAs(endHourText) {
                    start.linkTo(endHourUnderline.start)
                    end.linkTo(endHourUnderline.end)
                    baseline.linkTo(parent.baseline)
                }
                .clickable { onEndHourClick() },
            text = String.format("%02d", (endHour ?: DEFAULT_END_HOUR)),
            style = PlanzTypography.h1.copy(
                fontSize = 36.sp
            ),
            color = when {
                endHour == null -> Gray300
                startHour != null && isError -> SubCoral
                else -> MainPurple900
            },
        )

        Text(
            modifier = Modifier.constrainAs(toText) {
                start.linkTo(endHourUnderline.end, 8.dp)
                baseline.linkTo(parent.baseline)
            },
            text = stringResource(id = R.string.create_plan_time_range_to_text),
            style = PlanzTypography.subtitle1,
            color = Gray900
        )
    }
}

@Preview
@Composable
fun TimeRangeTimeBottomSheetItemPreview() {
    TimeRangeTimeBottomSheetItem(
        timeOption = TimeType.AM_01,
        onItemClick = {}
    )
}

@Preview
@Composable
fun TimeRangeScreenPreview() {
    TimeRangeScreen(
        exitCreateScreen = {},
        navigateToNextScreen = { },
        navigateToPreviousScreen = {}
    )
}
