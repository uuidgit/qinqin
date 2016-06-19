package com.qinqin.comparision;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FileHelper {
	private static Logger _logger = Logger
			.getLogger(FileHelper.class.getName());

	public static String getFileSize(File file) {
		if (file.isDirectory()) {
			return null;
		}
		try {
			@SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream(file);
			int size = fis.available();
			return String.format("%,d B", size);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			_logger.info("File not found:" + file.getName());
		} catch (IOException e) {
			e.printStackTrace();
			_logger.info("IO Exception:" + e.getMessage());
		}
		return null;

	}

	public static void traversingDir(File file, List<FileEntity> list) {
		String name = file.getName();
		String path = file.getPath();
		String size = getFileSize(file);

		Date lastModified = new Date(file.lastModified());

		if (file.isFile()) {
			FileEntity o = new FileEntity(name, path, lastModified, size,
					file.isHidden(), true, false);
			list.add(o);
		} else {
			FileEntity o = new FileEntity(name, path, lastModified, size,
					file.isHidden(), false, true);
			list.add(o);
			File[] files = file.listFiles();

			for (File f : files) {
				traversingDir(f, list);
			}
		}
	}

	public static void output(List<FileEntity> list) throws IOException {
		for (FileEntity fileEntity : list) {

			System.out.println(fileEntity.toString());

		}
	}

	public static void output(List<FileEntity> list, String srcPath,
			String destPath) throws Exception {
		File ff = new File(destPath);
		if (!ff.exists()) {
			ff.mkdirs();
		}

		for (FileEntity fileEntity : list) {
			// System.out.println(fileEntity.toString());
			if (fileEntity.isDirectory()) {
				srcPath = srcPath.replaceAll("\\\\", "/");
				destPath = destPath.replaceAll("\\\\", "/");
				String path = fileEntity.getPath().replaceAll("\\\\", "/");
				path = path.replaceAll(srcPath, destPath);
				path = path.replaceAll("/", "\\\\");
				new File(path).mkdirs();

			} else {

				srcPath = srcPath.replaceAll("\\\\", "/");
				destPath = destPath.replaceAll("\\\\", "/");
				String path = fileEntity.getPath().replaceAll("\\\\", "/");
				String old = path;
				path = path.replaceAll(srcPath, destPath);
				path = path.replaceAll("/", "\\\\");

				// System.out.println(old+":::"+path);
				copyFiles(old, path);
			}

		}
	}

	public static void getIncrementByList(List<String> lists, String srcPath,
			String destPath) throws Exception {
		File ff = new File(destPath);
		if (!ff.exists()) {
			ff.mkdirs();
		}

		for (String list : lists) {

			srcPath = srcPath.replaceAll("\\\\", "/");
			destPath = destPath.replaceAll("\\\\", "/");
			String path = list.replaceAll("\\\\", "/");
			String old = path;
			path = path.replaceAll(srcPath, destPath);
			path = path.replaceAll("/", "\\\\");

			// System.out.println(old+":::"+path);
			copyFiles(old, path);

		}
	}

	public static Map<Integer, List<FileEntity>> doCompare(
			List<FileEntity> srcList, List<FileEntity> destList, String src,
			String dest) throws IOException {
		Map<Integer, List<FileEntity>> map = new HashMap<Integer, List<FileEntity>>();
		removeHiddens(srcList);
		removeHiddens(destList);
		// 将原地址目录下的内容封装到map中
		for (FileEntity o : destList) {
			if (!o.getPath().contains("target")) {
				if (o.getLevel() != 0) {
					if (map.containsKey(o.getLevel())) {
						map.get(o.getLevel()).add(o);
					} else {
						List<FileEntity> list = new ArrayList<FileEntity>();
						list.add(o);
						map.put(o.getLevel(), list);
					}
				}
			}

		}
		// 将目的地址目录内容更新到map中
		for (FileEntity o : srcList) {
			if (!o.getPath().contains("target")) {
				if (o.getLevel() != 0) {
					if (map.containsKey(o.getLevel())) {
						// 判断源目录中是否包含目的目录
						FileEntity entity = getFileEntity(
								map.get(o.getLevel()), o);

						if (entity != null) {// 包含
							// 判断文件是否相等
							if (o.isFile() && entity.isFile()) {
								// 判断两个文件是否相等
								if (updateFileEntity(o, entity)) {
									// 两个文件相等，移除
									doRemove(map.get(o.getLevel()), entity);
								}
							}
							// 仅仅目录一样，移除
							if (o.isDirectory() && entity.isDirectory()) {
								doRemove(map.get(o.getLevel()), entity);
							}
						}
					}
				}
			}
		}
		return map;
	}

	public static Map<Integer, List<FileEntity>> doDelCompare(
			List<FileEntity> srcList, List<FileEntity> destList, String src,
			String dest) throws IOException {
		Map<Integer, List<FileEntity>> map = new HashMap<Integer, List<FileEntity>>();
		removeHiddens(srcList);
		removeHiddens(destList);

		for (FileEntity o : srcList) {
			if (!o.getPath().contains("target")) {
				if (o.getLevel() != 0) {
					if (map.containsKey(o.getLevel())) {
						map.get(o.getLevel()).add(o);
					} else {
						List<FileEntity> list = new ArrayList<FileEntity>();
						list.add(o);
						map.put(o.getLevel(), list);
					}
				}
			}
		}

		for (FileEntity o : destList) {
			if (!o.getPath().contains("target")) {
				if (o.getLevel() != 0) {

					if (map.containsKey(o.getLevel())) {
						FileEntity entity = getFileEntity(
								map.get(o.getLevel()), o);

						if (entity != null) {
							doRemove(map.get(o.getLevel()), entity);
						}
					}
				}
			}
		}
		return map;
	}

	public static boolean isContainsStr(String path, String str) {
		String[] strLists = str.split(",");

		for (int i = 0; i < strLists.length; i++) {
			if(path.contains(strLists[i])){
				return true;
			}
		}
		return false;
	}

	public static Map<Integer, List<FileEntity>> doCompare(
			List<FileEntity> srcList, List<FileEntity> destList, String src,
			String dest, String filter) throws IOException {

		Map<Integer, List<FileEntity>> map = new HashMap<Integer, List<FileEntity>>();
		removeHiddens(srcList);
		removeHiddens(destList);
		// 将原地址目录下的内容封装到map中
		for (FileEntity o : destList) {
			if (!isContainsStr(o.getPath(),filter)) {
				if (o.getLevel() != 0) {
					if (map.containsKey(o.getLevel())) {
						map.get(o.getLevel()).add(o);
					} else {
						List<FileEntity> list = new ArrayList<FileEntity>();
						list.add(o);
						map.put(o.getLevel(), list);
					}
				}
			}

		}
		// 将目的地址目录内容更新到map中
		for (FileEntity o : srcList) {
			if (!isContainsStr(o.getPath(),filter)) {
				if (o.getLevel() != 0) {
					if (map.containsKey(o.getLevel())) {
						// 判断源目录中是否包含目的目录
						FileEntity entity = getFileEntity(
								map.get(o.getLevel()), o);

						if (entity != null) {// 包含
							// 判断文件是否相等
							if (o.isFile() && entity.isFile()) {
								// 判断两个文件是否相等
								if (updateFileEntity(o, entity)) {
									// 两个文件相等，移除
									doRemove(map.get(o.getLevel()), entity);
								}
							}
							// 仅仅目录一样，移除
							if (o.isDirectory() && entity.isDirectory()) {
								doRemove(map.get(o.getLevel()), entity);
							}
						}
					}
				}
			}
		}
		return map;
	}

	public static Map<Integer, List<FileEntity>> doDelCompare(
			List<FileEntity> srcList, List<FileEntity> destList, String src,
			String dest, String filter) throws IOException {
		Map<Integer, List<FileEntity>> map = new HashMap<Integer, List<FileEntity>>();
		removeHiddens(srcList);
		removeHiddens(destList);

		for (FileEntity o : srcList) {
			if (!isContainsStr(o.getPath(),filter)) {
				if (o.getLevel() != 0) {
					if (map.containsKey(o.getLevel())) {
						map.get(o.getLevel()).add(o);
					} else {
						List<FileEntity> list = new ArrayList<FileEntity>();
						list.add(o);
						map.put(o.getLevel(), list);
					}
				}
			}
		}

		for (FileEntity o : destList) {
			if (!isContainsStr(o.getPath(),filter)) {
				if (o.getLevel() != 0) {

					if (map.containsKey(o.getLevel())) {
						FileEntity entity = getFileEntity(
								map.get(o.getLevel()), o);

						if (entity != null) {
							doRemove(map.get(o.getLevel()), entity);
						}
					}
				}
			}
		}
		return map;
	}

	private static void doRemove(List<FileEntity> list, FileEntity o) {
		FileEntity result = null;
		for (FileEntity entity : list) {
			if (o.equals(entity)) {
				result = entity;
				break;
			}

		}
		list.remove(result);
	}

	public static void removeHiddens(List<FileEntity> list) {
		List<FileEntity> hiddens = new ArrayList<FileEntity>();
		for (FileEntity o : list) {
			if (o.isHidden()) {
				hiddens.add(o);
			}
		}

		List<FileEntity> result = new ArrayList<FileEntity>();
		for (FileEntity o : hiddens) {
			for (FileEntity entity : list) {
				if (entity.getPath().indexOf(o.getName()) > -1) {
					result.add(entity);
				}
			}
		}

		list.removeAll(result);
	}

	/**
	 * 
	 * @param list
	 * @param o
	 * @return
	 * @throws IOException
	 */
	private static FileEntity getFileEntity(List<FileEntity> list, FileEntity o)
			throws IOException {
		for (FileEntity entity : list) {
			// 当两个file对象都是文件或者两者都是目录
			if ((o.isFile() && entity.isFile() || o.isDirectory()
					&& entity.isDirectory())) {
				// 判断文件名或者目录名是否相等
				if (o.getName().equals(entity.getName())) {
					return entity;
				}
			}
		}
		return null;
	}

	private static boolean updateFileEntity(FileEntity n, FileEntity o)
			throws IOException {

		MD5Check md5check = new MD5Check();
		if (md5check.getFileMD5String(new File(n.getPath())).equals(
				md5check.getFileMD5String(new File(o.getPath())))) {
			return true;
		}

		return false;
	}

	public static void setLevels(List<FileEntity> list, String src) {
		for (FileEntity o : list) {
			setLevel(o, src);
		}
	}

	private static void setLevel(FileEntity o, String src) {
		if (src.equals(o.getPath())) {
			o.setLevel(0);
		} else {
			src = src.replace(File.separator, "/");
			String path = o.getPath().replace(File.separator, "/");
			int srcLength = src.split("/").length;
			int length = path.split("/").length;
			length -= srcLength;
			o.setLevel(length);
		}

	}

	/**
	 * 复制文件，从源目录复制到目的目录中
	 * 
	 * @param fromPath
	 * @param toPath
	 * @throws Exception
	 */
	public static void copyFiles(String fromPath, String toPath)
			throws Exception {
		File fromFile = new File(fromPath);

		if (fromFile.exists()) {// 1.判断文件是否存在，存在
			File toFile = new File(toPath);
			if (toFile.isDirectory()) {// 2.判断目的字符串是文件还是目录
				if (fromFile.isFile()) {// 3.判断源字符串是文件还是目录
					FileInputStream inFile = new FileInputStream(fromFile);
					FileOutputStream outFile = new FileOutputStream(toFile);
					FileChannel inChannel = inFile.getChannel();
					FileChannel outChannel = outFile.getChannel();
					long bytesWritten = 0;
					long byteCount = inChannel.size();
					while (bytesWritten < byteCount) {
						bytesWritten += inChannel.transferTo(bytesWritten,
								byteCount - bytesWritten, outChannel);
					}
					// System.out.println("文件" + fromFile.getName()
					// + "绝对路径" + toFile.getAbsolutePath() + ".");
					inFile.close();
					outFile.close();

				} // end of if

			}// end of if
			else {// 目的目录不存在时
				if (fromFile.isFile()) {
					// 创建目的路径文件对象
					File newToFile = new File(toPath);

					String dest = newToFile.toString().substring(0,
							newToFile.toString().lastIndexOf("\\"));
					File destFile = new File(dest);

					// 判断存放目的文件的目录地址是否存在，不存在则新建
					if (!destFile.exists()) {
						destFile.mkdirs();
					}

					newToFile.createNewFile();
					FileInputStream inFile = new FileInputStream(fromFile);
					FileOutputStream outFile = new FileOutputStream(newToFile);
					FileChannel inChannel = inFile.getChannel();
					FileChannel outChannel = outFile.getChannel();
					long bytesWritten = 0;
					long byteCount = inChannel.size();
					while (bytesWritten < byteCount) {
						bytesWritten += inChannel.transferTo(bytesWritten,
								byteCount - bytesWritten, outChannel);
					}
					// System.out
					// .println("文件" + fromFile.getName() + "绝对路径"
					// + newToFile.getAbsolutePath() + ".");
					inFile.close();
					outFile.close();
				}// end of if
				else {
					// 目的字符串为目录，则创建文件目录
					if (toFile.mkdir()) {
						System.out.println("目录" + toFile.getAbsolutePath()
								+ "已经创建!");
						// 列出原文件中的文件列表
						File[] info = fromFile.listFiles();
						for (int i = 0; i < info.length; i++) {
							String toPathTemp = toPath + "\\"
									+ info[i].getName();
							copyFiles(info[i].getAbsolutePath(), toPathTemp);// 复制操作
						}
					}// end of if
					else {
						System.out.println("目录" + toFile.getAbsolutePath()
								+ "创建失败!");
					}// end of else

				}// end of else

			}// end of else
		}// end of if
		else {
			System.out.println("目录" + fromPath + "不存在,复制文件操作失败!");
		}// end of else
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			// int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				@SuppressWarnings("resource")
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					// bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	public static void writefile(String path, StringBuffer content) {
		try {

			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter writer = new FileWriter(path);
			writer.write("");

			BufferedWriter bw = new BufferedWriter(writer);

			String[] str = content.toString().split(",");

			if(!"".equals(bw.toString())&&bw!=null){
				for (int i = 0; i < str.length; i++) {

					if (i > 0) {
						bw.newLine();
					}

					bw.write(str[i]);
				}

				bw.close();
			}
			writer.close();

			// return true;
		} catch (Exception e) {
			e.printStackTrace();
			// return false;
		}
	}

	/**
	 * 功能：Java读取txt文件的内容 步骤：1：先获得文件句柄 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
	 * 3：读取到输入流后，需要读取生成字节流 4：一行一行的输出。readline()。 备注：需要考虑的是异常情况
	 * 
	 * @param filePath
	 */
	public static List<String> readTxtFileToArrayList(String filePath) {
		List<String> lists = new ArrayList<String>();
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);

			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					lists.add(lineTxt);
					System.out.println(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}

		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return lists;
	}

	/**
	 * 删除空目录
	 * 
	 * @param dir
	 *            将要删除的目录路径
	 */
	public static void doDeleteEmptyDir(String dir) {
		boolean success = (new File(dir)).delete();
		if (success) {
			System.out.println("Successfully deleted empty directory: " + dir);
		} else {
			System.out.println("Failed to delete empty directory: " + dir);
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	public static void main(String[] args) {
		String fromPath = "F:\\mytest\\trunk\\hs-med-access-beans\\delete.txt";
		// String toPath = "F:\\testF\\LOCK";
		try {
			// copyFiles(fromPath, toPath);
			readTxtFileToArrayList(fromPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}