package com.yapp.growth.presentation.ui.createPlan.title

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.growth.base.LoadState
import com.yapp.growth.presentation.R
import com.yapp.growth.presentation.component.*
import com.yapp.growth.presentation.ui.createPlan.CreatePlanContract.CreatePlanEvent.DecidePlace
import com.yapp.growth.presentation.ui.createPlan.CreatePlanContract.CreatePlanEvent.DecideTitle
import com.yapp.growth.presentation.ui.createPlan.CreatePlanViewModel
import com.yapp.growth.presentation.ui.createPlan.title.TitleContract.TitleEvent
import com.yapp.growth.presentation.ui.createPlan.title.TitleContract.TitleSideEffect
import com.yapp.growth.presentation.util.composableActivityViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TitleScreen(
    sharedViewModel: CreatePlanViewModel = composableActivityViewModel(),
    viewModel: TitleViewModel = hiltViewModel(),
    exitCreateScreen: () -> Unit,
    navigateToNextScreen: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
) {
    val viewState by viewModel.viewState.collectAsState()
    val focusManager = LocalFocusManager.current

    if (viewState.sampleTitle.isBlank()) {
        viewModel.setEvent(
            TitleEvent.InitHintText(sharedViewModel.viewState.value.category?.id ?: 0)
        )
    }

    Scaffold(
        topBar = {
            PlanzCreateStepTitle(
                currentStep = 2,
                title = stringResource(id = R.string.create_plan_title_title_text),
                onExitClick = { viewModel.setEvent(TitleEvent.OnClickExitButton) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            when (viewState.loadState) {
                LoadState.SUCCESS -> {
                    Column(
                        modifier = Modifier.padding(top = 44.dp),
                        verticalArrangement = Arrangement.spacedBy(26.dp)
                    ) {
                        PlanzTextField(
                            label = stringResource(id = R.string.create_plan_title_title_label),
                            hint = viewState.sampleTitle,
                            maxLength = MAX_LENGTH_TITLE,
                            text = viewState.title,
                            onInputChanged = { viewModel.setEvent(TitleEvent.FillInTitle(it)) },
                            onDeleteClicked = { viewModel.setEvent(TitleEvent.FillInTitle("")) },
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        )

                        PlanzTextField(
                            label = stringResource(id = R.string.create_plan_title_place_label),
                            hint = stringResource(id = R.string.create_plan_title_place_hint),
                            maxLength = MAX_LENGTH_PLACE,
                            text = viewState.place,
                            onInputChanged = { viewModel.setEvent(TitleEvent.FillInPlace(it)) },
                            onDeleteClicked = { viewModel.setEvent(TitleEvent.FillInPlace("")) },
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        )
                    }
                }
                LoadState.LOADING -> PlanzLoading()
                LoadState.ERROR -> PlanzError(
                    retryVisible = true,
                    onClickRetry = {
                        viewModel.setEvent(
                            TitleEvent.OnClickErrorRetryButton(
                                sharedViewModel.viewState.value.category?.id ?: 0
                            )
                        )
                    }
                )
            }

            PlanzButtonWithBack(
                text = stringResource(id = R.string.create_plan_next_button_text),
                enabled = !viewState.isError,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 32.dp),
                onClick = { viewModel.setEvent(TitleEvent.OnClickNextButton) },
                onBackClick = { viewModel.setEvent(TitleEvent.OnClickBackButton) }
            )
        }
    }

    LaunchedEffect(key1 = viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TitleSideEffect.ExitCreateScreen -> {
                    exitCreateScreen()
                }
                is TitleSideEffect.NavigateToNextScreen -> {
                    sharedViewModel.setEvent(
                        DecideTitle(viewState.title.ifBlank { viewState.sampleTitle })
                    )
                    sharedViewModel.setEvent(DecidePlace(viewState.place))
                    navigateToNextScreen()
                }
                is TitleSideEffect.NavigateToPreviousScreen -> {
                    navigateToPreviousScreen()
                }
            }
        }
    }
}

@Preview
@Composable
fun TitleScreenPreview() {
    TitleScreen(
        exitCreateScreen = {},
        navigateToNextScreen = { },
        navigateToPreviousScreen = {}
    )
}

const val MAX_LENGTH_TITLE = 10
const val MAX_LENGTH_PLACE = 10
