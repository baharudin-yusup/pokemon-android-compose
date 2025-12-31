package dev.baharudin.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.baharudin.common.util.Constants
import dev.baharudin.data.local.converter.Converters
import dev.baharudin.data.local.database.dao.PokemonBackpackDao
import dev.baharudin.data.local.database.dao.PokemonDao
import dev.baharudin.data.local.database.dao.PokemonUserDataDao
import dev.baharudin.data.local.entity.PokemonBackpackEntity
import dev.baharudin.data.local.entity.PokemonEntity
import dev.baharudin.data.local.entity.PokemonRemoteKeysEntity
import dev.baharudin.data.local.entity.PokemonUserDataEntity
import dev.baharudin.data.local.entity.RemoteKeysDao

@Database(
    entities = [
        PokemonEntity::class, 
        PokemonRemoteKeysEntity::class,
        PokemonUserDataEntity::class,
        PokemonBackpackEntity::class
    ],
    version = Constants.Database.VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun pokemonUserDataDao(): PokemonUserDataDao
    abstract fun pokemonBackpackDao(): PokemonBackpackDao
}

