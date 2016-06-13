package com.serena.dto;

import br.com.six2six.bfgex.RandomGen;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.function.AtomicFunction;
import br.com.six2six.fixturefactory.loader.TemplateLoader;


public class Fixtures implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(User.class).addTemplate("valid", new Rule() {{
            add("firstName", firstName());
            add("lastName", lastName());
            add("projects", has(3).of(Project.class, "IDM", "VM", "CM"));
        }});

        Fixture.of(Project.class).addTemplate("IDM", new Rule(){{
            add("title", "IDM");
            add("items", has(100).of(Item.class, "valid"));
        }});

        Fixture.of(Project.class).addTemplate("VM", new Rule(){{
            add("title", "VM");
            add("items", has(300).of(Item.class, "valid"));
        }});

        Fixture.of(Project.class).addTemplate("CM", new Rule(){{
            add("title", "CM");
            add("items", has(1000).of(Item.class, "valid"));
        }});

        Fixture.of(Item.class).addTemplate("valid", new Rule() {{
            add("itemId", new AtomicFunction() {
                @Override
                public  String generateValue() {
                    return RandomGen.word(4);
                }
            });
            add("description", new AtomicFunction() {
                @Override
                public String generateValue() {
                    return RandomGen.sentence();
                }
            });
            add("title", new AtomicFunction() {
                @Override
                public  String generateValue() {
                    return RandomGen.word();
                }
            });
        }});
    }

}
