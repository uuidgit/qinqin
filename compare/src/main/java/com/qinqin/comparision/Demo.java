package com.qinqin.comparision;

import java.io.File;
import java.util.logging.Logger;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args == null) {
			Logger.getLogger(Demo.class.getName()).info("Arguments error!!!");
			return;

		}

		if (args.length < 4 || args.length > 6) {
			for (int i = 0; i < args.length; i++) {
				System.out.println("arg" + i + ":" + args[i]);
			}
			Logger.getLogger(Demo.class.getName()).info(
					"args has been too many!!!");
			return;
		}
		try {

			for (int i = 0; i < args.length; i++) {
				System.out.println("Input parameter is "+i+"->"+args[i]);
			}

			// 组装增量或者删除文件前，先清空相应目录
			File inc = new File(args[2]);
			File del = new File(args[3]);

			if (inc.exists()) {
				FileHelper.deleteDir(inc);
			}

			if (del.exists()) {
				FileHelper.deleteDir(del);
			}

			if (args.length == 5) {
				DirectoryComparison.compare(args[0], args[1], args[2], args[3],
						args[4]);
			} else if (args.length == 6) {
				DirectoryComparison.compare(args[0], args[1], args[2], args[3],
						args[4], args[5]);
			} else {
				DirectoryComparison.compare(args[0], args[1], args[2], args[3]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}