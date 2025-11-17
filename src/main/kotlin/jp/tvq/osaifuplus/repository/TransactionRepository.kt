package jp.tvq.osaifuplus.repository

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jp.tvq.osaifuplus.domain.Transaction

@ApplicationScoped
class TransactionRepository: PanacheRepository<Transaction> {

}