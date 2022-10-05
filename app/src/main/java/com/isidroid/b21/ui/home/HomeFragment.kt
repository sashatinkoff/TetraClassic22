package com.isidroid.b21.ui.home

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.isidroid.b21.R
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.hideSoftKeyboard
import com.isidroid.core.ext.randomColor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.io.IOException
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val args: HomeFragmentArgs by navArgs()
    private val viewModel by viewModels<HomeViewModel>()
    private val documentsContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.pickImage(requireActivity(), it ?: return@registerForActivityResult)
    }

    private val headerGantView by lazy { HeaderGantViewHelperV2(binding.lineearLayout) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun createForm() {
        with(binding) {
            buttonForm.setOnClickListener {
                requireActivity().hideSoftKeyboard()
                startWorker(
                    inputAssigned.text.toString().toIntOrNull() ?: 0,
                    inputOnControl.text.toString().toIntOrNull() ?: 0,
                    inputOnRework.text.toString().toIntOrNull() ?: 0,
                    inputAccepted.text.toString().toIntOrNull() ?: 0,
                    inputFailed.text.toString().toIntOrNull() ?: 0,
                )
            }

            buttonRandomDataSet.setOnClickListener {
                randomDataSet()
            }
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

    override fun onReady() {
        startWorker(8, 8, 1, 0, 0)
        binding.button.setOnClickListener {
            startWorker(
                Random.nextInt(0, 10),
                Random.nextInt(0, 10),
                Random.nextInt(0, 10),
                Random.nextInt(0, 10),
                Random.nextInt(0, 10),
            )
        }
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

    fun showTitle(string: String) {
        Toast.makeText(requireActivity(), "$string", Toast.LENGTH_SHORT).show()
    }
}