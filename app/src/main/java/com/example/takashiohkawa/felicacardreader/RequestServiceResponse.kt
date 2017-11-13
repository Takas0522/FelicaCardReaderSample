package com.example.takashiohkawa.felicacardreader

/**
 * Created by takashiohkawa on 2017/11/09.
 */
class RequestServiceResponse(response: ByteArray) : NfcResponse(response) {
    /** ノードコードに対応するノードの鍵バージョンモデル */
    data class NodeKeyVersion(private val values: ByteArray) {
        val value: ByteArray
            get() {
                return ByteArray(2).apply {
                    this[0] = values[1]
                    this[1] = values[0]
                }
            }
    }

    /** レスポンスのサイズを取得する。 */
    override fun responseSize(): Int {
        return response[0].toInt()
    }

    /** レスポンスコードを取得する。 */
    override fun responseCode(): Byte {
        return response[1]
    }

    /** IDm を取得する。 */
    override fun IDm(): ByteArray {
        return response.copyOfRange(2, 10)
    }

    /** ノードの数を取得する。 */
    fun numberOfNodes(): Int {
        return response[11].toInt()
    }

    /** ノードの鍵バージョンの配列を取得する。 */
    fun nodeKeyVersions(): Array<NodeKeyVersion> {
        var i = 0
        val results = arrayListOf<NodeKeyVersion>()
        val rawNodeKeyVersions = response.copyOfRange(12, response.size)
        while (i < numberOfNodes()) {
            results += NodeKeyVersion(ByteArray(2).apply {
                this[0] = rawNodeKeyVersions[i * 2]
                this[1] = rawNodeKeyVersions[(i * 2) + 1]
            })
        }
        return results.toTypedArray()
    }
}