package com.app.fileprocess.queue;

/**
 * Object for file metadata
 * 
 * <P>Object that holds the metadata of a file to be put on the queue
 * 
 * <P>Since since two files with same filename is considered the same, the equals
 * and hashcode methods have been overriden
 * 
 * @author arunitillekeratne
 * @version 1.0
 *
 */
public class FileMetadata {

	private String filename;
	private String username;
	

	public FileMetadata(String filename, String username) {
		super();
		this.filename = filename;
		this.username = username;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FileMetadata))
            return false;
        FileMetadata other = (FileMetadata)o;
        boolean filenameEquals = (this.filename == null && other.filename == null)
          || (this.filename != null && this.filename.equals(other.filename));
        return filenameEquals;
    }

	@Override
	public final int hashCode() {
	    int result = 17;
	    if (filename != null) {
	        result = 31 * result + filename.hashCode();
	    }
	    return result;
	}
}
