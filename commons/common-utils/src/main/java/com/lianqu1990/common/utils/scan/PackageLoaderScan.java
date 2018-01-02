//mpsite_resource_jcr
package com.lianqu1990.common.utils.scan;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ClassName : PackageLoader 
 * Use : TODO. 
 * date : 2014-8-26 下午5:21:59 
 *
 * @author Han
 * @version 
 */
public class PackageLoaderScan implements IScan{
	
	@Override
	public Set<Class<?>> scan(String pack) {
		return scan(pack,null,null);
	}

	@Override
	public Set<Class<?>> scan(String pack, Class<? extends Annotation> annotation) {
		return scan(pack,annotation,null);
	} 
	
	@Override
	public Set<Class<?>> scan(String pack,Class<? extends Annotation> annotation,Class<?> iface) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		String packageDirName = pack.replace('.', '/');
		Enumeration<URL> dirs;
		boolean recursive = true;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					System.err.println("扫描到文件...");
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(pack, filePath, recursive, classes,annotation,iface);
				} else if ("jar".equals(protocol)) {
					findAndAddClassesByJar(pack, annotation, iface, classes, packageDirName, recursive, url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}

	private String findAndAddClassesByJar(String pack, Class<? extends Annotation> annotation, Class<?> iface, Set<Class<?>> classes, String packageDirName, boolean recursive, URL url) {
		// 如果是jar包文件
		// 定义一个JarFile
		System.err.println("扫描到jar包...");
		JarFile jar;
		try {
			// 获取jar
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			// 从此jar包 得到一个枚举类
			Enumeration<JarEntry> entries = jar.entries();
			// 同样的进行循环迭代
			while (entries.hasMoreElements()) {
				// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				// 如果是以/开头的
				if (name.charAt(0) == '/') {
					// 获取后面的字符串
					name = name.substring(1);
				}
				// 如果前半部分和定义的包名相同
				if (name.startsWith(packageDirName)) {
					int idx = name.lastIndexOf('/');
					// 如果以"/"结尾 是一个包
					if (idx != -1) {
						// 获取包名 把"/"替换成"."
						pack = name.substring(0, idx).replace('/', '.');
					}
					// 如果可以迭代下去 并且是一个包
					if ((idx != -1) || recursive) {
						// 如果是一个.class文件 而且不是目录
						if (name.endsWith(".class") && !entry.isDirectory()) {
							// 去掉后面的".class" 获取真正的类名
							String className = name.substring(pack.length() + 1, name.length() - 6);
							try {
								// 添加到classes
								Class<?> clazz = Class.forName(pack + '.' + className);
								if(checkAnnotation(clazz, annotation, iface)){
									classes.add(clazz);
								}
							} catch (ClassNotFoundException e) {
								// log
								// .error("添加用户自定义视图类错误 找不到此类的.class文件");
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// log.error("在扫描用户定义视图时从jar包获取文件出错");
			e.printStackTrace();
		}
		return pack;
	}
	
	public void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes,Class<? extends Annotation> annotation,Class<?> iface) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes,annotation,iface);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
					if(checkAnnotation(clazz, annotation, iface)){
						classes.add(clazz);
					}
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' +
					// className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
				} catch (ClassNotFoundException e) {
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public boolean checkAnnotation(Class<?> clazz,Class<? extends Annotation> annotation,Class<?> iface){
		if(annotation != null && !clazz.isAnnotationPresent(annotation)){
			return false;
		}
		if(iface != null && (!iface.isAssignableFrom(clazz) || clazz == iface)){
			return false;
		}
		return true;
	}

}
