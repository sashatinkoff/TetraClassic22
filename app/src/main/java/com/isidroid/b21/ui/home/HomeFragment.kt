package com.isidroid.b21.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.isidroid.b21.R
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.b21.databinding.ChipBinding
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.*
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val gson = Gson()

    private val headerGantView by lazy { HeaderGantViewHelperV2(binding.lineearLayout) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun createForm() {
        with(binding) {
            modifyInputValue(inputAssigned)
            modifyInputValue(inputOnControl)
            modifyInputValue(inputOnRework)
            modifyInputValue(inputAccepted)
            modifyInputValue(inputFailed)

            createChips()

            radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                val selectedRadio = radioGroup.children.filterIsInstance<MaterialRadioButton>().firstOrNull { it.isChecked }

                if (selectedRadio?.id == radioStatus.id)
                    headerGantView.showStatus()
                else
                    headerGantView.showBlockSize()

                submit(true)
            }

            checkboxEllipsis.setOnCheckedChangeListener { _, b ->
                headerGantView.showEllipsis = b
                submit(true)
            }

            characterCountInputLayout.setStartIconOnClickListener {
                val value = ((characterCountInput.text.toString().toIntOrNull() ?: 6) - 1).let { if (it <= 1) 1 else it }
                headerGantView.statusMaxLength = value
                characterCountInput.setText("$value")
                submit(true)
            }

            characterCountInputLayout.setEndIconOnClickListener {
                val value = ((characterCountInput.text.toString().toIntOrNull() ?: 6) + 1)
                headerGantView.statusMaxLength = value

                characterCountInput.setText("$value")
                submit(true)
            }
        }
    }

    override fun createAppBar() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_random -> randomData()
                R.id.action_submit -> submit(true)
                R.id.action_random_data_set -> randomDataSet()
            }
            true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun modifyInputValue(input: TextInputEditText) {
        val inputLayout: TextInputLayout = input.parent.parent as TextInputLayout

        inputLayout.setStartIconOnClickListener {
            val value = (input.text.toString().toIntOrNull() ?: 0).let { if (it < 1) 1 else it }

            input.setText("${value - 1}")
            submit(false)
        }
        inputLayout.setEndIconOnClickListener {
            val value = input.text.toString().toIntOrNull() ?: 0
            input.setText("${value + 1}")
            submit(false)
        }

        input.doOnEnter {
            submit(true)
        }
    }

    private fun randomDataSet() {
        headerGantView.reset()

        val blockCount = Random.nextInt(3, 15)
        repeat(blockCount) {
            val color = randomColor()

            headerGantView.addBlock(
                title = "Block ${it + 1}",
                titleColor = color,
                color = color,
                value = Random.nextInt(0, 100),
                onClick = { status, value -> showTitle("$status\n$value") }
            )
        }
        headerGantView.show()
    }

    private fun randomData() {
        startWorker(
            Random.nextInt(0, 10),
            Random.nextInt(0, 10),
            Random.nextInt(0, 10),
            Random.nextInt(0, 10),
            Random.nextInt(0, 10),
        )
    }

    override fun onReady() {
        startWorker(6, 5, 1, 52, 3)
//        startWorker(150, 3, 2, 3, 3)
    }

    private fun startWorker(assigned: Int, onControl: Int, onRework: Int, accepted: Int, failed: Int) {
        with(binding) {
            inputAssigned.setText("$assigned")
            inputOnControl.setText("$onControl")
            inputOnRework.setText("$onRework")
            inputAccepted.setText("$accepted")
            inputFailed.setText("$failed")
        }


        headerGantView.reset()
            .addBlock(
                titleRes = R.string.label_audit_new,
                titleColor = R.color.audit_text_status_not_started,
                color = R.color.audit_status_not_started,
                value = assigned,
                onClick = { status, value -> showTitle("$status\n$value") }
            )
            .addBlock(
                titleRes = R.string.label_audit_on_control,
                titleColor = R.color.audit_text_status_started,
                color = R.color.audit_status_started,
                value = onControl,
                onClick = { status, value -> showTitle("$status\n$value") }
            )
            .addBlock(
                titleRes = R.string.label_audit_on_rework,
                titleColor = R.color.audit_text_status_viewed,
                color = R.color.audit_status_viewed,
                value = onRework,
                onClick = { status, value -> showTitle("$status\n$value") }
            )
            .addBlock(
                titleRes = R.string.label_audit_completed2,
                titleColor = R.color.audit_text_status_passed,
                color = R.color.audit_status_passed,
                value = accepted,
                onClick = { status, value -> showTitle("$status\n$value") }
            )
            .addBlock(
                titleRes = R.string.status_failed,
                titleColor = R.color.audit_text_status_failed,
                color = R.color.audit_status_failed,
                value = failed,
                onClick = { status, value -> showTitle("$status\n$value") }
            )
            .show()
    }

    fun createChips() {
        val json = Settings.refreshToken.orEmpty()
        val data = tryCatch { gson.fromJson<List<Preset>>(json) }.orEmpty()
        binding.chipGroup.removeAllViews()
        data.forEach { createChip(it) }
    }

    @SuppressLint("SetTextI18n")
    private fun createChip(preset: Preset) {
        if (binding.chipGroup.children.any { it.tag == preset }) return

        ChipBinding.inflate(layoutInflater, binding.chipGroup, true).chipView.apply {
            tag = preset
            text = "${preset.assigned}, ${preset.onControl}, ${preset.onRework}, ${preset.accepted}, ${preset.failed}"

            setOnClickListener {
                startWorker(preset.assigned, preset.onControl, preset.onRework, preset.accepted, preset.failed)
            }
        }
    }

    fun showTitle(string: String) {
        Toast.makeText(requireActivity(), "$string", Toast.LENGTH_SHORT).show()
    }

    private fun submit(savePreset: Boolean) {
        with(binding) {
            requireActivity().hideSoftKeyboard()

            val assigned = inputAssigned.text.toString().toIntOrNull() ?: 0
            val onControl = inputOnControl.text.toString().toIntOrNull() ?: 0
            val onRework = inputOnRework.text.toString().toIntOrNull() ?: 0
            val accepted = inputAccepted.text.toString().toIntOrNull() ?: 0
            val failed = inputFailed.text.toString().toIntOrNull() ?: 0

            if (savePreset) {
                val preset = Preset(assigned, onControl, onRework, accepted, failed)
                val json = Settings.refreshToken.orEmpty()
                val data = tryCatch { gson.fromJson<List<Preset>>(json) }.orEmpty().toMutableList()

                if (!data.contains(preset))
                    data.add(preset)

                val result = data.takeLast(6)
                Settings.refreshToken = gson.toJson(result)
                createChips()
            }

            startWorker(assigned, onControl, onRework, accepted, failed)
        }
    }
}