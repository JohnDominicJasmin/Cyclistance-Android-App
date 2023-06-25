import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.example.cyclistance.feature_message.presentation.messaging_conversation.components.MessagingConversationContent
import com.example.cyclistance.feature_message.presentation.messaging_conversation.event.MessagingConversationUiEvent
import com.example.cyclistance.feature_message.presentation.messaging_conversation.state.MessagingConversationUiState

@Composable
fun MessagingConversationScreen(navController: NavController, paddingValues: PaddingValues) {
    var uiState by rememberSaveable { mutableStateOf(MessagingConversationUiState()) }

    val onToggleExpand = remember {
        {
            uiState = uiState.copy(
                messageAreaExpanded = !uiState.messageAreaExpanded
            )
        }
    }
    val onChangeValueMessage = remember<(TextFieldValue) -> Unit> {
        {
            uiState = uiState.copy(
                message = it
            )
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


    BackHandler(enabled = true, onBack = {
        if (uiState.messageAreaExpanded) {
            onToggleExpand()
        } else {
            navController.popBackStack()
        }
    })

    val onCloseMessageConversationScreen = remember {
        {
            navController.popBackStack()
        }
    }



    MessagingConversationContent(
        modifier = Modifier.padding(paddingValues),
        uiState = uiState,
        event = { event ->
            when (event) {
                is MessagingConversationUiEvent.ToggleMessageArea -> onToggleExpand()
                is MessagingConversationUiEvent.ResetSelectedIndex -> resetSelectedIndex()
                is MessagingConversationUiEvent.SelectChatItem -> onClickChatItem(event.index)
                is MessagingConversationUiEvent.OnChangeMessage -> onChangeValueMessage(event.message)
                is MessagingConversationUiEvent.CloseMessagingConversationScreen -> onCloseMessageConversationScreen()
            }
        }
    )
}