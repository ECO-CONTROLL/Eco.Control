package com.ecocontroll.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//CONTATO COMO O BANCO
@Serializable
data class SupabaseUserInsert(
    @SerialName("nome_completo")
    val nomeCompleto: String,

    @SerialName("nome_usuario")
    val nomeUsuario: String,

    val email: String,

    @SerialName("senha_hash")
    val senhaHash: String
)

@Serializable
data class SupabaseUserResponse(
    val id: String,

    @SerialName("nome_completo")
    val nomeCompleto: String,

    @SerialName("nome_usuario")
    val nomeUsuario: String,

    val email: String,

    @SerialName("senha_hash")
    val senhaHash: String
)
