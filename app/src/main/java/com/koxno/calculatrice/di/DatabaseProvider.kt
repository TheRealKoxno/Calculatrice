package com.koxno.calculatrice.di

import android.content.Context
import com.koxno.calculatrice.data.db.MainDatabase

object DatabaseProvider {

    private var db: MainDatabase? = null

    fun get(context: Context): MainDatabase {
        return db ?: MainDatabase.create(context).also { db = it }
    }
}

