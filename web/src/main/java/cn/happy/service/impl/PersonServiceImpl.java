package cn.happy.service.impl;

import cn.happy.domain.Node;
import cn.happy.service.PersonService;
import cn.happy.component.NeoComponent;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.NodeValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<Map<String, Object>> getAllNodes() {
        initDriver();
        try(Session session = driver.session()) {
            String cypher = "match(n:Movie) return n";
            List<Record> list = session.run(cypher).list();
            return list.stream().map(x -> {
                NodeValue node = (NodeValue) x.get("n");
                return node.asMap();
            }).collect(Collectors.toList());
        }
    }


    @Override
    public List<Node> getAllGraphNodes() {
        initDriver();
        try(Session session = driver.session()) {
            String cypher = "match(n:Movie)  return n  order by n.released desc  limit 10 ";
            List<Record> list = session.run(cypher).list();
            List<Node> nodeList = new ArrayList<>();
            for (Record x : list) {
                NodeValue node = (NodeValue) x.get("n");
                long id = node.asEntity().id();
                Map<String, Object> map = node.asMap();
                Node graphNode = new Node();
                graphNode.setId(id);
                graphNode.setLabel((String) map.get("title"));
                graphNode.setColor("green");
                graphNode.setSize(10);
                nodeList.add(graphNode);
            }
            return nodeList;
        }
    }


    @Override
    public void close() throws Exception {
        driver.close();
    }


    public static void main(String[] args) {
        List<Node> allGraphNodes = new PersonServiceImpl().getAllGraphNodes();
        System.out.println(allGraphNodes);
    }



}
