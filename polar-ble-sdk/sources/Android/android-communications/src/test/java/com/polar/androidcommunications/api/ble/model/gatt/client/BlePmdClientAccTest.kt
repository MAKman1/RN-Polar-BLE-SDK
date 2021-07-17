package com.polar.androidcommunications.api.ble.model.gatt.client

import org.junit.Assert
import org.junit.Test
import kotlin.math.abs

class BlePmdClientAccTest {
    @Test
    fun test_parseAccDataDelta_withResolution16() {
        // Arrange
        // HEX: 71 07 F0 6A 9E 8D 0A 38 BE 5C BE BA 2F 96 B3 EE 4B E5 AD FB 42 B9 EB BE 4C FE BA 2F 92 BF EE 4B E4 B1 FB 12 B9 EC BD 3C 3E BB 2F 8F D3 DE 4B E3 B5 F7 D2 B8 ED BD 30 7E 7B 2F 8B E3 CE 8B E2 BA F7 A2 B8 EE BC 20 BE 7B 2F 88 F3 CE CB E1 BD EF 52 F8 EF BC 18 FE 3B 2F 84 03 BF CB E0 C2 EF 32 B8 F0 BB 04 4E BC 2E 81 13 AF 0B E0 C6 EF F2 F7 F1 B9 FC 7D BC 2E 7D 27 9F 4B DF CA EB C2 F7 F2 B8 EC CD 7C 2E 7B 37 8F 4B DE CE E3 92 F7 F3 B8 E0 0D FD 2D 77 4B 7F CB DD D2 DF 62 37 F5 B7 D4 4D BD 2D 74 5B 6F CB DC D7 D7 32 37 F6 B5 C8 8D 7D 2D 71 6B 4F 4B DC DC D3 F2 36 F7 B4 BC DD FD 2C 6F 7B 3F 4B DB E0 CF D2 36 F8 B2 B0 2D BE 2C 6C 8F 1F CB DA E3 C7 A2 76 F9
        // index    type                                            data:
        // 0-5:    Reference sample                     size 6:    0xC9 0xFF 0x12 0x00 0x11 0x00
        //      Sample 0 (aka. reference sample):
        //      channel 0: 71 07 => 0x0771 => 1905
        val refSample0Channel0 = 1905
        //      channel 1: F0 6A => 0x6AF0 => 27376
        val refSample0Channel1 = 27376
        //      channel 2: 9E 8D => 0x8D9E => -29282
        val refSample0Channel2 = -29282
        // Delta dump: 0A 38 | BE 5C BE BA 2F 96 B3 EE 4B E5 AD ...
        // 6:      Delta size                           size 1:    0x0A (10 bits)
        // 7:      Sample amount                        size 1:    0x38 (Delta block contains 56 samples)
        // 8:                                                      0xBE (binary: 1011 1110)
        // 9:                                                      0x5C (binary: 0101 11 | 00)
        // 10:                                                     0xBE (binary: 1011 | 1110)
        //      Sample 1 - channel 0, size 10 bits: 00 1011 1110
        //      Sample 1 - channel 1, size 10 bits: 11 1001 0111
        // 11:                                                     0xBA (binary: 10 | 11 1010)
        //      Sample 1 - channel 2, size 10 bits: 11 1010 1011
        val refSample1Channel0 = 190
        val refSample1Channel1 = -105
        val refSample1Channel2 = -85
        val amountOfSamples = 1 + 56 // reference sample + delta samples
        val measurementFrame = byteArrayOf(
            0x71.toByte(),
            0x07.toByte(),
            0xF0.toByte(),
            0x6A.toByte(),
            0x9E.toByte(),
            0x8D.toByte(),
            0x0A.toByte(),
            0x38.toByte(),
            0xBE.toByte(),
            0x5C.toByte(),
            0xBE.toByte(),
            0xBA.toByte(),
            0x2F.toByte(),
            0x96.toByte(),
            0xB3.toByte(),
            0xEE.toByte(),
            0x4B.toByte(),
            0xE5.toByte(),
            0xAD.toByte(),
            0xFB.toByte(),
            0x42.toByte(),
            0xB9.toByte(),
            0xEB.toByte(),
            0xBE.toByte(),
            0x4C.toByte(),
            0xFE.toByte(),
            0xBA.toByte(),
            0x2F.toByte(),
            0x92.toByte(),
            0xBF.toByte(),
            0xEE.toByte(),
            0x4B.toByte(),
            0xE4.toByte(),
            0xB1.toByte(),
            0xFB.toByte(),
            0x12.toByte(),
            0xB9.toByte(),
            0xEC.toByte(),
            0xBD.toByte(),
            0x3C.toByte(),
            0x3E.toByte(),
            0xBB.toByte(),
            0x2F.toByte(),
            0x8F.toByte(),
            0xD3.toByte(),
            0xDE.toByte(),
            0x4B.toByte(),
            0xE3.toByte(),
            0xB5.toByte(),
            0xF7.toByte(),
            0xD2.toByte(),
            0xB8.toByte(),
            0xED.toByte(),
            0xBD.toByte(),
            0x30.toByte(),
            0x7E.toByte(),
            0x7B.toByte(),
            0x2F.toByte(),
            0x8B.toByte(),
            0xE3.toByte(),
            0xCE.toByte(),
            0x8B.toByte(),
            0xE2.toByte(),
            0xBA.toByte(),
            0xF7.toByte(),
            0xA2.toByte(),
            0xB8.toByte(),
            0xEE.toByte(),
            0xBC.toByte(),
            0x20.toByte(),
            0xBE.toByte(),
            0x7B.toByte(),
            0x2F.toByte(),
            0x88.toByte(),
            0xF3.toByte(),
            0xCE.toByte(),
            0xCB.toByte(),
            0xE1.toByte(),
            0xBD.toByte(),
            0xEF.toByte(),
            0x52.toByte(),
            0xF8.toByte(),
            0xEF.toByte(),
            0xBC.toByte(),
            0x18.toByte(),
            0xFE.toByte(),
            0x3B.toByte(),
            0x2F.toByte(),
            0x84.toByte(),
            0x03.toByte(),
            0xBF.toByte(),
            0xCB.toByte(),
            0xE0.toByte(),
            0xC2.toByte(),
            0xEF.toByte(),
            0x32.toByte(),
            0xB8.toByte(),
            0xF0.toByte(),
            0xBB.toByte(),
            0x04.toByte(),
            0x4E.toByte(),
            0xBC.toByte(),
            0x2E.toByte(),
            0x81.toByte(),
            0x13.toByte(),
            0xAF.toByte(),
            0x0B.toByte(),
            0xE0.toByte(),
            0xC6.toByte(),
            0xEF.toByte(),
            0xF2.toByte(),
            0xF7.toByte(),
            0xF1.toByte(),
            0xB9.toByte(),
            0xFC.toByte(),
            0x7D.toByte(),
            0xBC.toByte(),
            0x2E.toByte(),
            0x7D.toByte(),
            0x27.toByte(),
            0x9F.toByte(),
            0x4B.toByte(),
            0xDF.toByte(),
            0xCA.toByte(),
            0xEB.toByte(),
            0xC2.toByte(),
            0xF7.toByte(),
            0xF2.toByte(),
            0xB8.toByte(),
            0xEC.toByte(),
            0xCD.toByte(),
            0x7C.toByte(),
            0x2E.toByte(),
            0x7B.toByte(),
            0x37.toByte(),
            0x8F.toByte(),
            0x4B.toByte(),
            0xDE.toByte(),
            0xCE.toByte(),
            0xE3.toByte(),
            0x92.toByte(),
            0xF7.toByte(),
            0xF3.toByte(),
            0xB8.toByte(),
            0xE0.toByte(),
            0x0D.toByte(),
            0xFD.toByte(),
            0x2D.toByte(),
            0x77.toByte(),
            0x4B.toByte(),
            0x7F.toByte(),
            0xCB.toByte(),
            0xDD.toByte(),
            0xD2.toByte(),
            0xDF.toByte(),
            0x62.toByte(),
            0x37.toByte(),
            0xF5.toByte(),
            0xB7.toByte(),
            0xD4.toByte(),
            0x4D.toByte(),
            0xBD.toByte(),
            0x2D.toByte(),
            0x74.toByte(),
            0x5B.toByte(),
            0x6F.toByte(),
            0xCB.toByte(),
            0xDC.toByte(),
            0xD7.toByte(),
            0xD7.toByte(),
            0x32.toByte(),
            0x37.toByte(),
            0xF6.toByte(),
            0xB5.toByte(),
            0xC8.toByte(),
            0x8D.toByte(),
            0x7D.toByte(),
            0x2D.toByte(),
            0x71.toByte(),
            0x6B.toByte(),
            0x4F.toByte(),
            0x4B.toByte(),
            0xDC.toByte(),
            0xDC.toByte(),
            0xD3.toByte(),
            0xF2.toByte(),
            0x36.toByte(),
            0xF7.toByte(),
            0xB4.toByte(),
            0xBC.toByte(),
            0xDD.toByte(),
            0xFD.toByte(),
            0x2C.toByte(),
            0x6F.toByte(),
            0x7B.toByte(),
            0x3F.toByte(),
            0x4B.toByte(),
            0xDB.toByte(),
            0xE0.toByte(),
            0xCF.toByte(),
            0xD2.toByte(),
            0x36.toByte(),
            0xF8.toByte(),
            0xB2.toByte(),
            0xB0.toByte(),
            0x2D.toByte(),
            0xBE.toByte(),
            0x2C.toByte(),
            0x6C.toByte(),
            0x8F.toByte(),
            0x1F.toByte(),
            0xCB.toByte(),
            0xDA.toByte(),
            0xE3.toByte(),
            0xC7.toByte(),
            0xA2.toByte(),
            0x76.toByte(),
            0xF9.toByte()
        )
        val resolution = 16
        val range = 8
        val factor = 2.44E-4f
        val timeStamp: Long = 0

        // Act
        val accData = BlePMDClient.AccData(measurementFrame, factor, resolution, timeStamp)

        // Assert
        Assert.assertEquals(
            (factor * refSample0Channel0.toFloat() * 1000f).toInt(),
            accData.accSamples[0].x
        )
        Assert.assertEquals(
            (factor * refSample0Channel1.toFloat() * 1000f).toInt(),
            accData.accSamples[0].y
        )
        Assert.assertEquals(
            (factor * refSample0Channel2.toFloat() * 1000f).toInt(),
            accData.accSamples[0].z
        )
        Assert.assertEquals(
            (factor * (refSample0Channel0 + refSample1Channel0) * 1000f).toInt(),
            accData.accSamples[1].x
        )
        Assert.assertEquals(
            (factor * (refSample0Channel1 + refSample1Channel1) * 1000f).toInt(),
            accData.accSamples[1].y
        )
        Assert.assertEquals(
            (factor * (refSample0Channel2 + refSample1Channel2) * 1000f).toInt(),
            accData.accSamples[1].z
        )

        // validate data in range
        for (sample in accData.accSamples) {
            Assert.assertTrue(abs(sample.x) <= range * 1000)
            Assert.assertTrue(abs(sample.y) <= range * 1000)
            Assert.assertTrue(abs(sample.z) <= range * 1000)
        }

        // validate data size
        Assert.assertEquals(amountOfSamples.toLong(), accData.accSamples.size.toLong())
    }

