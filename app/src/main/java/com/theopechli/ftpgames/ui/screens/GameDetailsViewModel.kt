package com.theopechli.ftpgames.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.theopechli.ftpgames.FtPGamesApplication
import com.theopechli.ftpgames.data.FtPGamesRepository
import com.theopechli.ftpgames.model.GameDao
import com.theopechli.ftpgames.model.GameDetails
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GameDetailsUiState {
    data class Success(val game: GameDetails) : GameDetailsUiState
    object Error : GameDetailsUiState
    object Loading : GameDetailsUiState
}

class GameDetailsViewModel(
    id: Long,
    private val ftpGamesRepository: FtPGamesRepository,
    private val gameDao: GameDao
) : ViewModel() {
    var gameDetailsUiState: GameDetailsUiState by mutableStateOf(GameDetailsUiState.Loading)
        private set

    init {
        getGameDetails(id, gameDao)
    }

    private fun getGameDetails(id: Long, gameDao: GameDao) {
        viewModelScope.launch {
            gameDetailsUiState = GameDetailsUiState.Loading
            gameDetailsUiState = try {
                GameDetailsUiState.Success(ftpGamesRepository.getGameById(id))
            } catch (e: IOException) {
                GameDetailsUiState.Error
            } catch (e: HttpException) {
                GameDetailsUiState.Error
            }
        }
    }

    class Factory(private val id: Long) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as FtPGamesApplication
            val ftpGamesRepository = application.container.ftpGamesRepository
            val gameDatabase = application.container.gameDatabase
            val gameDao = gameDatabase.gameDao()

            return GameDetailsViewModel(
                ftpGamesRepository = ftpGamesRepository,
                gameDao = gameDao,
                id = id
            ) as T
        }
    }
}