package com.sumygg.anarts.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HashingTest {
    // Fixed vectors calculated independently with Python hashlib/OpenSSL.
    @Test
    fun hashesMatchKnownVectors() {
        val algorithms = listOf("SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512")
        val vectors = listOf(
            "" to listOf(
                "da39a3ee5e6b4b0d3255bfef95601890afd80709",
                "d14a028c2a3a2bc9476102bb288234c415a2b01f828ea62ac5b3e42f",
                "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                "38b060a751ac96384cd9327eb1b1e36a21fdb71114be07434c0cc7bf63f6e1da274edebfe76f65fbd51ad2f14898b95b",
                "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e"
            ),
            "abc" to listOf(
                "a9993e364706816aba3e25717850c26c9cd0d89d",
                "23097d223405d8228642a477bda255b32aadbce4bda0b3f7e36c9da7",
                "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                "cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed8086072ba1e7cc2358baeca134c825a7",
                "ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f"
            ),
            "生成式艺术 🎨" to listOf(
                "d11b58e450fcd803af6076f7412aaee82317942d",
                "8f7f507d2a89af90fa833a6048eddfaa8561b77574afad78be8a088f",
                "6983461627406c1b594788db0c6ce7ff0bdf75dd6dbba1971dd925de58bc1e34",
                "f852350aac2b70d7a44e08b94805afb5d6199591774c5f9e2c08ceb3be4f02ca7aee58245bb6de5d3e122e6f32d2f691",
                "0ae443f9887cde0d179edb008815f31a97577f0c817a4c8e1653b89d495805cf3f2921d311b2452b3c288a2eaaee45be1f1519149db8ab8f06a9869dca5c1772"
            ),
            "a".repeat(1000) to listOf(
                "291e9a6c66994949b57ba5e650361e98fc36b1ba",
                "4e8f0ce90b64661a2b5e84be6d93a7d9b76871062f1814433d04a03d",
                "41edece42d63e8d9bf515a9ba6932e1c20cbc9f5a5d134645adb5db1b9737ea3",
                "f54480689c6b0b11d0303285d9a81b21a93bca6ba5a1b4472765dca4da45ee328082d469c650cd3b61b16d3266ab8ced",
                "67ba5535a46e3f86dbfbed8cbbaf0125c76ed549ff8b0b9e03e0c88cf90fa634fa7b12b47d77b694de488ace8d9a65967dc96df599727d3292a8d9d447709c97"
            )
        )
        for ((input, expected) in vectors) {
            algorithms.forEachIndexed { i, algorithm ->
                assertEquals(expected[i], hashHex(input, algorithm), "$algorithm for input length ${input.length}")
            }
        }
    }

    @Test
    fun rejectsUnknownAlgorithm() {
        assertFailsWith<IllegalArgumentException> { hashHex("abc", "unknown") }
    }
}
