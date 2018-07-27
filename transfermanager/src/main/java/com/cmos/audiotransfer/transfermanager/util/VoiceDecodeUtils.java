package com.cmos.audiotransfer.transfermanager.util;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午4:19
 * Description:
 */
public class VoiceDecodeUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceDecodeUtils.class);

    private static String[] evnStrings = null;

    private VoiceDecodeUtils() {

    }

    static {
        final Map<String, String> env = new HashMap<String, String>(System.getenv());
        if (isWindows()) {
            env.put("Path", env.get("Path") + ";" + System.getProperty("user.dir") + File.separator
                + "tool/mthost_aubox_windows_pa");
        } else {
            env.put("PATH", env.get("PATH") + ":" + System.getProperty("user.dir") + File.separator
                + "tool/mthost_linux_aubox_pa");
            LOGGER.info("user.dir:" + System.getProperty("user.dir"));
            LOGGER.info("env path:" + env.get("PATH") + ":" + System.getProperty("user.dir")
                + File.separator + "tool/mthost_linux_aubox_pa");
        }
        evnStrings = mapToStringArray(env);
    }

    static String[] mapToStringArray(Map<String, String> map) {
        final String[] strings = new String[map.size()];
        int i = 0;
        for (Map.Entry<String, String> e : map.entrySet()) {
            strings[i] = e.getKey() + '=' + e.getValue();
            i++;
        }
        return strings;
    }

    public static boolean transVoice(boolean flag, String fromFile, String toFile,
        String sampleRate, String expandFormatTag) {
        // aubox decode <infile> <outfile> [samp_rate=6000(for vox)] [expand_format_tag]

        // 判断转码结果的文件夹是否存储
        File targetFile = new File(toFile);
        try {
            File targetParentDir = targetFile.getParentFile();
            // 加锁
            synchronized (VoiceDecodeUtils.class) {
                if (!targetParentDir.exists()) {
                    FileUtils.forceMkdir(targetParentDir);
                    try {
                        while (true) {
                            VoiceDecodeUtils.class.wait(1000);
                            String str = "";
                            if ("".equals(str)) {
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                        LOGGER.error("trans voice error", e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("VoiceDecode process error", e);
        }

        String tool = "";
        String param = "";
        String command = "";
        Runtime runtime = Runtime.getRuntime();
        try {
            if (isWindows()) {
                tool = System.getProperty("user.dir") + File.separator + "aubox.exe";
                param =
                    " decode " + fromFile + " " + toFile + " " + sampleRate + " " + expandFormatTag;
            } else {
                tool = System.getProperty("user.dir") + File.separator + "./aubox";
                if (flag) {
                    tool = System.getProperty("user.dir") + File.separator + "./aubox_20171027";
                }
                param =
                    " decode " + fromFile + " " + toFile + " " + sampleRate + " " + expandFormatTag;
            }

            command = tool + " " + param;

            // 将当前的语音进行转码
            final Process proc = runtime.exec(command, evnStrings);

            LOGGER.info("exec transcoding voice[执行转码]. command:" + command);

            new Thread(new Runnable() {

                public void run() {
                    BufferedReader inBr =
                        new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    try {
                        String inline = inBr.readLine();
                        while (inline != null) {
                            inline = inBr.readLine();
                        }
                        inBr.close();
                    } catch (Exception e) {
                        LOGGER.error("关闭流失败", e);
                    }
                }
            }).start();

            BufferedReader errorReaader = new BufferedReader(
                new InputStreamReader(proc.getErrorStream(), Charset.forName("GBK")));

            do {
                String errLine = errorReaader.readLine();
                if (errLine == null) {
                    break;
                } else {
                    LOGGER.info(errLine);
                }
            } while (true);
            errorReaader.close();
            proc.waitFor();

            if (proc.exitValue() != 0) {
                return false;
            }

        } catch (Exception e) {
            LOGGER.error("执行失败", e);
            return false;
        }
        return true;
    }

    // 判断是否是windows系统
    public static boolean isWindows() {
        boolean isWindows = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindows = true;
        }
        return isWindows;
    }
}
