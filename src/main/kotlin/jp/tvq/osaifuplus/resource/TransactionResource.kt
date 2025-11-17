package jp.tvq.osaifuplus.resource

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Response
import jp.tvq.osaifuplus.dto.TransactionRequest
import jp.tvq.osaifuplus.dto.TransactionResponse
import jp.tvq.osaifuplus.service.transaction.TransactionService
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.UUID

@ApplicationScoped
@Path("/api/v1/transactions")
class TransactionResource {

    @Inject
    lateinit var transactionService: TransactionService

    @Inject
    lateinit var jwt: JsonWebToken

    @POST
    @Transactional
    fun createTransaction(request: TransactionRequest): Response {
        return try {

            // TODO: Get real userId from JWT
            val userId = UUID.fromString(jwt.subject)

            val newTransaction = transactionService.createTransaction(userId, request)

            val resData = TransactionResponse(
                id = newTransaction.id.toString(),
                type = newTransaction.type,
                amount = newTransaction.amount,
                category = newTransaction.category,
                note = newTransaction.note,
                created_at = newTransaction.created_at.toString()
            )

            Response.ok(resData).build()

        } catch (e: Exception) {
            Response.status(Response.Status.BAD_REQUEST)
                .entity("Error: ${e.message}")
                .build()
        }
    }
}
