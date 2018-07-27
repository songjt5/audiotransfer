package com.cmos.audiotransfer.transfermanager.util;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午6:48
 * Description:
 */
public class ONestBucketUtil {

    private ONestBucketUtil() {

    }

    /**
     * 提取桶名
     *
     * @param voiceUri
     * @return
     */
    public static String parseBucketName(String voiceUri) {
        if (voiceUri.contains("~")) {
            return voiceUri.substring(0, voiceUri.indexOf('~'));
        }
        return null;
    }

    /**
     * 提取onest的key
     *
     * @param voiceUri
     * @return
     */
    public static String parseUriKey(String voiceUri) {
        if (voiceUri.contains("~")) {
            return voiceUri.substring(voiceUri.indexOf('~') + 1);
        }
        return voiceUri;
    }

    public static String calNewBucketNameTxtByTaskFileName(String voiceFullPath) {
        return ONestBucketUtil.calNewBucketNameByTaskFileName(voiceFullPath, "txt");
    }

    public static String calNewBucketNameXmlByTaskFileName(String voiceFullPath) {
        return ONestBucketUtil.calNewBucketNameByTaskFileName(voiceFullPath, "xml");
    }

    public static String calNewBucketNameXmlByFullPath(String voiceFullPath) {
        return ONestBucketUtil.calNewBucketNameByFullPath(voiceFullPath, "xml");
    }

    /**
     * 计算新桶名
     *
     * @param taskFileName 如DHXX_zyzj_0_20180503112409_zyzx_77110086_TRANS_INPUT_ijeijf-efief-fe.txt
     * @return
     */
    private static String calNewBucketNameByTaskFileName(String taskFileName, String fileType) {
        String bucketName = "";//Configure.getProperty(Constants.BUCKET_NAME);
        String testName = "";
        if (bucketName.startsWith("test")) {
            testName = "test_";
        }
        if (taskFileName == null) {
            return null;
        }
        String[] taskArr = taskFileName.split("_");
        if (taskArr.length == 0) {
            return null;
        }
        return testName + taskArr[3].substring(0, 6) + "_" + taskArr[5].substring(0, 3) + "_zx_"
            + fileType;
    }

    /**
     * 计算新桶名
     *
     * @param voiceFullPath string  如/home/platform/zyzj/public/task/trans/DHXX_zyzj_0_20180503112409_zyzx_77110086_TRANS_INPUT_ijeijf-efief-fe/871/2018050107008787_771.wav
     * @return
     */
    private static String calNewBucketNameByFullPath(String voiceFullPath, String fileType) {
        String bucketName = ""; //Configure.getProperty(Constants.BUCKET_NAME);
        String testName = "";
        if (bucketName.startsWith("test")) {
            testName = "test_";
        }
        String[] arr = voiceFullPath.split("/");
        String taskFileName = null;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].startsWith("DHXX_")) {
                taskFileName = arr[i];
                break;
            }
        }
        if (taskFileName == null) {
            return null;
        }
        String[] taskArr = taskFileName.split("_");
        if (taskArr.length == 0) {
            return null;
        }
        return testName + taskArr[3].substring(0, 6) + "_" + taskArr[5].substring(0, 3) + "_zx_"
            + fileType;
    }
}
