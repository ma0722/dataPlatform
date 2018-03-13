package util;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


public class HDFSFileUtil {

    private static FileSystem fs;
    private static String hadoop_master;
    static {
        try{
            Properties properties = new Properties();
            properties.load(HDFSFileUtil.class.getResourceAsStream("/cluster.properties"));
            hadoop_master = "hdfs://" + properties.getProperty("hadoop_master_ip") + ":" + properties.getProperty("hadoop_port");
            fs = FileSystem.get(new URI(hadoop_master), new Configuration(), "hadoop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public String HDFSPath(String path) {
        return hadoop_master + path;
    }

    static public void upload(String sourcePath, String targetPath, boolean isRemove) throws Exception{
        fs.copyFromLocalFile(isRemove, new Path(sourcePath), new Path(HDFSPath(targetPath)));
    }

    static public boolean mkdir(String path) throws IllegalArgumentException, IOException{
        return fs.mkdirs(new Path(path));
    }

    static public void download(String sourcePath ,String targetPath, boolean isRemove) throws IllegalArgumentException, IOException{
        fs.copyToLocalFile(isRemove, new Path(sourcePath), new Path(targetPath));
    }

    static public boolean delFile(String path, boolean isDir) throws IllegalArgumentException, IOException{
        if(isDir)
            return fs.delete(new Path(path),true);
        else
            return fs.deleteOnExit(new Path(path));
    }

    static public boolean rename(String sourcePath, String targetPath) throws IllegalArgumentException, IOException{
        return fs.rename(new Path(sourcePath),new Path(targetPath));
    }

    static public boolean checkFile(String path) throws IllegalArgumentException, IOException{
        return fs.exists(new Path(path));
    }

    static public ArrayList fileLoc(String path) throws IllegalArgumentException, IOException{
        FileStatus filestatus = fs.getFileStatus(new Path(path));
        BlockLocation[] blkLocations=fs.getFileBlockLocations(filestatus, 0, filestatus.getLen());
        ArrayList<String> arrayList = new ArrayList<String>();
        for(BlockLocation blkLocation: blkLocations){
            String[] hosts = blkLocation.getHosts();
            arrayList.add(hosts[0]);
        }
        return arrayList;
    }
}