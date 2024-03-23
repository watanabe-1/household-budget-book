package org.book.app.study.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * ユーザ情報サービス.
 */
@Service
class AppUserDetailsService(private val accountService: AccountService) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userId: String): UserDetails {
        val account = accountService.findOne(userId) ?: throw UsernameNotFoundException("User not found")

        return AppUserDetails(account)
    }
}
