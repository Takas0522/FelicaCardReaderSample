package com.example.takashiohkawa.felicacardreader

import android.util.Log

/**
 * Created by takashiohkawa on 2017/11/09.
 */
class ReadWithoutEncryptionResponse : NfcResponse {
    val blocks: Array<ByteArray>
    constructor(response: ByteArray): super(response) {
        blocks = blocks()
    }


    override fun responseSize(): Int {
        return response[0].toInt()
    }

    override fun responseCode(): Byte {
        return response[1]
    }

    override fun IDm(): ByteArray {
        return response.copyOfRange(2,10)
    }

    fun statusFlag1(): Byte {
        return response[10]
    }

    fun statusFlag2(): Byte {
        return response[11]
    }

    fun numberOfBlocks(): Int {
        return response[12].toInt()
    }

    private fun blocks(): Array<ByteArray> {
        var i = 0
        var results = arrayOf<ByteArray>()
        var raw = response.copyOfRange(13, response.size)
        while ( i < numberOfBlocks()) {
            var addResult = raw.copyOfRange(i * 16, i * 16 + 16)
            results += addResult

            Log.d("Felica.parse", "${addResult[0].toInt()}")
            i++
        }
        return  results
    }
}