    @Test
    fun test_parseAccData_withResolution16() {
        // Arrange
        // HEX: 02    7A B4 86 FF C7 87 52 08   01    F7 FF FF FF E7 03 F8 FF FE FF E5 03 F9 FF FF FF E5 03 FA FF FF FF E6 03 FA FF FE FF E6 03 F9 FF FF FF E5 03 F8 FF FF FF E6 03 F8 FF FE FF E6 03 FA FF FF FF E5 03 FA FF FF FF E7 03 FA FF FF FF E5 03 F8 FF FF FF E6 03 F7 FF FF FF E6 03 F8 FF FE FF E6 03 F9 FF FE FF E7 03 F9 FF 00 00 E6 03 F9 FF FF FF E6 03 F7 FF FE FF E5 03 F9 FF FF FF E5 03 F9 FF FF FF E5 03 FA FF 00 00 E6 03 F9 FF FE FF E6 03 F8 FF FF FF E6 03 F8 FF FF FF E5 03 F9 FF FF FF E6 03 F9 FF FF FF E5 03 FA FF FF FF E6 03 F9 FF FF FF E5 03 F9 FF FF FF E5 03 F8 FF FE FF E6 03 F9 FF FF FF E6 03 F9 FF FF FF E6 03 F9 FF 00 00 E5 03 F9 FF FE FF E6 03 F8 FF FE FF E6 03 F7 FF FE FF E6 03
        // index                                                   data:
        // 0        type                                           01
        val type: Byte = 0x01
        // 1..2     x value                                        F7 FF (-9)
        val xValue1 = -9
        // 3..4     y value                                        FF FF (-1)
        val yValue1 = -1
        // 5..6     z value                                        E7 03 (999)
        val zValue1 = 999
        // 7..8     x value                                        F8 FF (-8)
        val xValue2 = -8
        // 9..10    y value                                        FF FE (-2)
        val yValue2 = -2
        // 11..12   z value                                        E5 03 (997)
        val zValue2 = 997
        val measurementFrame = byteArrayOf(
            0xF7.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE7.toByte(),
            0x03.toByte(),
            0xF8.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xFA.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xFA.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xF8.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF8.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xFA.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xFA.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE7.toByte(),
            0x03.toByte(),
            0xFA.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xF8.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF7.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF8.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE7.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF7.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xFA.toByte(),
            0xFF.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF8.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF8.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xFA.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xF8.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0xE5.toByte(),
            0x03.toByte(),
            0xF9.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF8.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte(),
            0xF7.toByte(),
            0xFF.toByte(),
            0xFE.toByte(),
            0xFF.toByte(),
            0xE6.toByte(),
            0x03.toByte()
        )
        val timeStamp: Long = 0
        val amountOfSamples =
            measurementFrame.size / 2 / 3 // measurement frame size / resolution in bytes / channels

        // Act
        val accData = BlePMDClient.AccData(type, measurementFrame, timeStamp)

        // Assert
        Assert.assertEquals(xValue1, accData.accSamples[0].x)
        Assert.assertEquals(yValue1, accData.accSamples[0].y)
        Assert.assertEquals(zValue1, accData.accSamples[0].z)
        Assert.assertEquals(xValue2, accData.accSamples[1].x)
        Assert.assertEquals(yValue2, accData.accSamples[1].y)
        Assert.assertEquals(zValue2, accData.accSamples[1].z)

        // validate data size
        Assert.assertEquals(amountOfSamples, accData.accSamples.size)
    }
}