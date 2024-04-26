package com.project.ser.home

import FabType
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.project.design.theme.SERTheme
import com.project.ser.api.RetrofitClient
import com.project.ser.model.EmotionResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class HomeActivity : ComponentActivity() {

    private val emotion: MutableState<String> = mutableStateOf("")
    private val probDistribution: MutableState<List<Int>> = mutableStateOf(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SERTheme {
                HomeScreen(
                    onFabClicked = { fabType ->
                        onFabClicked(fabType)
                    },
                    emotions = listOf(emotion.value),
                    probDistributions = listOf(probDistribution.value)
                )
            }
        }
    }

    private fun onFabClicked(fabType: FabType) {
        when (fabType) {
            FabType.UPLOAD -> {
                openMediaPicker()
            }

            FabType.RECORD -> {
                // Handle audio recording
            }
        }
    }

    private fun openMediaPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // Set the MIME type to allow selection of any type of media
        startActivityForResult(intent, REQUEST_CODE_UPLOAD)
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
        val originalFileName = getFileName(fileUri) ?: "default_audio_file.wav"
        val fileDescriptor = contentResolver.openFileDescriptor(fileUri, "r") ?: return

        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val file = File(cacheDir, originalFileName)
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()

        val requestBody = RequestBody.create(
            "audio/wav".toMediaTypeOrNull(),
            file
        )
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestBody)

        Log.d("FILE_NAME", file.name)
        RetrofitClient.apiService.uploadAudio(multipartBody)
            .enqueue(object : retrofit2.Callback<EmotionResponse> {
                override fun onResponse(
                    call: Call<EmotionResponse>,
                    response: Response<EmotionResponse>
                ) {
                    val responseBody = response.body()
                    Log.d("@@@@@@@", "SUCCESS - " + responseBody.toString())
                    if (responseBody != null) {
                        emotion.value = responseBody.emo
                        probDistribution.value = responseBody.prob
                        Log.d("EMOTION_ACTIVITY", emotion.value)
                        Log.d("PROB_DISTRIBUTION", probDistribution.value.toString())
                    }
                }

                override fun onFailure(call: Call<EmotionResponse>, t: Throwable) {
                    Log.d("@@@@@@@", "FAILURE - " + t.message.toString())
                }

            })
    }

    private fun getFileName(fileUri: Uri): String? {
        val cursor = contentResolver.query(fileUri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameColumnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameColumnIndex != -1) {
                    return it.getString(displayNameColumnIndex)
                }
            }
        }
        return null
    }


    companion object {
        private const val REQUEST_CODE_UPLOAD = 1
    }
}
