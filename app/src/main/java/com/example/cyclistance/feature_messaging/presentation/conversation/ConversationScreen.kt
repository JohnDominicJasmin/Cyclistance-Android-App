package com.example.cyclistance.feature_messaging.presentation.conversation

import android.Manifest
import android.os.Build
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.permissions.requestPermission
import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.presentation.conversation.components.ConversationContent
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationUiEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationVmEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationUiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues,
    userReceiverMessage: MessagingUserItemModel,
    userSenderMessage: MessagingUserItemModel,
    newConversationDetails: (MessagingUserItemModel) -> Unit
) {


    val conversationState = viewModel.conversationState
    val state by viewModel.state.collectAsStateWithLifecycle()
    var uiState by rememberSaveable { mutableStateOf(ConversationUiState()) }
    var message by rememberSaveable(stateSaver = TextFieldValue.Saver) {
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
            message = it
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


    val sendMessage = remember {
        {

            val receiverId = state.userReceiverMessage!!.userDetails.uid
            viewModel.onEvent(
                event = ConversationVmEvent.SendMessage(
                    sendMessageModel = SendMessageModel(
                        receiverId = receiverId,
                        message = message.text
                    )
                )).also {
                message = TextFieldValue()
            }

        }
    }





    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted){
                sendMessage()
            }
        }
    )


    val notificationPermissionState = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    ){ permissionGranted ->
        if(permissionGranted){
            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

    }


    val notificationPermissionDialogVisibility = remember{{ visible: Boolean ->
        uiState = uiState.copy(notificationPermissionVisible = visible)
    }}




    val onSendMessage = remember{{

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionState.requestPermission(onGranted = {
                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }, onExplain = {
                showPermissionDialog()
            }, onDenied = {
                sendMessage()
            })
        } else {
            sendMessage()
        }
    }}





    LaunchedEffect(key1 = userReceiverMessage){

        viewModel.onEvent(
            event = ConversationVmEvent.OnInitialized(
                userReceiverMessage = userReceiverMessage,
                userSenderMessage = userSenderMessage))

        newConversationDetails(userReceiverMessage)
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
        uiState = uiState,
        state = state,
        message = message,
        event = { event ->
            when (event) {
                is ConversationUiEvent.CloseConversationScreen -> closeConversationMessage()
                is ConversationUiEvent.OnSendMessage -> onSendMessage()
                is ConversationUiEvent.ResetSelectedIndex -> resetSelectedIndex()
                is ConversationUiEvent.SelectChatItem -> onClickChatItem(event.index)
                is ConversationUiEvent.ToggleMessageArea -> onToggleExpand()
                is ConversationUiEvent.OnChangeValueMessage -> onChangeValueMessage(event.message)
                is ConversationUiEvent.DismissNotificationPermissionDialog -> dismissPermissionDialog()
            }
        }
    )
}

