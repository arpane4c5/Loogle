package intranetFileSearch;

/**
 * Represents a single result to a search query.
 */
public class SearchResult {

    private String fileName;
    private String serverIp;
    private String fileType;
    private Long fileSize;
    private String filePath;
    private String typeOfServer;
    private static final String SERVER_TYPES[] = { "FTP", "SMB" };

    public SearchResult(final String serverIp, final String filePath,
            final String fileName, final String fileType,
            final Long fileSize, final int typeOfServer) {
        this.serverIp = serverIp;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.typeOfServer = SERVER_TYPES[typeOfServer];
        this.fileSize = fileSize;
    }

    public final String getTypeOfServer() {
        return typeOfServer;
    }

    public final void setTypeOfServer(final String typeOfServer) {
        this.typeOfServer = typeOfServer;
    }

    public final String getFileName() {
        return fileName;
    }

    public final void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public final String getServerIp() {
        return serverIp;
    }

    public final void setServerIp(final String serverIp) {
        this.serverIp = serverIp;
    }

    public final String getFileType() {
        return fileType;
    }

    public final void setFileType(final String fileType) {
        this.fileType = fileType;
    }

    public final Long getFileSize() {
        return fileSize;
    }

    public final void setFileSize(final Long fileSize) {
        this.fileSize = fileSize;
    }

    public final String getFilePath() {
        return filePath;
    }

    public final void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof SearchResult) {
            SearchResult result = (SearchResult) obj;
            if (result == this) {
                return true;
            }
            if (result == null) {
                return false;
            }
            return (fileName.equals(result.fileName)
                    && fileType.equals(result.fileType)
                    && fileSize.equals(result.fileSize)
                    && serverIp.equals(result.serverIp)
                    && filePath.equals(result.filePath)
                    && typeOfServer.equals(result.typeOfServer));
        }
        return false;
    }

    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.fileName != null ? this.fileName.hashCode() : 0);
        hash = 71 * hash + (this.serverIp != null ? this.serverIp.hashCode() : 0);
        hash = 71 * hash + (this.fileType != null ? this.fileType.hashCode() : 0);
        hash = 71 * hash + (this.fileSize != null ? this.fileSize.hashCode() : 0);
        hash = 71 * hash + (this.filePath != null ? this.filePath.hashCode() : 0);
        hash = 71 * hash + (this.typeOfServer != null ? this.typeOfServer.hashCode() : 0);
        return hash;
    }
}
