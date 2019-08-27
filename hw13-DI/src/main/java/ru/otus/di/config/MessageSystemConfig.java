package ru.otus.di.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.otus.di.addressee.DBAddressee;
import ru.otus.di.addressee.DBUserAddressee;
import ru.otus.di.addressee.FrontendAddressee;
import ru.otus.di.addressee.FrontendUserAddressee;
import ru.otus.di.utils.MessageSystemContext;
import ru.otus.di.ws.WebSocketSender;
import ru.otus.hibernate.dao.User;
import ru.otus.hibernate.service.DBService;
import ru.otus.ms.Address;
import ru.otus.ms.MessageSystem;

@Configuration
public class MessageSystemConfig {

    @Autowired
    private DBService<User> userDBService;

    @Autowired
    private SimpMessagingTemplate template;

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystem();
    }

    @Bean
    public MessageSystemContext messageSystemContext() {
        MessageSystemContext messageSystemContext = new MessageSystemContext(messageSystem());
        messageSystemContext.setFrontAddress(new Address("Frontend"));
        messageSystemContext.setDbAddress(new Address("DB"));
        messageSystemContext.getMessageSystem().start();
        return messageSystemContext;
    }

    @Bean
    public FrontendAddressee frontendAddressee() {
        MessageSystemContext messageSystemContext = messageSystemContext();
        FrontendUserAddressee frontendUserAddressee = new FrontendUserAddressee(
                messageSystemContext, messageSystemContext.getFrontAddress(), webSocketSender());
        frontendUserAddressee.init();
        return frontendUserAddressee;
    }

    @Bean
    public DBAddressee dbAddressee() {
        MessageSystemContext messageSystemContext = messageSystemContext();
        DBUserAddressee dbUserAddressee = new DBUserAddressee(
                messageSystemContext, messageSystemContext.getDbAddress(), userDBService);
        dbUserAddressee.init();
        return dbUserAddressee;
    }

    @Bean
    public WebSocketSender webSocketSender() {
        return new WebSocketSender(template);
    }

}
