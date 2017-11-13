package com.example.takashiohkawa.felicacardreader

/**
 * Created by takashiohkawa on 2017/11/09.
 */
class BlockListElement2(accessMode: AccessMode, serviceCodeIndex: Int, number: Int) : BlockListElement(accessMode, serviceCodeIndex, number) {
    /** エレメントのパケットを取得する。 */
    override fun toByteArray(): ByteArray {
        return ByteArray(2).apply {
            this[0] = firstByte()
            this[1] = number.toByte()
        }
    }

    /** サイズを取得する。 */
    override fun size(): Int {
        return 2
    }
}
