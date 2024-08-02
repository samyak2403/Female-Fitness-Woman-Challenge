package com.arrowwould.womenhomeworkout.common

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import com.utillity.objects.LocalDB
import java.util.*


class AppControl: Application()  {

    companion object {
        var textToSpeech: TextToSpeech? = null

        fun speechText(context: Context, strSpeechText: String) {
            if (checkVoiceOnOrOff(context)) {
                if (textToSpeech != null) {
                    textToSpeech!!.setSpeechRate(0.9f)
                    textToSpeech!!.speak(strSpeechText, TextToSpeech.QUEUE_FLUSH, null,null)
                }
            }
        }

        private fun checkVoiceOnOrOff(context: Context): Boolean {
            return !LocalDB.getSoundMute(context) && LocalDB.getVoiceGuide(context)
        }

    }

    override fun onCreate() {
        super.onCreate()

        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.ENGLISH
            }
        })

    }

}
