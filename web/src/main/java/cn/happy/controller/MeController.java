package cn.happy.controller;

import cn.happy.domain.LayUiTable;
import cn.happy.domain.Node;
import cn.happy.service.PersonService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class MeController {

    @Resource
    PersonService personService;


    @RequestMapping("/layui")
    @ResponseBody
    public ModelAndView layui() {
        ModelAndView mv = new ModelAndView("layui");
        return mv;
    }


    @RequestMapping("/name/{name}")
    @ResponseBody
    public ModelAndView getPersonByName(@PathVariable(value = "name") String name) {
        Map<String, Object> map = personService.getPersonByName(name);
        Gson gson = new Gson();
        String mapJson = gson.toJson(map);
        List<String> list = Arrays.asList(mapJson);
        ModelAndView mv = new ModelAndView("page1");
        mv.addObject("nodes", list);
        return mv;
    }


    @RequestMapping("/province/{province}")
    @ResponseBody
    public ModelAndView getPersonByProvince(@PathVariable(value = "province") String province) {
        //各个账户下的金额
        List<Map<String, Object>> list = personService.getPersonByProvince(province);
        List<String> jsonList = list.stream().map(x -> {
               String name = (String) x.get("name");
                return String.format("{name:\"%s\"}", name);
            }
        ).collect(Collectors.toList());
        ModelAndView mv = new ModelAndView("page1");
        mv.addObject("nodes", jsonList.subList(1,10));
        return mv;
    }

    @GetMapping("/first")
    public ModelAndView toTest() {
        return  new ModelAndView("first");
    }


    @RequestMapping(value="findAll")
    @ResponseBody
    public LayUiTable findAll(){
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Node node = new Node();
            node.setId(Long.parseLong(""+i));
            node.setColor("red");
            node.setLabel("name"+i);
            node.setSize(100+i);
            nodes.add(node);
        }
        LayUiTable table = new LayUiTable();
        table.setCode(0);
        table.setData(nodes);
        table.setMsg("获取成功数据");
        table.setCount(nodes.size());
        return table;
    }

    @RequestMapping(value="findNodes")
    @ResponseBody
    public List<Node> findNodes(){
        List<Node> nodes = personService.getAllGraphNodes();
        return nodes;
    }




}
