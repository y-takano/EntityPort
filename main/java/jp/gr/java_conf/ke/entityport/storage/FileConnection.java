package jp.gr.java_conf.ke.entityport.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import jp.gr.java_conf.ke.entityport.exception.FileAccessException;
import jp.gr.java_conf.ke.entityport.EntityPort;
import jp.gr.java_conf.ke.json.JsonFactory;

import android.content.Context;
import android.util.Log;

class FileConnection implements StorageConnection {

	private static final String BR = System.getProperty("line.separator");
	private static final String ROOT_PATH = ".andrepository";

	private final Context ctxt;
	private final String filePath;

	FileConnection(Context ctxt, String schemaName) {
		this.ctxt = ctxt;
		this.filePath = new StringBuilder(ROOT_PATH).append(schemaName)
				.append("/").toString();
	}

	@Override
	public void insertOrUpdate(String tableName, String className,
			String classVersion, String primaryKey, String data,
			boolean persistance) {
		StorageRecord record = new StorageRecord(className, classVersion,
				primaryKey, data, persistance);
		String json = JsonFactory.toJson(record) + BR;
		String path = filePath + tableName;
		StringBuilder sb;
		if (file(path).exists()) {
			sb = readAll(path);
			int index = sb.indexOf(BR);
			sb.insert(index, json);
		} else {
			sb = new StringBuilder("[").append(json).append(BR).append("]");
		}
		writeAll(path, sb.toString());
	}

	@Override
	public StorageRecord findByPrimaryKey(String tableName, String primaryKey) {
		String path = filePath + tableName;
		File file = file(path);
		StorageRecord ret = null;
		if (file.exists()) {
			StringBuilder sb = readAll(path);
			int index = sb.indexOf("primaryKey:" + primaryKey);
			if (-1 < index) {
				int end = sb.indexOf(BR, index);
				int start = sb.substring(0, end - 1).lastIndexOf(BR);
				String record = sb.substring(start, end);
				ret = JsonFactory.toObject(record, StorageRecord.class);
			}
		}
		return ret;
	}

	public void deleteIfExists(String tableName, String primaryKey) {
		String path = filePath + tableName;
		File file = file(path);
		if (file.exists()) {
			StringBuilder sb = readAll(path);
			int index = sb.indexOf("primaryKey:" + primaryKey);
			if (-1 < index) {
				int end = sb.indexOf(BR, index);
				int start = sb.substring(0, end - 1).lastIndexOf(BR);
				sb.delete(start, end);
				if (sb.length() < 5) {
					file.delete();
				} else {
					writeAll(path, sb.toString());
				}
			}
		}
	}

	private StringBuilder readAll(String path) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file(path))));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb;
		} catch (FileNotFoundException e) {
			Log.i(EntityPort.TAG, "File is not found: " + path);
			throw new FileAccessException(e);

		} catch (IOException e) {
			Log.e(EntityPort.TAG, "I/O error occurred in read file.", e);
			throw new FileAccessException(e);

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					Log.e(EntityPort.TAG,
							"Exception occurred in close a file: " + path, e);
				}
			}
		}
	}

	private void writeAll(String path, String data) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file(path), false)));
			bw.append(data);
			bw.flush();
		} catch (FileNotFoundException e) {
			Log.i(EntityPort.TAG, "File is not found: " + path);
			throw new FileAccessException(e);

		} catch (IOException e) {
			Log.e(EntityPort.TAG, "I/O error occurred in read file.", e);
			throw new FileAccessException(e);

		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception e) {
				Log.w(EntityPort.TAG, "Exception occurred in close file", e);
			}
		}
	}

	private File file(String path) {
		return ctxt.getFileStreamPath(path + ".dat");
	}

}
