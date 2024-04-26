package com.project.ser.home

import FabButton
import FabButtonState
import FabType
import MultiFloatingActionButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberEndAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberTopAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.project.design.component.AppTopBar
import com.project.ser.R

@Composable
fun HomeScreen(
    onFabClicked: (FabType) -> Unit,
    emotions: List<String>,
    probDistributions: List<List<Int>>
) {
    var fabButtonState by remember { mutableStateOf<FabButtonState>(FabButtonState.Collapsed) }
    val listOfEmotions = listOf("Angry","Disgust","Fear","Happy","Neutral","Sad","Surprise")
    val bottomAxisValueFormatter =
        CartesianValueFormatter { x, _, _ -> "${listOfEmotions[x.toInt() % 12]} ’${20 + x.toInt() / 12}" }
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
                itemsIndexed(
                    probDistributions,
                    key = { index, _ -> emotions[index] }) { index, probDistribution ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (probDistribution.isNotEmpty()) "Most Frequent Emotion: ${emotions[index]}" else "",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        CartesianChart(probDistribution = probDistribution,bottomAxisValueFormatter)
                    }
                }
            }
        }
    }
}

@Composable
fun CartesianChart(probDistribution: List<Int>,bottomAxisValueFormatter: CartesianValueFormatter) {
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
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider =
                    ColumnCartesianLayer.ColumnProvider.series(
                        columnColors.map { color ->
                            rememberLineComponent(
                                color = color,
                                thickness = 8.dp
                            )
                        },
                    ),
                ),
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = bottomAxisValueFormatter,
                    itemPlacer =
                    remember { AxisItemPlacer.Horizontal.default(spacing = 3, addExtremeLabelPadding = true) },
                ),
            ),
            modelProducer = modelProducer
        )
        Text(
            text = "Angry -  ${probDistribution[0]}%",
            color = Color.Black,
            fontSize = 16.sp
        )
        Text(
            text = "Disgust - ${probDistribution[1]}%",
            color = Color.Black,
            fontSize = 16.sp
        )
        Text(
            text = "Fear - ${probDistribution[2]}%",
            color = Color.Black,
            fontSize = 16.sp
        )
        Text(
            text = "Happy - ${probDistribution[3]}%",
            color = Color.Black,
            fontSize = 16.sp
        )
        Text(
            text = "Neutral - ${probDistribution[4]}%",
            color = Color.Black,
            fontSize = 16.sp
        )
        Text(
            text = "Sad - ${probDistribution[5]}%",
            color = Color.Black,
            fontSize = 16.sp
        )
        Text(
            text = "Surprise - ${probDistribution[6]}%",
            color = Color.Black,
            fontSize = 16.sp
        )
    } else {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "No data available", color = Color.Black, fontSize = 16.sp)
        }
    }
}

private val columnColors = listOf(Color(0xff916cda), Color(0xffd877d8), Color(0xfff094bb))
