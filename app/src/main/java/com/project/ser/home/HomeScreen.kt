package com.project.ser.home

import FabButton
import FabButtonState
import FabType
import MultiFloatingActionButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.project.design.component.AppTopBar
import com.project.ser.R

@Composable
fun HomeScreen(
    onFabClicked: (FabType) -> Unit,
    emotions: List<String>,
    probDistributions: List<List<Int>>
) {
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
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(probDistributions, key = { index, _ -> emotions[index] }) { index, probDistribution ->
                    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(text = emotions[index])
                        CartesianChart(probDistribution = probDistribution)
                    }
                }
            }
        }
    }
}

@Composable
fun CartesianChart(probDistribution: List<Int>) {
    if (probDistribution.isNotEmpty()) {
        val modelProducer = remember { CartesianChartModelProducer.build() }
        LaunchedEffect(probDistribution) {
            modelProducer.tryRunTransaction {
                columnSeries {
                    series(
                        y = probDistribution
                    )
                }
            }
        }
        CartesianChartHost(
            rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
            ),
            modelProducer,
        )
    } else {
        Text(text = "No data available")
    }
}