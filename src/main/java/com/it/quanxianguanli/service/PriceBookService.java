package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.PriceBook;
import com.it.quanxianguanli.repository.PriceBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PriceBookService {

    @Autowired
    private PriceBookRepository priceBookRepository;

    @Transactional(readOnly = true)
    public List<PriceBook> findAll() {
        return priceBookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PriceBook> findById(String id) {
        return priceBookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<PriceBook> findByProductCode(String productCode) {
        return priceBookRepository.findByProductCode(productCode);
    }

    @Transactional(readOnly = true)
    public List<PriceBook> findByStatus(String status) {
        return priceBookRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public PriceBook findCurrentPriceByProductCode(String productCode) {
        return priceBookRepository.findCurrentPriceByProductCode(productCode, LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<PriceBook> findPriceHistoryByProductCode(String productCode) {
        return priceBookRepository.findByProductCodeOrderByEffectiveDateDesc(productCode);
    }

    @Transactional(readOnly = true)
    public List<PriceBook> findByEffectiveDateRange(LocalDate startDate, LocalDate endDate) {
        return priceBookRepository.findByEffectiveDateBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<PriceBook> findByExpireDateRange(LocalDate startDate, LocalDate endDate) {
        return priceBookRepository.findByExpireDateBetween(startDate, endDate);
    }

    @Transactional
    public PriceBook save(PriceBook priceBook) {
        return priceBookRepository.save(priceBook);
    }

    @Transactional
    public void deleteById(String id) {
        priceBookRepository.deleteById(id);
    }

    @Transactional
    public PriceBook update(PriceBook priceBook) {
        Optional<PriceBook> existing = priceBookRepository.findById(priceBook.getId());
        if (existing.isPresent()) {
            return priceBookRepository.save(priceBook);
        } else {
            throw new RuntimeException("价格记录不存在");
        }
    }

    @Transactional
    public PriceBook updateStatus(String id, String status) {
        PriceBook priceBook = priceBookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("价格记录不存在"));
        priceBook.setStatus(status);
        return priceBookRepository.save(priceBook);
    }

    @Transactional
    public PriceBook createNewPriceVersion(PriceBook newPrice) {
        // 查找当前生效的价格版本
        PriceBook currentPrice = priceBookRepository.findCurrentPriceByProductCode(
                newPrice.getProductCode(), LocalDate.now());

        // 如果存在当前生效的价格，设置其失效日期为新价格生效日期的前一天
        if (currentPrice != null && "ACTIVE".equals(currentPrice.getStatus())) {
            currentPrice.setStatus("EXPIRED");
            currentPrice.setExpireDate(newPrice.getEffectiveDate().minusDays(1));
            priceBookRepository.save(currentPrice);
        }

        // 保存新的价格版本
        newPrice.setStatus("ACTIVE");
        return priceBookRepository.save(newPrice);
    }
}
