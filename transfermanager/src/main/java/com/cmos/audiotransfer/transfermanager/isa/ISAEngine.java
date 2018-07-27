package com.cmos.audiotransfer.transfermanager.isa;

import com.cmos.audiotransfer.transfermanager.bean.TransformResult;

import java.util.Map;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午5:23
 * Description:
 */
public interface ISAEngine {
    /**
     * Describes why the annotated element is incompatible with GWT. Since this is
     */
    public boolean Initialize();

    /**
     * 引擎逆初始化
     */
    public void Finalize();

    /**
     * 处理语音
     *
     * @param uri
     *            语音路径
     * @param fmt
     *            语音格式
     * @param param
     *            处理参数
     * @return 成功返回true，反之返回false
     */
    public boolean ProcessFile(final String uri, final String fmt, final String param);

    /**
     * 获取解码结果
     *
     * @param uri
     *            语音路径
     * @return 解析后的解码结果
     */
    public TransformResult FetchOneBest(final String uri);

    public String getResult(final String uri);


    public boolean DeleteOneBest(final String uri);

    public Map<String, String> BatchProcessFile(final String uriList, final String fmt, final String param);
}
