package org.book.app.study.model.properties

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

/**
 * Rsa key properties
 *
 * @constructor Create empty Rsa key properties
 */
class RsaKeyProperties {
    lateinit var publicKey: RSAPublicKey
    lateinit var privateKey: RSAPrivateKey
}