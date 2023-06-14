package cn.happy.service.impl;

import cn.happy.service.PersonService;
import cn.happy.component.NeoComponent;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.NodeValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {


    private Driver driver;

    private void initDriver(){
        driver = NeoComponent.getDriver(NeoComponent.getDefaultNeoParam());
    }
    @Override
    public Map<String, Object> getPersonByName(String name) {
        initDriver();
        try (Session session = driver.session()) {
            String cypher = String.format("match(n:Person{name:'%s'}) return n", name);
            Result result = session.run(cypher);
            Record single = result.single();
            return single.get("n").asMap();
        }
    }

    @Override
    public List<Map<String, Object>> getPersonByProvince(String province) {
        initDriver();
        try(Session session = driver.session()) {
            String cypher = String.format("match(n:Person{province:'%s'}) return n", province);
            List<Record> list = session.run(cypher).list();
            return list.stream().map(x -> {
                NodeValue node = (NodeValue) x.get("n");
                return node.asMap();
            }).collect(Collectors.toList());
        }
    }



    @Override
    public void close() throws Exception {
        driver.close();
    }
}
