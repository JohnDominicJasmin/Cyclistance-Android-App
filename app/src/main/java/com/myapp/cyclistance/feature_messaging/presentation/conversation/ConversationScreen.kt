package com.myapp.cyclistance.feature_messaging.presentation.conversation

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.myapp.cyclistance.core.utils.permissions.isGranted
import com.myapp.cyclistance.core.utils.permissions.requestPermission
import com.myapp.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.myapp.cyclistance.feature_messaging.presentation.conversation.components.ConversationContent
import com.myapp.cyclistance.feature_messaging.presentation.conversation.event.ConversationEvent
import com.myapp.cyclistance.feature_messaging.presentation.conversation.event.ConversationUiEvent
import com.myapp.cyclistance.feature_messaging.presentation.conversation.event.ConversationVmEvent
import com.myapp.cyclistance.feature_messaging.presentation.conversation.state.ConversationUiState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues,
    userReceiverId: String,
    isInternetAvailable: Boolean
) {

    val context = LocalContext.current
    val conversationState = viewModel.conversationState
    val state by viewModel.state.collectAsStateWithLifecycle()
    var uiState by rememberSaveable { mutableStateOf(ConversationUiState()) }
    var messageInput by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val onToggleExpand = remember {
        {
            uiState = uiState.copy(
                messageAreaExpanded = !uiState.messageAreaExpanded
            )
        }
    }
    val onChangeValueMessage = remember<(TextFieldValue) -> Unit> {
        {
            messageInput = it
        }
    }

    val onClickChatItem = remember {
        { index: Int ->
            uiState = uiState.copy(
                chatItemSelectedIndex = if (uiState.chatItemSelectedIndex == index) {
                    -1
                } else {
                    index
                })
        }
    }

    val resetSelectedIndex = remember {
        {
            uiState = uiState.copy(
                chatItemSelectedIndex = -1
            )
        }
    }

    val closeConversationMessage = remember {
        {
            navController.popBackStack()
        }
    }


    val sendMessage = remember(messageInput) {
        { message: String ->

            val receiverId = state.userReceiverMessage!!.userDetails.uid
            viewModel.onEvent(
                event = ConversationVmEvent.SendMessage(
                    sendMessageModel = SendMessageModel(
                        receiverId = receiverId,
                        message = message
                    )
                )).also {
                messageInput = TextFieldValue()
            }

        }
    }





    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            sendMessage(messageInput.text)
        }
    )



    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )

    val notificationPermissionDialogVisibility = remember{{ visibility: Boolean ->
        uiState = uiState.copy(notificationPermissionVisible = visibility)
    }}



    fun onSendMessage(){

        sendMessage(messageInput.text)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return

        }
        uiState = uiState.copy(prominentNotificationDialogVisible = !notificationPermissionState.isGranted())




    }

    val resendMessageDialogVisibility = remember{{ visibility: Boolean ->
        uiState = uiState.copy(resendDialogVisible = visibility)
    }}

    val resendMessage = remember{{
        viewModel.onEvent(event = ConversationVmEvent.ResendMessage)
    }}
    val markAsSeen = remember{{ messageId: String ->
        viewModel.onEvent(event = ConversationVmEvent.MarkAsSeen(messageId))
    }}

    val dismissProminentNotificationDialog = remember{{
        uiState = uiState.copy(prominentNotificationDialogVisible = false)
    }}

    val allowProminentNotificationDialog = remember{{
        notificationPermissionState.requestPermission(onGranted = {
            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }, onDenied = {
            notificationPermissionDialogVisibility(true)
        })
    }}


    LaunchedEffect(key1 = notificationPermissionState.isGranted()){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            return@LaunchedEffect
        }

        if(!notificationPermissionState.isGranted()){
            return@LaunchedEffect
        }

        notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

    }

    LaunchedEffect(key1 = userReceiverId){

        viewModel.onEvent(
            event = ConversationVmEvent.OnInitialized(userReceiverId = userReceiverId))
    }


    LaunchedEffect(key1 = true){
        viewModel.event.collectLatest { event ->
            when(event){

                is ConversationEvent.ResendMessageFailed -> {
                    Toast.makeText(context, "Resend failed", Toast.LENGTH_SHORT).show()
                }


            }
        }
    }
    BackHandler(enabled = true, onBack = {
        if (uiState.messageAreaExpanded) {
            onToggleExpand()
        } else {
            navController.popBackStack()
        }
    })




    ConversationContent(
        conversation = conversationState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        isInternetAvailable = isInternetAvailable,
        uiState = uiState,
        state = state,
        message = messageInput,
        event = { event ->
            when (event) {
                is ConversationUiEvent.CloseConversationScreen -> closeConversationMessage()
                is ConversationUiEvent.OnSendMessage -> onSendMessage()
                is ConversationUiEvent.ResetSelectedIndex -> resetSelectedIndex()
                is ConversationUiEvent.SelectChatItem -> onClickChatItem(event.index)
                is ConversationUiEvent.ToggleMessageArea -> onToggleExpand()
                is ConversationUiEvent.OnChangeValueMessage -> onChangeValueMessage(event.message)
                is ConversationUiEvent.DismissNotificationPermissionDialog -> notificationPermissionDialogVisibility(false)
                is ConversationUiEvent.ResendDialogVisibility -> resendMessageDialogVisibility(event.visible)
                is ConversationUiEvent.ResendMessage -> resendMessage()
                is ConversationUiEvent.MarkAsSeen -> markAsSeen(event.messageId)
                ConversationUiEvent.AllowProminentNotificationDialog -> allowProminentNotificationDialog()
                ConversationUiEvent.DismissProminentNotificationDialog -> dismissProminentNotificationDialog()
            }
        }
    )
}

