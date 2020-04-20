package ru.kashintsev.Controllers;

import org.springframework.web.bind.annotation.*;
import ru.kashintsev.Exceptions.NotFoundException;

import java.util.*;

@RestController
@RequestMapping("strings")
public class StringController {
    private int counter = 4;
    public List<Map<String, String>> strings = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>(){{put("id", "1"); put("text", "12345");}});
        add(new HashMap<String, String>(){{put("id", "2"); put("text", "67890");}});
        add(new HashMap<String, String>(){{put("id", "3"); put("text", "qwert");}});
    }};


    @GetMapping
    public String firstPage() {
        StringBuffer page = new StringBuffer("<pre>");
        strings.forEach(str -> page.append(str).append("\n"));
        page.append("</pre>");
        return page.toString();
    }

    @GetMapping("{id}")
    public Map<String, String> getById(@PathVariable String id){
        return strings.stream().filter(e -> e.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("/reverse/{id}")
    public String reverseById(@PathVariable String id){
        StringBuilder result = new StringBuilder();
        strings.forEach(item -> {
            if (item.get("id").equals(id)) {
                result.append(item.get("text"));
                return;
            }
        });
        return result.reverse().toString();
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> item){
        item.put("id", String.valueOf(counter++));
        strings.add(item);
        return item;
    }

    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @PathVariable Map<String, String> item){
        Map<String, String> itemFrom = getById(id);
        itemFrom.putAll(item);
        itemFrom.put("id", id);
        return itemFrom;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Map<String, String> itemFrom = getById(id);
        strings.remove(itemFrom);
    }
}