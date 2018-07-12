package com.cmos.audiotransfer.taskmanager.weights;

import com.cmos.audiotransfer.taskmanager.beans.ChannelWeightBean;

import java.util.*;
import java.util.stream.Collectors;

public class WeightManager {


    private Map<String, TreeMap<Integer, String>> weightTrees = new HashMap<>();
    private Map<String, Integer> totalWeights = new HashMap<>();
    private Map<String, List<String>> channels = new HashMap<>();


    public void init(WeightConfigs weightConfigs) {
        List<ChannelWeightBean> weightBeans = weightConfigs.getWeightConfigs();

        weightBeans.stream().collect(Collectors.groupingBy(ChannelWeightBean::getResourceCode))
            .forEach((k, v) -> {
                int i = 0;
                totalWeights.put(k, v.stream().mapToInt(p -> p.getWeight()).sum());
                TreeMap<Integer, String> weightTree = new TreeMap<>();
                v.sort((b1, b2) -> {
                    if (b1.getWeight() > b2.getWeight())
                        return -1;
                    else if (b1.getWeight() < b2.getWeight())
                        return 1;
                    return 0;
                });
                for (ChannelWeightBean weightBean : v) {
                    i += weightBean.getWeight();
                    weightTree.put(i, weightBean.getChannelId());
                }

                channels.put(k, v.stream().map(p -> p.getChannelId()).collect(Collectors.toList()));
                weightTrees.put(k, weightTree);
            });

    }


    public List<String> getReflectChannelList(String resourceCode) {
        return this.channels.get(resourceCode);
    }


    public String getChannel(String resouceCode) {
        if (totalWeights.containsKey(resouceCode)) {
            int total = totalWeights.get(resouceCode);
            SortedMap<Integer, String> randomTree =
                weightTrees.get(resouceCode).tailMap((int) (Math.random() * total));
            return randomTree.get(randomTree.firstKey());
        } else {
            return null;
        }
    }
}
