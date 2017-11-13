package com.example.takashiohkawa.felicacardreader

/**
 * Created by takashiohkawa on 2017/11/10.
 */
class BlockListElementRireki(accessMode: AccessMode, serviceCodeIndex: Int, number: Int) : BlockListElement(accessMode, serviceCodeIndex, number) {
    /** エレメントのパケットを取得する。 */
    override fun toByteArray(): ByteArray {
        return ByteArray(20).apply {
            this[0] = firstByte()
            this[19] = number.toByte()
        }
    }
    override fun size(): Int {

        /** サイズを取得する。 */
        return 20
    }
}
