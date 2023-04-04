package com.maxwellxin.qr_scanner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.maxwellxin.qr_scanner.databinding.SampleQrScannerBinding
import java.io.IOException

class QrScanner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs) {

    private var binding: SampleQrScannerBinding = SampleQrScannerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    //Variable
    private var surfaceView: SurfaceView = binding.surfaceView
    private var overlayView: OverlayView = binding.overlayView
    private lateinit var cameraSource: CameraSource

    init {
        attrs?.let {
            applyCustomProperty(it)
        }
    }

    private fun applyCustomProperty(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs, androidx.constraintlayout.widget.R.styleable.ConstraintLayout_Layout, 0, 0)
    }

    fun setupControls(scanListener: ScanListener) {
        //Bar code detector
        var barcodeDetector: BarcodeDetector = BarcodeDetector.Builder(context)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(context, "Scanner has been closed", Toast.LENGTH_SHORT).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() > 0) {
                    for (i in 0 until barcodes.size()) {
                        val barcode = barcodes.valueAt(i)
                        val boundingBox = barcode.boundingBox

                        val rect: Rect = overlayView.getRectangle()
                        if (rect.contains(boundingBox.centerX(), boundingBox.centerY())) {
                            scanListener.result(barcodes.valueAt(0).rawValue)
                        }
                    }
                }
            }
        })

        //Camera Source
        cameraSource = CameraSource.Builder(context, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        //Camera Preview
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
    }
}