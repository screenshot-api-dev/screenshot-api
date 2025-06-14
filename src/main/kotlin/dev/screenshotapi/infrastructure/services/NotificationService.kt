package dev.screenshotapi.infrastructure.services

import dev.screenshotapi.core.domain.entities.ScreenshotJob
import dev.screenshotapi.infrastructure.services.models.WebhookPayload
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory


class NotificationService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
    }

    suspend fun sendWebhook(webhookUrl: String, job: ScreenshotJob) {
        try {
            logger.info("Webhook request initiated: jobId={}, webhookUrl={}, status={}", 
                job.id, webhookUrl, job.status.name)

            val payload = WebhookPayload(
                jobId = job.id,
                status = job.status.name.lowercase(),
                url = job.request.url,
                resultUrl = job.resultUrl,
                errorMessage = job.errorMessage,
                processingTimeMs = job.processingTimeMs,
                completedAt = job.completedAt?.toString()
            )

            val response: HttpResponse = httpClient.post(webhookUrl) {
                contentType(ContentType.Application.Json)
                setBody(payload)
            }

            if (response.status.isSuccess()) {
                logger.info("Webhook delivered successfully: jobId={}, webhookUrl={}, statusCode={}", 
                    job.id, webhookUrl, response.status.value)
            } else {
                logger.warn("Webhook delivery failed: jobId={}, webhookUrl={}, statusCode={}, statusText={}", 
                    job.id, webhookUrl, response.status.value, response.status.description)
            }

        } catch (e: Exception) {
            logger.error("Webhook delivery exception: jobId={}, webhookUrl={}, error={}", 
                job.id, webhookUrl, e.message, e)
        }
    }

    suspend fun sendEmail(to: String, subject: String, body: String) {
        try {
            logger.info("Sending email to $to: $subject")
            logger.warn("Email service not implemented - email would be sent to $to")
        } catch (e: Exception) {
            logger.error("Failed to send email to $to", e)
        }
    }

    fun close() {
        httpClient.close()
    }
}
