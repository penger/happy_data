package cn.happy.service;

import cn.happy.domain.Node;

import java.util.List;
import java.util.Map;

public interface PersonService extends AutoCloseable{

    public Map<String,Object> getPersonByName(String name);

    public List<Map<String,Object>> getPersonByProvince(String province);


    public List<Map<String,Object>> getAllNodes();

    List<Node> getAllGraphNodes();
}
