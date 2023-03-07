package com.isidroid.b21.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.google.android.material.appbar.MaterialToolbar
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.visible
import com.isidroid.core.ui.AppBarListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindFragment(), HomeView, AppBarListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun createToolbar(toolbar: MaterialToolbar, navController: NavController) {
        super.createToolbar(toolbar, navController)
        toolbar.visible(true)
        toolbar.title = "Hello Sample World"
    }

    override fun createForm() {
        binding.button.setOnClickListener { onReady() }
    }

    override fun onReady() {
        viewModel.makePreview(
            arrayOf(
                "https://www.reddit.com/r/aww/comments/11khymd/my_bonded_cats_having_an_afternoon_nap_together_oc/",
                "https://en.wikipedia.org/wiki/Mike_Tyson?useskin=vector",
                "https://source.android.com/docs/core/display/material",
                "https://www.nytimes.com/2023/03/06/sports/tennis/djokovic-biden-miami-open-covid-vaccine.html",
                "https://twitter.com/rojamaibo/status/1629512888040710150",
                "https://www.instagram.com/p/CpektdBN_aR/",
                "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/User-Agent",
                "https://www.profinance.ru/news/2023/03/07/c8cq-sberbank-dal-prognoz-kursa-rublya-na-vtornik.html",
                "https://www.cybersport.ru/tags/games/paradox-anonsirovala-igru-v-stile-the-sims",
                "https://edition.cnn.com/2023/03/06/entertainment/bruce-willis-wife-dementia-paparazzi-intl-scli-wellness/index.html",
                "https://eu.usatoday.com/story/money/small-business/2023/03/06/bill-finke-sons-fort-wright-goetta-cincinnati-ohio-northern-kentucky/11385913002/",
                "https://www.foxnews.com/world/first-four-americans-kidnapped-mexico-been-identified",
                "https://www.washingtonpost.com/arts-entertainment/2023/03/06/chris-rock-netflix-special-slap/",
                "https://www.usnews.com/news/economy/articles/2023-03-06/feds-powell-jobs-dominate-economic-calendar",
                "https://www.facebook.com/1996communitty/photos/a.159293865739624/751683083167363/",
                "https://www.facebook.com/hashtag/givememylaback",
                "https://www.tiktok.com/@maniraj_en3d/video/7206408135718866182"
            )
        )
    }
}