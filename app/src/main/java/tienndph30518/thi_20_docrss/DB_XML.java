package tienndph30518.thi_20_docrss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DB_XML extends SQLiteOpenHelper {
    static final String NAME = "name";
    static final int VERSION = 1;

    public DB_XML(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table doc (idxml integer primary key autoincrement ," +
                "title text not null ," +
                "link text not null," +
                "noidung text not null," +
                "thoigian text not null," +
                "anh text not null," +
                "trangthai integer )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String delete = "drop table if exists doc";
        db.execSQL(delete);

    }
//
    public ArrayList<Item> getAllDS() {
        SQLiteDatabase db = getReadableDatabase();
        String select = "select * from doc";
        ArrayList<Item> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                Item item = new Item();
                item.setId(cursor.getInt(0));
                item.setTitle(cursor.getString(1));
                item.setLink(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setPubData(cursor.getString(4));
                item.setImgAvata(cursor.getString(5));
                item.setTrangThai(cursor.getInt(6));
                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;
    }



    public long delete() {
        SQLiteDatabase db = getReadableDatabase();
        return db.delete("doc", null, null);
    }

    public long updata(int id, int trangthai) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangthai", trangthai);
        return db.update("doc", values, "idxml=?", new String[]{String.valueOf(id)});
    }

    public long inset(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("link", item.getLink());
        values.put("noidung", item.getDescription());
        values.put("thoigian", item.getPubData());
        values.put("anh", item.getImgAvata());
        values.put("trangthai", item.getTrangThai());
        return db.  insert("doc",null,values);
    }
}
