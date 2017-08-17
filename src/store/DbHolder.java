package store;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class DbHolder {
	
	static DB db = null;
	
	public static void initialize(boolean test) {
		if(test) {
			db =  DBMaker.fileDB("test_file.db").closeOnJvmShutdown().make();
		}else {
			db = DBMaker.fileDB("file.db").closeOnJvmShutdown().make();
		}
	}
	
	public static  DB getDB() {
		return db;
	}
}
