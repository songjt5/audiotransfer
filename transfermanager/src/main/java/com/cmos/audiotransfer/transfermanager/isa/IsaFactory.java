package com.cmos.audiotransfer.transfermanager.isa;

/**
 * Created by hejie
 * Date: 18-7-24
 * Time: 下午5:21
 * Description:
 */
import com.cmos.audiotransfer.transfermanager.config.IsaEngineConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component public class IsaFactory {
    @Autowired IsaEngineConfigs engineConfig;

    public ISAEngine getNewEngine() {
        synchronized (IsaFactory.class) {
            QisrBasedISAEngine engine = new QisrBasedISAEngine(engineConfig.getXmlOption());
            engine.setSearchParam(engineConfig.getSearchParams());
            return engine;
        }
    }
}
