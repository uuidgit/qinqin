package com.qinqin.comparision;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DirectoryComparison {

	/**
	 * 比较两个目录 并将差异文件分别存入到指定目录中*（指定目录指：删除的 更新的+新增的）
	 * 
	 * @param src
	 * @param dest
	 * @param incre
	 * @param del
	 * @throws Exception
	 */
	public static void compare(String src, String dest, String incre,
			String del, String list) throws Exception {
		File srcFile = new File(src);
		File destFile = new File(dest);
		if (!"".equals(list)) {
			File listFile = new File(list);
			if (!listFile.exists()) {
				if (!listFile.mkdirs()) {
					System.out.println("Dir create fail!");
				}
			}
		} else {
			list = src;
		}

		List<FileEntity> srcList = new ArrayList<FileEntity>();
		List<FileEntity> destList = new ArrayList<FileEntity>();
		Map<Integer, List<FileEntity>> map = null;

		String temppath = "";
		StringBuffer content = new StringBuffer("");

		if (!srcFile.exists() || !destFile.exists()) {
			return;
		}

		FileHelper.traversingDir(srcFile, srcList);
		FileHelper.traversingDir(destFile, destList);
		FileHelper.setLevels(srcList, src);
		FileHelper.setLevels(destList, dest);
		map = FileHelper.doCompare(srcList, destList, src, dest);
		// System.out.println(map.toString());
		System.out.println("----------------INC AND MODIFY:-----------------\n");
		for (Entry<Integer, List<FileEntity>> entry : map.entrySet()) {
			// FileHelper.output(entry.getValue());
			if (!entry.getValue().isEmpty()) {
				System.out.println("########  Level:" + entry.getKey()
						+ "   ###################");
				// 组装增量包
				FileHelper.output(entry.getValue(), dest, incre);

				for (FileEntity file : entry.getValue()) {
					if (file.isFile()) {
						System.out.println(file.getPath());
						temppath = list + File.separator + "increment.txt";
						content.append(file.getPath() + ",");

					}
				}
				System.out.println();

			}

		}
		if(content!=null&&!"".equals(content.toString())){
			FileHelper.writefile(temppath, content);
		}

		map = FileHelper.doDelCompare(srcList, destList, src, dest);

		content = new StringBuffer("");
		System.out.println("----------------DEL:-----------------\n");
		for (Entry<Integer, List<FileEntity>> entry : map.entrySet()) {

			// FileHelper.output(entry.getValue());
			if (!entry.getValue().isEmpty()) {
				System.out.println("########  Level:" + entry.getKey()
						+ "   ###################");
				FileHelper.output(entry.getValue(), src, del);

				for (FileEntity file : entry.getValue()) {
					if (file.isFile()) {
						System.out.println(file.getPath());
						temppath = list + File.separator + "delete.txt";
						content.append(file.getPath() + ",");
					}
				}
				System.out.println();
			}
		}
		if(content!=null&&!"".equals(content.toString())){
			FileHelper.writefile(temppath, content);
		}
	}

	public static void compare(String src, String dest, String incre, String del)
			throws Exception {
		File srcFile = new File(src);
		File destFile = new File(dest);
		List<FileEntity> srcList = new ArrayList<FileEntity>();
		List<FileEntity> destList = new ArrayList<FileEntity>();
		Map<Integer, List<FileEntity>> map = null;

		String temppath = "";
		StringBuffer content = new StringBuffer("");

		if (!srcFile.exists() || !destFile.exists()) {
			return;
		}

		FileHelper.traversingDir(srcFile, srcList);
		FileHelper.traversingDir(destFile, destList);
		FileHelper.setLevels(srcList, src);
		FileHelper.setLevels(destList, dest);
		map = FileHelper.doCompare(srcList, destList, src, dest);
		// System.out.println(map.toString());
		System.out.println("----------------INC AND MODIFY:-----------------\n");
		for (Entry<Integer, List<FileEntity>> entry : map.entrySet()) {
			// FileHelper.output(entry.getValue());
			if (!entry.getValue().isEmpty()) {
				System.out.println("########  Level:" + entry.getKey()
						+ "   ###################");
				// 组装增量包
				FileHelper.output(entry.getValue(), dest, incre);

				for (FileEntity file : entry.getValue()) {
					if (file.isFile()) {
						System.out.println(file.getPath());
						temppath = src + File.separator + "increment.txt";
						content.append(file.getPath() + ",");

					}
				}
				System.out.println();

			}

		}
		if(content!=null&&!"".equals(content.toString())){
			FileHelper.writefile(temppath, content);
		}

		map = FileHelper.doDelCompare(srcList, destList, src, dest);

		content = new StringBuffer("");
		System.out.println("----------------DEL:-----------------\n");
		for (Entry<Integer, List<FileEntity>> entry : map.entrySet()) {

			// FileHelper.output(entry.getValue());
			if (!entry.getValue().isEmpty()) {
				System.out.println("########  Level:" + entry.getKey()
						+ "   ###################");
				FileHelper.output(entry.getValue(), src, del);

				for (FileEntity file : entry.getValue()) {
					if (file.isFile()) {
						System.out.println(file.getPath());
						temppath = src + File.separator + "delete.txt";
						content.append(file.getPath() + ",");
					}
				}
				System.out.println();
			}

		}
		if(content!=null&&!"".equals(content.toString())){
			FileHelper.writefile(temppath, content);
		}
	}

	public static void compare(String src, String dest, String incre,
			String del, String list,String filter) throws Exception {
		File srcFile = new File(src);
		File destFile = new File(dest);
		if (!"".equals(list)) {
			File listFile = new File(list);
			if (!listFile.exists()) {
				if (!listFile.mkdirs()) {
					System.out.println("Dir create fail!");
				}
			}
		} else {
			list = src;
		}

		List<FileEntity> srcList = new ArrayList<FileEntity>();
		List<FileEntity> destList = new ArrayList<FileEntity>();
		Map<Integer, List<FileEntity>> map = null;

		String temppath = "";
		StringBuffer content = new StringBuffer("");

		if (!srcFile.exists() || !destFile.exists()) {
			return;
		}

		FileHelper.traversingDir(srcFile, srcList);
		FileHelper.traversingDir(destFile, destList);
		FileHelper.setLevels(srcList, src);
		FileHelper.setLevels(destList, dest);
		map = FileHelper.doCompare(srcList, destList, src, dest,filter);
		// System.out.println(map.toString());
		System.out.println("----------------INC AND MODIFY:-----------------\n");
		for (Entry<Integer, List<FileEntity>> entry : map.entrySet()) {
			// FileHelper.output(entry.getValue());
			if (!entry.getValue().isEmpty()) {
				System.out.println("########  Level:" + entry.getKey()
						+ "   ###################");
				// 组装增量包
				FileHelper.output(entry.getValue(), dest, incre);

				for (FileEntity file : entry.getValue()) {
					if (file.isFile()) {
						System.out.println(file.getPath());
						temppath = list + File.separator + "increment.txt";
						content.append(file.getPath() + ",");

					}
				}
				System.out.println();

			}

		}
		if(content!=null&&!"".equals(content.toString())){
			FileHelper.writefile(temppath, content);
		}

		map = FileHelper.doDelCompare(srcList, destList, src, dest,filter);

		content = new StringBuffer("");
		System.out.println("----------------DEL:-----------------\n");
		for (Entry<Integer, List<FileEntity>> entry : map.entrySet()) {

			// FileHelper.output(entry.getValue());
			if (!entry.getValue().isEmpty()) {
				System.out.println("########  Level:" + entry.getKey()
						+ "   ###################");
				FileHelper.output(entry.getValue(), src, del);

				for (FileEntity file : entry.getValue()) {
					if (file.isFile()) {
						System.out.println(file.getPath());
						temppath = list + File.separator + "delete.txt";
						content.append(file.getPath() + ",");
					}
				}
				System.out.println();
			}
		}
		if(content!=null&&!"".equals(content.toString())){
			FileHelper.writefile(temppath, content);
		}
	}

	public static void getIncByList(String path, String listFile, String incre)
			throws Exception {
		File srcFile = new File(path);
		List<String> lists = new ArrayList<String>();

		if (!srcFile.exists()) {
			return;
		}

		lists = FileHelper.readTxtFileToArrayList(listFile);
		FileHelper.getIncrementByList(lists, path, incre);
	}

}
