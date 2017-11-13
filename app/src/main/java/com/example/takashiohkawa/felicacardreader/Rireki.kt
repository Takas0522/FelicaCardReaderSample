import android.util.SparseArray

class Rireki {
    var termId: Int = 0
    var procId: Int = 0
    var year = 0
    var month = 0
    var day = 0
    var remain = 0

    private fun init(res: ByteArray) {
        var termId = res[0].toInt() //0: 端末種
        if (termId < 0) {
            termId += 256
        }
        this.termId = termId
        var procId = res[1].toInt()
        this.procId = procId
        var mixInt = toInt(res, 4, 5)
        this.year = mixInt shr 9 and 0x07f
        this.month = mixInt shr 5 and 0x00f
        this.day = mixInt and 0x01f
        this.remain  = toInt(res, 11,10)
    }

    private fun toInt(res: ByteArray, vararg idx: Int): Int {
        var num = 0
        for (i in idx.indices) {
            num = num shl 8
            num += res[idx[i]].toInt() and 0x0ff
        }
        return num
    }

    override fun toString(): String {
        return ("機器種別：${TERM_MAP.get(termId)}\n" +
                "利用種別:${PROC_MAP.get(procId)}\n" +
                "日付:${this.year}/${this.month}/${this.day} \n" +
                "残額：${this.remain} \n\n") }

    companion object {

        fun parse(res: ByteArray): Rireki {
            val self = Rireki()
            self.init(res)
            return self
        }

        val TERM_MAP = SparseArray<String>()
        val PROC_MAP = SparseArray<String>()

        init {
            TERM_MAP.put(3, "精算機")
            TERM_MAP.put(4, "携帯型端末")
            TERM_MAP.put(5, "車載端末")
            TERM_MAP.put(7, "券売機")
            TERM_MAP.put(8, "券売機")
            TERM_MAP.put(9, "入金機")
            TERM_MAP.put(18, "券売機")
            TERM_MAP.put(20, "券売機等")
            TERM_MAP.put(21, "券売機等")
            TERM_MAP.put(22, "改札機")
            TERM_MAP.put(23, "簡易改札機")
            TERM_MAP.put(24, "窓口端末")
            TERM_MAP.put(25, "窓口端末")
            TERM_MAP.put(26, "改札端末")
            TERM_MAP.put(27, "携帯電話")
            TERM_MAP.put(28, "乗継精算機")
            TERM_MAP.put(29, "連絡改札機")
            TERM_MAP.put(31, "簡易入金機")
            TERM_MAP.put(70, "VIEW ALTTE")
            TERM_MAP.put(72, "VIEW ALTTE")
            TERM_MAP.put(199, "物販端末")
            TERM_MAP.put(200, "自販機")

            PROC_MAP.put(1, "運賃支払(改札出場)")
            PROC_MAP.put(2, "チャージ")
            PROC_MAP.put(3, "券購(磁気券購入)")
            PROC_MAP.put(4, "精算")
            PROC_MAP.put(5, "精算 (入場精算)")
            PROC_MAP.put(6, "窓出 (改札窓口処理)")
            PROC_MAP.put(7, "新規 (新規発行)")
            PROC_MAP.put(8, "控除 (窓口控除)")
            PROC_MAP.put(13, "バス (PiTaPa系)")
            PROC_MAP.put(15, "バス (IruCa系)")
            PROC_MAP.put(17, "再発 (再発行処理)")
            PROC_MAP.put(19, "支払 (新幹線利用)")
            PROC_MAP.put(20, "入A (入場時オートチャージ)")
            PROC_MAP.put(21, "出A (出場時オートチャージ)")
            PROC_MAP.put(31, "入金 (バスチャージ)")
            PROC_MAP.put(35, "券購 (バス路面電車企画券購入)")
            PROC_MAP.put(70, "物販")
            PROC_MAP.put(72, "特典 (特典チャージ)")
            PROC_MAP.put(73, "入金 (レジ入金)")
            PROC_MAP.put(74, "物販取消")
            PROC_MAP.put(75, "入物 (入場物販)")
            PROC_MAP.put(198, "物現 (現金併用物販)")
            PROC_MAP.put(203, "入物 (入場現金併用物販)")
            PROC_MAP.put(132, "精算 (他社精算)")
            PROC_MAP.put(133, "精算 (他社入場精算)")
        }
    }
}