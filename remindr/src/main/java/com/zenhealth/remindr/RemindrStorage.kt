import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "remindr_prefs")

object RemindrStorage {
    private const val LAST_SHOWN_PREFIX = "last_shown_"

    suspend fun getLastShown(context: Context, id: String): Long? {
        val prefs = context.dataStore.data.first()
        return prefs[longPreferencesKey(LAST_SHOWN_PREFIX + id)]
    }

    suspend fun setLastShown(context: Context, id: String, timestamp: Long) {
        context.dataStore.edit { prefs ->
            prefs[longPreferencesKey(LAST_SHOWN_PREFIX + id)] = timestamp
        }
    }
}
