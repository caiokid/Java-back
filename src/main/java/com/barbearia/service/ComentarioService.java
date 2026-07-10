package com.barbearia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barbearia.dao.ComentarioDAO;
import com.barbearia.model.Comentarios;



@Service 
public class ComentarioService {

   private final ComentarioDAO dao;
  
   ComentarioService(ComentarioDAO dao){
      this.dao = dao;   
   }        
   
   public List<Comentarios> list(){           
     return dao.listComments();
   } 
}