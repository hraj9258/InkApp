package com.hraj9258.inkapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.UiThread
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.ink.authoring.InProgressStrokeId
import androidx.ink.authoring.InProgressStrokesFinishedListener
import androidx.ink.authoring.InProgressStrokesView
import androidx.ink.rendering.android.canvas.CanvasStrokeRenderer
import androidx.ink.strokes.Stroke
import com.hraj9258.inkapp.presentation.drawing_screen.DrawingScreen
import com.hraj9258.inkapp.ui.theme.InkAppTheme

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
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        DrawingScreen(
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

