package com.example.frontendassesmentmandiri.presentation.detail

import android.content.Intent
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import com.example.frontendassesmentmandiri.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    viewModel: DetailViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val reviewFlow = remember(viewModel, movieId, lifecycleOwner) {
        viewModel.fetchMovieReviews(movieId).flowWithLifecycle(lifecycleOwner.lifecycle)
    }
    val reviewDatas = reviewFlow.collectAsLazyPagingItems()

    var hasVideoError by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.movie_detail_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is DetailsUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is DetailsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = state.details.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )
                                Text(
                                    text = "Release Date: ${state.details.releaseDate}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(
                                    modifier = Modifier.height(16.dp)
                                )
                                Text(
                                    text = state.details.overview,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(
                                    modifier = Modifier.height(24.dp)
                                )

                                state.details.youtubeTrailer?.let { videoId ->
                                    if (hasVideoError) {
                                        Button(
                                            onClick = {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    "https://www.youtube.com/watch?v=$videoId".toUri()
                                                )
                                                context.startActivity(intent)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp)
                                        ) {
                                            Text(text = stringResource(R.string.watch_on_yt))
                                        }
                                    } else {
                                        AndroidView(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(250.dp),
                                            factory = { context ->
                                                YouTubePlayerView(context).apply {
                                                    lifecycleOwner.lifecycle.addObserver(this)

                                                    addYouTubePlayerListener(
                                                        object : AbstractYouTubePlayerListener() {
                                                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                                                youTubePlayer.cueVideo(videoId, 0f)
                                                            }

                                                            override fun onError(
                                                                youTubePlayer: YouTubePlayer,
                                                                error: PlayerConstants.PlayerError
                                                            ) {
                                                                hasVideoError = true
                                                            }
                                                        })
                                                }
                                            }
                                        )
                                    }
                                } ?: Text(
                                    text = stringResource(R.string.no_trailer_available),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(
                                    modifier = Modifier.height(24.dp)
                                )
                                Text(
                                    text = stringResource(R.string.reviews),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }

                        if (reviewDatas.itemCount == 0 && reviewDatas.loadState.refresh is LoadState.NotLoading) {
                            item {
                                Text(
                                    text = stringResource(R.string.no_review_available),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        items(reviewDatas.itemCount) { index ->
                            reviewDatas[index]?.let { review ->
                                Column(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp)
                                ) {
                                    Text(
                                        text = review.author,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(
                                        modifier = Modifier.height(4.dp)
                                    )
                                    Text(
                                        text = review.content,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(top = 16.dp)
                                    )
                                }
                            }
                        }

                        reviewDatas.apply {
                            when {
                                loadState.append is LoadState.Error -> {
                                    item {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = stringResource(R.string.failed_to_load),
                                                color = MaterialTheme.colorScheme.error
                                            )
                                            Button(onClick = { retry() }) {
                                                Text(text = stringResource(R.string.retry))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                is DetailsUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}