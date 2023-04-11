package com.yapp.growth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yapp.growth.domain.entity.User
import com.yapp.growth.presentation.R
import com.yapp.growth.presentation.theme.Gray800
import com.yapp.growth.presentation.theme.MainPurple900
import com.yapp.growth.presentation.theme.PlanzTypography

@Composable
fun PlanzRespondentBottomSheetContent(
    promisingName: String,
    respondents: List<User>,
) {
    val respondentText = StringBuilder()
    respondents.forEachIndexed { index, user ->
        when (index) {
            0, 3, 7 -> respondentText.append(user.userName)
            2, 6 -> respondentText.append(", ${user.userName.plus("\n")}")
            else -> respondentText.append(", ${user.userName}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .padding(top = 4.dp),
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
}
