package baqari.dev.com.progresview

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar

class MainActivity : Activity() {

    lateinit var progressView : ProgressView
    var seekBar : SeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressView = findViewById(R.id.progress_view) as ProgressView
        seekBar = findViewById(R.id.seek_bar) as SeekBar
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                progressView.percent = p1.toFloat()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

    }
}
