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
    public MessageSystemContext messageSystemContextBean() {
        MessageSystemContext messageSystemContext = new MessageSystemContext(new MessageSystem());
        FrontendAddressee frontendAddressee = frontendAddressee(messageSystemContext);
        DBAddressee dbAddressee = dbAddressee(messageSystemContext);
        messageSystemContext.setFrontAddress(frontendAddressee.getAddress());
        messageSystemContext.setDbAddress(dbAddressee.getAddress());
        messageSystemContext.getMessageSystem().start();
        return messageSystemContext;
    }

    @Bean
    public FrontendAddressee frontendAddressee(MessageSystemContext messageSystemContext) {
        Address frontendAddress = new Address("Frontend");
        FrontendUserAddressee frontendUserAddressee = new FrontendUserAddressee(
                messageSystemContext, frontendAddress, webSocketSender());
        frontendUserAddressee.init();
        return frontendUserAddressee;
    }

    @Bean
    public DBAddressee dbAddressee(MessageSystemContext messageSystemContext) {
        Address dbAddress = new Address("DB");
        DBUserAddressee dbUserAddressee = new DBUserAddressee(
                messageSystemContext, dbAddress, userDBService);
        dbUserAddressee.init();
        return dbUserAddressee;
    }

    @Bean
    public WebSocketSender webSocketSender() {
        return new WebSocketSender(template);
    }

}
