package com.github.diegopacheco.sandbox.java.rocksdb.simple;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;

public class SimpleMain {

	static {
		RocksDB.loadLibrary();
	}

	public static void main(String[] args) throws Throwable {
		
		Options options = new Options();
		options.setCreateIfMissing(true);
		
		RocksDB db = RocksDB.open(options, "/tmp/");
		
		db.put("hello".getBytes(), "world".getBytes());
		byte[] value = db.get("hello".getBytes());
        
		assert ("world".equals(new String(value)));
		System.out.println("Value from Rocksdb: " + new String(value));
		
		byte[] countBytes = db.get("count".getBytes());
		countBytes = (countBytes==null) ? "0".getBytes() : countBytes;
		Integer count = new Integer(new String(countBytes));
		count = count + 1 ;
		System.out.println("Count from Rocksdb: " + count);
		
		db.put("count".getBytes(), count.toString().getBytes());
	}
}
