package com.project.ser.home

import FabType
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.project.design.theme.SERTheme
import java.io.File

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SERTheme {
                HomeScreen(
                    onFabClicked = { fabType ->
                        onFabClicked(fabType)
                    },
                    emotionsResponseList = emptyList()
                )
            }
        }
    }

    private fun onFabClicked(fabType: FabType) {
        when (fabType) {
            FabType.UPLOAD -> {
                val audioIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(audioIntent, REQUEST_CODE_UPLOAD)
            }

            FabType.RECORD -> {
                // TODO() Handle audio recording
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_UPLOAD && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                uploadAudio(uri)
            }
        }
    }

    private fun uploadAudio(fileUri: Uri) {
        val contentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(fileUri)
        val file = File(cacheDir, "temp_audio_file") // Creating a temp file

        file.outputStream().use {
            inputStream?.copyTo(it)
        }

        // Now you have a File object 'file' that you can upload
        // Implement the file upload logic here
    }

    companion object {
        private const val REQUEST_CODE_UPLOAD = 1
    }

}
