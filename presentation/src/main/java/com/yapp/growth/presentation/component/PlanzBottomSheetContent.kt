package com.yapp.growth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yapp.growth.domain.entity.TimeTable
import com.yapp.growth.domain.entity.User
import com.yapp.growth.presentation.R
import com.yapp.growth.presentation.theme.Gray700
import com.yapp.growth.presentation.theme.Gray800
import com.yapp.growth.presentation.theme.MainPurple900
import com.yapp.growth.presentation.theme.PlanzTypography
import com.yapp.growth.presentation.util.getCurrentBlockDate
import com.yapp.growth.presentation.util.toHour
import com.yapp.growth.presentation.util.toPlanDate

@Composable
fun PlanzParticipantBottomSheetContent(
    timeTable: TimeTable,
    currentClickTimeIndex: Pair<Int, Int>,
    currentClickUserData: List<User>,
    onClickSelectPlan: (String) -> Unit = { },
    isLeader: Boolean,
) {
    if (currentClickTimeIndex.first < 0 || currentClickTimeIndex.second < 0) return

    val day = timeTable.availableDates[currentClickTimeIndex.first].split('T').first()
    val hour = timeTable.minTime.toHour()
    val time = "${day}T${hour}:00"

    val respondUserText = StringBuilder()
    currentClickUserData.forEachIndexed { index, user ->
        when (index) {
            0, 3, 7 -> respondUserText.append(user.userName)
            2, 6 -> respondUserText.append(", ${user.userName.plus("\n")}")
            else -> respondUserText.append(", ${user.userName}")
        }
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.planz_component_participant_bottom_sheet_content_promising_date),
                style = PlanzTypography.subtitle2,
                color = Gray700
            )

            Text(
                text = time.getCurrentBlockDate(currentClickTimeIndex.second).toPlanDate(),
                style = PlanzTypography.subtitle2,
                color = MainPurple900
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.planz_component_participant_bottom_sheet_content_respondent),
                style = PlanzTypography.subtitle2,
                color = Gray700
            )

            Text(
                text = if (respondUserText.isNotEmpty()) respondUserText.toString() else stringResource(
                    id = R.string.planz_component_participant_bottom_sheet_content_empty
                ),
                style = PlanzTypography.caption,
                color = Gray800,
                textAlign = TextAlign.End
            )
        }

        if (isLeader) {
            PlanzBasicBottomButton(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
                text = stringResource(id = R.string.fix_plan_fix_button_title),
                onClick = {
                    onClickSelectPlan(time.getCurrentBlockDate(currentClickTimeIndex.second))
                })
        }
    }
}

@Composable
fun PlanzRespondentBottomSheetContent(
    promisingName: String,
    respondents: List<User>,
    onExitClick: () -> Unit,
) {
    val respondentText = StringBuilder()
    respondents.forEachIndexed { index, user ->
        when (index) {
            0, 3, 7 -> respondentText.append(user.userName)
            2, 6 -> respondentText.append(", ${user.userName.plus("\n")}")
            else -> respondentText.append(", ${user.userName}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 20.dp, end = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row {
                    Text(
                        text = promisingName,
                        style = PlanzTypography.subtitle1,
                        color = MainPurple900
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = stringResource(R.string.planz_component_respondent_bottom_sheet_content_party_member),
                        style = PlanzTypography.subtitle1,
                        color = Gray800
                    )
                }

                Text(
                    text = respondentText.toString(),
                    style = PlanzTypography.caption,
                    color = Gray800
                )
            }

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_exit_24),
                tint = Color.Unspecified,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .clickable { onExitClick() }
            )
        }
    }
}
