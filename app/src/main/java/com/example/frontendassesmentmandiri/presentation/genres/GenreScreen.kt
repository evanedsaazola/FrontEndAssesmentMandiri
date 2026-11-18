    package com.example.frontendassesmentmandiri.presentation.genres

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.Button
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.HorizontalDivider
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TopAppBar
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.unit.dp
    import androidx.hilt.navigation.compose.hiltViewModel
    import androidx.lifecycle.compose.collectAsStateWithLifecycle
    import com.example.frontendassesmentmandiri.R
    import com.example.frontendassesmentmandiri.domain.model.GenrePayload

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GenreScreen(
        viewModel: GenreViewModel = hiltViewModel(),
        onGenreClick: (Int) -> Unit
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Scaffold(
            topBar = { TopAppBar(title = { Text(text = stringResource(R.string.genre_screen_title)) }) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (val state = uiState) {
                    is GenreUiState.Loading -> CircularProgressIndicator()

                    is GenreUiState.Success -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.genres) { genreItem ->
                                GenreItem(
                                    genre = genreItem,
                                    onClick = { onGenreClick(genreItem.id) }
                                )
                                HorizontalDivider()
                            }
                        }
                    }

                    is GenreUiState.Error -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                            Button(onClick = { viewModel.fetchGenres() }) {
                                Text(text = stringResource(R.string.retry))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun GenreItem(genre: GenrePayload, onClick: () -> Unit) {
        Text(
            text = genre.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
        )
    }