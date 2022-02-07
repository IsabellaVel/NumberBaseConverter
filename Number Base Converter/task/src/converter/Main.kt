package converter

import java.math.BigInteger
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

fun main() {
    //askQuestion()
    firstLevel()
}

fun firstLevel() {
    println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
    val list = readLine()!!.split(" ")
    when (list[0]) {
        "/exit" -> return
        else -> {
            val sourceBase = list[0].toBigInteger()
            val targetBase = list[1].toBigInteger()
            secondLevel(sourceBase, targetBase)
        }
    }
}

fun secondLevel(srcB: BigInteger, tgtB: BigInteger) {
    println("Enter number in base $srcB to convert to base $tgtB (To go back type /back)")
    when (val line = readLine()!!) {
        "/back" -> firstLevel()
        else -> {
            //convert to target base
             convertNewDecimal(line, srcB, tgtB)
            secondLevel(srcB, tgtB)
        }
    }
}

fun convertNewDecimal(string: String, srcB: BigInteger, tgB: BigInteger) {
    if (!string.contains(".")) {
        val result = convertInteger(string, srcB, tgB)
        println("Conversion result: $result")
    }
    if (string.contains(".")) {
        val answer = toNewDecimal(string, srcB) // string of integer & fraction

        if (string.substringAfter(".").toBigDecimalOrNull() == BigDecimal.ZERO) {
            //val round = string.toBigDecimal().setScale(0, RoundingMode.DOWN)
            val result = convertInteger(string.substringBefore("."), srcB, tgB)
            println("Conversion result: $result.00000")
        } else {
            val intString = string.substringBefore(".") // don`t convert twice to decimal, with answer variable
            val fraction = "0." + answer.substringAfter(".")

            val calcInt = convertInteger(intString, srcB, tgB)
            println("$intString - $fraction - $calcInt $answer")
            //calculate fraction
            var calcFraction = "0."
            val scale = fraction.length
            var start = fraction.toBigDecimal() * tgB.toBigDecimal().setScale(scale,RoundingMode.CEILING) // 7+ 0.5
            var item = start.toString().substringBefore(".") // get integer part
            var add = ""
            add = if (item == "0") {
                "0"
            } else {
                convertInterim(item, srcB, tgB)
            }
            calcFraction += add
            start -= item.toBigDecimal()

            while (start > BigDecimal.ZERO) {
                start *= tgB.toBigDecimal()
                item = start.toString().substringBefore(".")
                add = if (item == "0") {
                    "0"
                } else {
                    convertInterim(item, srcB, tgB)
                }

                calcFraction += add
                start -= item.toBigDecimal()
                if (calcFraction.length > 6) {
                    break
                }
           }
            println(calcFraction)
            while (calcFraction.length < 7) {
                calcFraction += "0"
            }
            //val rounded = BigDecimal(calcFraction).setScale(5, RoundingMode.CEILING)
            val result = "$calcInt.${calcFraction.toString().substringAfter(".")}"
            println("Conversion result: $result")
        }

    }

}

fun convertInteger(string: String, srcB: BigInteger, tgtB: BigInteger): String {
    var decimal = string.toBigInteger(srcB.toInt())
    var string = ""
    while (decimal > BigInteger.ZERO) {
        val (quot, rem) = decimal.divideAndRemainder(tgtB)

        string += rem.toString(tgtB.toInt())
        decimal = quot
    }
    return string.reversed()
}

fun convertInterim(string: String, srcB: BigInteger, tgtB: BigInteger): String {
    var decimal = string.toBigInteger()
    var string = ""
    while (decimal > BigInteger.ZERO) {
        val (quot, rem) = decimal.divideAndRemainder(tgtB)

        string += rem.toString(tgtB.toInt())
        decimal = quot
    }
    return string.reversed()
}


fun askQuestion() {
    println("Do you want to convert /from decimal or /to decimal? (To quit type /exit)")
    val choice = readLine()!!

    when (choice) {
        "/from" -> {
            convertDecimal()
            askQuestion()
        }
        "/to" -> {
            //toDecimal()
            askQuestion()
        }
        "/exit" -> return
    }
}

fun toDecimal(srcNumber: String, srcBase: BigInteger): BigInteger {
    var start = BigInteger.ONE
    var total = BigInteger.ZERO
    var string = ""
    return srcNumber.toBigInteger(srcBase.toInt())

}

fun toNewDecimal(srcNumber: String, srcBase: BigInteger): String {
    var zero = BigDecimal.ZERO
    var string = "."
    val decimal = srcNumber.substringBefore(".")
    val fractional = srcNumber.substringAfter(".")

    if (!srcNumber.contains(".")) {
        val answer = srcNumber.toBigInteger(srcBase.toInt())
        return "$answer"
    }
    if (fractional.toBigDecimalOrNull() == BigDecimal.ZERO) {
        val answer = decimal.toBigInteger(srcBase.toInt())
        return "$answer.00000"
    }

    //convert integer part to decimal notation
    val intPart = decimal.toBigInteger(srcBase.toInt())

    //convert fraction to decimal notation, base 10
    var count =  1
    var sum = BigDecimal.ZERO
    for (i in fractional) {
        var add = i.digitToInt(srcBase.toInt())
        val scale = fractional.length
        sum += (BigDecimal.ONE.setScale(scale, RoundingMode.CEILING) / srcBase.pow(count).toBigDecimal()) * add.toBigDecimal()
        count += 1
    }
    val result =  intPart.toBigDecimal().setScale(5, RoundingMode.CEILING) + sum
    return result.toString()

}

fun convertDecimal() {
    println("Enter a number in decimal system:")
    val num = readLine()!!.toInt()
    println("Enter the target base:")
    val radix = readLine()!!.toInt()

    when (radix) {
        2 -> {
            val bin = Integer.toBinaryString(num)
            println("Conversion result: $bin")
        }
        8 -> {
            val oct = Integer.toOctalString(num)
            println("Conversion result: $oct")
        }
        16 -> {
            val hex = Integer.toHexString(num)
            println("Conversion result: $hex")
        }
    }
}

