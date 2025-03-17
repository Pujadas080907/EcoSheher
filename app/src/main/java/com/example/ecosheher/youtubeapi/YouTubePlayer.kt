import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayer(videoId: String, lifecycleOwner: LifecycleOwner) {
    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this@apply)  // Attach lifecycle observer
                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0f)  // Load video at 0 seconds
                    }
                })
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
