package com.talk2us.ui.chat.dataSource

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.talk2us.models.Message
import com.talk2us.ui.chat.dataSource.ChatDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Time
import java.sql.Timestamp

@Database(entities = [Message::class], version = 2)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun wordDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: ChatDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ChatDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatDatabase::class.java,
                    "messages"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wordDao())
                    }
                }
            }
        }

        fun populateDatabase(wordDao: ChatDao) {

            val word = Message("Hello", "YO",false,false,"Client")
            wordDao.sendMessage(word)
            Log.d("hello",wordDao.getAllMessages().value.toString());//Utils.toast("Message Sent")
        }
    }

}
