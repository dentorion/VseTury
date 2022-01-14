package com.entin.room.db.di

import android.content.Context
import androidx.room.Room
import com.entin.room.db.Db
import com.entin.room.db.dao.ToursDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): Db =
        Room.databaseBuilder(
            context,
            Db::class.java,
            Db.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDAO(db: Db): ToursDAO = db.newsDao()
}