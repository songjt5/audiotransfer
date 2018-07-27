package com.cmos.audiotransfer.transfermanager.isa;

import com.cmos.audiotransfer.transfermanager.bean.TransformResult;
import com.cmos.audiotransfer.transfermanager.bean.OneBestWordItem;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午6:46
 * Description:
 */
@Component public class XmlResultParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlResultParser.class);

    private class OneBestHandler extends DefaultHandler {

        private boolean is_in_search = false;

        private boolean is_in_1best = false;

        private boolean is_in_1best_text = false;

        private boolean is_in_1best_time = false;

        private boolean is_in_silence = false;

        private boolean is_in_mixchannel = false;

        private boolean is_in_separation = false;

        private boolean is_in_search_separation = false;

        private boolean is_in_search_spn0 = false;

        private boolean is_in_spn0 = false;

        private boolean is_in_search_spn1 = false;

        private boolean is_in_spn1 = false;

        private boolean is_in_spmix = false;

        private boolean is_in_interrupted = false;

        private int chan = 0;

        private String ob_text = "";

        private String ob_time = "";

        private List<Integer[]> vad0List = new ArrayList<Integer[]>();

        private List<Integer[]> vad1List = new ArrayList<Integer[]>();

        private TransformResult rst = new TransformResult();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
            // 如果是instance节点
            if ("instance".compareToIgnoreCase(qName) == 0) {
                // 保存语音的基本信息
                rst.setUri(attributes.getValue("waveuri"));
                rst.setFormat(attributes.getValue("fmt"));
                rst.setBitRate(Integer.valueOf(attributes.getValue("bit-rate")));
                rst.setSampleRate(Integer.valueOf(attributes.getValue("sample-rate")));
                rst.setChannel(attributes.getValue("channel"));
                rst.setDuration(Integer.valueOf(attributes.getValue("duration")));
            } else if ("subject".compareToIgnoreCase(qName) == 0) {
                if (attributes.getValue("value").compareToIgnoreCase("speaker-separation") == 0) {
                    is_in_separation = true;
                }

                if ("search".compareToIgnoreCase(attributes.getValue("value")) == 0) {
                    is_in_search_separation = true;
                }
            } else if ("channel".compareToIgnoreCase(qName) == 0) {
                // 确定声道
                if (attributes.getValue("no").compareToIgnoreCase("n0") == 0) {
                    chan = 0;
                    rst.setOneBest(0, new Vector<OneBestWordItem>());
                } else if ("n1".compareToIgnoreCase(attributes.getValue("no")) == 0) {
                    chan = 1;
                    rst.setOneBest(1, new Vector<OneBestWordItem>());
                } else if ("mix".compareToIgnoreCase(attributes.getValue("no")) == 0) {
                    is_in_mixchannel = true;
                }

                if (is_in_separation && "n0".compareToIgnoreCase(attributes.getValue("no")) == 0) {
                    is_in_spn0 = true;
                    is_in_spmix = false;
                    is_in_spn1 = false;
                }
                if (is_in_separation && "n1".compareToIgnoreCase(attributes.getValue("no")) == 0) {
                    is_in_spn1 = true;
                    is_in_spn0 = false;
                    is_in_spmix = false;
                }

                if (is_in_search_separation && "n0".compareToIgnoreCase(attributes.getValue("no")) == 0) {
                    is_in_search_spn0 = true;
                    is_in_search_spn1 = false;
                }
                if (is_in_search_separation && "n1".compareToIgnoreCase(attributes.getValue("no")) == 0) {
                    is_in_search_spn1 = true;
                    is_in_search_spn0 = false;
                }

                if (is_in_separation && "mix".compareToIgnoreCase(attributes.getValue("no")) == 0) {
                    is_in_spmix = true;
                    is_in_spn1 = false;
                    is_in_spn0 = false;
                }
            } else if ("function".compareToIgnoreCase(qName) == 0) {
                String value = attributes.getValue("value");
                if (value != null && "1-best".compareToIgnoreCase(value) == 0) {
                    // 找到1best节点
                    is_in_1best = true;
                    ob_text = "";
                    ob_time = "";
                }

                if (value != null && "long-silence".compareToIgnoreCase(value) == 0) {
                    is_in_silence = true;
                }

                if (value != null && "interrupted".compareToIgnoreCase(value) == 0) {
                    is_in_interrupted = true;
                }
            } else if ("text".compareToIgnoreCase(qName) == 0) {
                if (!is_in_1best)
                    return;

                is_in_1best_text = true;
            } else if ("time".compareToIgnoreCase(qName) == 0) {
                if (!is_in_1best)
                    return;

                is_in_1best_time = true;
            } else if ("items".compareToIgnoreCase(qName) == 0) {
                if (is_in_spn0) {
                    rst.setN0VadDuration(Integer.valueOf(attributes.getValue("duration")));
                }
                if (is_in_spn1) {
                    rst.setN1VadDuration(Integer.valueOf(attributes.getValue("duration")));
                }
                if (is_in_spmix && "mono".compareToIgnoreCase(rst.getChannel()) == 0) {
                    rst.setVadDuration(rst.getN0VadDuration() + rst.getN1VadDuration());
                }
                if (is_in_spmix && "stereo".compareToIgnoreCase(rst.getChannel()) == 0) {
                    rst.setVadDuration(rst.getN0VadDuration() >= rst.getN1VadDuration() ? rst.getN0VadDuration()
                        : rst.getN1VadDuration());
                }
            } else if ("item".compareToIgnoreCase(qName) == 0) {
                if (is_in_mixchannel && is_in_silence) {
                    String start = attributes.getValue("start");
                    String end = attributes.getValue("end");

                    int frontCh = XmlResultParser.beforeWhoSay(vad0List, vad1List, Integer.parseInt(start));
                    int backCh = XmlResultParser.backWhoSay(vad0List, vad1List, Integer.parseInt(end));

                    String duration = attributes.getValue("duration");
                    rst.setSilences(rst.getSilences() + frontCh + "" + backCh + fillNumber(duration, "-", 10) + "|"
                        + start + "-" + end + " ");
                    rst.setSilenceLong(rst.getSilenceLong() + Integer.parseInt(duration));
                }

                // 在channel的n1声道中，表示坐席抢客户
                if (is_in_search_spn1 && is_in_interrupted) {
                    String start = attributes.getValue("start");
                    String end = attributes.getValue("end");

                    int frontCh = XmlResultParser.beforeWhoSay(vad0List, vad1List, Integer.parseInt(start));
                    int backCh = XmlResultParser.backWhoSay(vad0List, vad1List, Integer.parseInt(end));

                    String duration = attributes.getValue("duration");
                    rst.setInterrupted(rst.getInterrupted() + frontCh + "" + backCh + fillNumber(duration, "-", 10)
                        + "|" + start + "-" + end + " ");
                }

                if (is_in_spn0) {
                    int start = Integer.parseInt(attributes.getValue("start"));
                    int end = Integer.parseInt(attributes.getValue("end"));
                    vad0List.add(new Integer[] { start, end });

                    if (attributes.getIndex("speed") >= 0 && attributes.getIndex("energy") >= 0) {
                        int speed = (int) (Double.parseDouble(attributes.getValue("speed"))) * 1000;
                        int energy = (int) (Double.parseDouble(attributes.getValue("energy"))) * 1000;
                        rst.setN0Speeds(rst.getN0Speeds() + "00" + Strings
                            .padStart(Integer.toString(speed), 10, '0')
                            + "|" + start + "-" + end + " ");
                        rst.setN0Energys(rst.getN0Energys() + "00" + Strings.padStart(Integer.toString(energy), 10, '0')
                            + "|" + start + "-" + end + " ");
                    }
                }

                if (is_in_spn1) {
                    int start = Integer.parseInt(attributes.getValue("start"));
                    int end = Integer.parseInt(attributes.getValue("end"));
                    vad1List.add(new Integer[] { start, end });

                    if (attributes.getIndex("speed") >= 0 && attributes.getIndex("energy") >= 0) {
                        int speed = (int) (Double.parseDouble(attributes.getValue("speed"))) * 1000;
                        int energy = (int) (Double.parseDouble(attributes.getValue("energy"))) * 1000;
                        rst.setN1Speeds(rst.getN1Speeds() + "00" + Strings.padStart(Integer.toString(speed), 10, '0')
                            + "|" + start + "-" + end + " ");
                        rst.setN1Energys(rst.getN1Energys() + "00" + Strings.padStart(Integer.toString(energy), 10, '0')
                            + "|" + start + "-" + end + " ");
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("text".compareToIgnoreCase(qName) == 0) {
                if (!is_in_1best)
                    return;

                is_in_1best_text = false;
            } else if ("time".compareToIgnoreCase(qName) == 0) {
                if (!is_in_1best)
                    return;

                is_in_1best_time = false;
                Boolean oneBest = ob_text == "" || ob_time == ""; // 判断One_Best里是否有数据
                if (!oneBest) { // 提取One_Best里已存在的数据
                    // 声道的1best提取完毕
                    assert (ob_text != null && ob_time != null);
                    String[] txt_lst = ob_text.split(" ");
                    String[] time_lst = ob_time.split(" ");

                    assert (txt_lst.length == time_lst.length);
                    // 将文本和时间对上
                    for (int i = 0; i < txt_lst.length; ++i) {
                        String[] be_pair = time_lst[i].split(",");
                        assert (be_pair.length == 2);
                        OneBestWordItem item = new OneBestWordItem();
                        item.setWord(new String(txt_lst[i]));
                        item.setBegin(Integer.valueOf(be_pair[0]));
                        item.setEnd(Integer.valueOf(be_pair[1]));
                        item.setChannel(chan);
                        (rst.getOneBest())[chan].add(item);
                    }
                }
            } else if ("subject".compareToIgnoreCase(qName) == 0) {
                is_in_separation = false;
                is_in_search_separation = false;
                is_in_spn0 = false;
                is_in_search_spn0 = false;
                is_in_spn1 = false;
                is_in_search_spn1 = false;
                is_in_spmix = false;
                if (is_in_search) {
                    is_in_search = false;
                }

            } else if ("function".compareToIgnoreCase(qName) == 0) {
                is_in_silence = false;
                if (is_in_mixchannel) {
                    is_in_interrupted = false;
                }
                is_in_mixchannel = false;
                if (is_in_1best_text || is_in_1best_time)
                    return;

                is_in_1best = false;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (is_in_1best && is_in_1best_text) {
                // 将1best数据保存起来
                ob_text += new String(ch, start, length);
            }
            if (is_in_1best && is_in_1best_time) {
                ob_time += new String(ch, start, length);
            }
        }

        public String fillNumber(String number, String fillChar, int totalLen) {
            String numberTemp = number;
            int len = numberTemp.length();

            for (int i = 0; i < totalLen - len; i++) {
                numberTemp = "0" + numberTemp;
            }

            return numberTemp;
        }

        public TransformResult getOneBestResult() {
            return rst;
        }

    }

    public XmlResultParser() {
        SAXParserFactory fact = SAXParserFactory.newInstance();
        fact.setNamespaceAware(false);
        fact.setValidating(false);

        // 构造解析器
        try {
            parser = fact.newSAXParser();
        } catch (Exception e) {
            // e.printStackTrace();
            LOGGER.error("parse error", e);
            return;
        }
    }

    public TransformResult ParseXML(File path) {
        try {
            OneBestHandler handler = new OneBestHandler();
            parser.parse(path, handler);
            return handler.getOneBestResult();
        } catch (Exception e) {
            LOGGER.error("parse error", e);
            return null;
        }
    }

    public TransformResult ParseXML(String xml) {
        assert (parser != null);

        try {
            OneBestHandler handler = new OneBestHandler();
            parser.parse(new InputSource(new StringReader(xml)), handler);
            return handler.getOneBestResult();
        } catch (Exception e) {
            LOGGER.error("parse error", e);
            return null;
        }
    }

    private SAXParser parser = null;

    /*
     * 0:坐席 1:客户
     */
    public static int beforeWhoSay(List<Integer[]> vad0List, List<Integer[]> vad1List, Integer starttime) {
        try {
            int n1count = vad0List.size();
            int i = n1count - 1;
            int n1starttime = -1;

            if (n1count == 0) {
                return 2;
            }

            for (; i >= 0; i--) {
                int endtime = vad0List.get(i)[1];

                if (starttime >= endtime) {
                    n1starttime = vad0List.get(i)[0];
                    break;
                }
            }

            int n2count = vad1List.size();
            int j = n2count - 1;
            if (n2count == 0) {
                return 2;
            }

            int n2starttime = -1;

            for (; j >= 0; j--) {
                int endtime = vad1List.get(j)[1];

                if (starttime >= endtime) {
                    n2starttime = vad1List.get(j)[0];
                    break;
                }
            }

            if (n1starttime > n2starttime) { // 坐席说话
                return 0;
            } else if (n1starttime < n2starttime) {
                return 1;
            }

            return 2;
        } catch (Exception ex) {
            LOGGER.error("parse error", ex);
            return 2;
        }
    }

    /*
     * 对象后是否客服说话rue:客服说话 false:用户说话
     */
    public static int backWhoSay(List<Integer[]> vad0List, List<Integer[]> vad1List, int endtime) {
        try {

            int n1count = vad0List.size();
            int n1starttime = 1;

            if (n1count == 0) {
                return 2;
            }

            for (int i = 0; i < n1count; i++) {
                int starttime = vad0List.get(i)[0];

                if (endtime <= starttime) {
                    n1starttime = starttime;
                    break;
                }
            }

            int n2count = vad1List.size();

            if (n2count == 0) {
                return 2;
            }

            int n2starttime = 1;

            for (int j = 0; j < n2count; j++) {
                int starttime = vad1List.get(j)[0];

                if (endtime <= starttime) {
                    n2starttime = starttime;
                    break;
                }
            }

            if ((n1starttime < n2starttime) || (n2starttime < 0 && n1starttime >= 0)) { // 坐席说话
                return 0;
            } else if ((n1starttime > n2starttime) || (n1starttime < 0 && n2starttime >= 0)) {
                return 1;
            }

            return 2;
        } catch (Exception ex) {
            LOGGER.error("parse error", ex);
            return 2;
        }
    }

}
