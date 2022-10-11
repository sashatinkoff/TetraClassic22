package com.isidroid.b21.ui.home;

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout;
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.isidroid.b21.databinding.IncTrainingReportGantBlockBinding
import timber.log.Timber
import java.util.UUID

private const val MAX_STATUS_LENGTH = 6

class HeaderGantViewHelperV2(private val container: LinearLayout) {
    private val context = container.context
    private val blocks = mutableListOf<Block>()
    private val layoutInflater by lazy { LayoutInflater.from(context) }

    fun reset() = apply { blocks.clear() }

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
//            val statusTitle = block.title.take(MAX_STATUS_LENGTH)
            val statusTitle = "${block.value}"

            color = block.color
            titleColor = block.titleColor
            textView.text = "${block.value}"
            statusTextView.text = statusTitle

            val horizontalPadding = statusTextView.paddingStart

            val widths = arrayOf(
                statusTextView.paint.measureText(statusTextView.text.toString()).toInt(),
                textView.paint.measureText(textView.text.toString()).toInt()
            )

            block.minWidth = widths.max() + horizontalPadding * 2
            root.setOnClickListener { block.onClick(textView.text, statusTextView.text) }
        }
    }

    fun show() {
        container.alpha = 0f
        container.removeAllViews()
        blocks
            .sortedBy { it.value }
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
                    statusTextView.text = block.title
                    updateSize(calculatedWidth)
                }
            }
        }
        container.animate().alpha(1f).setDuration(300).start()
    }

    private fun IncTrainingReportGantBlockBinding.updateSize(calculatedWidth: Int) {
        arrayOf(statusTextView, textView, root).forEach { it.updateLayoutParams<LinearLayout.LayoutParams> { width = calculatedWidth } }
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

    interface Listener {
        fun clickOnHeaderGant(status: CharSequence?, value: CharSequence?)
    }
}

fun Context.color(@ColorRes color: Int) = ContextCompat.getColor(this, color)
fun Float.round(decimals: Int): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (kotlin.math.round(this * multiplier) / multiplier).toFloat()
}