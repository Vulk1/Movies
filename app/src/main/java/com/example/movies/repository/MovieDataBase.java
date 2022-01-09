package com.example.movies.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDataBase extends RoomDatabase {

    private static MovieDataBase instance;

    public abstract MovieDao movieDao();

    public static synchronized MovieDataBase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDataBase.class, "movie_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }

        return instance;
    }

    private static  RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateMovieDbAsyncTask(instance).execute();
        }
    };

    public static class populateMovieDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieDao movieDao;

        populateMovieDbAsyncTask(MovieDataBase dataBase) {
            this.movieDao = dataBase.movieDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieDao.insert(new Movie("tt3896198", "Guardians of the Galaxy Vol. 2", "https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg"));
            movieDao.insert(new Movie("tt0078788", "Apocalypse Now", "https://m.media-amazon.com/images/M/MV5BMDdhODg0MjYtYzBiOS00ZmI5LWEwZGYtZDEyNDU4MmQyNzFkXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg"));
            movieDao.insert(new Movie("tt1727776", "Scouts Guide to the Zombie Apocalypse", "https://m.media-amazon.com/images/M/MV5BMTY4NjczNjE4OV5BMl5BanBnXkFtZTgwODk0MjQ5NjE@._V1_SX300.jpg"));
            movieDao.insert(new Movie("tt0337103", "Crimson Rivers 2: Angels of the Apocalypse", "https://m.media-amazon.com/images/M/MV5BNzc5OTA4NDgtMjAzZi00MjFhLTk4OWQtNDRkNmNjZDNlN2FjXkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1_SX300.jpg"));

            return null;
        }
    }



}
