package org.babyfish.jimmer.spring.repository

import org.babyfish.jimmer.Input
import org.babyfish.jimmer.Dto
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.query.SortDsl
import org.babyfish.jimmer.sql.kt.ast.query.KConfigurableRootQuery
import org.springframework.core.annotation.AliasFor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

@NoRepositoryBean
interface KRepository<E: Any, ID: Any> : PagingAndSortingRepository<E, ID> {

    val sql: KSqlClient

    fun pager(pageIndex: Int, pageSize: Int): Pager

    fun pager(pageable: Pageable): Pager

    fun findNullable(id: ID, fetcher: Fetcher<E>? = null): E?

    fun <S: Dto<E>> findStaticNullable(staticType: KClass<S>, id: ID): S?

    override fun findById(id: ID): Optional<E> =
        Optional.ofNullable(findNullable(id))

    fun findById(id: ID, fetcher: Fetcher<E>): Optional<E> =
        Optional.ofNullable(findNullable(id, fetcher))

    fun <S: Dto<E>> findStaticById(staticType: KClass<S>, id: ID): Optional<S> =
        Optional.ofNullable(findStaticNullable(staticType, id))

    @AliasFor("findAllById")
    fun findByIds(ids: Iterable<ID>, fetcher: Fetcher<E>? = null): List<E>

    @AliasFor("findByIds")
    override fun findAllById(ids: Iterable<ID>): List<E> = 
        findByIds(ids)

    fun <S: Dto<E>> findStaticByIds(staticType: KClass<S>, ids: Iterable<ID>): List<S>

    fun findMapByIds(ids: Iterable<ID>, fetcher: Fetcher<E>? = null): Map<ID, E>

    fun <S: Dto<E>> findStaticMapByIds(staticType: KClass<S>, ids: Iterable<ID>): Map<ID, S>

    override fun findAll(): List<E> =
        findAll(null, null)

    fun findAll(fetcher: Fetcher<E>? = null, block: (SortDsl<E>.() -> Unit)? = null): List<E>

    fun <S: Dto<E>> findAllStatic(staticType: KClass<S>, block: (SortDsl<E>.() -> Unit)? = null): List<S>

    override fun findAll(sort: Sort): List<E> =
        findAll(null, sort)

    fun findAll(fetcher: Fetcher<E>? = null, sort: Sort): List<E>

    fun <S: Dto<E>> findAllStatic(staticType: KClass<S>, sort: Sort): List<S>

    fun findAll(
        pageIndex: Int,
        pageSize: Int,
        fetcher: Fetcher<E>? = null,
        block: (SortDsl<E>.() -> Unit)? = null
    ): Page<E>

    fun <S: Dto<E>> findAllStatic(
        staticType: KClass<S>,
        pageIndex: Int,
        pageSize: Int,
        block: (SortDsl<E>.() -> Unit)? = null
    ): Page<S>

    fun findAll(
        pageIndex: Int,
        pageSize: Int,
        fetcher: Fetcher<E>? = null,
        sort: Sort
    ): Page<E>

    fun <S: Dto<E>> findAllStatic(
        staticType: KClass<S>,
        pageIndex: Int,
        pageSize: Int,
        sort: Sort
    ): Page<S>

    override fun findAll(pageable: Pageable): Page<E>

    fun findAll(pageable: Pageable, fetcher: Fetcher<E>? = null): Page<E>

    fun <S: Dto<E>> findAllStatic(staticType: KClass<S>, pageable: Pageable): Page<S>

    override fun existsById(id: ID): Boolean =
        findNullable(id) != null
    
    override fun count(): Long

    fun insert(input: Input<E>): E =
        insert(input.toEntity())

    fun insert(entity: E): E

    fun update(input: Input<E>): E =
        update(input.toEntity())

    fun update(entity: E): E

    override fun <S: E> save(entity: S): S

    override fun <S : E> saveAll(entities: Iterable<S>): List<S>

    fun save(input: Input<E>): E

    override fun delete(entity: E)

    override fun deleteById(id: ID)

    @AliasFor("deleteAllById")
    fun deleteByIds(ids: Iterable<ID>)

    @AliasFor("deleteByIds")
    override fun deleteAllById(ids: Iterable<ID>) {
        deleteByIds(ids)
    }

    override fun deleteAll()

    override fun deleteAll(entities: Iterable<E>)

    val graphql: GraphQl<E>

    interface Pager {

        fun <T> execute(query: KConfigurableRootQuery<*, T>): Page<T>
    }

    interface GraphQl<E> {

        fun <X: Any> load(prop: KProperty1<E, X?>, sources: Collection<E>): Map<E, X>
    }
}