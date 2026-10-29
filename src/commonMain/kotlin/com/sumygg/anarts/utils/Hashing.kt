package com.sumygg.anarts.utils

/** SHA digests for Identicon, shared by desktop and browser without platform crypto dependencies. */
fun hashHex(text: String, algorithm: String): String {
    val bytes = text.encodeToByteArray()
    return when (algorithm) {
        "SHA-1" -> sha1(bytes)
        "SHA-224" -> sha256(bytes, short = true)
        "SHA-256" -> sha256(bytes, short = false)
        "SHA-384" -> sha512(bytes, short = true)
        "SHA-512" -> sha512(bytes, short = false)
        else -> throw IllegalArgumentException("Unsupported hash algorithm: $algorithm")
    }.toHexString()
}

private fun padded(input: ByteArray, blockSize: Int, lengthSize: Int): ByteArray {
    val size = ((input.size + 1 + lengthSize + blockSize - 1) / blockSize) * blockSize
    val result = input.copyOf(size)
    result[input.size] = 0x80.toByte()
    val bitLength = input.size.toLong() * 8
    for (i in 0..7) result[size - 1 - i] = (bitLength ushr (i * 8)).toByte()
    return result
}

private fun readInt(bytes: ByteArray, offset: Int): Int {
    var value = 0
    for (i in 0..3) value = (value shl 8) or (bytes[offset + i].toInt() and 0xff)
    return value
}

private fun IntArray.bytes(count: Int = size): ByteArray = ByteArray(count * 4) { i ->
    (this[i / 4] ushr (24 - (i % 4) * 8)).toByte()
}

private fun sha1(input: ByteArray): ByteArray {
    val state = intArrayOf(0x67452301, 0xefcdab89.toInt(), 0x98badcfe.toInt(), 0x10325476, 0xc3d2e1f0.toInt())
    val bytes = padded(input, 64, 8)
    val words = IntArray(80)
    for (offset in bytes.indices step 64) {
        for (i in 0..15) words[i] = readInt(bytes, offset + i * 4)
        for (i in 16..79) words[i] = (words[i - 3] xor words[i - 8] xor words[i - 14] xor words[i - 16]).rotateLeft(1)
        var a = state[0]; var b = state[1]; var c = state[2]; var d = state[3]; var e = state[4]
        for (i in 0..79) {
            val f = when (i) {
                in 0..19 -> (b and c) or (b.inv() and d)
                in 40..59 -> (b and c) or (b and d) or (c and d)
                else -> b xor c xor d
            }
            val k = when (i) {
                in 0..19 -> 0x5a827999
                in 20..39 -> 0x6ed9eba1
                in 40..59 -> 0x8f1bbcdc.toInt()
                else -> 0xca62c1d6.toInt()
            }
            val next = a.rotateLeft(5) + f + e + k + words[i]
            e = d; d = c; c = b.rotateLeft(30); b = a; a = next
        }
        state[0] += a; state[1] += b; state[2] += c; state[3] += d; state[4] += e
    }
    return state.bytes()
}

// Fractional cube roots of the first 80 primes, as specified by SHA-512.
private val sha512Constants = arrayOf(
    "428a2f98d728ae22", "7137449123ef65cd", "b5c0fbcfec4d3b2f", "e9b5dba58189dbbc",
    "3956c25bf348b538", "59f111f1b605d019", "923f82a4af194f9b", "ab1c5ed5da6d8118",
    "d807aa98a3030242", "12835b0145706fbe", "243185be4ee4b28c", "550c7dc3d5ffb4e2",
    "72be5d74f27b896f", "80deb1fe3b1696b1", "9bdc06a725c71235", "c19bf174cf692694",
    "e49b69c19ef14ad2", "efbe4786384f25e3", "0fc19dc68b8cd5b5", "240ca1cc77ac9c65",
    "2de92c6f592b0275", "4a7484aa6ea6e483", "5cb0a9dcbd41fbd4", "76f988da831153b5",
    "983e5152ee66dfab", "a831c66d2db43210", "b00327c898fb213f", "bf597fc7beef0ee4",
    "c6e00bf33da88fc2", "d5a79147930aa725", "06ca6351e003826f", "142929670a0e6e70",
    "27b70a8546d22ffc", "2e1b21385c26c926", "4d2c6dfc5ac42aed", "53380d139d95b3df",
    "650a73548baf63de", "766a0abb3c77b2a8", "81c2c92e47edaee6", "92722c851482353b",
    "a2bfe8a14cf10364", "a81a664bbc423001", "c24b8b70d0f89791", "c76c51a30654be30",
    "d192e819d6ef5218", "d69906245565a910", "f40e35855771202a", "106aa07032bbd1b8",
    "19a4c116b8d2d0c8", "1e376c085141ab53", "2748774cdf8eeb99", "34b0bcb5e19b48a8",
    "391c0cb3c5c95a63", "4ed8aa4ae3418acb", "5b9cca4f7763e373", "682e6ff3d6b2b8a3",
    "748f82ee5defb2fc", "78a5636f43172f60", "84c87814a1f0ab72", "8cc702081a6439ec",
    "90befffa23631e28", "a4506cebde82bde9", "bef9a3f7b2c67915", "c67178f2e372532b",
    "ca273eceea26619c", "d186b8c721c0c207", "eada7dd6cde0eb1e", "f57d4f7fee6ed178",
    "06f067aa72176fba", "0a637dc5a2c898a6", "113f9804bef90dae", "1b710b35131c471b",
    "28db77f523047d84", "32caab7b40c72493", "3c9ebe0a15c9bebc", "431d67c49c100d4c",
    "4cc5d4becb3e42b6", "597f299cfc657e2a", "5fcb6fab3ad6faec", "6c44198c4a475817",
).map { it.toULong(16).toLong() }.toLongArray()

