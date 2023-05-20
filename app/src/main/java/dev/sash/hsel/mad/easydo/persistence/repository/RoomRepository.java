package dev.sash.hsel.mad.easydo.persistence.repository;

import android.content.Context;
import android.util.Log;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

import dev.sash.hsel.mad.easydo.model.Todo;
import dev.sash.hsel.mad.easydo.utils.DataConverter;

public class RoomRepository implements LocalRepository {

    public static final String TAG = RoomRepository.class.getSimpleName();

    private Database database;

    public RoomRepository(Context context) {
        this.database = Room.databaseBuilder(context, Database.class, Database.NAME).fallbackToDestructiveMigration().build();
        Log.i(TAG, "RoomDriver connected!");
    }

    @Override public long create(Todo todo) {
        return database.access().insert(todo);
    }

    @Override public Todo read(long id) {
        return database.access().select(id);
    }

    @Override public List<Todo> read() {
        return database.access().select();
    }

    @Override public boolean update(Todo todo) {
        return database.access().update(todo) > 0;
    }

    @Override public boolean delete(Todo todo) {
        return database.access().delete(todo) > 0;
    }

    @Override public boolean delete() {
        return database.access().delete() > 0;
    }

    public int count() {
        return database.access().count();
    }

    @androidx.room.Database(entities = {Todo.class}, version = 1, exportSchema = false)
    @TypeConverters(DataConverter.class)
    public abstract static class Database extends RoomDatabase {
        public static final String NAME = "todo_db";
        public static final String TABLE_NAME_TODO = "todo";

        public abstract Dao access();
    }

    @androidx.room.Dao
    public interface Dao {
        @Insert long insert(Todo todo);

        @Query("SELECT * FROM " + Database.TABLE_NAME_TODO + " WHERE id = :id") Todo select(long id);

        @Query("SELECT * FROM " + Database.TABLE_NAME_TODO) List<Todo> select();

        @Update int update(Todo todo);

        @Delete int delete(Todo todo);

        @Query("DELETE FROM " + Database.TABLE_NAME_TODO) int delete();

        @Query("SELECT COUNT(*) FROM " + Database.TABLE_NAME_TODO) int count();
    }

}
