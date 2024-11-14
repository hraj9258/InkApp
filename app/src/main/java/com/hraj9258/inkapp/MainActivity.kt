package com.hraj9258.inkapp

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.UiThread
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.ink.authoring.InProgressStrokeId
import androidx.ink.authoring.InProgressStrokesFinishedListener
import com.hraj9258.inkapp.ui.theme.InkAppTheme
import androidx.ink.authoring.InProgressStrokesView
import androidx.ink.brush.Brush
import androidx.ink.brush.StockBrushes
import androidx.ink.rendering.android.canvas.CanvasStrokeRenderer
import androidx.ink.strokes.Stroke
import androidx.input.motionprediction.MotionEventPredictor

class MainActivity : ComponentActivity(), InProgressStrokesFinishedListener {

    private lateinit var inProgressStrokesView: InProgressStrokesView
    private val finishedStrokesState = mutableStateOf(emptySet<Stroke>())
    private lateinit var canvasStrokeRenderer: CanvasStrokeRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inProgressStrokesView = InProgressStrokesView(this)
        inProgressStrokesView.addFinishedStrokesListener(this)
        canvasStrokeRenderer = CanvasStrokeRenderer.create()

        enableEdgeToEdge()
        setContent {
            InkAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier
                        .padding(innerPadding)
                        .border(12.dp, MaterialTheme.colorScheme.onSecondaryContainer)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                    ){
                        DrawingSurface(
                            inProgressStrokesView = inProgressStrokesView,
                            finishedStrokesState = finishedStrokesState
                        )
                    }

                }
            }
        }
    }

    @UiThread
    override fun onStrokesFinished(strokes: Map<InProgressStrokeId, Stroke>) {
        finishedStrokesState.value += strokes.values
        inProgressStrokesView.removeFinishedStrokes(strokes.keys)
    }
}

@SuppressLint("ClickableViewAccessibility")
@Composable
fun DrawingSurface(
    inProgressStrokesView: InProgressStrokesView,
    finishedStrokesState: MutableState<Set<Stroke>>
) {
    val canvasStrokeRenderer = CanvasStrokeRenderer.create()

    val currentPointerId = remember { mutableStateOf<Int?>(null) }
    val currentStrokeId = remember { mutableStateOf<InProgressStrokeId?>(null) }

    // Pressure sensitive brush
    val defaultBrush = Brush.createWithColorIntArgb(
        family = StockBrushes.pressurePenLatest,
        colorIntArgb = Color.Magenta.toArgb(),
        size = 5F,
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

    Box(modifier = Modifier.fillMaxSize()) {
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
                                            brush = highlighterBrush
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
                canvasStrokeRenderer.draw(stroke = stroke, canvas = canvas, strokeToScreenTransform = canvasTransform)
            }
        }
    }
}