private val sha256Constants = IntArray(64) { (sha512Constants[it] ushr 32).toInt() }

private fun sha256(input: ByteArray, short: Boolean): ByteArray {
    val state = if (short) intArrayOf(
        0xc1059ed8.toInt(), 0x367cd507, 0x3070dd17, 0xf70e5939.toInt(),
        0xffc00b31.toInt(), 0x68581511, 0x64f98fa7, 0xbefa4fa4.toInt()
    ) else intArrayOf(
        0x6a09e667, 0xbb67ae85.toInt(), 0x3c6ef372, 0xa54ff53a.toInt(),
        0x510e527f, 0x9b05688c.toInt(), 0x1f83d9ab, 0x5be0cd19
    )
    val bytes = padded(input, 64, 8)
    val words = IntArray(64)
    for (offset in bytes.indices step 64) {
        for (i in 0..15) words[i] = readInt(bytes, offset + i * 4)
        for (i in 16..63) {
            val x = words[i - 15]; val y = words[i - 2]
            val s0 = x.rotateRight(7) xor x.rotateRight(18) xor (x ushr 3)
            val s1 = y.rotateRight(17) xor y.rotateRight(19) xor (y ushr 10)
            words[i] = words[i - 16] + s0 + words[i - 7] + s1
        }
        var a = state[0]; var b = state[1]; var c = state[2]; var d = state[3]
        var e = state[4]; var f = state[5]; var g = state[6]; var h = state[7]
        for (i in 0..63) {
            val s1 = e.rotateRight(6) xor e.rotateRight(11) xor e.rotateRight(25)
            val choice = (e and f) xor (e.inv() and g)
            val t1 = h + s1 + choice + sha256Constants[i] + words[i]
            val s0 = a.rotateRight(2) xor a.rotateRight(13) xor a.rotateRight(22)
            val majority = (a and b) xor (a and c) xor (b and c)
            val t2 = s0 + majority
            h = g; g = f; f = e; e = d + t1; d = c; c = b; b = a; a = t1 + t2
        }
        state[0] += a; state[1] += b; state[2] += c; state[3] += d
        state[4] += e; state[5] += f; state[6] += g; state[7] += h
    }
    return state.bytes(if (short) 7 else 8)
}

private fun sha512(input: ByteArray, short: Boolean): ByteArray {
    val state = (if (short) arrayOf(
    "cbbb9d5dc1059ed8", "629a292a367cd507", "9159015a3070dd17", "152fecd8f70e5939",
    "67332667ffc00b31", "8eb44a8768581511", "db0c2e0d64f98fa7", "47b5481dbefa4fa4"
    ) else arrayOf(
    "6a09e667f3bcc908", "bb67ae8584caa73b", "3c6ef372fe94f82b", "a54ff53a5f1d36f1",
    "510e527fade682d1", "9b05688c2b3e6c1f", "1f83d9abfb41bd6b", "5be0cd19137e2179"
    )).map { it.toULong(16).toLong() }.toLongArray()
    val bytes = padded(input, 128, 16)
    val words = LongArray(80)
    for (offset in bytes.indices step 128) {
        for (i in 0..15) {
            val pos = offset + i * 8
            words[i] = (readInt(bytes, pos).toLong() shl 32) or (readInt(bytes, pos + 4).toLong() and 0xffffffffL)
        }
        for (i in 16..79) {
            val x = words[i - 15]; val y = words[i - 2]
            val s0 = x.rotateRight(1) xor x.rotateRight(8) xor (x ushr 7)
            val s1 = y.rotateRight(19) xor y.rotateRight(61) xor (y ushr 6)
            words[i] = words[i - 16] + s0 + words[i - 7] + s1
        }
        var a = state[0]; var b = state[1]; var c = state[2]; var d = state[3]
        var e = state[4]; var f = state[5]; var g = state[6]; var h = state[7]
        for (i in 0..79) {
            val s1 = e.rotateRight(14) xor e.rotateRight(18) xor e.rotateRight(41)
            val choice = (e and f) xor (e.inv() and g)
            val t1 = h + s1 + choice + sha512Constants[i] + words[i]
            val s0 = a.rotateRight(28) xor a.rotateRight(34) xor a.rotateRight(39)
            val majority = (a and b) xor (a and c) xor (b and c)
            val t2 = s0 + majority
            h = g; g = f; f = e; e = d + t1; d = c; c = b; b = a; a = t1 + t2
        }
        state[0] += a; state[1] += b; state[2] += c; state[3] += d
        state[4] += e; state[5] += f; state[6] += g; state[7] += h
    }
    return ByteArray(if (short) 48 else 64) { i -> (state[i / 8] ushr (56 - i % 8 * 8)).toByte() }
}
