package ru.otus.di.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.di.addressee.DBAddressee;
import ru.otus.di.addressee.DBUserAddressee;
import ru.otus.di.addressee.FrontendAddressee;
import ru.otus.di.addressee.FrontendUserAddressee;
import ru.otus.di.utils.MessageSystemContext;
import ru.otus.ms.Address;
import ru.otus.ms.MessageSystem;

@Configuration
public class MessageSystemConfig {

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystem();
    }

    @Bean
    public MessageSystemContext messageSystemContext() {
        MessageSystemContext messageSystemContext = new MessageSystemContext(messageSystem());
        messageSystemContext.setFrontAddress(frontAddress());
        messageSystemContext.setDbAddress(dbAddress());
        return messageSystemContext;
    }

    @Bean
    public Address frontAddress() {
        return new Address("Frontend");
    }

    @Bean
    public Address dbAddress() {
        return new Address("DB");
    }

    @Bean
    public FrontendAddressee frontendAddressee() {
        FrontendUserAddressee frontendUserAddressee = new FrontendUserAddressee(messageSystemContext(), frontAddress());
        frontendUserAddressee.init();
        return frontendUserAddressee;
    }

    @Bean
    public DBAddressee dbAddressee() {
        DBUserAddressee dbUserAddressee = new DBUserAddressee(messageSystemContext(), dbAddress());
        dbUserAddressee.init();
        return dbUserAddressee;
    }

    @Bean
    public void startMessageSystem() {
        messageSystem().start();
    }

}
