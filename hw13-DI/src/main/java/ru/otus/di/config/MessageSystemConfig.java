package ru.otus.di.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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

    private MessageSystemContext messageSystemContext = new MessageSystemContext(new MessageSystem());

    @Bean
    @DependsOn({"frontend", "db"})
    public MessageSystemContext messageSystemContextBean(
            FrontendAddressee frontendAddressee,
            DBAddressee dbAddressee
    ) {
        messageSystemContext.setFrontAddress(frontendAddressee.getAddress());
        messageSystemContext.setDbAddress(dbAddressee.getAddress());
        messageSystemContext.getMessageSystem().start();
        return messageSystemContext;
    }

    @Bean("frontend")
    public FrontendAddressee frontendAddressee() {
        Address frontendAddress = new Address("Frontend");
        FrontendUserAddressee frontendUserAddressee = new FrontendUserAddressee(
                messageSystemContext, frontendAddress, webSocketSender());
        frontendUserAddressee.init();
        return frontendUserAddressee;
    }

    @Bean("db")
    public DBAddressee dbAddressee() {
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
