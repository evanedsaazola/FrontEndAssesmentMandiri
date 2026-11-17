package com.example.frontendassesmentmandiri.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.frontendassesmentmandiri.data.api.TMDBApiService
import com.example.frontendassesmentmandiri.data.model.Movie

import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val apiService: TMDBApiService,
    private val apiKey: String,
    private val genreId: Int
): PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getDiscoverMovies(
                apiKey = apiKey,
                genreId = genreId,
                page = page
            )
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty() || page >= response.totalPages) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}