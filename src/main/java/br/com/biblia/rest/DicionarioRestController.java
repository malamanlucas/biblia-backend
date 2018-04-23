package br.com.biblia.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import br.com.biblia.dao.DicionarioDAO;
import br.com.biblia.enums.IdiomaEnum;
import br.com.biblia.model.Dicionario;
import br.com.biblia.model.DicionarioKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/dicionarios")
public class DicionarioRestController {
    
    @Autowired
    private DicionarioDAO dao;
    
    @PostMapping("/")
    public List<Dicionario> findAll(@RequestBody DicionarioSearchParam param) {
    	List<DicionarioKey> keys = Lists.newArrayList();
    	param.dics.forEach( id -> keys.add( new DicionarioKey(id, param.getIdioma()) ) );	
        return dao.findByKeyIn(keys);
    }
    
    @Data @AllArgsConstructor @NoArgsConstructor
    static class DicionarioSearchParam {
    	private List<Integer> dics;
    	private IdiomaEnum idioma;
    }
    
}
