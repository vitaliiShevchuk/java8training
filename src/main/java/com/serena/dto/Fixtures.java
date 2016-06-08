package com.serena.dto;

import br.com.six2six.bfgex.RandomGen;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.List;

public class Fixtures implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(User.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("firstName", firstName());
            add("lastName", lastName());
            add("projects", has(10).of(Project.class, "valid"));
        }});

        Fixture.of(Project.class).addTemplate("valid", new Rule(){{
            add("id", random(Long.class, range(1L, 200L)));
            add("title", random("IDM", "Global", "CM", "Vacation Manager", "Dimensions"));
            add("prefix", random("XX", "YY", "ZZ"));
            add("items", has(100).of(Item.class, "valid"));
        }});

        Fixture.of(Item.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("itemId", RandomGen.word(4));
            add("description", RandomGen.sentence());
            add("title", RandomGen.word());
        }});
    }

    public static void main(String[] args) {
        FixtureFactoryLoader.loadTemplates("com.serena.dto");
        List<User> valid = Fixture.from(User.class).gimme(100, "valid");

        System.out.println(valid);
    }
}
