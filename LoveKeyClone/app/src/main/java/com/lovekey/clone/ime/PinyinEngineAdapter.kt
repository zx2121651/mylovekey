package com.lovekey.clone.ime

import android.content.Context
import android.util.Log
import com.yuyan.inputmethod.core.Rime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

object PinyinEngineAdapter {
    private const val TAG = "PinyinEngineAdapter"
    private var isInitialized = false
    private lateinit var rimeEngine: Rime

    /**
     * Initializes the C++ Rime Engine.
     * This requires copying the 'rime' assets to the local filesDir so the C++ library can read them.
     */
    fun init(context: Context) {
        if (isInitialized) return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                copyAssets(context)
                rimeEngine = Rime.getInstance(context, false)
                isInitialized = true
                Log.d(TAG, "Rime Engine initialized successfully.")
            } catch (e: Throwable) {
                Log.e(TAG, "Failed to initialize Rime Engine", e)
            }
        }
    }

    private fun copyAssets(context: Context) {
        val assetManager = context.assets
        val destDir = File(context.filesDir, "rime")
        if (!destDir.exists()) {
            destDir.mkdirs()
        }

        fun copyFileOrDir(path: String) {
            try {
                val assets = assetManager.list(path)
                if (assets.isNullOrEmpty()) {
                    // It's a file
                    val destFile = File(context.filesDir, path)
                    if (!destFile.exists()) {
                        assetManager.open(path).use { inputStream ->
                            FileOutputStream(destFile).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                    }
                } else {
                    // It's a directory
                    val dir = File(context.filesDir, path)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    for (asset in assets) {
                        copyFileOrDir("$path/$asset")
                    }
                }
            } catch (e: Throwable) {
                Log.e(TAG, "Failed to copy asset: $path", e)
            }
        }

        copyFileOrDir("rime")
    }

    fun getCandidates(pinyin: String): List<String> {
        if (!isInitialized) return emptyList()
        if (pinyin.isEmpty()) return emptyList()

        try {
            // Rime workflow:
            // 1. Clear previous composition
            Rime.clearRimeComposition()

            // 2. Input new key sequence
            for (char in pinyin) {
                // ASCII values mapping to keycodes for letters. Rime schema expects lowercase.
                val keycode = char.lowercaseChar().code
                Rime.processRimeKey(keycode, 0)
            }

            // 3. Get Context/Menu which contains candidates
            val context = Rime.getRimeContext()
            val candidates = context?.candidates?.map { it.text } ?: emptyList()
            return candidates
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to get candidates for $pinyin", e)
            return emptyList()
        }
    }

    fun clearComposing() {
         if (!isInitialized) return
         Rime.clearRimeComposition()
    }
}
