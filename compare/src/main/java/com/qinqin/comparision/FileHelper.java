package com.qinqin.comparision;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
		if(!ff.exists()) {
			ff.mkdirs() ;
		}
		
		for (FileEntity fileEntity : list) {

			//System.out.println(fileEntity.toString());
			
			if (fileEntity.isDirectory()) {
				srcPath = srcPath.replaceAll("\\\\", "/");
				destPath = destPath.replaceAll("\\\\", "/");
				String path = fileEntity.getPath().replaceAll("\\\\", "/");
				path = path.replaceAll(srcPath, destPath);
				path = path.replaceAll("/", "\\\\");
				new File(path).mkdirs();
				
			}else {
				String filename = fileEntity.getPath();
				System.out.println(filename);
				
				srcPath = srcPath.replaceAll("\\\\", "/");
				destPath = destPath.replaceAll("\\\\", "/");
				String path = fileEntity.getPath().replaceAll("\\\\", "/");
				String old = path ;
				path = path.replaceAll(srcPath, destPath);
				path = path.replaceAll("/", "\\\\");
				
				System.out.println(old+":::"+path);
				copyFiles(old, path);
			}

		}
	}

	public static Map<Integer, List<FileEntity>> doCompare(
			List<FileEntity> srcList, List<FileEntity> destList, String src,
			String dest) throws IOException {
		Map<Integer, List<FileEntity>> map = new HashMap<Integer, List<FileEntity>>();
		removeHiddens(srcList);
		removeHiddens(destList);

		for (FileEntity o : srcList) {
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

		for (FileEntity o : destList) {
			if (o.getLevel() != 0) {

				if (map.containsKey(o.getLevel())) {
					FileEntity entity = getFileEntity(map.get(o.getLevel()), o);
					if (entity == null) {
						map.get(o.getLevel()).add(o);
					} else {
						// 文件名相等
						if (o.isFile() && entity.isFile()) {
							if (!updateFileEntity(o, entity)) {
								// 移除老的文件，加入新文件
								doRemove(map.get(o.getLevel()), entity);
								map.get(o.getLevel()).add(o);
							} else {
								doRemove(map.get(o.getLevel()), entity);
							}
						} else {
							doRemove(map.get(o.getLevel()), entity);
						}

					}
				} else {
					List<FileEntity> list = new ArrayList<FileEntity>();
					list.add(o);
					map.put(o.getLevel(), list);
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

		for (FileEntity o : destList) {
			if (o.getLevel() != 0) {
				
				if (map.containsKey(o.getLevel())) {
					FileEntity entity = getFileEntity(map.get(o.getLevel()), o);
					
					if (entity != null){
						doRemove(map.get(o.getLevel()), entity);
					}
				}
			}
		}
		System.out.println("new============="+map.toString());
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

	private static FileEntity getFileEntity(List<FileEntity> list, FileEntity o)
			throws IOException {
		for (FileEntity entity : list) {
			if ((o.isFile() && entity.isFile() || o.isDirectory()
					&& entity.isDirectory())) {
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

	public static void copyFiles(String fromPath, String toPath)
			throws Exception {
		File fromFile = new File(fromPath);

		if (fromFile.exists()) {
			File toFile = new File(toPath);
			if (toFile.isDirectory()) {
				System.out.println("目录" + toPath + "已经存在");
				if (fromFile.isFile()) {

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
					System.out.println("文件" + fromFile.getName() + "已经成功复制到"
							+ toFile.getAbsolutePath() + ".");
					inFile.close();
					outFile.close();
				} // end of if

			}// end of if
			else {
				if (fromFile.isFile()) {
					// 复制文件
					File newToFile = new File(toPath);
					
					String dest = newToFile.toString().substring(0, newToFile.toString().lastIndexOf("\\"));
					File destFile = new File(dest) ;
					
					//如果目标目录中不存在该目录，则创建该目录
					if(!destFile.exists()) {
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
					System.out.println("文件" + fromFile.getName() + "已经成功复制到"
							+ newToFile.getAbsolutePath() + ".");
					inFile.close();
					outFile.close();
				}// end of if
				else {
					// 处理文件夹
					if (toFile.mkdir()) {
						// 复制文件夹
						System.out.println("目录" + toFile.getAbsolutePath()
								+ "已经创建!");
						File[] info = fromFile.listFiles();
						for (int i = 0; i < info.length; i++) {
							String toPathTemp = toPath + "\\"
									+ info[i].getName();
							copyFiles(info[i].getAbsolutePath(), toPathTemp);// 递归调用
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

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
		//	int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				@SuppressWarnings("resource")
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
				//	bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	public static void main(String[] args) {
		String fromPath = "F:\\testF\\NEW\\add.txt";
		String toPath = "F:\\testF\\LOCK";
		try {
			copyFiles(fromPath, toPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}