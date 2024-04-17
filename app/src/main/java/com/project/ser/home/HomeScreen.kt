package com.project.ser.home

import FabButton
import FabButtonState
import FabType
import MultiFloatingActionButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.project.design.component.AppTopBar
import com.project.design.component.EmptyView
import com.project.ser.R
import com.project.ser.model.EmotionResponse

@Composable
fun HomeScreen(onFabClicked: (FabType) -> Unit, emotionsResponseList: List<EmotionResponse>) {
    var fabButtonState by remember { mutableStateOf<FabButtonState>(FabButtonState.Collapsed) }

    Scaffold(
        topBar = { AppTopBar(topBarTitle = R.string.dashboard) },
        floatingActionButton = {
            MultiFloatingActionButton(
                fabButtons = listOf(
                    FabButton(
                        fabType = FabType.UPLOAD,
                        iconRes = R.drawable.baseline_audio_file_24
                    ),
                    FabButton(
                        fabType = FabType.RECORD,
                        iconRes = R.drawable.baseline_record_voice_over_24
                    )
                ),
                fabButtonState = fabButtonState,
                onFabButtonStateChange = {
                    fabButtonState = it
                },
                onFabClick = onFabClicked
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(contentPadding)
        ) {
            if (emotionsResponseList.isEmpty()) {
                EmptyView(
                    icon = R.drawable.face,
                    text = R.string.it_s_a_bit_emotionless_in_here_tap_to_add_some_drama
                )
            } else {

            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen({}, emptyList())
}