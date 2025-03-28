package org.example.expert.domain.todo.entity

import jakarta.persistence.*
import org.example.expert.domain.comment.entity.Comment
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.manager.entity.Manager
import org.example.expert.domain.user.entity.User

@Entity
@Table(name = "todos")
class Todo (
    var title: String,
    var contents: String,
    var weather: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User
) : Timestamped() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.REMOVE])
    var comments: MutableList<Comment> = mutableListOf()

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.PERSIST])
    var managers: MutableList<Manager> = mutableListOf()

    constructor() : this("","","",User())
}