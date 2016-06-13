package com.serena;

//import br.com.six2six.fixturefactory.Fixture;
//import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
//import br.com.six2six.fixturefactory.loader.TemplateLoader;
//import br.com.six2six.fixturefactory.processor.HibernateProcessor;
import com.serena.dto.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;


@Component
public class LoadDBAndData {

    @Autowired
    SessionFactory sessionFactory;

    @Transactional
    public void generateDatabase() {
        Session currentSession = sessionFactory.getCurrentSession();
//        FixtureFactoryLoader.loadTemplates("com.serena.dto");
//        Fixture.from(User.class).uses(new HibernateProcessor(currentSession)).gimme(100, "valid");
    }

    public static void main(String[] args) throws SQLException {
//        ConfigurableApplicationContext context = SpringApplication.run(Config.class);
//        LoadDBAndData loadDBAndData = context.getBean(LoadDBAndData.class);
//
//        loadDBAndData.generateDatabase();

    }

}
