package com.example.win.easy

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.win.easy.dao.SongDao
import com.example.win.easy.dao.SongListDao
import com.example.win.easy.dao.SongXSongListDao
import com.example.win.easy.dao.OurDatabase
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class DatabaseRule: TestRule {

    private val db=Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), OurDatabase::class.java).build()

    val songDao: SongDao
        get() = db.songDao()

    val songListDao: SongListDao
        get() = db.songListDao()

    val songXSongListDao: SongXSongListDao
        get() = db.songXSongListDao()

    override fun apply(base: Statement?, description: Description?): Statement =
            object : Statement() {
                override fun evaluate() {
                    base!!.evaluate()
                    db.close()
                }
            }
}