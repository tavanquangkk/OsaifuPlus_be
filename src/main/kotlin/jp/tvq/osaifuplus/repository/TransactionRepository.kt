package jp.tvq.osaifuplus.repository

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jp.tvq.osaifuplus.domain.Transaction
import jp.tvq.osaifuplus.dto.MonthlyAmount
import java.util.UUID

@ApplicationScoped
class TransactionRepository: PanacheRepository<Transaction> {

    @Inject
    lateinit var em: EntityManager

    fun getTransactionById(id: UUID): Transaction?{
        return find("id",id).firstResult()
    }

    fun getAllTransactionByUserId(userId: UUID): List<Transaction>?{
        return find("userId",userId).list()
    }

    fun getMountIncome(userId: UUID) : List<MonthlyAmount> {
        val sql = """
            SELECT to_char(created_at,'YYYY-MM') as month,
            SUM (amount) as total
            FROM transactions
            WHERE type='INCOME'
            AND userid = :userId
            GROUP BY 1
            ORDER BY 1;
        """.trimIndent()

        return em.createNativeQuery(sql).setParameter("userId",userId)
            .resultList.map {
                val row = it as Array<*>
                MonthlyAmount(
                    month = row[0] as String,
                    amount = (row[1] as Number).toLong()
                )
            }

    }

    fun getMountExpense(userId: UUID) : List<MonthlyAmount> {
        val sql = """
            SELECT to_char(created_at,'YYYY-MM') as month,
                    SUM (amount) as total
            FROM transactions
            WHERE type='EXPENSE'
            AND userid = :userId
            GROUP BY 1
            ORDER BY 1;
        """.trimIndent()

        return em.createNativeQuery(sql).setParameter("userId",userId).resultList.map {
            var row = it as Array<*>
            MonthlyAmount(
                month = row[0] as String,
                amount = (row[1] as Number).toLong()
            )
        }



    }



}