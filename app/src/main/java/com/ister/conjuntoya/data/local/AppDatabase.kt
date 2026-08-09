package com.ister.conjuntoya.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ister.conjuntoya.data.local.dao.AlicuotaDao
import com.ister.conjuntoya.data.local.dao.InvitadoDao
import com.ister.conjuntoya.data.local.entity.AlicuotaEntity
import com.ister.conjuntoya.data.local.entity.InvitadoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

@Database(
    entities = [AlicuotaEntity::class, InvitadoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alicuotaDao(): AlicuotaDao
    abstract fun invitadoDao(): InvitadoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "conjuntoya.db"
                ).fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(object : Callback() {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        super.onCreate(db)
                        scope.launch {
                            INSTANCE?.alicuotaDao()?.insertarTodas(generarAlicuotasSemilla())
                        }
                    }
                }).build().also { INSTANCE = it }
            }
        }

        private fun generarAlicuotasSemilla(): List<AlicuotaEntity> {
            val hoy = Calendar.getInstance()
            val anioActual = hoy.get(Calendar.YEAR)
            val mesActual = hoy.get(Calendar.MONTH) + 1
            return (0..2).map { offset ->
                val mes = ((mesActual - 1 - offset + 12) % 12) + 1
                val anio = if (mesActual - offset <= 0) anioActual - 1 else anioActual
                AlicuotaEntity(
                    mes = mes,
                    anio = anio,
                    monto = 45.0,
                    pagado = offset != 0
                )
            }
        }
    }
}
