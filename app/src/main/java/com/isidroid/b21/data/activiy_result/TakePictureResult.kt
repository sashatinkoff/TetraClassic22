package com.isidroid.b21.data.activiy_result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import com.isidroid.b21.ext.tmpFileUri

class TakePictureResult : ActivityResultContract<Void?, Uri?>() {
    private var uri: Uri? = null

    @CallSuper
    override fun createIntent(context: Context, input: Void?): Intent {
        uri = context.tmpFileUri

        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, uri)
    }

    override fun getSynchronousResult(context: Context, input: Void?): SynchronousResult<Uri?>? = null

    @Suppress("AutoBoxing")
    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK) uri else null
    }
}