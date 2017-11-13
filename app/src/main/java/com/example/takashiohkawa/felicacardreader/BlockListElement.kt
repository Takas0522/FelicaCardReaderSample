package com.example.takashiohkawa.felicacardreader

/**
 * Created by takashiohkawa on 2017/11/09.
 */
abstract class BlockListElement(val accessMode: AccessMode, val serviceCodeIndex: Int, val number: Int) {
    /** アクセスモードの列挙体 */
    enum class AccessMode(val value: Int) {
        toNotParseService(0), toParseService(1)
    }

    /** ブロックリストエレメントの最初の byte を取得する。 */
    fun firstByte(): Byte {
        // 1byte
        //    [0] エレメントのサイズ (1bit) 0: 2byteエレメント, 1: 3byteエレメント
        //    [1] アクセスモード (1bit)
        //    [2..7] エレメントが対象とするサービスコードのサービスコードリスト内の番号 (6bit)
        //    x 0 0 0 0 0 0 0 <- エレメントのサイズ
        //    0 x 0 0 0 0 0 0 <- アクセスモード
        //  & 0 0 x x x x x x <- サービスコードリスト内の順番
        return ((0 shl 7) and (accessMode.value shl 6) and (serviceCodeIndex shl 3)).toByte()
    }
    abstract fun toByteArray(): ByteArray
    abstract fun size(): Int
}
