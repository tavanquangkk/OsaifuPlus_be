package jp.tvq.osaifuplus.resource

import io.quarkus.security.Authenticated
import io.smallrye.common.expression.Expression
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Response
import jp.tvq.osaifuplus.domain.Transaction
import jp.tvq.osaifuplus.dto.MonthlySeparatedSummary
import jp.tvq.osaifuplus.dto.TransactionRequest
import jp.tvq.osaifuplus.dto.TransactionResponse
import jp.tvq.osaifuplus.service.transaction.TransactionService
import jp.tvq.osaifuplus.utils.ApiResponseAllTrasactions
import jp.tvq.osaifuplus.utils.ApiResponseMonthlySeparatedSummary
import jp.tvq.osaifuplus.utils.ApiResponseTrasaction
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.UUID

@ApplicationScoped
@Path("/api/v1/transactions")
@Authenticated
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
            val apiRes = ApiResponseTrasaction("success","更新に成功しました", resData)
            Response.ok(apiRes).build()

        } catch (e: Exception) {
            Response.status(Response.Status.BAD_REQUEST)
                .entity("Error: ${e.message}")
                .build()
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    fun updateTransaction(@PathParam("id") id: String,request: TransactionRequest): Response{
            val updatedTransaction = transactionService.updateTransaction(UUID.fromString(id),request)
            if(updatedTransaction == null){
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
            val resData = TransactionResponse(
                updatedTransaction.id.toString(),
                updatedTransaction.type,
                updatedTransaction.amount,
                updatedTransaction.category,
                updatedTransaction.note,
                updatedTransaction.created_at.toString()
            )

            val apiRes = ApiResponseTrasaction("success","更新に成功しました", resData)
            return Response.ok(apiRes).build()
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    fun deleteTransaction(@PathParam("id")id:String): Response{
        val isDeleted = transactionService.deleteTransaction(UUID.fromString(id))
        val apiRes = ApiResponseTrasaction("error","削除に失敗しました", null)
        if(!isDeleted){
            return Response.status(Response.Status.BAD_REQUEST).entity(apiRes).build()
        }
        return Response.ok(apiRes.copy("success","削除に成功しました")).build()

    }

    @GET
    @Path("/{id}")
    fun getTransaction(@PathParam("id")id:String): Response{
        val transaction = transactionService.getATransaction(UUID.fromString(id))
        val apiRes = ApiResponseTrasaction("error","削除に失敗しました", null)
        if(transaction == null){
            return Response.status(Response.Status.BAD_REQUEST).entity(apiRes).build()
        }
        val resData = TransactionResponse(
            transaction.id.toString(),
            transaction.type,
            transaction.amount,
            transaction.category,
            transaction.note,
            transaction.created_at.toString()
        )
        return Response.ok(apiRes.copy("success","トランザクションの取得に成功しました",resData)).build()

    }

    @GET
    fun getAllTransaction(): Response{
        val userId = UUID.fromString(jwt.subject)
        val allTrans: List<Transaction>? = transactionService.getAllTransactions(userId)
        val resData:List<TransactionResponse>? = allTrans?.map { item -> TransactionResponse(item.id.toString(),item.type,item.amount,item.category,item.note
        ,item.created_at.toString()) }
        return try {
            var apiRes = ApiResponseAllTrasactions("success","一覧のトランザクションの取得に成功しました",resData)
            Response.ok(apiRes).build()
        }
        catch (e: Exception){
            val apiRes = ApiResponseAllTrasactions("error","一覧のトランザクションの取得に失敗しました")
            Response.status(Response.Status.BAD_REQUEST).entity(apiRes).build()
        }

    }

    @GET
    @Path("/monthly-separated")
    fun monthlySeparated(): Response{
        val userId = UUID.fromString(jwt.subject)
        return  try {
            val monthlytransactions = transactionService.getMonthlySeparatedSummary(userId)
            val apiRes = ApiResponseMonthlySeparatedSummary("success","月毎の取引の取得に成功しました",monthlytransactions)
            Response.ok(apiRes).build()
        }
        catch (e: Exception){
            val apiRes = ApiResponseMonthlySeparatedSummary("error","月毎の取引の取得に失敗しました", data = null)
            Response.status(Response.Status.BAD_REQUEST).entity(apiRes).build()
        }


    }
}
