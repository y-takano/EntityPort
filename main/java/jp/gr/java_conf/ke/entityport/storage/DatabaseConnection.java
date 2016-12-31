package jp.gr.java_conf.ke.entityport.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseConnection extends SQLiteOpenHelper implements StorageConnection {

	// version: 1.0.0
	private static final int VERSION = 100;

	// テーブル名のプリコンパイル指定不能なのでStringBuilderで結合する
	// [1]: テーブル名
	private static final String SQL_CHECK_0 = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='";
	private static final String SQL_CHECK_1 = "';";

	// テーブル名のプリコンパイル指定不能なのでStringBuilderで結合する
	// [1]: テーブル名
	private static final String SQL_CREATE_0 = "CREATE TABLE ";
	private static final String SQL_CREATE_1 = "(name text not null, version text not null, primaryKey text primary key, data text not null, persistent integer);";

	// テーブル名のプリコンパイル指定不能なのでStringBuilderで結合する
	// 使用頻度が高そうなので後々高速化を実装してもよい
	// [1]: テーブル名, [2]: クラス名, [3]: クラスVer, [4]: 主キー, [5]: データ, [6]: 永続化フラグ
	private static final String SQL_INSERT_0 = "INSERT INTO ";
	private static final String SQL_INSERT_1 = "(name,version,primaryKey,data,persistent) values ('";
	private static final String SQL_INSERT_2 = "', '";
	private static final String SQL_INSERT_3 = "', '";
	private static final String SQL_INSERT_4 = "', '";
	private static final String SQL_INSERT_5 = "', ";
	private static final String SQL_INSERT_6 = ");";

	// テーブル名のプリコンパイル指定不能なのでStringBuilderで結合する
	// 使用頻度が高そうなので後々高速化を実装してもよい
	// [1]: テーブル名, [2]: クラス名, [3]: クラスVer, [4]: データ, [5]: 永続化フラグ, [6]: 主キー
	private static final String SQL_UPDATE_0 = "UPDATE ";
	private static final String SQL_UPDATE_1 = " SET name = '";
	private static final String SQL_UPDATE_2 = "', version = '";
	private static final String SQL_UPDATE_3 = "', data = '";
	private static final String SQL_UPDATE_4 = "', persistent = ";
	private static final String SQL_UPDATE_5 = " WHERE primaryKey = '";
	private static final String SQL_UPDATE_6 = "';";

	// テーブル名のプリコンパイル指定不能なのでStringBuilderで結合する
	// [1]: テーブル名, [2]: カラム名(主キー), [3]: 主キー
	private static final String SQL_DELETE_0 = "DELETE FROM ";
	private static final String SQL_DELETE_1 = "WHERE ";
	private static final String SQL_DELETE_2 = " = ";
	private static final String SQL_DELETE_3 = ";";

	// テーブル名のプリコンパイル指定不能なのでStringBuilderで結合する
	// [1]: テーブル名, [2]: 主キー
	private static final String SQL_SELECT_0 = "SELECT * FROM '";
	private static final String SQL_SELECT_1 = "' WHERE  primaryKey = '";
	private static final String SQL_SELECT_2 = "';";

	public DatabaseConnection(Context context, String name) {
		super(context, name, null, VERSION);
	}

	@Deprecated
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Deprecated
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	@Override
	public void insertOrUpdate(String tableName, String className,
			String classVersion, String primaryKey, String data,
			boolean persistance) {

		String sql;
		StringBuilder sb = new StringBuilder();
		if (isExsist(tableName)) {
			// [1]: テーブル名, [2]: クラス名, [3]: クラスVer, [4]: データ, [5]: 永続化フラグ,
			// [6]: 主キー
			sql = sb.append(SQL_UPDATE_0).append(tableName)
					.append(SQL_UPDATE_1).append(className)
					.append(SQL_UPDATE_2).append(classVersion)
					.append(SQL_UPDATE_3).append(data).append(SQL_UPDATE_4)
					.append(persistance ? "1" : "0").append(SQL_UPDATE_5)
					.append(primaryKey).append(SQL_UPDATE_6).toString();

		} else {
			// テーブル作成
			createTable(tableName);

			// [1]: テーブル名, [2]: クラス名, [3]: クラスVer, [4]: 主キー, [5]: データ, [6]:
			// 永続化フラグ
			sql = sb.append(SQL_INSERT_0).append(tableName)
					.append(SQL_INSERT_1).append(className)
					.append(SQL_INSERT_2).append(classVersion)
					.append(SQL_INSERT_3).append(primaryKey)
					.append(SQL_INSERT_4).append(data).append(SQL_INSERT_5)
					.append(persistance ? "1" : "0").append(SQL_INSERT_6)
					.toString();
		}
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.execSQL(sql);
		} finally {
			db.close();
		}
	}

	@Override
	public void deleteIfExists(String tableName, String primaryKey) {
		// [1]: テーブル名, [2]: カラム名(主キー), [3]: 主キー
		String sql = new StringBuilder().append(SQL_DELETE_0).append(tableName)
				.append(SQL_DELETE_1).append("primaryKey").append(SQL_DELETE_2)
				.append("'").append(primaryKey).append("'")
				.append(SQL_DELETE_3).toString();
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.execSQL(sql);
		} finally {
			db.close();
		}
	}

	@Override
	public StorageRecord findByPrimaryKey(String tableName, String primaryKey) {
		// [1]: テーブル名, [2]: 主キー
		String sql = new StringBuilder().append(SQL_SELECT_0).append(tableName)
				.append(SQL_SELECT_1).append(primaryKey).append(SQL_SELECT_2)
				.toString();
		SQLiteDatabase db = getReadableDatabase();
		StorageRecord record = null;
		try {
			Cursor c = db.rawQuery(sql, null);
			if (c.moveToNext()) {
				String className = c.getString(0);
				String classVersion = c.getString(1);
				String pKey = c.getString(2);
				String data = c.getString(3);
				boolean persistent = c.getInt(4) == 1;
				record = new StorageRecord(className, classVersion, pKey, data,
						persistent);
			}
		} finally {
			db.close();
		}
		return record;
	}

	private boolean isExsist(String tableName) {

		boolean ret = false;
		String sql = new StringBuilder().append(SQL_CHECK_0).append(tableName)
				.append(SQL_CHECK_1).toString();
		SQLiteDatabase db = getReadableDatabase();
		try {
			Cursor c = db.rawQuery(sql, null);
			c.moveToFirst();
			String result = c.getString(0);
			if ("1".equals(result))
				ret = true;
		} finally {
			db.close();
		}
		return ret;
	}

	private void createTable(String tableName) {
		String sql = new StringBuilder().append(SQL_CREATE_0).append(tableName)
				.append(SQL_CREATE_1).toString();
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.execSQL(sql);
		} finally {
			db.close();
		}
	}

}
