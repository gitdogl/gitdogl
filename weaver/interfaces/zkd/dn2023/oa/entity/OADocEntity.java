package weaver.interfaces.zkd.dn2023.oa.entity;

public class OADocEntity {
    //附件id
    private String imageFileId;

    //附件名称
    private String imageFileName;

    //附件存放路径
    private String fileRealPath;

    //附件的大小
    private String fileSize;

    //附件类型
    private String docfiletype;

    public String getDocfiletype() {
        return docfiletype;
    }

    public void setDocfiletype(String docfiletype) {
        this.docfiletype = docfiletype;
    }

    public String getImageFileId() {
        return imageFileId;
    }

    public void setImageFileId(String imageFileId) {
        this.imageFileId = imageFileId;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getFileRealPath() {
        return fileRealPath;
    }

    public void setFileRealPath(String fileRealPath) {
        this.fileRealPath = fileRealPath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "OADocEntity{" +
                "imageFileId='" + imageFileId + '\'' +
                ", imageFileName='" + imageFileName + '\'' +
                ", fileRealPath='" + fileRealPath + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", docfiletype='" + docfiletype + '\'' +
                '}';
    }
}
