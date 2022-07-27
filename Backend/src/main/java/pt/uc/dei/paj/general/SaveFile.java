package pt.uc.dei.paj.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

public class SaveFile {

	/**
	 * Utility method to save InputStream data to target location/file
	 * 
	 * @param inStream - InputStream to be saved
	 * @param target   - full path to destination file
	 * @throws IOException
	 */
	public static String saveToFile(String fileName, InputStream is2, String UPLOAD_FOLDER) throws IOException {
		// String folderToUpload = System.getProperty("jboss.home.dir") +
		// "\\standalone\\deployments\\images" + UPLOAD_FOLDER;
		String folderToUpload = System.getProperty("jboss.home.dir") + "\\bin\\images" + UPLOAD_FOLDER;
	
		try {
			createFolderIfNotExists(folderToUpload);

		} catch (SecurityException se) {
			return null;
		}
		File fileToUpload = new File(folderToUpload + fileName + ".jpg");
		OutputStream os;
		try {
			os = new FileOutputStream(fileToUpload);

			byte[] b = new byte[2048];
			int length;

			while ((length = is2.read(b)) != -1) {
				os.write(b, 0, length);
			}

			os.close();
			return fileToUpload.getAbsolutePath();
		} catch (IOException ex) {
			return null;
		}

	}

	/**
	 * Creates a folder to desired location if it not already exists
	 * 
	 * @param dirName - full path to the folder
	 * @throws SecurityException - in case you don't have permission to create the
	 *                           folder
	 */
	private static void createFolderIfNotExists(String dirName) throws SecurityException {
		File theDir = new File(dirName);
		if (!theDir.exists()) {
			theDir.mkdir();
		}
	}

	public static String convertFileToFrontEnd(String Path) {
		File f = new File(Path);
		byte[] content = new byte[(int) f.length()];
		InputStream in = null;
		try {
			in = new FileInputStream(f);
			for (int off = 0, read; (read = in.read(content, off, content.length - off)) > 0; off += read)
				;

			String base64 = DatatypeConverter.printBase64Binary(content);
			return "data:image/jpg;base64, " + base64;
		} catch (IOException e) {
			return null;
			// Some error occured
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					return null;
				}
		}
	}
}
