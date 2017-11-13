package com.example.takashiohkawa.felicacardreader

/**
 * Created by takashiohkawa on 2017/11/09.
 */
data class RequestServiceCommand(private val IDm: ByteArray, private val nodeCodeList: Array<ByteArray>) : NfcCommand {
    /** ノードコードが１つの場合のためのセカンダリコンストラクタ。 */
    constructor(IDm: ByteArray, nodeCode: ByteArray): this(IDm, arrayOf(nodeCode))

    /** Request Service コマンドのコマンドコードは 0x02 */
    override val commandCode: Byte
        get() = 0x2

    /** ノードコードの件数 */
    private val numberOfNodes = (nodeCodeList.size).toByte()

    /** パケットのサイズ */
    private val packetSize = (11 + nodeCodeList.size * 2)

    /** リクエストパケットを取得する。 */
    override fun requestPacket(): ByteArray {
        return ByteArray(packetSize).apply {
            var i = 0
            this[i++] = packetSize.toByte()       // [0] 最初はリクエストパケットのサイズが入る。
            this[i++] = commandCode               // [1] コマンドコードが入る。
            IDm.forEach { this[i++] = it }        // [2..10] IDm (8byte) が入る。
            this[i++] = numberOfNodes             // [11] ノードの数が入る。
            nodeCodeList.forEach {
                it.forEachIndexed { index, byte ->
                    if ((index % 2) == 0) {           // [12..] ノードコード (2byte) が入る。
                        this[i + 1] = byte              //        ノードコードはリトルエンディアンなので
                    } else {                          //        2byte が反転するように格納する。
                        this[i - 1] = byte
                    }
                    i++
                }
            }
        }
    }
}
