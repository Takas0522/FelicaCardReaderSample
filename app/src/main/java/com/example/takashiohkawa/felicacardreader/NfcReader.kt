package com.example.takashiohkawa.felicacardreader

import android.nfc.Tag
import android.nfc.tech.NfcF
import android.util.Log
import java.io.ByteArrayOutputStream


/**
 * Created by takashiohkawa on 2017/11/09.
 */
class NfcReader() {
    fun read(tag: Tag): ReadWithoutEncryptionResponse? {
        val nfc = NfcF.get(tag)
        Log.d("tag.getId", "${tag.id}")
        try {
            nfc.connect()
            val result = read(nfc)
            Log.d("FelicaSample", "read without encryption: $result")

            if (result != null) {
                result.blocks.forEachIndexed { i, bytes -> Log.d("Felica sample", "block[$i] '${bytes.joinToString(" ")}' " ) }
            }
            nfc.close()

            return  result
        } catch (e: Exception) {
            Log.e("FelicaSample", "cannot read nfc, $e")
            e.printStackTrace()
            if (nfc.isConnected) {
                nfc.close()
            }
            return  null
        }
    }

    private fun read(nfc: NfcF): ReadWithoutEncryptionResponse {
        // System 1のシステムコード -> 0x0003 (SUICA/PASUMO などの鉄道系)
        val targetSystemCode = byteArrayOf(0x00.toByte(), 0x03.toByte())

        // [1]: Polling コマンドオブジェクトのリクエストパケットを nfc に送信し、Polling の結果を受け取る。
        val pollingCommand = PollingCommand(targetSystemCode)
        val pollingRequest = pollingCommand.requestPacket()
        val rawPollingResponse = nfc.transceive(pollingRequest)
        val pollingResponse = PollingResponse(rawPollingResponse)

        // Polling で得られた IDm を取得する。
        val targetIDm = pollingResponse.IDm()

        // 実行したいサービスコードを生成する。
//        val serviceCode = byteArrayOf(0x00.toByte(), 0x8B.toByte())
        val serviceCode = byteArrayOf(0x0f.toByte(), 0x09.toByte())

        // [2]: Request Service コマンドオブジェクトのリクエストパケットを nfc に送信し、Request Service の結果を受け取る。
        val requestServiceCommand = RequestServiceCommand(targetIDm, serviceCode)
        val requestServiceRequest = requestServiceCommand.requestPacket()
        val rawRequestServiceResponse = nfc.transceive(requestServiceRequest)
        val requestServiceResponse = RequestServiceResponse(rawRequestServiceResponse)

        // [3]: Read Without Encryption コマンドオブジェクトのリクエストパケットを nfc に送信し、Read Without Encryption の結果を受け取る。
//        val readWithoutEncryptionCommand = ReadWithoutEncryptionCommand(IDm = targetIDm, serviceCode = serviceCode, blocks = arrayOf(BlockListElement2(BlockListElement.AccessMode.toNotParseService, 0, 0)))
        // 履歴情報はブロック数20のデータが必要なので、20ブロックのByteArrayデータを作成する
//        val readWithoutEncryptionCommand = ReadWithoutEncryptionCommand(IDm = targetIDm, serviceCode = serviceCode, blocks = Array(10, {i -> BlockListElement2(BlockListElement.AccessMode.toNotParseService, 0, i) }))
//        val readWithoutEncryptionRequest = readWithoutEncryptionCommand.requestPacket()
//        val rawReadWithoutEncryptionResponse = nfc.transceive(readWithoutEncryptionRequest)
        val req = readWthOutEncryptionCommand(targetIDm, 10)
        val rawReadWithoutEncryptionResponse = nfc.transceive(req)

        Log.d("TAG", "res:"+toHex(rawReadWithoutEncryptionResponse));
        val readWithouEncryptionResponse = ReadWithoutEncryptionResponse(rawReadWithoutEncryptionResponse)

        return readWithouEncryptionResponse
    }

    private fun readWthOutEncryptionCommand(idm: ByteArray, size: Int): ByteArray {
        val bout = ByteArrayOutputStream(100)

        bout.write(0)           // データ長バイトのダミー
        bout.write(0x06)        // Felicaコマンド「Read Without Encryption」
        bout.write(idm)         // カードID 8byte
        bout.write(1)           // サービスコードリストの長さ(以下２バイトがこの数分繰り返す)
        bout.write(0x0f)        // 履歴のサービスコード下位バイト
        bout.write(0x09)        // 履歴のサービスコード上位バイト
        bout.write(size)        // ブロック数
        for (i in 0 until size) {
            bout.write(0x80)    // ブロックエレメント上位バイト 「Felicaユーザマニュアル抜粋」の4.3項参照
            bout.write(i)       // ブロック番号
        }

        val msg = bout.toByteArray()
        msg[0] = msg.size.toByte() // 先頭１バイトはデータ長
        return msg
    }

    private fun toHex(id: ByteArray): String {
        val sbuf = StringBuilder()
        for (i in id.indices) {
            var hex = "0" + Integer.toString(id[i].toInt() and 0x0ff, 16)
            if (hex.length > 2)
                hex = hex.substring(1, 3)
            sbuf.append(" $i:$hex")
        }
        return sbuf.toString()
    }

}