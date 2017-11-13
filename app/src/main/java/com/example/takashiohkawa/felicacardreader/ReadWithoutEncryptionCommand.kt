package com.example.takashiohkawa.felicacardreader

import java.io.ByteArrayOutputStream

/**
 * Created by takashiohkawa on 2017/11/09.
 */
data class ReadWithoutEncryptionCommand(private val IDm: ByteArray, private val serviceList: Array<ByteArray>, val blocks: Array<BlockListElement>) : NfcCommand {
    /** サービスコードが 1種類の場合のためのセカンダリコンストラクタ。 */
    constructor(IDm: ByteArray, serviceCode: ByteArray, blocks: Array<BlockListElement>): this(IDm, arrayOf(serviceCode), blocks)

    /** Read Without Encryption コマンドのコマンドコードは 0x06 */
    override val commandCode: Byte
        get() = 0x06.toByte()

    /** サービスの数 */
    private val numberOfServices = serviceList.size

    /** ブロックの数 */
    private val numberOfBlocks: Int = blocks.size

    /** ブロックリスト全体の byte 数 */
    private val blockSize = blocks.map { it.size() }.reduce { a, b -> a + b }

    /** パケットサイズ */
    private val packetSize = 13 + (numberOfServices * 2) + blockSize

    /** リクエストパケットを取得する。 */
    override fun requestPacket(): ByteArray {
        return ByteArray(packetSize).apply {
            var i = 0
            this[i++] = packetSize.toByte()       // [0] 最初はリクエストパケットのサイズが入る。
            this[i++] = commandCode               // [1] コマンドコードが入る。
            IDm.forEach { this[i++] = it }        // [2..10] IDｍ (8byte) が入る。
            this[i++] = numberOfServices.toByte() // [11] サービスコードリストの数が入る。
            serviceList.forEach {
                it.forEachIndexed { index, byte ->  // [12..] サービスコード (2byte) が入る。
                    if ((index % 2) == 0) {           //        サービスコードはリトルエンディアンなので
                        this[i + 1] = byte              //        2byte が反転するように格納する。
                    } else {
                        this[i - 1] = byte
                    }
                    i++
                }
            }
            this[i++] = numberOfBlocks.toByte()    // [12 + 2 * numberOfServices + 1] ブロックリストの数が入る。
            blocks.forEach { it.toByteArray().forEach { this[i++] = it } }
            // [...] ブロックリストエレメントのパケットが順繰り入る。
        }
    }
}