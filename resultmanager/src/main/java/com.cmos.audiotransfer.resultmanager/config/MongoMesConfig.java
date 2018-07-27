package com.cmos.audiotransfer.resultmanager.config;

import com.cmos.audiotransfer.resultmanager.Dao.ResultMongoDB;
import com.cmos.audiotransfer.resultmanager.DaoImpl.ResultMongoDBImpl;
import com.cmos.audiotransfer.resultmanager.handlers.SendMessageConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoMesConfig {

    Logger logger = LoggerFactory.getLogger(MongoMesConfig.class);

    @Bean
    public SendMessageConsumer sendMessageConsumer(ResultMongoDB resultMongoDB) {
        SendMessageConsumer sendMessageConsumer =
                new SendMessageConsumer();
        sendMessageConsumer.setResultMongoDB(resultMongoDB);
        try {
            sendMessageConsumer.init();
        } catch (MQClientException e) {
            logger.error("Resource Consumer Init Failed!", e);
            return null;
        }
        return sendMessageConsumer;
    }

    @Bean
    public ResultMongoDB resultMongoDB(ResultMongoDBImpl resultMongoDBImpl) {
        return  resultMongoDBImpl;
    }

}
