package org.acme.domain

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import java.util.UUID

class User : PanacheEntity() {
    lateinit var user_id : UUID
    lateinit var email: String
    lateinit var username: String
    lateinit var password: String
}