package com.cleanPhone.mobileCleaner.filestorage;

public class FileListItem implements Comparable<FileListItem> {
    private boolean directory;
    private String filename;
    private String location;
    private boolean marked;
    private long time;

    public String getFilename() {
        return this.filename;
    }

    public String getLocation() {
        return this.location;
    }

    public long getTime() {
        return this.time;
    }

    public boolean isDirectory() {
        return this.directory;
    }

    public boolean isMarked() {
        return this.marked;
    }

    public void setDirectory(boolean z) {
        this.directory = z;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public void setLocation(String str) {
        this.location = str;
    }

    public void setMarked(boolean z) {
        this.marked = z;
    }

    public void setTime(long j) {
        this.time = j;
    }

    @Override
    public int compareTo(FileListItem fileListItem) {
        if (fileListItem.isDirectory() && isDirectory()) {
            return this.filename.toLowerCase().compareTo(fileListItem.getFilename().toLowerCase());
        }
        if (fileListItem.isDirectory() || isDirectory()) {
            return (!fileListItem.isDirectory() || isDirectory()) ? -1 : 1;
        }
        return this.filename.toLowerCase().compareTo(fileListItem.getFilename().toLowerCase());
    }
}
