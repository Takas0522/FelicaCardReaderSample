package com.example.takashiohkawa.felicacardreader

/**
 * Created by takashiohkawa on 2017/11/09.
 */
abstract class NfcResponse {
    companion object {
        var response: ByteArray = ByteArray(0)
    }
    constructor(sendResponse: ByteArray) {
        response = sendResponse
    }
    abstract fun responseSize(): Int
    abstract fun responseCode(): Byte
    abstract  fun IDm(): ByteArray
}