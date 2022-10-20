package com.isidroid.b21.ui.home;

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.LinearLayout;
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.updateLayoutParams
import com.isidroid.b21.R
import com.isidroid.b21.databinding.IncTrainingReportGantBlockBinding
import timber.log.Timber
import java.util.UUID

private const val DISPLAY_TYPE_STATUS = 0
private const val DISPLAY_TYPE_SIZE = 1

class HeaderGantViewHelperV2(private val container: LinearLayout) {
    private val context = container.context
    private val layoutInflater by lazy { LayoutInflater.from(context) }
    private var displayType = DISPLAY_TYPE_STATUS
    var statusMaxLength = 6
    var showEllipsis = true
    var useMonoSpaceFont = false

    private val _blocks = mutableListOf<Block>()
    val blocks: List<Block> get() = _blocks

    fun reset() = apply { _blocks.clear() }

    fun showStatus() {
        displayType = DISPLAY_TYPE_STATUS
    }

    fun showBlockSize() {
        displayType = DISPLAY_TYPE_SIZE
    }

    fun addBlock(
        title: String,
        titleColor: Int,
        color: Int,
        value: Int,
        onClick: (Block) -> Unit
    ) = apply {
        _blocks.add(
            Block(
                title = title,
                titleColor = titleColor,
                color = color,
                value = value,
                binding = IncTrainingReportGantBlockBinding.inflate(layoutInflater),
                onClick = onClick,
            )
        )
    }

    fun addBlock(
        @StringRes titleRes: Int,
        @ColorRes titleColor: Int,
        @ColorRes color: Int,
        value: Int,
        onClick: (status: Block) -> Unit
    ) = apply {
        addBlock(
            title = context.getString(titleRes),
            titleColor = context.color(titleColor),
            color = context.color(color),
            value = value,
            onClick = onClick
        )
    }

    private fun bindBlockUI(block: Block) {
        with(block.binding) {
            val statusTitle = when (displayType) {
                DISPLAY_TYPE_SIZE -> "${block.value}"
                else -> block.title.fix()
            }

            val valueTitle = when (displayType) {
                DISPLAY_TYPE_SIZE -> "123"
                else -> "${block.value}"
            }

            color = block.color
            titleColor = block.titleColor
            textView.text = valueTitle
            statusTextView.text = statusTitle

            val typeFace = if (useMonoSpaceFont) Typeface.MONOSPACE else ResourcesCompat.getFont(context, R.font.regular)
            textView.typeface = typeFace
            statusTextView.typeface = typeFace


            val horizontalPadding = statusTextView.paddingStart

            val widths = arrayOf(
                getTextWidth(statusTextView) + statusTextView.paddingStart * 2,
                getTextWidth(textView) + textView.paddingStart * 2,
            )

            block.minWidth = widths.max() + horizontalPadding * 2
            textView.text = "${block.value}"

            root.setOnClickListener { block.onClick(block) }
        }
    }

    fun show(onComplete: (() -> Unit)? = null) {
        container.alpha = 0f
        container.removeAllViews()
        _blocks
            .forEach {
                bindBlockUI(it)
                container.addView(it.binding.root)
            }

        container.post {
            val parentWidth = container.width
            var spaceLeft = parentWidth

            val weight = _blocks.sumOf { it.value }.toFloat().let { if (it == 0f) null else it }
            _blocks.forEach { block ->
                block.weight = weight?.let { (block.value / weight).round(2) } ?: 1f
            }

            val maxWeight = _blocks.maxBy { it.weight }.weight
            val maxCount = _blocks.filter { it.weight == maxWeight }.size

            _blocks.sortedBy { it.weight }.forEachIndexed { index, block ->
                var calculatedWidth = (spaceLeft.toFloat() * block.weight).toInt()
//
//                val weightLeft = _blocks.takeLast(_blocks.size - index).sumOf { it.weight.toDouble() }
//                var calculatedWidth = (parentWidth.toFloat() * block.weight).toInt()
                val isMaxWeight = block.weight == maxWeight

                Timber.i("${block.title}, weight=${block.weight}, width=$calculatedWidth, min=${block.minWidth}, spaceLeft=$spaceLeft")

                if (calculatedWidth < block.minWidth)
                    calculatedWidth = block.minWidth

                if (isMaxWeight) {
                    calculatedWidth = spaceLeft / maxCount
                } else {
                    if (calculatedWidth > spaceLeft)
                        calculatedWidth = spaceLeft
                }

                if (!isMaxWeight)
                    spaceLeft -= calculatedWidth

                with(block.binding) {
                    val text = when (displayType) {
                        DISPLAY_TYPE_SIZE -> "$calculatedWidth"
                        else -> block.title
                    }

                    val widths = arrayOf(
                        getTextWidth(statusTextView, text) + statusTextView.paddingStart * 2,
                        getTextWidth(textView, "${block.value}") + textView.paddingStart * 2,
                    )

                    val textLength = widths.max() + statusTextView.paddingStart * 2
                    val w = calculatedWidth - (statusTextView.paddingStart * 2)
                    val isTrim = textLength > w

                    statusTextView.text = text.fix(trim = isTrim)
                    block.calculatedWidth = calculatedWidth
                    block.textLength = textLength

                    if (block.value > 20)
                        Timber.e("${block.title}, weight=${block.weight}, width=$calculatedWidth, min=${block.minWidth}")


                    updateSize(calculatedWidth)
                }
            }

            onComplete?.invoke()
        }
        container.animate().alpha(1f).setDuration(300).start()
    }

    private fun IncTrainingReportGantBlockBinding.updateSize(calculatedWidth: Int) {
        arrayOf(statusTextView, textView, root).forEach { it.updateLayoutParams<LinearLayout.LayoutParams> { width = calculatedWidth } }
    }

    private fun String.fix(trim: Boolean = true) = replace(" ", "&nbsp;")
        .let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT).toString() }
        .let {
            if (!trim)
                it
            else
                it
                    .takeIf { it.length <= statusMaxLength }
                    ?: buildString {
                        var length = statusMaxLength
                        if (showEllipsis) length--
                        append(it.take(length))
                        if (showEllipsis) append("…")
                    }
        }

    private fun getTextWidth(textView: TextView, text: String = textView.text.toString()): Int {
        val bounds = Rect()
        val paint = textView.paint

        paint.typeface = textView.typeface
        paint.textSize = textView.textSize

        paint.getTextBounds(text, 0, text.length, bounds)

        return bounds.width()
    }

    inner class Block(
        val id: String = UUID.randomUUID().toString(),
        val title: String,
        val titleColor: Int,
        val color: Int,
        val value: Int,
        val binding: IncTrainingReportGantBlockBinding,
        var weight: Float = 0f,
        var minWidth: Int = 0,
        var calculatedWidth: Int = 0,
        var textLength: Int = 0,
        inline val onClick: (Block) -> Unit
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Block

            return id == other.id
        }

        override fun hashCode(): Int = id.hashCode()
        override fun toString(): String {
            return "value=$value, calculatedWidth=$calculatedWidth"
        }
    }
}

fun Context.color(@ColorRes color: Int) = ContextCompat.getColor(this, color)
fun Float.round(decimals: Int): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (kotlin.math.round(this * multiplier) / multiplier).toFloat()
}