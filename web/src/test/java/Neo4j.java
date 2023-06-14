import cn.happy.component.NeoComponent;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.NodeValue;

import java.util.List;
import java.util.Map;

public class Neo4j {

    public static void main(String[] args) {

        NeoComponent.NeoParam neoParam = NeoComponent.getDefaultNeoParam();
        Driver driver = NeoComponent.getDriver(neoParam);
        Session session = driver.session();
        selectNode(session);
        selectNodes(session);
        session.close();
        driver.close();
    }


    public static void selectNode(Session session){
        String simpleNodeCypher = "MATCH(n:Movie {title:'The Matrix'}) return n";
        Result result = session.run(simpleNodeCypher);
        List<Record> list = result.list();
        System.out.println(list.size());
        for (Record record : list) {
            NodeValue value = (NodeValue) record.get("n");
            Map<String, Object> map = value.asMap();
            System.out.println(map);
        }
    }

    public static void selectNodes(Session session){
        String simpleNodeCypher = "MATCH(n:Movie) return n limit 10";
        Result result = session.run(simpleNodeCypher);
        List<Record> list = result.list();
        System.out.println(list.size());
        for (Record record : list) {
            System.out.println(record.keys());
            NodeValue value = (NodeValue) record.get("n");
            Map<String, Object> map = value.asMap();
            System.out.println(map);
        }
    }









}
