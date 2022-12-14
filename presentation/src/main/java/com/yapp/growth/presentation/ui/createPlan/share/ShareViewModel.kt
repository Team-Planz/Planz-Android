package com.yapp.growth.presentation.ui.createPlan.share

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.yapp.growth.base.BaseViewModel
import com.yapp.growth.presentation.BuildConfig
import com.yapp.growth.presentation.R
import com.yapp.growth.presentation.firebase.SchemeType
import com.yapp.growth.presentation.firebase.getDeepLink
import com.yapp.growth.presentation.ui.createPlan.share.ShareContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class ShareViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ShareViewState, ShareSideEffect, ShareEvent>(ShareViewState()) {

    private val planId = savedStateHandle.get<Long>("planId")

    init {
        updateState { copy(planId = this@ShareViewModel.planId) }

    }
    override fun handleEvents(event: ShareEvent) {
        when (event) {
            is ShareEvent.OnClickExit -> sendEffect({ ShareSideEffect.FinishCreatePlan })
            is ShareEvent.OnClickCopy -> {
                updateState { copy(snackBarType = ShareViewState.SnackBarType.SUCCESS) }
                sendEffect({ ShareSideEffect.CopyShareUrl })
            }
            is ShareEvent.OnClickShare -> {
                sendEffect({ ShareSideEffect.SendKakaoShareMessage })
            }
            is ShareEvent.FailToShare -> {
                updateState { copy(snackBarType = ShareViewState.SnackBarType.FAIL) }
                sendEffect({ ShareSideEffect.ShowFailToShareSnackBar })
            }
        }
    }

    fun getDynamicLink(
        context: Context,
        scheme: SchemeType = SchemeType.RESPOND,
        id: String = planId.toString(),
        thumbNailTitle: String,
        thumbNailDescription: String,
        thumbNailImageUrl: String,
    ) {
        Firebase.dynamicLinks.shortLinkAsync {
            link = getDeepLink(scheme.name, scheme.key, id)
            domainUriPrefix = BuildConfig.PLANZ_FIREBASE_PREFIX
            androidParameters(context.packageName) { }
            iosParameters(context.packageName) {
                setFallbackUrl(Uri.parse("https://jalynne.notion.site/3379be16ecc04914bb98f8a57c980a46"))
            }
            socialMetaTagParameters {
                title = thumbNailTitle
                description = thumbNailDescription
                imageUrl = Uri.parse(thumbNailImageUrl)
            }

        }.addOnSuccessListener { (shortLink, _) ->
            updateState { copy(shareUrl = shortLink.toString()) }
        }
    }
}
