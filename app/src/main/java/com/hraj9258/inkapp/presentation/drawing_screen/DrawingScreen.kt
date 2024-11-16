package com.hraj9258.inkapp.presentation.drawing_screen

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.ink.authoring.InProgressStrokeId
import androidx.ink.authoring.InProgressStrokesView
import androidx.ink.brush.Brush
import androidx.ink.brush.StockBrushes
import androidx.ink.rendering.android.canvas.CanvasStrokeRenderer
import androidx.ink.strokes.Stroke
import androidx.input.motionprediction.MotionEventPredictor
import com.hraj9258.inkapp.presentation.drawing_screen.components.TopToolBar
import com.hraj9258.inkapp.ui.theme.InkAppTheme
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class)
@SuppressLint("ClickableViewAccessibility")
@Composable
fun DrawingScreen(
    inProgressStrokesView: InProgressStrokesView,
    finishedStrokesState: MutableState<Set<Stroke>>,
    modifier: Modifier = Modifier
) {
    val canvasStrokeRenderer = CanvasStrokeRenderer.create()

    val currentPointerId = remember { mutableStateOf<Int?>(null) }
    val currentStrokeId = remember { mutableStateOf<InProgressStrokeId?>(null) }

    // Pressure sensitive brush
    val defaultBrush = Brush.createWithColorIntArgb(
        family = StockBrushes.pressurePenLatest,
        colorIntArgb = Color.Magenta.toArgb(),
        size = 8F,
        epsilon = 0.1F
    )

    // Highlighter brush, no pressure sensitivity
    val highlighterBrush = Brush.createWithColorIntArgb(
        family = StockBrushes.highlighterLatest,
        colorIntArgb = Color(0x9000BCD4).toArgb(),
        size = 50F,
        epsilon = 0.1F
    )

    // Circular brush, no pressure sensitivity
    val markerBrush = Brush.createWithColorIntArgb(
        family = StockBrushes.markerLatest,
        colorIntArgb = Color.Magenta.toArgb(),
        size = 5F,
        epsilon = 0.1F
    )

    val selectedBrush = remember { mutableStateOf(defaultBrush) }
    val brushes = listOf(defaultBrush, highlighterBrush, markerBrush)

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val rootView = FrameLayout(context)
                inProgressStrokesView.apply {
                    layoutParams =
                        FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT,
                        )
                }
                val predictor = MotionEventPredictor.newInstance(rootView)
                val touchListener =
                    View.OnTouchListener { view, event ->
                        predictor.record(event)
                        val predictedEvent = predictor.predict()

                        try {
                            when (event.actionMasked) {
                                MotionEvent.ACTION_DOWN -> {
                                    Log.d("DrawingSurface", "ACTION_DOWN")
                                    // First pointer - treat it as inking.
                                    view.requestUnbufferedDispatch(event)
                                    val pointerIndex = event.actionIndex
                                    val pointerId = event.getPointerId(pointerIndex)
                                    currentPointerId.value = pointerId
                                    currentStrokeId.value =
                                        inProgressStrokesView.startStroke(
                                            event = event,
                                            pointerId = pointerId,
                                            brush = selectedBrush.value
                                        )
                                    true
                                }

                                MotionEvent.ACTION_MOVE -> {
                                    Log.d("DrawingSurface", "ACTION_MOVE")
                                    val pointerId = checkNotNull(currentPointerId.value)
                                    val strokeId = checkNotNull(currentStrokeId.value)

                                    for (pointerIndex in 0 until event.pointerCount) {
                                        if (event.getPointerId(pointerIndex) != pointerId) continue
                                        inProgressStrokesView.addToStroke(
                                            event,
                                            pointerId,
                                            strokeId,
                                            predictedEvent
                                        )
                                    }
                                    true
                                }

                                MotionEvent.ACTION_UP -> {
                                    Log.d("DrawingSurface", "ACTION_UP")
                                    val pointerIndex = event.actionIndex
                                    val pointerId = event.getPointerId(pointerIndex)
                                    check(pointerId == currentPointerId.value)
                                    val currentStrokeId = checkNotNull(currentStrokeId.value)
                                    inProgressStrokesView.finishStroke(
                                        event,
                                        pointerId,
                                        currentStrokeId
                                    )
                                    view.performClick()
                                    true
                                }

                                MotionEvent.ACTION_CANCEL -> {
                                    Log.d("DrawingSurface", "ACTION_CANCEL")
                                    val pointerIndex = event.actionIndex
                                    val pointerId = event.getPointerId(pointerIndex)
                                    check(pointerId == currentPointerId.value)

                                    val currentStrokeId = checkNotNull(currentStrokeId.value)
                                    inProgressStrokesView.cancelStroke(currentStrokeId, event)
                                    true
                                }

                                else -> false
                            }
                        } finally {
                            predictedEvent?.recycle()
                        }

                    }
                rootView.setOnTouchListener(touchListener)
                rootView.addView(inProgressStrokesView)
                rootView
            },
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            var canvasTransform = Matrix()
            drawContext.canvas.nativeCanvas.concat(canvasTransform)
            val canvas = drawContext.canvas.nativeCanvas

            finishedStrokesState.value.forEach { stroke ->
                canvasStrokeRenderer.draw(
                    stroke = stroke,
                    canvas = canvas,
                    strokeToScreenTransform = canvasTransform
                )
            }
        }
    }
    TopToolBar(
        brushList = brushes,
        selectedBrush = selectedBrush,
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
    )
}

@Preview
@Composable
private fun DrawingScreenPreview() {
    InkAppTheme {
        DrawingScreen(
            inProgressStrokesView = InProgressStrokesView(LocalContext.current),
            finishedStrokesState = finishedStrokesStatePreview,
            modifier = Modifier
        )
    }
}

internal val finishedStrokesStatePreview = mutableStateOf(emptySet<Stroke>())