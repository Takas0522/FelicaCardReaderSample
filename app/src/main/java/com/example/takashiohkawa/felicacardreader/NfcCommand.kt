package com.example.takashiohkawa.felicacardreader

/**
 * Created by takashiohkawa on 2017/11/09.
 */
interface NfcCommand {
    val commandCode: Byte
    fun requestPacket(): ByteArray
}