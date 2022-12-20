package com.isidroid.b21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.isidroid.b21.databinding.FragmentDetailsBinding
import com.isidroid.b21.utils.base.BindFragment

class DetailsFragment : BindFragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }
}