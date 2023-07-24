package com.example.cyclistance.feature_messaging.presentation.chat.chats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.top_bars.TitleTopAppBar


@Composable
internal fun MessagingTopAppBarTitle(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    searchQuery: TextFieldValue,
    isSearching: Boolean,
    onChangeValueQuery: (TextFieldValue) -> Unit = {},
    onClearSearchQuery: () -> Unit = {},
    onClickSearch: () -> Unit = {},
) {


    Row(
        modifier = modifier.fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        if (isSearching) {


            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            MessagingSearchBar(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .weight(0.65f),
                value = searchQuery,
                onValueChange = onChangeValueQuery
            )

            IconButton(onClick = onClearSearchQuery) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Clear Search",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.weight(0.35f)
                )
            }

        } else {

            TitleTopAppBar(title = "Chats")
            IconButton(onClick = onClickSearch) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colors.onBackground,
                )
            }
        }

    }
}