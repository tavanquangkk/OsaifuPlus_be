package jp.tvq.osaifuplus.service.transaction

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jp.tvq.osaifuplus.domain.Transaction
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
}
