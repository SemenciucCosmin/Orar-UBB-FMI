package com.ubb.fmi.orar.data.news.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.NewsDao
import com.ubb.fmi.orar.data.database.model.ArticleEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.network.service.NewsApi
import com.ubb.fmi.orar.data.news.model.Article
import com.ubb.fmi.orar.data.news.model.ArticleType
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlNewsParser
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.ByteString.Companion.encodeUtf8

/**
 * Data source for managing room related information
 */
class NewsDataSourceImpl(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao,
    private val logger: Logger,
) : NewsDataSource {

    override suspend fun getNewsFromCache(): Flow<List<Article>> {
        return newsDao.getAllAsFlow().map { entities ->
            entities.map(::mapEntityToArticle)
        }
    }

    override suspend fun saveNewsInCache(articles: List<Article>) {
        val entities = articles.map(::mapArticleToEntity)
        newsDao.insertAll(entities)
    }

    override suspend fun getNewsFromApi(): Resource<List<Article>> {
        return coroutineScope {
            val studentNewsAsync = async { newsApi.getStudentNewsHtml() }
            val teacherNewsAsync = async { newsApi.getTeacherNewsHtml() }

            val studentNewsResource = studentNewsAsync.await()
            val teacherNewsResource = teacherNewsAsync.await()

            logger.d(TAG, "getNewsFromApi student: $studentNewsResource")
            logger.d(TAG, "getNewsFromApi teacher: $teacherNewsResource")

            val studentNewsHtml = studentNewsResource.payload
            val teacherNewsHtml = teacherNewsResource.payload

            val studentHtmlArticles = studentNewsHtml?.let(HtmlNewsParser::extractNews)
            val teacherHtmlArticles = teacherNewsHtml?.let(HtmlNewsParser::extractNews)

            val studentArticles = studentHtmlArticles?.map { htmlArticle ->
                val id = listOf(
                    htmlArticle.title,
                    htmlArticle.text,
                    htmlArticle.date,
                    htmlArticle.imageUrl,
                    htmlArticle.url,
                ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

                Article(
                    id = id,
                    title = htmlArticle.title,
                    text = htmlArticle.text,
                    date = htmlArticle.date,
                    url = htmlArticle.url,
                    type = ArticleType.STUDENT,
                    imageUrl = htmlArticle.imageUrl,
                )
            } ?: emptyList()

            val teacherArticles = teacherHtmlArticles?.map { htmlArticle ->
                val id = listOf(
                    htmlArticle.title,
                    htmlArticle.text,
                    htmlArticle.date,
                    htmlArticle.imageUrl,
                    htmlArticle.url,
                ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

                Article(
                    id = id,
                    title = htmlArticle.title,
                    text = htmlArticle.text,
                    date = htmlArticle.date,
                    url = htmlArticle.url,
                    type = ArticleType.TEACHER,
                    imageUrl = htmlArticle.imageUrl,
                )
            } ?: emptyList()

            val articles = studentArticles + teacherArticles
            val networkStatus = listOf(
                studentNewsResource.status,
                teacherNewsResource.status
            ).firstOrNull { it.isSuccess() }

            val status = when {
                articles.isEmpty() -> Status.Empty
                networkStatus == null -> Status.NotFoundError
                else -> networkStatus
            }

            logger.d(TAG, "getNewsFromApi status: $status")
            logger.d(TAG, "getNewsFromApi articles: $articles")
            return@coroutineScope Resource(articles, status)
        }
    }

    override suspend fun invalidate() {
        newsDao.deleteAll()
    }

    private fun mapEntityToArticle(entity: ArticleEntity): Article {
        return Article(
            id = entity.id,
            title = entity.title,
            text = entity.text,
            date = entity.date,
            url = entity.url,
            type = ArticleType.getById(entity.typeId),
            imageUrl = entity.imageUrl
        )
    }

    private fun mapArticleToEntity(article: Article): ArticleEntity {
        return ArticleEntity(
            id = article.id,
            title = article.title,
            text = article.text,
            date = article.date,
            url = article.url,
            typeId = article.type.id,
            imageUrl = article.imageUrl
        )
    }

    companion object {
        private const val TAG = "NewsDataSource"
    }
}
