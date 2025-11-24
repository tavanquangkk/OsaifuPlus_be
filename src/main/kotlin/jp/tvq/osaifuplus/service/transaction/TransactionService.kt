package jp.tvq.osaifuplus.service.transaction

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jp.tvq.osaifuplus.domain.Transaction
import jp.tvq.osaifuplus.dto.MonthlySeparatedSummary
import jp.tvq.osaifuplus.dto.TransactionRequest
import jp.tvq.osaifuplus.repository.TransactionRepository
import java.util.UUID

@ApplicationScoped
class TransactionService {

    @Inject
    lateinit var transactionRepository: TransactionRepository

    fun createTransaction(userId: UUID, request: TransactionRequest): Transaction {

        val transaction = Transaction(
            userId = userId,
            type = request.type,
            amount = request.amount,
            category = request.category,
            note = request.note
        )

        transactionRepository.persist(transaction)
        return transaction
    }

    fun updateTransaction(id:UUID, request:TransactionRequest):Transaction?{
        var currentTransaction = transactionRepository.getTransactionById(id)
        if(currentTransaction == null){
            return null
        }
        currentTransaction?.apply {
            type = request.type
            amount = request.amount
            category = request.category
            note = request.note

        }
        transactionRepository.persist(currentTransaction)
        return currentTransaction

    }

    fun deleteTransaction(id: UUID): Boolean{
        var currentTransaction = transactionRepository.getTransactionById(id)
        if(currentTransaction == null){
            return false
        }
        transactionRepository.delete(currentTransaction)
        return true
    }

    fun getAllTransactions(userId: UUID): List<Transaction>?{
        return try {
            transactionRepository.getAllTransactionByUserId(userId)
        }catch (e: Exception){
            null
        }

    }

    fun getATransaction(id: UUID): Transaction?{
        return try {
            transactionRepository.getTransactionById(id)
        }catch (e: Exception){
            null
        }
    }

    fun getMonthlySeparatedSummary(userId: UUID): MonthlySeparatedSummary{
            val incomeList = transactionRepository.getMountIncome(userId)
            val expenseList = transactionRepository.getMountExpense(userId)
        return MonthlySeparatedSummary(
            income = incomeList,
            expense = expenseList
        )

    }

}
