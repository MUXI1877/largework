package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.PersonTransfer;
import com.it.quanxianguanli.repository.PersonTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonTransferService {

  @Autowired
  private PersonTransferRepository personTransferRepository;

  public List<PersonTransfer> findAll() {
    return personTransferRepository.findAll();
  }

  public PersonTransfer findById(String id) {
    return personTransferRepository.findById(id).orElse(null);
  }

  @Transactional
  public PersonTransfer save(PersonTransfer personTransfer) {
    return personTransferRepository.save(personTransfer);
  }

  @Transactional
  public void deleteById(String id) {
    personTransferRepository.deleteById(id);
  }
}

