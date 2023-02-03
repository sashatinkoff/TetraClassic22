package com.isidroid.b21.domain.use_case

import com.dwolla.Dwolla
import com.dwolla.DwollaEnvironment
import com.isidroid.core.ext.json
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeUseCase @Inject constructor() {
    fun start() = flow {
        val dwolla = Dwolla(
            key = "7HRH3mT31lX1KFfgYGdyGERdJJU9TByUVQLEsAqDYycz3CCOwt",
            secret = "2KGNDk0sLnCSq5qXavlU4pFH0cGLHJeGuXSjYZBOkaXGHQrm0q",
            environment = DwollaEnvironment.SANDBOX // defaults to PRODUCTION
        )

        val account = dwolla.accounts.get(
            id = "",
        )

        Timber.i("${account.json}")

        emit(true)
    }
}