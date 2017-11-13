package com.example.takashiohkawa.felicacardreader

/**
 * Created by takashiohkawa on 2017/11/09.
 */
class PollingResponse(response: ByteArray) : NfcResponse(response) {
    /** レスポンスのサイズを取得する。 */
    override fun responseSize(): Int {
        return response[0].toInt()
    }

    /** レスポンスコードの取得をする。 */
    override fun responseCode(): Byte {
        return response[1]
    }

    /** IDm を取得する。 */
    override fun IDm(): ByteArray {
        return response.copyOfRange(2, 10)
    }
}