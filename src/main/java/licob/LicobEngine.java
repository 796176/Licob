/* Licob - Licob Is a Chain-Oriented Backup
 * Copyright (C) 2024 Yegore Vlussove
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package licob;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

public class LicobEngine implements LicobEngineInterface {
	private final LicobStatus status = new LicobStatus();
	private File dst = null;
	private File src = null;
	private boolean interrupted = false;

	@Override
	public void backupContent(File source, File destination, File[] exceptions) throws IOException {
		assert source != null && destination != null && exceptions != null;

		dst = destination;
		src = source;
		Arrays.sort(exceptions);
		Path sourcePath = source.toPath();
		FileVisitor<Path> fileVisitor = new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attributes) throws IOException{
				if (Arrays.binarySearch(exceptions, path.toFile()) >= 0)
					return FileVisitResult.SKIP_SUBTREE;

				LinkedList<File> ll = status.getAllFiles();
				ll.add(path.toFile());
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
				if (Arrays.binarySearch(exceptions, path.toFile()) < 0) {
					status.getAllFiles().add(path.toFile());
					status.setTotal(status.getTotal() + Files.size(path));
				}
				return FileVisitResult.CONTINUE;
			}
		};
		Files.walkFileTree(sourcePath, fileVisitor);
		status.getAllFiles().remove(source);

		BackgroundProcess backgroundProcess = new BackgroundProcess();
		Thread thread = new Thread(backgroundProcess);
		thread.start();
		status.setThread(thread);
	}

	@Override
	public void backupContent(File source, File dst) throws IOException {
		backupContent(source, dst, new File[]{});
	}

	@Override
	public void createBackup(File source, File destination, File[] exceptions) throws IOException {
		assert source != null && destination != null && exceptions != null;

		File[] parentExceptions = source.getParentFile().listFiles(pathname -> !pathname.equals(source));
		exceptions = Arrays.copyOf(exceptions, exceptions.length + parentExceptions.length);
		System.arraycopy(
			parentExceptions, 0,
			exceptions, exceptions.length - parentExceptions.length,
			parentExceptions.length
		);
		backupContent(source.getParentFile(), destination, exceptions);
	}

	@Override
	public void createBackup(File source, File dst) throws IOException {
		createBackup(source, dst, new File[]{});
	}

	@Override
	public LicobStatus getStatus() {
		return status;
	}

	@Override
	public void interrupt(){
		interrupted = true;
		try {
			status.getThread().join();
		} catch (InterruptedException ignored) {}
	}

	private void setAttributes(Path currentPath, Path currentPathDst) throws IOException{
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			Set<String> attributeKeys =
				Set.of("owner", "group", "lastAccessTime", "lastModifiedTime", "creationTime", "permissions");
			for (String key : attributeKeys) {
				Files.setAttribute(
					currentPathDst,
					"posix:" + key,
					Files.getAttribute(currentPath, "posix:" + key)
				);
			}
		}
	}

	private class BackgroundProcess implements Runnable {
		@Override
		public void run() {
			try {
				Iterator<File> files = status.getAllFiles().iterator();
				while (!interrupted && files.hasNext()) {
					File currentFile = files.next();
					status.setCurrentFile(currentFile);
					File currentFileDst =
						new File(dst, currentFile.getAbsolutePath().replace(src.getAbsolutePath(), ""));

					if (currentFile.isDirectory()) {
						currentFileDst.mkdir();
						setAttributes(currentFile.toPath(), currentFileDst.toPath());
						status.getFinishedFiles().add(currentFile);
						continue;
					}

					int bsize = 1024 * 8;
					status.setCurrentTotal(Files.size(currentFile.toPath()));
					try (
						BufferedInputStream bis = new BufferedInputStream(new FileInputStream(currentFile), bsize);
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(currentFileDst), bsize)
					) {
						byte[] buffer = new byte[1024];
						int byteRead;
						do {
							byteRead = bis.readNBytes(buffer, 0, buffer.length);
							status.setCurrentProgress(status.getCurrentProgress() + byteRead);
							bos.write(buffer, 0, byteRead);
						} while (byteRead != 0 && !interrupted);
					}
					setAttributes(currentFile.toPath(), currentFileDst.toPath());
					status.getFinishedFiles().add(currentFile);
					status.setCurrentProgress(0);
					status.setProgress(status.getProgress() + status.getCurrentTotal());
					status.setCurrentTotal(0);
				}
			} catch (Exception e) {
				status.setException(e);
			}

			status.setIsFinished(true);
		}
	}
}
