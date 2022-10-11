package com.isidroid.b21.ui.home;

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout;
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.updateLayoutParams
import com.isidroid.b21.databinding.IncTrainingReportGantBlockBinding
import timber.log.Timber
import java.util.UUID

private const val DISPLAY_TYPE_STATUS = 0
private const val DISPLAY_TYPE_SIZE = 1

class HeaderGantViewHelperV2(private val container: LinearLayout) {
    private val context = container.context
    private val blocks = mutableListOf<Block>()
    private val layoutInflater by lazy { LayoutInflater.from(context) }
    private var displayType = DISPLAY_TYPE_STATUS
    var statusMaxLength = 6
    var showEllipsis = true

    fun reset() = apply { blocks.clear() }

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
        onClick: (status: CharSequence?, value: CharSequence?) -> Unit
    ) = apply {
        blocks.add(
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
        onClick: (status: CharSequence?, value: CharSequence?) -> Unit
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
                else -> block.title.take(statusMaxLength).fix()
            }

            val valueTitle = when (displayType) {
                DISPLAY_TYPE_SIZE -> "123"
                else -> "${block.value}"
            }

            color = block.color
            titleColor = block.titleColor
            textView.text = valueTitle
            statusTextView.text = statusTitle

            val horizontalPadding = statusTextView.paddingStart

            val widths = arrayOf(
                statusTextView.paint.measureText(statusTextView.text.toString()).toInt(),
                textView.paint.measureText(textView.text.toString()).toInt()
            )

            block.minWidth = widths.max() + horizontalPadding * 2
            textView.text = "${block.value}"

            root.setOnClickListener { block.onClick(textView.text, statusTextView.text) }
        }
    }

    fun show() {
        container.alpha = 0f
        container.removeAllViews()
        blocks
            .forEach {
                bindBlockUI(it)
                container.addView(it.binding.root)
            }

        container.post {
            val parentWidth = container.width
            var spaceLeft = parentWidth

            val weight = blocks.sumOf { it.value }.toFloat().let { if (it == 0f) null else it }
            blocks.forEach { block ->
                block.weight = weight?.let { (block.value / weight).round(2) } ?: 1f
            }

            val maxWeight = blocks.maxBy { it.weight }.weight
            val maxCount = blocks.filter { it.weight == maxWeight }.size

            blocks.sortedBy { it.weight }.forEach { block ->
                var calculatedWidth = (parentWidth.toFloat() * block.weight).toInt()
                val isMaxWeight = block.weight == maxWeight

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

                    statusTextView.text = text.fix()
                    updateSize(calculatedWidth)
                }
            }
        }
        container.animate().alpha(1f).setDuration(300).start()
    }

    private fun IncTrainingReportGantBlockBinding.updateSize(calculatedWidth: Int) {
        arrayOf(statusTextView, textView, root).forEach { it.updateLayoutParams<LinearLayout.LayoutParams> { width = calculatedWidth } }
    }

    private fun String.fix() = replace(" ", "&nbsp;")
        .let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT).toString() }
        .let {
            it
                .takeIf { it.length <= statusMaxLength }
                ?: buildString {
                    var length = statusMaxLength
                    if (showEllipsis) length--

                    append(it.take(length))
                    if (showEllipsis) append("…")
                }
        }

    private class Block(
        val id: String = UUID.randomUUID().toString(),
        val title: String,
        val titleColor: Int,
        val color: Int,
        val value: Int,
        val binding: IncTrainingReportGantBlockBinding,
        var weight: Float = 0f,
        var minWidth: Int = 0,
        var calculatedWidth: Int = 0,
        inline val onClick: (status: CharSequence?, value: CharSequence?) -> Unit
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