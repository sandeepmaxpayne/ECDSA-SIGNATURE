package com.sandeep.ecdasignature

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigInteger
import java.security.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "ECDSA_Signature"
        supportActionBar?.elevation = "1".toFloat()
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.purple))

        btnSig.setOnClickListener {
            getSignature()
        }


    }

    fun getSignature(){

        val keyGen = KeyPairGenerator.getInstance("EC")
        val random = SecureRandom.getInstance("SHA1PRNG")

        keyGen.initialize(256, random)

        // keyGen.initialize(ECGenParameterSpec("secp256r1"), SecureRandom())

        val pair = keyGen.generateKeyPair()
        val priv = pair.private
        val pub = pair.public

//        println("private${priv}")
//        println("public$pub")

        /*
         * Create a Signature object and initialize it with the private key
         */

        val dsa = Signature.getInstance("SHA1withECDSA")
        // val dsa = Signature.getInstance("SHA256withECDSA")
        println()
        dsa.initSign(priv)


        val str = name.text.toString()
        privKey.text = "Private Key:\n ${getSHA(str)}"

        val strByte = str.toByteArray(charset("UTF-8"))
        dsa.update(strByte)

        /*
         * Now that all the data to be signed has been read in, generate a
         * signature for it
         */

        val realSig = dsa.sign()
        val sig = "Signature: " + BigInteger(1, realSig).toString(16)
        signature.text = sig
//        Log.d("Sig", "Sign: $sig")
//        println(sig)
//        println(sig.length)

    }


    fun getSHA(input: String): String? {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(input.toByteArray())
            val num = BigInteger(1, messageDigest)
            var hashText = num.toString(16)
            while (hashText.length < 32) {
                hashText = "0$hashText"
            }
            hashText
        } catch (ex: NoSuchAlgorithmException) {
            println("Exception Occured: ${ex.message}")
            null
        }
    }
}
