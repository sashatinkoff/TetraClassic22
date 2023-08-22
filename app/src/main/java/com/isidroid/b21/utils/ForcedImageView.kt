package com.isidroid.b21.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.isidroid.b21.GlideApp
import jp.wasabeef.glide.transformations.BlurTransformation
import java.lang.Integer.min

class ForcedImageView : FrameLayout {
    private var backgroundImageView: ImageView? = null
    private var imageView: ImageView? = null
    private var pendingUrl: String? = null
    private val loadListener = LoadListener()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val width = measureDimension(desiredWidth, widthMeasureSpec)
        val height = measureDimension(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        synchronized(this) {
            if (backgroundImageView == null)
                backgroundImageView = createImageView()
                    .also { it.scaleType = ImageView.ScaleType.CENTER_CROP }

            if (imageView == null)
                imageView = createImageView()


            val loadUrl = pendingUrl
            if (loadUrl != null) {
                pendingUrl = null
                load(loadUrl)
            }
        }
    }

    // custom implementation
    private fun createImageView(): ImageView {
        val childView = ImageView(context)
        addView(childView, width, height)
        return childView
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST)
                result = min(result, specSize)
        }
        return result
    }

    fun load(url: String) {
        val backgroundImage = backgroundImageView
        val image = imageView

        if (image == null || backgroundImage == null) {
            pendingUrl = url
            return
        }

        backgroundImage.visibility = View.INVISIBLE
        image.visibility = View.INVISIBLE

        loadListener.reset(image, backgroundImage)

        GlideApp.with(context)
            .load(url)
            .transform(BlurTransformation(20, 3))
            .listener(loadListener)
            .into(backgroundImage)


        GlideApp.with(context)
            .load(url)
            .listener(loadListener)
            .into(image)
    }

    private class LoadListener : RequestListener<Drawable> {
        private var count = 0
        private var image: ImageView? = null
        private var backgroundImage: ImageView? = null

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            onDataReceived()
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            onDataReceived()
            return false
        }

        private fun onDataReceived() {
            count++

            if (count >= 2) {
                image?.visibility = View.VISIBLE
                backgroundImage?.visibility = View.VISIBLE
            }
        }

        fun reset(image: ImageView, backgroundImage: ImageView) {
            count = 0
            this.image = image
            this.backgroundImage = backgroundImage
        }

    }
}