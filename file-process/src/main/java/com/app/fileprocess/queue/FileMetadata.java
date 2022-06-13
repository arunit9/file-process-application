package com.app.fileprocess.queue;

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
