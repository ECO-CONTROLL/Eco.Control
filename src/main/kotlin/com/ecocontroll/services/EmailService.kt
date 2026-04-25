package com.ecocontroll.services

import com.ecocontroll.models.EmailRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend fun enviarEmailReset(
    client: HttpClient,
    apiKey: String,
    destino: String,
    token: String
) {
    client.post("https://api.resend.com/emails") {
        header("Authorization", "Bearer $apiKey")
        contentType(ContentType.Application.Json)

        setBody(
            EmailRequest(
                from = "EcoControll <onboarding@resend.dev>",
                to = listOf(destino),
                subject = "Redefinição de senha - EcoControll",
                html = """
    <div style="font-family: Arial, sans-serif; background-color: #1F3D49; padding: 30px;">
        <div style="max-width: 520px; margin: auto; text-align: center;">

            <div style="margin-bottom: 25px;">
                <div style="font-size: 58px;"><img 
    src="https://koljpnfxgclmnyqkirgb.supabase.co/storage/v1/object/public/logo/imagem_2025-11-22_001618890-removebg-preview%202.svg"
    width="120"
/></div>

                <h1 style="color: #3DB54A; margin: 10px 0 5px; font-size: 32px;">
                    Eco Control
                </h1>

                <p style="color: #ffffff; font-size: 14px; margin: 0;">
                    Gestão inteligente de sua casa sustentável
                </p>
            </div>

            <div style="
                background-color: #ffffff;
                border-radius: 22px;
                padding: 32px 24px;
                box-shadow: 0 8px 20px rgba(0,0,0,0.15);
            ">
                <h2 style="color: #1F3D49; margin-bottom: 10px;">
                    Redefinição de senha
                </h2>

                <p style="color: #444; font-size: 15px;">
                    Recebemos uma solicitação para redefinir sua senha.
                </p>

                <p style="color: #444; font-size: 15px;">
                    Use o código abaixo no aplicativo:
                </p>

                <div style="margin: 28px 0;">
                    <span style="
                        display: inline-block;
                        background-color: #3DB54A;
                        color: #ffffff;
                        font-size: 30px;
                        font-weight: bold;
                        padding: 16px 30px;
                        border-radius: 10px;
                        letter-spacing: 5px;
                    ">
                        $token
                    </span>
                </div>

                <p style="color: #777; font-size: 13px;">
                    Este código expira em 3 minutos.
                </p>

                <p style="color: #999; font-size: 12px; margin-top: 24px;">
                    Se você não solicitou essa alteração, ignore este email.
                </p>
            </div>

            <p style="color: #d8e6ea; font-size: 12px; margin-top: 20px;">
                Eco Control © 2026
            </p>
        </div>
    </div>
""".trimIndent()
            )
        )
    }
}