package org.example.expert.domain.todo.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name="log")
class Log (
    var message: String,
    var requestIp: String,
    var requestUser: String,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @PrePersist
    fun prePersist() {
        this.createdAt = LocalDateTime.now()
    }
    protected constructor() :this("","","")
}