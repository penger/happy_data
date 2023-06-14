package cn.happy.controller;

import cn.happy.service.PersonService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
        //各个账户下的金额
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

    @GetMapping("/test")
    public ModelAndView toTest() {
        return new ModelAndView("test");
    }

}
