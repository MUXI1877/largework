package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.PersonTransfer;
import com.it.quanxianguanli.service.PersonTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person-transfer")
public class PersonTransferController {

  @Autowired
  private PersonTransferService personTransferService;

  @GetMapping("/list")
  public Result<List<PersonTransfer>> list() {
    return Result.success(personTransferService.findAll());
  }

  @GetMapping("/{id}")
  public Result<PersonTransfer> getById(@PathVariable String id) {
    PersonTransfer transfer = personTransferService.findById(id);
    if (transfer == null) {
      return Result.error("调动记录不存在");
    }
    return Result.success(transfer);
  }

  @PostMapping("/save")
  public Result<PersonTransfer> save(@RequestBody PersonTransfer personTransfer) {
    return Result.success(personTransferService.save(personTransfer));
  }

  @PutMapping("/update")
  public Result<PersonTransfer> update(@RequestBody PersonTransfer personTransfer) {
    if (personTransfer.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(personTransferService.save(personTransfer));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    personTransferService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

