package com.example.takashiohkawa.felicacardreader

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private val nfcAdapter by lazy { NfcAdapter.getDefaultAdapter(this) }

    companion object {
        val NFC_TYPES = arrayOf(NfcF:: class.java.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var textView = findViewById<TextView>(R.id.textView1)
        textView.setMovementMethod(ScrollingMovementMethod.getInstance())
    }

    override fun onResume() {
        super.onResume()

        // PendingIntent 起動時に実行される intent の生成と PendingIntent のインスタンス化をする。
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val feliCaPendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        // NfcAdapter.ACTION_NDEF_DISCOVERED をアクション名とするフィルタを生成する。
        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply { this.addDataType("*/*") }

        // NfcAdapter の検出開始を実行する。
        nfcAdapter.enableForegroundDispatch(this, feliCaPendingIntent, arrayOf(filter), arrayOf(NFC_TYPES))
    }

    override fun onNewIntent(intent: Intent?) {
        if(intent == null) {
            return
        }
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

        if (tag!=null){
            val result = NfcReader().read(tag)
            Toast.makeText(this, "tag: '$tag', id: '${tag.id.joinToString(" ")}'", Toast.LENGTH_SHORT).show()
            var i = 0
            textView1.text = ""
            result?.blocks?.forEach {
                var rireki = Rireki.parse(it)
                Log.d("FelicaRireki", rireki.toString())
                var outputText = "${textView1.text}${rireki.toString()}"
                textView1.text = outputText
                i++
            }
        } else {
            Log.d("FALSE", "$intent")
        }
    }
}
