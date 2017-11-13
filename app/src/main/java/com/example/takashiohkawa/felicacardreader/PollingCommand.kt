package com.example.takashiohkawa.felicacardreader

import android.app.DownloadManager

/**
 * Created by takashiohkawa on 2017/11/09.
 */
data class PollingCommand(private val systemCode: ByteArray, private val request: Request = PollingCommand.Request.systemCode) : NfcCommand {
    /** Polling コマンドで取得する情報の列挙体。 */
    enum class Request(val value: Byte) {
        none(0x00), systemCode(0x01), communicationAbility(0x02)
    }

    /** Polling コマンドのコマンドコードは 0x00 */
    override val commandCode: Byte
        get() = 0x00

    /** タイムスロットは 0F 固定とする（ほんとはもっとちゃんと指定できる。仕様編参照） */
    val timeSlot: Byte
        get() = 0x0f

    /** リクエストコードのバイト値 */
    val requestCode: Byte
        get() = request.value

    /** リクエストパケットを取得する。 */
    override fun requestPacket(): ByteArray {
        return ByteArray(6).apply {
            var i = 0
            this[i++] = 0x06              // [0] 最初はリクエストパケットのサイズが入る。6byte固定。
            this[i++] = commandCode       // [1] コマンドコードが入る。
            this[i++] = systemCode[0]     // [2] システムコードの先頭byteが入る。
            this[i++] = systemCode[1]     // [3] システムコードの末尾byteが入る。
            this[i++] = requestCode       // [4] リクエストコードが入る。
            this[i++] = timeSlot          // [5] タイムスロットが入る。
        }
    }
}